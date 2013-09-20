package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRFloat implements SDR {

   
 public float m_value;

public SDRFloat() {
      
   }
   
   public SDRFloat(float value) {
      m_value = value;
   }
   
   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      m_value = in.readFloat(context);

   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      out.writeFloat(m_value, context);

   }

   public void reset() {
      // TODO Auto-generated method stub
      
   }

   public int size() {
      return 4;
   }

}
