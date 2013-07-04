
package com.keba.jsdr.sdr.impl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.keba.jsdr.sdr.IConnection;
import com.keba.jsdr.sdr.SDR;
import com.keba.jsdr.sdr.SDRException;
import com.keba.jsdr.sdr.SDRInputStream;
import com.keba.jsdr.sdr.SDROutputStream;
import com.keba.jsdr.sdr.SDRUtil;
import com.keba.jsdr.sdr.SdrConnection;

public class Connection implements IConnection, Runnable {

   private int               refCnt;

   protected SDRInputStream  m_inStream;
   protected SDROutputStream m_outStream;

   protected Message[]       msgBuffer;
   protected Object          sendLock;
   protected Object          connectionLock;

   protected boolean         m_running;
   protected Thread          m_receiver;
   protected Socket          socket;
   protected int             m_lockCnt = 0;
   protected long            m_maxTimeout;
   protected String          m_hostIP;
   protected boolean         m_forceLock;
   private int               m_lastMsgIdx;
   protected int             m_defaultStation = -1;

   public Connection() {
      connectionLock = new Object();
      sendLock = new Object();
      initMsgBuffer();

   }

   /**
    * Initializes the message buffer
    */
   protected void initMsgBuffer() {
      msgBuffer = new Message[64];
      for (int i = 0; i < 64; i++) {
         msgBuffer[i] = new Message(i);
      }
   }

   /**
    * Connects to the server
    * 
    * @param hostIp
    *           target ip adress
    * @param timeOut
    *           message timeout
    * @throws UnknownHostException
    * @throws IOException
    */
   private void connect(String hostIp, long timeOut)
         throws UnknownHostException, IOException {
      m_maxTimeout = timeOut;
      m_hostIP = hostIp;
      socket = new Socket(InetAddress.getByName(hostIp), 2002);
      socket.setSoTimeout(10000);
      socket.setTcpNoDelay(true);
      m_inStream = new SDRInputStream(socket.getInputStream());
      m_outStream = new SDROutputStream(socket.getOutputStream());
      m_running = true;
      m_receiver = new Thread(this, "SDRReceiver_"+hostIp);
      m_receiver.start();
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#disConnect()
    */
   public void disConnect() throws IOException {
      m_running = false;
      socket.close();
      synchronized (m_receiver) {
         m_receiver.notifyAll();
      }
   }

   /**
    * Runmethod of the receiver thread
    * 
    * @see java.lang.Runnable#run()
    */
   public void run() {
      try {
         while (m_running) {
            try {
               receive();
            } catch (InterruptedIOException e) {
               if(e.getMessage() != null && e.getMessage().toLowerCase().indexOf("timeout")!=-1) {
                  //ignore
               } else {
                  throw e;
               }
            } catch (ArithmeticException th) {
               th.printStackTrace();
            }
            synchronized (m_receiver) {
               if (!checkOutstandingMsg()) {
                  try {
                     m_receiver.wait();
                  } catch (InterruptedException e) {
                  }
               }
            }
         }
      } catch (IOException ex) {
         if (m_running) {
            forceStreamLock();
            try {
               connectionBroken(ex);
            } finally {
               m_running = false;
               m_forceLock = false;
               releaseOutStreamLock();
            }
         }
      }
   }

   private void connectionBroken(IOException ex) {
      for (int i = 0; i < 64; i++) {
         Message msg = getOutstandingMsg(i);
         if (msg != null) {
            msg.setErrorCode(SDRException.CONNECTION_BROKEN);
            msg.setErrorMessage(ex.getLocalizedMessage());
            msg.setDone(true);
            synchronized (msg) {
               msg.notifyAll();
            }
         }
      }

   }

   /**
    * Checks the waiting messages if a timeout has occured
    */
   private boolean checkOutstandingMsg() {
      int cnt = 0;
      for (int i = 0; i < 64; i++) {
         Message msg = getOutstandingMsg(i);
         if (msg != null) {
            cnt++;
            synchronized (msg) {
               if (msg.isTimedOut(m_maxTimeout)) {
                  System.out.println((msg.m_t - msg.m_initTime));
                  msg.setErrorCode(SDRException.TIMEOUT);
                  msg.setDone(true);
                  msg.notifyAll();
                  cnt--;
               } else {
                  if (msg.done()) {
                     cnt--;
                  }
               }
            }
         }
      }
      return cnt > 0;

   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#send(int, int, com.keba.jsdr.sdr.SDR,
    *      com.keba.jsdr.sdr.SDR, boolean)
    */
   public int send(int station, int serviceNr, SDR arg, SDR ret, boolean ext)
         throws IOException, SDRException {
      forceStreamLock();
      try {
         if(!m_running) {
            throw new SDRException(SDRException.NOT_CONNECTED);
         }
         Message msg = requestMessage();
         if (msg == null) {
            throw new SDRException(SDRException.OVERFLOW);
         }
         msg.setStrategy(StrategyFactory.getStrategy(ext));
         msg.serviceNr = serviceNr;
         msg.rcvId = (byte) (station & 0xff);
         msg.setArgs(arg, ret);
         msg.init();

         msg.send(this);
         synchronized (m_receiver) {
            m_receiver.notifyAll();
         }
         return msg.m_msgNr;
      } finally {
         m_forceLock = false;
         releaseOutStreamLock();
      }
   }

   /**
    * Trys a force operation to get the lock for the output stream
    */
   protected void forceStreamLock() {
      m_forceLock = true;

      synchronized (sendLock) {
         while (m_lockCnt != 0) {
            try {
               sendLock.wait();
            } catch (InterruptedException e) {
               // to nothing
            }
         }
         m_lockCnt++;
      }

   }

   /**
    * Requests a new unused message object
    * 
    * @return
    */
   private Message requestMessage() {
      Message msg = null;
      synchronized (msgBuffer) {
         if (m_lastMsgIdx == 63) {
            m_lastMsgIdx = 0;
         }
         for (int i = m_lastMsgIdx; i < 64; i++) {
            msg = msgBuffer[i];
            if (msg.m_used) {
               msg = null;
            } else {
               msg.m_used = true;
               msg.setDone(false);
               m_lastMsgIdx = i + 1;
               break;
            }
         }
      }
      return msg;
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#waitFor(int)
    */
   public boolean waitFor(int msgNr) throws SDRException, IOException {
      Message msg = getOutstandingMsg(msgNr);
      if (msg != null) {
         synchronized (msg) {
            while (!msg.done()) {
               try {
                  msg.wait();
               } catch (InterruptedException e) {
                  if (!m_running) {
                     return false;
                  }
               }
            }
         }
         int error = msg.getErrorCode();
         synchronized(msgBuffer) {
            msg.setDone(false);
            msg.m_used = false;
         }
         if (error > 0) {
            if (msg.getErrorCode() >= SDRException.PROTOCOL_ERROR) {
               return false;
            } else if (msg.getErrorCode() >= SDRException.CONNECTION_BROKEN) {
               throw new IOException(msg.getErrorMessage());
            }
            throw new SDRException(error);
         }
      } else {
         throw new SDRException(SDRException.UNKNOWN_MSG);
      }
      return true;
   }

   // /**
   // * Invoked when an protocol error in the datastream is detected
   // *
   // * @param error
   // * error number
   // * @throws UnknownHostException
   // * @throws IOException
   // */
   // private void protocolErrorOccured(int error) throws UnknownHostException,
   // IOException {
   // getOutStreamLock();
   // try {
   // try {
   // disConnect();
   // } catch (IOException e1) {
   // }
   // while (m_receiver.isAlive()) {
   // try {
   // Thread.sleep(500);
   //
   // } catch (InterruptedException e) {
   // // TODO Auto-generated catch block
   // e.printStackTrace();
   // }
   // }
   // connect(m_hostIP, m_maxTimeout);
   // for (int i = 0; i < 64; i++) {
   // Message msg = getOutstandingMsg(i);
   // if (msg != null) {
   // synchronized (msg) {
   // msg.setErrorCode(error);
   // msg.setDone(true);
   // msg.notifyAll();
   // }
   // }
   // }
   // } finally {
   // releaseOutStreamLock();
   // }
   // }

   /**
    * Method of the receiver task to receive asynchron message response
    * 
    * @throws IOException
    */
   protected void receive() throws IOException {
      m_inStream.ensureAvailableBytes(2);
      int msgNr = (m_inStream.readRawValue() & 0xff) << 8
            | (m_inStream.readRawValue() & 0xff);
      Message msg = getOutstandingMsg(msgNr);
      if (msg != null) {
         synchronized (msg) {
            int result = msg.receive(this);
            switch (result) {
            case Message.MSG_FINISHED:
               msg.setDone(true);
               msg.notifyAll();
               break;
            case Message.MSG_ERROR:
               if (msg.getErrorCode() >= SDRException.PROTOCOL_ERROR) {
                  msg.setDone(true);
                  msg.notifyAll();
                  // error in protocol
                  // protocolErrorOccured(msg.getErrorCode());
               } else {
                  msg.setDone(true);
                  msg.notifyAll();
               }
               break;
            case Message.MSG_NOT_FINISHED:
               break;
            case Message.MSG_INVALID:
               System.out.println("msg invalid");
               break;
            }
         }
      } else {
         Message.skipMessage(this);
      }

   }

   /**
    * Returns the outstanding message with the given id.
    * 
    * @param msgNr
    *           message id
    * @return outstanding message or null if there is no outstanding message
    *         with the given id
    */
   private Message getOutstandingMsg(int msgNr) {
      Message msg = null;
      synchronized (msgBuffer) {
         msg = msgBuffer[msgNr & SDRUtil.MASK_6Bit];
         if (!msg.m_used) {
            msg = null;
         }
      }
      return msg;
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#getInStream()
    */
   public SDRInputStream getInStream() {
      return m_inStream;
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#getOutStream()
    */
   public SDROutputStream getOutStream() {
      return m_outStream;
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#getOutStreamLock()
    */
   public void getOutStreamLock() {
      synchronized (sendLock) {
         while (m_lockCnt != 0 || m_forceLock) {
            try {
               sendLock.wait();
            } catch (InterruptedException e) {
               // to nothing
            }
         }
         m_lockCnt++;
      }
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#releaseOutStreamLock()
    */
   public void releaseOutStreamLock() {
      synchronized (sendLock) {
         m_lockCnt--;
         sendLock.notifyAll();
      }
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#incRefCount()
    */
   public void incRefCount() {
      refCnt++;
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#decRefCount()
    */
   public void decRefCount() {
      refCnt--;
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#getRefCount()
    */
   public int getRefCount() {
      return refCnt;
   }

   /**
    * @see com.keba.jsdr.sdr.IConnection#connect(com.keba.jsdr.sdr.SdrConnection)
    */
   public void connect(SdrConnection conData) throws UnknownHostException,
         IOException {
      connect(conData.ip, conData.timeOut);

   }

   public void setTimeOut(int timeMillis) {
     if(socket != null) {
        try {
         socket.setSoTimeout(timeMillis);
      } catch (SocketException e) {
         //
      }
     }
      
   }

   public int getDefaultStation() {
      return m_defaultStation;
   }

   public void setDefaultStation(int id) {
      m_defaultStation = id;
      
   }

   /**
    * Returns the socket where this client is connected to
    * @return host
    */
   public Socket getSocket() {
      return socket;
   }
}
