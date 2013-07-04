package com.keba.jsdr.sdr;

public class SDRContext {

   
   //bytes to read on the stream
   public int remainingStreamBytes;
   
   //flag if last read/write operation was successful
   public boolean done;
   

   //number of used bytes of the internal buffer
   public int byteCnt;
   
   //temporary buffer
   public byte[] buffer = new byte[9];
   
   //outstanding bytes to read for arrays, strings
   public int remainingDataBytes = -1;
   
   
   public int bufferReadIdx;
}
