
package com.keba.jsdr.sdr;

import java.io.IOException;
import java.io.OutputStream;

public class SDROutputStream {
   protected byte[]           buf;
   protected static final int BUFFER_SIZE = 1032;
   protected OutputStream     m_outStream;
   protected int              bufWritePos;

   public SDROutputStream(OutputStream out) {
      this.m_outStream = out;
      buf = new byte[BUFFER_SIZE];
      bufWritePos = 0;
   }

   public void writeInt(int value, SDRContext context) throws SDRException,
         IOException {

      if (!serializeBuffer(context)) {
         if ((value >> 5) == 0 || (value >> 5) == -1) { // 1 byte coding
            writerRawValue((byte) (value & SDRUtil.MASK_6Bit), context);
         } else if ((value >> 13) == 0 || (value >> 13) == -1) { // 2 byte
            // coding
            writerRawValue(
                  (byte) ((getByte2(value) & SDRUtil.MASK_6Bit) | 1 << 6),
                  context);
            writerRawValue(getByte1(value), context);
         } else if ((value >> 21) == 0 || (value >> 21) == -1) { // 3 byte
            // coding
            writerRawValue(
                  (byte) (getByte3(value) & SDRUtil.MASK_6Bit | (byte) (2 << 6)),
                  context);
            writerRawValue(getByte2(value), context);
            writerRawValue(getByte1(value), context);

         } else { // 5 byte coding
            writerRawValue((byte) (3 << 6), context);
            writerRawValue(getByte4(value), context);
            writerRawValue(getByte3(value), context);
            writerRawValue(getByte2(value), context);
            writerRawValue(getByte1(value), context);
         }
      }
   }

   private void writerRawValue(byte value, SDRContext context)
         throws IOException {
      if (context.remainingStreamBytes == 0) {
         context.buffer[context.byteCnt] = value;
         context.byteCnt++;
         context.done = false;
      } else {
         buf[bufWritePos] = value;
         bufWritePos++;
         context.remainingStreamBytes--;
         context.done = true;
      }
   }

   public void writerRawValue(byte value) throws IOException {

      buf[bufWritePos] = value;
      bufWritePos++;
   }

   private boolean serializeBuffer(SDRContext context) throws IOException {
      if (context.byteCnt == 0) {
         return false;
      } else {
         writeBytesToStream(context.buffer, 0, context.byteCnt);
         context.remainingStreamBytes -= context.byteCnt;
         context.byteCnt = 0;
         context.done = true;
         return true;
      }
   }

   public void writeUInt(int value, SDRContext context) throws SDRException,
         IOException {
      if (!serializeBuffer(context)) {
         if ((value & ~SDRUtil.MASK_6Bit) == 0) { // 1 byte coding
            writeByte((byte) value, context);
         } else if ((value & ~SDRUtil.MASK_14Bit) == 0) { // 2 byte
            // coding
            writerRawValue(
                  (byte) ((getByte2(value) & SDRUtil.MASK_6Bit) | 1 << 6),
                  context);
            writerRawValue(getByte1(value), context);
         } else if ((value & ~SDRUtil.MASK_22Bit) == 0) { // 3 byte
            // coding
            writerRawValue(
                  (byte) ((getByte3(value) & SDRUtil.MASK_6Bit) | 2 << 6),
                  context);
            writerRawValue(getByte2(value), context);
            writerRawValue(getByte1(value), context);

         } else { // 5 byte coding
            writerRawValue((byte) (3 << 6), context);
            writerRawValue(getByte4(value), context);
            writerRawValue(getByte3(value), context);
            writerRawValue(getByte2(value), context);
            writerRawValue(getByte1(value), context);
         }
      }
   }

   public void writeLong(long value, SDRContext context) throws SDRException,
         IOException {
      if (!serializeBuffer(context)) {
         if ((value >> 21) == 0 || (value >> 21) == -1) {
            writeInt((int) value, context);
         } else {// 9 byte encoding
            writerRawValue((byte) (3 << 6), context);
            writerRawValue(getByte8(value), context);
            writerRawValue(getByte7(value), context);
            writerRawValue(getByte6(value), context);
            writerRawValue(getByte5(value), context);
            writerRawValue(getByte4(value), context);
            writerRawValue(getByte3(value), context);
            writerRawValue(getByte2(value), context);
            writerRawValue(getByte1(value), context);
         }
      }

   }

   public void writeULong(long value, SDRContext context) throws SDRException,
         IOException {
      if (!serializeBuffer(context)) {
         if ((value & ~SDRUtil.MASK_22Bit) == 0) {
            writeUInt((int) value, context);
         } else {// 9 byte encoding
            writerRawValue((byte) (3 << 6), context);
            writerRawValue(getByte8(value), context);
            writerRawValue(getByte7(value), context);
            writerRawValue(getByte6(value), context);
            writerRawValue(getByte5(value), context);
            writerRawValue(getByte4(value), context);
            writerRawValue(getByte3(value), context);
            writerRawValue(getByte2(value), context);
            writerRawValue(getByte1(value), context);
         }
      }
   }

   public void writeFloat(float value, SDRContext context) throws SDRException,
         IOException {
      if (!serializeBuffer(context)) {
         int intBits = Float.floatToIntBits(value);
         writerRawValue((byte) (intBits >>> 24), context);
         writerRawValue((byte) (intBits >>> 16), context);
         writerRawValue((byte) (intBits >>> 8), context);
         writerRawValue((byte) intBits, context);
      }

   }

   public void writeDouble(double value, SDRContext context)
         throws SDRException, IOException {
      if (!serializeBuffer(context)) {
         long longBits = Double.doubleToLongBits(value);
         writerRawValue((byte) (longBits >>> 56), context);
         writerRawValue((byte) (longBits >>> 48), context);
         writerRawValue((byte) (longBits >>> 40), context);
         writerRawValue((byte) (longBits >>> 32), context);
         writerRawValue((byte) (longBits >>> 24), context);
         writerRawValue((byte) (longBits >>> 16), context);
         writerRawValue((byte) (longBits >>> 8), context);
         writerRawValue((byte) longBits, context);
      }
   }

   public void writeBool(boolean value, SDRContext context)
         throws SDRException, IOException {
      if (!serializeBuffer(context)) {
         writeInt((byte) (value ? 1 : 0), context);
      }

   }

   public void writeString(StringBuffer value, SDRContext context)
         throws SDRException, IOException {
      byte[] tmp = value.toString().getBytes("ISO-8859-1");
      if (!serializeBuffer(context)) { // serialize stored
         // stringlength
         if ((context).remainingDataBytes == -1) {
            (context).remainingDataBytes = tmp.length;
            writeUInt((context).remainingDataBytes, context);
            if (!context.done) {
               return;
            }
         }
      }

      writeBytes(tmp, tmp.length, context);
   }

   public void writeBytes(byte[] b, int length, SDRContext context)
         throws SDRException, IOException {
      int cnt = 0;
      if (length == 0) {
         context.done = true;
      } else {
         if ((context).remainingDataBytes == -1) {
            (context).remainingDataBytes = length;
         }
         cnt = Math.min((context).remainingStreamBytes,
               (context).remainingDataBytes);
         writeBytesToStream(b, length - (context).remainingDataBytes, cnt);
         (context).remainingDataBytes -= cnt;
         (context).remainingStreamBytes -= cnt;
         context.done = (context).remainingDataBytes == 0;
      }
      if (context.done) {
         (context).remainingDataBytes = -1;
      }
   }

   private void writeBytesToStream(byte[] b, int offset, int length)
         throws IOException {
      System.arraycopy(b, offset, buf, bufWritePos, length);
      bufWritePos += length;
   }

   public void writeByte(byte value, SDRContext context) throws SDRException,
         IOException {
      if (!serializeBuffer(context)) {
         writerRawValue(value, context);
      }
   }

   public void writeUByte(byte value, SDRContext context) throws SDRException,
         IOException {
      if (!serializeBuffer(context)) {
         writerRawValue(value, context);
      }
   }

   public void writeShort(short value, SDRContext context) throws SDRException,
         IOException {
      writeInt(value, context);
   }

   public void writeUShort(short value, SDRContext context)
         throws SDRException, IOException {
      writeUInt(value & 0xFFFF, context);
   }

   public void flush() throws IOException {
      m_outStream.write(buf, 0, bufWritePos);
      m_outStream.flush();
      bufWritePos = 0;
   }

   private byte getByte1(int v) {
      return (byte) v;
   }

   private byte getByte2(int v) {
      return (byte) (v >> 8);
   }

   private byte getByte3(int v) {
      return (byte) (v >> 16);
   }

   private byte getByte4(int v) {
      return (byte) (v >> 24);
   }

   private byte getByte1(long v) {
      return (byte) v;
   }

   private byte getByte2(long v) {
      return (byte) (v >> 8);
   }

   private byte getByte3(long v) {
      return (byte) (v >> 16);
   }

   private byte getByte4(long v) {
      return (byte) (v >> 24);
   }

   private byte getByte5(long v) {
      return (byte) (v >> 32);
   }

   private byte getByte6(long v) {
      return (byte) (v >> 40);
   }

   private byte getByte7(long v) {
      return (byte) (v >> 48);
   }

   private byte getByte8(long v) {
      return (byte) (v >> 52);
   }

}
