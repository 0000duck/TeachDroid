
package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRShort implements SDR {

   public short m_value;
   protected boolean m_unsigned;
   
   public SDRShort() {
      this(false);
   }

   public SDRShort(short value) {
      this(value, false);
   }   
   
   
   protected SDRShort(boolean unsigned) {
      m_unsigned = unsigned;
   }

   protected SDRShort(short value, boolean unsigned) {
      this(unsigned);
      m_value = value;
   }
   
   
   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      if (m_unsigned) {
         m_value = in.readUShort(context);
      } else {
         m_value = in.readShort(context);
      }

   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      if (m_unsigned) {
         out.writeUShort(m_value, context);
      } else {
         out.writeShort(m_value, context);
      }

   }

   public void reset() {
      // TODO Auto-generated method stub
      
   }

   public int size() {
      if(m_unsigned) {
         return SDRUtil.sizeUShort(m_value);
      }
      return SDRUtil.sizeShort(m_value);
   }
}
