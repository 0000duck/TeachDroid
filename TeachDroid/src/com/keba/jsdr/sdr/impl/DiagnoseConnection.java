
package com.keba.jsdr.sdr.impl;

import java.io.IOException;
import java.net.UnknownHostException;

import com.keba.jsdr.sdr.SDR;
import com.keba.jsdr.sdr.SDRException;
import com.keba.jsdr.sdr.SdrConnection;

public class DiagnoseConnection extends Connection {

   public void connect(SdrConnection conData)
         throws UnknownHostException, IOException {
      logMessage("connect to: " + conData.ip);
      super.connect(conData);
      logMessage("connected to: " + conData.ip);
   }

   public void disConnect() throws IOException {
      logMessage("disconnect");
      super.disConnect();
      logMessage("disconnected");
   }

   protected void forceStreamLock() {
      logMessage("forceStreamLock");
      super.forceStreamLock();
      logMessage("forceStreamLock have lock");
   }

   public void getOutStreamLock() {
      logMessage("getOutStreamLock");
      super.getOutStreamLock();
      logMessage("getOutStreamLock have lock");
   }

   protected void receive() throws IOException {
      logMessage("receive");
      super.receive();
      logMessage("received");
   }

   public void releaseOutStreamLock() {
      logMessage("realesOutstreamLock");
      super.releaseOutStreamLock();
   }

   public int send(int station, int serviceNr, SDR arg, SDR ret, boolean ext)
         throws IOException, SDRException {
      logMessage("send: station " + station + " service " + serviceNr + " ext "
            + ext);
      int result = super.send(station, serviceNr, arg, ret, ext);
      logMessage("send: station " + station + " service " + serviceNr + " ext "
            + ext + "  result: " + result);
      return result;
   }

   public boolean waitFor(int msgNr) throws SDRException, IOException {
      logMessage("waitFor Message: " + msgNr);
      boolean result = super.waitFor(msgNr);
      logMessage("waitFor Message: " + msgNr + " result: " + result);
      return result;
   }

   protected void initMsgBuffer() {
      msgBuffer = new Message[64];
      for (int i = 0; i < 64; i++) {
         msgBuffer[i] = new DiagnoseMessage(i);
      }
   }

   private void logMessage(String msg) {
      System.out.println("[Connection] " + Thread.currentThread() + ": " + msg);
   }

}
