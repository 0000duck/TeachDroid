package com.keba.jsdr.sdr.impl;

import java.io.IOException;

import com.keba.jsdr.sdr.IConnection;
import com.keba.jsdr.sdr.SDRException;
import com.keba.jsdr.sdr.SDROutputStream;

public class DiagnoseMessage extends Message {

   public DiagnoseMessage(int nr) {
      super(nr);
   }

   public int receive(IConnection connection) throws IOException {
      logMessage("receive");
      int result = super.receive(connection);
      logMessage("receive result: "+result);
      return result;
   }

   public void send(IConnection connection) throws IOException, SDRException {
      logMessage("send");
      super.send(connection);
      logMessage("send finished");
   }

   public void sendHeader(SDROutputStream outStream, int size) throws IOException {
      logMessage("sendHeader - size "+size);
      super.sendHeader(outStream, size);
      logMessage("sendHeader finished");
   }

   public void setDone(boolean done) {
      logMessage("setDone "+done);
      super.setDone(done);
   }

   public void setErrorCode(int code) {
      logMessage("setErrorCode "+code);
      super.setErrorCode(code);
   }

   public void setRemainingData(int i) throws SDRException {
      logMessage("setRemainingData "+i);
      super.setRemainingData(i);
   }

   public void setRequestNr(byte b) {
      logMessage("setRequestNr "+b);
      super.setRequestNr(b);
   }
   
   private void logMessage(String msg) {
      System.out.println("[Message] "+Thread.currentThread()+"  msgId: "+m_msgNr+": "+msg);
   }


}
