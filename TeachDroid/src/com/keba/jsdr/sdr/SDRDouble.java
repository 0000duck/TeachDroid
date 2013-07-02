package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRDouble implements SDR {

   public double m_value;
   
   public SDRDouble() {
      
   }
   
   public SDRDouble(double value) {
      m_value = value;
   }
   
   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      m_value = in.readDouble(context);

   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      out.writeDouble(m_value, context);

   }

   public void reset() {
      // TODO Auto-generated method stub
      
   }

   public int size() {
      return 8;
   }

}
