package com.keba.jsdr.sdr;

public class SDRUInt extends SDRInt {
   public SDRUInt() {
      super(true);
   }

   public SDRUInt(int value) {
      super(value, true);
   }
}
