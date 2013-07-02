
package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRByte implements SDR {

   public byte     m_value;
   private boolean m_unsigned;

   public SDRByte() {
      this(false);
   }

   public SDRByte(byte value) {
      this(value, false);
   }

   protected SDRByte(boolean unsigned) {
      m_unsigned = unsigned;
   }

   protected SDRByte(byte value, boolean unsigend) {
      this(unsigend);
      m_value = value;
   }

   public void reset() {
      // TODO Auto-generated method stub

   }

   public int size() {
      return 1;
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      if (m_unsigned) {
         m_value = in.readUByte(context);
      } else {
         m_value = in.readByte(context);
      }

   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      if (m_unsigned) {
         out.writeUByte(m_value, context);
      } else {
         out.writeByte(m_value, context);
      }

   }

}
