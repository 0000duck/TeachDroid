
package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRInt implements SDR {

   public int m_value;
   protected boolean m_unsigned;

   public SDRInt() {

   }

   public SDRInt(int value) {
      m_value = value;
   }

   protected SDRInt(boolean unsigned) {
      m_unsigned = unsigned;
   }

   protected SDRInt(int value, boolean unsigned) {
      this(unsigned);
      m_value = value;
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      if (m_unsigned) {
         m_value = in.readUInt(context);
      } else {
         m_value = in.readInt(context);
      }

   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      if (m_unsigned) {
         out.writeUInt(m_value, context);
      } else {
         out.writeInt(m_value, context);
      }

   }

   public void reset() {
      // TODO Auto-generated method stub

   }

   public int size() {
      return m_unsigned ? SDRUtil.sizeUInt(m_value):SDRUtil.sizeInt(m_value);
   }
}
