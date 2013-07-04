
package com.keba.jsdr.sdr.impl;

import java.io.IOException;

import com.keba.jsdr.sdr.IConnection;
import com.keba.jsdr.sdr.IMessage;
import com.keba.jsdr.sdr.ISendStrategy;
import com.keba.jsdr.sdr.SDR;
import com.keba.jsdr.sdr.SDRContext;
import com.keba.jsdr.sdr.SDRException;
import com.keba.jsdr.sdr.SDRInputStream;
import com.keba.jsdr.sdr.SDROutputStream;
import com.keba.jsdr.sdr.SDRUtil;

/* Aufbau des Nachrichtenkopfes (Anfrage, bei Antwort entfällt "Service")
 *
 * +----------+-----+-----+----------+----------+----------//----------+
 * |MsgNr     |Recvr|MSndr|Service   |Length    |         Data         |
 * +----------+-----+-----+----------+----------+----------//----------+
 *  2 +        1 +   1 +   2 +        2          = max. 8 Bytes
 *


 Aufbau MsgNr:
 unsigned int mCount  : 6;   // laufende Nachrichtennummer auf Sender
 unsigned int mPrior  : 1;   // Priorität (1 = hoch, 0 = niedrig)
 unsigned int mType   : 1;   // Nachrichtentyp (1 = Anforderung, 0 = Antwort)
 unsigned int mSender : 8;   // Adresse der Senderstation (anfragebezogen)
 
 
 * +----------+-----+------+---------+
 * |mSender   |mType|mPrior|mCount   |
 * +----------+-----+------+---------+
 
 
 


 MSndr:   1 - 255  bzw. 0 allgemein gültige nummer

 Service: Servicenummer 

 Length

 unsigned int mByteLen : 11;   // Längen von 0 bis 1024 zulässig
 unsigned int mErrCode :  4;   // siehe SvcComm.h, Typ TErrorCode, Wert-1
 unsigned int mErrFlag :  1;   // mErrCode != eCommSuccess ?
 
 * +----------+--------+--------+
 * |mErrFlag  |mErrCode|mByteLen|
 * +----------+--------+--------+
 
 
 
 */

public class Message implements IMessage {

   public static final int MSG_FINISHED     = 0;
   public static final int MSG_NOT_FINISHED = 1;
   public static final int MSG_ERROR        = 2;
   public static final int MSG_INVALID      = 3;

   protected SDRContext    m_msgContext;
   protected SDR           m_argValue;
   protected SDR           m_retValue;
   protected int           m_msgNr;
   public byte             requestNr;
   public int              serviceNr;
   public boolean          m_argsSend;
   public int              m_remainingData;

   boolean                 m_used;

   private ISendStrategy   m_strategy;
   private boolean         m_done;
   public byte             rcvId;

   public int              m_sendNr         = 0;
   private int             m_errorCode;
    long m_initTime;
   private String m_errorMsg;
    long m_t;

   public Message(int nr) {
      m_msgNr = 0xFF80;
      m_msgNr |= (nr & SDRUtil.MASK_6Bit);
      m_msgContext = new SDRContext();
      init();
   }

   public void init() {
		// if(SystemInfo.getHWType()==SystemInfo.HW_HT) {
         m_initTime = System.currentTimeMillis();
		// } else {
		// m_initTime = NativeSystem.currentTimeMillis();
		// }
      m_msgContext.byteCnt = 0;
      m_msgContext.done = false;
      m_msgContext.remainingDataBytes = -1;
      m_msgContext.remainingStreamBytes = 0;
   }

   public int receive(IConnection connection) throws IOException {
      // 2byte messagenr read outside
      int result = 0;
      SDRInputStream inStream = connection.getInStream();
      inStream.ensureAvailableBytes(4);
      inStream.skip(1);
      int sendNr = (inStream.readRawValue() & 0xff);

      int b1 = inStream.readRawValue();
      int b2 = inStream.readRawValue();
      int length = (b1 & 0xff) << 8 | (b2 & 0xff);

      int msgLength = length & 0x7FF;

      boolean error = ((length >>> 15) & 0x01) == 1;

      boolean sendOk = sendNr == m_sendNr || sendNr == 0;

      if (error) {
         m_errorCode = ((length >> 11) & 0x0F) + 1;
         inStream.skip(msgLength);
         result = MSG_ERROR;
      } else {
         m_errorCode = 0;
         if (sendOk) {
            try {
               m_strategy.receive(this, connection, msgLength);
               result = m_msgContext.done ? MSG_FINISHED : MSG_NOT_FINISHED;
            } catch (SDRException e) {
               m_errorCode = e.getReason();
               result = MSG_ERROR;
            }
         } else {
            inStream.skip(msgLength);
            result = MSG_INVALID;
         }
      }

      return result;
   }

   public void send(IConnection connection) throws IOException, SDRException {
      m_strategy.send(this, connection);
   }

   public void sendHeader(SDROutputStream outStream, int size)
         throws IOException {
      m_sendNr++;
      if (m_sendNr == 255) {
         m_sendNr = 1;
      }
      // MsgNR 2 Byte
      outStream.writerRawValue((byte) (m_msgNr >>> 8));
      outStream.writerRawValue((byte) m_msgNr);

      // Receiver (Station) 1 Byte
      outStream.writerRawValue(rcvId);

      // Sendnr 1 Byte
      outStream.writerRawValue((byte) (m_sendNr & 0xff));

      // Servicenumber 2 Byte
      outStream.writerRawValue((byte) (serviceNr >>> 8));// , m_msgContextSend);
      outStream.writerRawValue((byte) serviceNr);// , m_msgContextSend);

      // Length 2 Byte
      outStream.writerRawValue((byte) (size >>> 8));// , m_msgContextSend);
      outStream.writerRawValue((byte) size);// , m_msgContextSend);
   }

   public void setStrategy(ISendStrategy strategy) {
      m_strategy = strategy;

   }

   public void setArgs(SDR arg, SDR ret) {
      m_argValue = arg;
      m_retValue = ret;

   }

   public boolean done() {
      return m_done;
   }

   public void setDone(boolean done) {
      m_done = done;

   }

   public SDR getArgValue() {
      return m_argValue;
   }

   public boolean getArgsSend() {

      return m_argsSend;
   }

   public int getRemainingData() {
      return m_remainingData;
   }

   public byte getRequestNr() {
      return requestNr;
   }

   public SDR getRetValue() {
      return m_retValue;
   }

   public int getServiceNr() {
      return serviceNr;
   }

   public void setArgsSend(boolean done) {
      m_argsSend = done;

   }

   public void setRemainingData(int i) throws SDRException {
      if(i<0) {
         throw new SDRException(SDRException.DATA_ERROR);
      }
      m_remainingData = i;

   }

   public void setRequestNr(byte b) {
      requestNr = b;
   }

   public SDRContext getMsgContext() {
      return m_msgContext;
   }

   public int getErrorCode() {
      return m_errorCode;
   }

   
   public void setErrorCode(int code) {
      m_errorCode = code;
   }

   public boolean isTimedOut(long maxTime) {
		// if(SystemInfo.getHWType()==SystemInfo.HW_HT) {
         return (System.currentTimeMillis()-m_initTime) >= maxTime;
		// }
		// m_t = NativeSystem.currentTimeMillis();
//      System.out.println("t "+t+"\n init "+m_initTime+"\n max "+maxTime+"\n dif "+(t-m_initTime));
		// return (m_t-m_initTime) >= maxTime;
   }

   public static void skipMessage(Connection connection) throws IOException {
      SDRInputStream inStream = connection.getInStream();
      inStream.ensureAvailableBytes(4);
      inStream.skip(1);
      int sendNr = (inStream.readRawValue() & 0xff);

      int b1 = inStream.readRawValue();
      int b2 = inStream.readRawValue();
      int length = (b1 & 0xff) << 8 | (b2 & 0xff);

      int msgLength = length & 0x7FF;
      inStream.ensureAvailableBytes(msgLength);
      inStream.skip(msgLength);
   }

   
   public void setErrorMessage(String msg) {
     m_errorMsg = msg;
   }
   public String getErrorMessage() {
      return m_errorMsg;
   }
}
