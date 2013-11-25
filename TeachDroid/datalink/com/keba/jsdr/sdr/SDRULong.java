
package com.keba.jsdr.sdr;

public class SDRULong extends SDRLong {
   public SDRULong() {
      super(true);
   }

   public SDRULong(long value) {
      super(value, true);
   }
}
