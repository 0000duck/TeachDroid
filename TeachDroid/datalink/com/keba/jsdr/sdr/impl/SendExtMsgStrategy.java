
package com.keba.jsdr.sdr.impl;

import java.io.IOException;

import com.keba.jsdr.sdr.IConnection;
import com.keba.jsdr.sdr.IMessage;
import com.keba.jsdr.sdr.ISendStrategy;
import com.keba.jsdr.sdr.SDRException;
import com.keba.jsdr.sdr.SDRInputStream;

public class SendExtMsgStrategy implements ISendStrategy {

   private static byte cNoReqHdl      = (byte) 0xFF;
   private static int  cExResReplyLen = 5;
   private static int  cExArgHdrLen   = 7;

   public void receive(IMessage msg, IConnection connection, int length)
         throws IOException, SDRException {
      connection.getInStream().ensureAvailableBytes(length);
      // msg.getMsgContext().remainingStreamBytes = length;
      if (!msg.getArgsSend()) {
         sendRemainingArgData(msg, connection);
      } else {
         receiveMessageFragment(msg, connection, length);
      }

   }

   protected void receiveMessageFragment(IMessage msg, IConnection connection,
         int length) throws IOException, SDRException {
      int resLength = 0;
      // all arguments sent
      SDRInputStream inStream = connection.getInStream();
      if (msg.getRequestNr() == cNoReqHdl) {
         if (length >= cExResReplyLen) {
            // first answer
            inStream.ensureAvailableBytes(5);
            msg.setRequestNr(inStream.readRawValue());
            msg.setRemainingData((inStream.readRawValue() & 0xff) << 24
                  | (inStream.readRawValue() & 0xff) << 16
                  | (inStream.readRawValue() & 0xff) << 8
                  | (inStream.readRawValue() & 0xff));

//            resLength = Math.min(length-cExResReplyLen, msg.getRemainingData());
            resLength = length-cExResReplyLen;

         } else {
            msg.getMsgContext().remainingStreamBytes = length;
            inStream.skip(length);
            throw new SDRException(SDRException.PROT_ERROR_NO_REQEST_HDL);
         }
      } else {
//         resLength = Math.min(length, msg.getRemainingData());
         resLength = length;
      }

      connection.getInStream().ensureAvailableBytes(resLength);
      msg.getMsgContext().remainingStreamBytes = resLength;
      try {
         msg.getRetValue().read(connection.getInStream(), msg.getMsgContext());
         msg.setRemainingData(msg.getRemainingData() - resLength);
      }catch(SDRException ex) {
         inStream.skip(msg.getMsgContext().remainingStreamBytes);
         throw ex;
      }

      if (msg.getMsgContext().done) {
         if (msg.getRemainingData() != 0) {
            inStream.skip(msg.getMsgContext().remainingStreamBytes);
            throw new SDRException(SDRException.DATA_ERROR);
         } else if(msg.getMsgContext().remainingStreamBytes != 0) {
            inStream.skip(msg.getMsgContext().remainingStreamBytes);
            throw new SDRException(SDRException.DATA_ERROR);
         }
      } else {
         if (msg.getRemainingData() == 0) {
            throw new SDRException(SDRException.DATA_ERROR_MORE_EXPECTED);
         } else if(msg.getMsgContext().remainingStreamBytes != 0) {
            inStream.skip(msg.getMsgContext().remainingStreamBytes);
            throw new SDRException(SDRException.DATA_ERROR);
         } else {
            connection.getOutStreamLock();
            try {
               msg.sendHeader(connection.getOutStream(), 3);
               connection.getOutStream().writerRawValue(msg.getRequestNr());
               int reqS = Math.min(msg.getRemainingData(), 1024);
               connection.getOutStream().writerRawValue((byte) (reqS >>> 8));
               connection.getOutStream().writerRawValue((byte) reqS);
               connection.getOutStream().flush();
            } finally {
               connection.releaseOutStreamLock();
            }
         }
      }
   }

   protected void sendRemainingArgData(IMessage msg, IConnection connection)
         throws IOException, SDRException {
      if (msg.getRequestNr() == cNoReqHdl) {
         // first serveranswer containing requesthandle
         connection.getInStream().ensureAvailableBytes(1);
         msg.setRequestNr(connection.getInStream().readRawValue());
      } else {
         if(connection.getInStream().readRawValue() != msg.getRequestNr()) {
            throw new SDRException(SDRException.PROTOCOL_ERROR);
         }
      }

      int size = Math.min(1023, msg.getRemainingData());
      msg.setRemainingData(msg.getRemainingData() - size);
      // msg.sendMsgNr(connection.getOutStream());
      connection.getOutStreamLock();
      try {
         msg.sendHeader(connection.getOutStream(), size + 1);
         connection.getOutStream().writerRawValue(msg.getRequestNr());
         msg.getMsgContext().remainingStreamBytes = size;
         msg.getArgValue()
               .write(connection.getOutStream(), msg.getMsgContext());
         connection.getOutStream().flush();
      } finally {
         connection.releaseOutStreamLock();
      }
      msg.setArgsSend(msg.getMsgContext().done);
      if (msg.getArgsSend()) {
         msg.getMsgContext().done = false;
         msg.getMsgContext().byteCnt = 0;
         msg.getMsgContext().bufferReadIdx = 0;
      }
      if (msg.getArgsSend()) {
         // all arguments send next package from serve is answer containen
         // result infos
         msg.setRequestNr(cNoReqHdl);
      }
   }

   public void send(IMessage msg, IConnection connection) throws IOException,
         SDRException {
      // sendMsgNr(outStream); outside
      int argSize = msg.getArgValue() == null ? 0 : msg.getArgValue().size();
      int size = Math.min(1024, argSize + cExArgHdrLen);
      // msg.getMsgContextSend().remainingStreamBytes =size + 5;
      msg.sendHeader(connection.getOutStream(), size);

      msg.setRequestNr(cNoReqHdl);
      connection.getOutStream().writerRawValue(cNoReqHdl);
      connection.getOutStream().writerRawValue(
            (byte) (msg.getServiceNr() >>> 8));
      connection.getOutStream().writerRawValue((byte) msg.getServiceNr());

      // int argSize = msg.getArgValue().size();
      size = Math.min(1024 - cExArgHdrLen, argSize);
      msg.setRemainingData(argSize - size);
      connection.getOutStream().writerRawValue((byte) (argSize >>> 24));
      connection.getOutStream().writerRawValue((byte) (argSize >>> 16));
      connection.getOutStream().writerRawValue((byte) (argSize >>> 8));
      connection.getOutStream().writerRawValue((byte) argSize);

      msg.getMsgContext().remainingStreamBytes = Math.min(1024 - cExArgHdrLen,
            argSize);
      if (msg.getArgValue() != null) {
         msg.getArgValue()
               .write(connection.getOutStream(), msg.getMsgContext());
         msg.setArgsSend(msg.getMsgContext().done);
      } else {
         msg.setArgsSend(true);
      }

      connection.getOutStream().flush();
   }

}
