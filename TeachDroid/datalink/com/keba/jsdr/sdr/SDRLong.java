
package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRLong implements SDR {
   public long       m_value;
   protected boolean m_unsigned;

   public SDRLong() {
      this(false);
   }

   public SDRLong(long value) {
      this(value, false);
   }

   protected SDRLong(boolean unsigned) {
      m_unsigned = unsigned;
   }

   public SDRLong(long value, boolean unsigned) {
      this(unsigned);
      m_value = value;
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      if (m_unsigned) {
         m_value = in.readULong(context);
      } else {
         m_value = in.readLong(context);
      }

   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      if (m_unsigned) {
         out.writeULong(m_value, context);
      } else {
         out.writeLong(m_value, context);
      }

   }

   public void reset() {
      // TODO Auto-generated method stub

   }

   public int size() {
      return m_unsigned ? SDRUtil.sizeULong(m_value):SDRUtil.sizeLong(m_value);
   }

}
