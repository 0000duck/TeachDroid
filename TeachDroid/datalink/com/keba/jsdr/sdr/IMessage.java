
package com.keba.jsdr.sdr;

import java.io.IOException;

public interface IMessage {

   public boolean getArgsSend();

   public byte getRequestNr();

   public void setRequestNr(byte b);

   public int getRemainingData();

   public void setRemainingData(int i) throws SDRException;

   public void sendHeader(SDROutputStream outStream, int size)
         throws IOException;

   public SDRContext getMsgContext();

   public SDR getArgValue();

   public void setArgsSend(boolean done);

   public SDR getRetValue();

   public int getServiceNr();
   
   public void setErrorMessage(String msg);
    public String getErrorMessage();
}
