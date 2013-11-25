
package com.keba.jsdr.sdr;

public class SDRUtil {
   public static final int MASK_6Bit  = 0x3F;     // 2^6-1;
   public static final int MASK_14Bit = 0x3FFF;   // 2^14-1;
   public static final int MASK_22Bit = 0x3FFFFF; // 2^22-1;

   public static final int sizeUInt(int value) {
      int size = 0;
      if ((value & ~SDRUtil.MASK_6Bit) == 0) { // 1 byte coding
         size = 1;
      } else if ((value & ~SDRUtil.MASK_14Bit) == 0) { // 2 byte coding
         size = 2;
      } else if ((value & ~SDRUtil.MASK_22Bit) == 0) { // 3 byte coding
         size = 3;

      } else { // 5 byte coding
         size = 5;
      }
      return size;
   }

   public static final int sizeULong(long value) {
      int size = 0;
      if ((value & ~SDRUtil.MASK_22Bit) == 0) { // 3 byte coding
         size = sizeUInt((int) value);
      } else { // 5 byte coding
         size = 9;
      }
      return size;
   }
   
   public static final int sizeUShort(short value) {
      int size = 0;
      if ((value & ~SDRUtil.MASK_22Bit) == 0) { // 3 byte coding
         size = sizeUInt(value);
      } else { // 5 byte coding
         size = 9;
      }
      return size;
   }

   public static final int sizeInt(int value) {
      int size = 0;
      if (value >> 5 == 0 || value >> 5 == -1) { // 1 byte coding
         size = 1;
      } else if (value >> 13 == 0 || value >> 13 == -1) { // 2 byte coding
         size = 2;
      } else if ((value >> 21 == 0 || value >> 21 == -1)) { // 3 byte coding
         size = 3;

      } else { // 5 byte coding
         size = 5;
      }
      return size;
   }

   public static final int sizeLong(long value) {
      int size = 0;
      if ((value >> 21 == 0 || value >> 21 == -1)) { // 3 byte coding
         size = sizeInt((int) value);

      } else { // 5 byte coding
         size = 9;
      }
      return size;
   }
   
   public static final int sizeShort(short value) {
      int size = 0;
      if ((value >> 21 == 0 || value >> 21 == -1)) { // 3 byte coding
         size = sizeInt(value);

      } else { // 5 byte coding
         size = 9;
      }
      return size;
   }
   
   public static final int sizeString(StringBuffer string) {
      return sizeUInt(string.length())+string.length();
      
   }
   
   public static final int sizeBytes(byte[] bytes, int length) {
      return length;
   }
   
   public static final int sizeByte(byte value) {
      return 1;
   }
   public static final int sizeUByte(byte value) {
      return 1;
   }
   
   public static final int sizeBool(boolean value) {
      return 1;
   }
   
   public static final int sizeDouble(double value) {
      return 8;
   }
   
   public static final int sizeFloat(float value) {
      return 4;
   }

}
