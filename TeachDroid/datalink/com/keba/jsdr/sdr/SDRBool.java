
package com.keba.jsdr.sdr;

import java.io.IOException;

public class SDRBool implements SDR {

   public boolean m_value;

   public SDRBool() {
   }

   public SDRBool(boolean value) {
      m_value = value;
   };

   public void read(SDRInputStream in, SDRContext context) throws SDRException,
         IOException {
      m_value = in.readBool(context);

   }

   public void reset() {
      // TODO Auto-generated method stub

   }

   public int size() {
      return 1;
   }

   public void write(SDROutputStream out, SDRContext context)
         throws SDRException, IOException {
      out.writeBool(m_value, context);

   }

}
