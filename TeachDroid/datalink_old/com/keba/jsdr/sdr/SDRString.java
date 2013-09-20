package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRString implements SDR {
   public StringBuffer m_value;

   public SDRString() {
      m_value = new StringBuffer();
   }
   
   public SDRString(StringBuffer value) {
      m_value = value;
   }
   
   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      m_value = in.readString(m_value, context);

   }

   public void reset() {
      // TODO Auto-generated method stub

   }

   public int size() {
      int length = m_value.length();
      return length + SDRUtil.sizeUInt(length);
   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      out.writeString(m_value, context);

   }

}
