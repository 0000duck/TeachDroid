
package com.keba.jsdr.sdr;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class for serializing data into the sdr format
 * @author gas
 *
 */
public class SDRInputStream {
   protected static final int BUFFER_SIZE = 1024;
   protected InputStream      m_inStream;

   protected byte[]           buf;
   protected int              bufferReadPos;

   protected int              bufferEnd;

   protected int              size;

   

   public SDRInputStream(InputStream in) {
      m_inStream = in;
      size = 0;
      buf = new byte[BUFFER_SIZE];
      bufferReadPos = 0;
      bufferEnd = 0;
   }

   public void skip() throws IOException {
      bufferReadPos = 0;
      bufferEnd = 0;
      size =0;
      m_inStream.skip(m_inStream.available());
   }
   
   public void skip(int bytes) throws IOException {
      if(size == 0 && bytes >0) {
         readBuffer(-1);
      }
      if(bytes == 0) {
         return;
      }
      bufferReadPos+=bytes;
      if(bufferReadPos >= BUFFER_SIZE) {
         bufferReadPos = bufferReadPos-BUFFER_SIZE;
      }
      size-=bytes;
   }

   public int readInt(SDRContext context) throws SDRException, IOException {
      return readInt(context, true);
   }

   private int readInt(SDRContext context, boolean invalidate)
         throws SDRException, IOException {
      int value = 0;

      byte byte1 = (byte) (readRawValue(context) & 0xff);
      int codeType = (byte) (byte1 >>> 6) & 0x03;
      byte1 = (byte) ((byte1 & 0xff) << 2);
      byte1 = (byte) (byte1 >> 2);
      switch (codeType) {
      case 0:
         value = byte1;
         break;
      case 1:
         value = (byte1) << 8;
         value |= (readRawValue(context) & 0xff);
         break;
      case 2:
         value = (byte1) << 16;
         value |= (readRawValue(context) & 0xff) << 8;
         value |= (readRawValue(context) & 0xff);
         break;
      case 3:
         value |= (readRawValue(context) & 0xff) << 24;
         value |= (readRawValue(context) & 0xff) << 16;
         value |= (readRawValue(context) & 0xff) << 8;
         value |= (readRawValue(context) & 0xff);
         break;
      }
      if (invalidate) {
         invalidateContext(context);
      }

      return value;

   }

   private void invalidateContext(SDRContext context) {
      if (context.done) {
         context.byteCnt = 0;
         (context).remainingDataBytes = -1;
      }
      (context).bufferReadIdx = 0;
   }

   public int readUInt(SDRContext context) throws SDRException, IOException {
      return readUInt(context, true);
   }

   private int readUInt(SDRContext context, boolean invalidate)
         throws SDRException, IOException {
      int value = 0;
      byte byte1 = (byte) (readRawValue(context) & 0xff);
      int codeType = (byte) (byte1 >>> 6) & 0x03;

      switch (codeType) {
      case 0:
         value = byte1 & SDRUtil.MASK_6Bit;
         break;
      case 1:
         value = (byte1 & SDRUtil.MASK_6Bit) << 8;
         value |= (readRawValue(context) & 0xff);
         break;
      case 2:
         value = (byte1 & SDRUtil.MASK_6Bit) << 16;
         value |= (readRawValue(context) & 0xff) << 8;
         value |= (readRawValue(context) & 0xff);
         break;
      case 3:
         value |= (readRawValue(context) & 0xff) << 24;
         value |= (readRawValue(context) & 0xff) << 16;
         value |= (readRawValue(context) & 0xff) << 8;
         value |= (readRawValue(context) & 0xff);
         break;
      }
      if (invalidate) {
         invalidateContext(context);
      }

      return value;
   }

   public long readLong(SDRContext context) throws SDRException, IOException {
      long value = -1;
      byte byte1 = (byte) (readRawValue(context) & 0xff);
      if (context.done) {
         int codeType = (byte) (byte1 >>> 6) & 0x03;
         switch (codeType) {
         case 0:
         case 1:
         case 2:
            context.bufferReadIdx--;
            value = readInt(context);
            break;
         case 3:
            value = 0;
            value |= (((long) readRawValue(context)) & 0xff) << 56;
            value |= (((long) readRawValue(context)) & 0xff) << 48;
            value |= (((long) readRawValue(context)) & 0xff) << 40;
            value |= (((long) readRawValue(context)) & 0xff) << 32;
            value |= (((long) readRawValue(context)) & 0xff) << 24;
            value |= (((long) readRawValue(context)) & 0xff) << 16;
            value |= (((long) readRawValue(context)) & 0xff) << 8;
            value |= (readRawValue(context) & 0xff);
            invalidateContext(context);
            break;
         }
      }
      return value;
   }

   public long readULong(SDRContext context) throws SDRException, IOException {
      long value = -1;
      byte byte1 = (byte) (readRawValue(context) & 0xff);
      if (context.done) {
         int codeType = (byte) (byte1 >>> 6) & 0x03;
         if (codeType == 3) {
            value = 0;
            value |= (long) (readRawValue(context) & 0xff) << 56;
            value |= (long) (readRawValue(context) & 0xff) << 48;
            value |= (long) (readRawValue(context) & 0xff) << 40;
            value |= (long) (readRawValue(context) & 0xff) << 32;
            value |= (long) (readRawValue(context) & 0xff) << 24;
            value |= (long) (readRawValue(context) & 0xff) << 16;
            value |= (long) (readRawValue(context) & 0xff) << 8;
            value |= (readRawValue(context) & 0xff);
            invalidateContext(context);
         } else {
            context.bufferReadIdx--;
            value = readUInt(context);
         }
      }
      return value;
   }

   public float readFloat(SDRContext context) throws SDRException, IOException {
      int iValue = 0;
      iValue |= (readRawValue(context) & 0xff) << 24;
      iValue |= (readRawValue(context) & 0xff) << 16;
      iValue |= (readRawValue(context) & 0xff) << 8;
      iValue |= (readRawValue(context) & 0xff);

      float value = -1;
      if (context.done) {
         value = Float.intBitsToFloat(iValue);
      }
      invalidateContext(context);
      return value;
   }

   public double readDouble(SDRContext context) throws SDRException,
         IOException {
      double value = -1;
      long lValue = 0;
      lValue |= (((long) readRawValue(context)) & 0xff) << 56;
      lValue |= (((long) readRawValue(context)) & 0xff) << 48;
      lValue |= (((long) readRawValue(context)) & 0xff) << 40;
      lValue |= (((long) readRawValue(context)) & 0xff) << 32;
      lValue |= (((long) readRawValue(context)) & 0xff) << 24;
      lValue |= (((long) readRawValue(context)) & 0xff) << 16;
      lValue |= (((long) readRawValue(context)) & 0xff) << 8;
      lValue |= ((long) readRawValue(context)) & 0xff;
      if (context.done) {
         value = Double.longBitsToDouble(lValue);
      }
      invalidateContext(context);
      return value;
   }

   public boolean readBool(SDRContext context) throws SDRException, IOException {
      return readInt(context) > 0;
   }

   public StringBuffer readString(StringBuffer in, SDRContext context)
         throws SDRException, IOException {
      StringBuffer buffer = in;
      if (buffer == null) {
         buffer = new StringBuffer();
      }

      if (context.remainingDataBytes == -1) {
         int strLength = readUInt(context, false);
         if (context.done) {
            context.remainingDataBytes = strLength;
            in.setLength(0);
            if (strLength == 0) {
               invalidateContext(context);
               return in;
            }
         } else {
            invalidateContext(context);
            return in;
         }
      }

      context.done = context.remainingStreamBytes > 0;

      if (context.done) {
         int cnt = Math.min(context.remainingDataBytes,
               context.remainingStreamBytes);
         ensureAvailableBytes(cnt);
         byte[] array = new byte[cnt];

         if (bufferReadPos + cnt > BUFFER_SIZE) {
            int tmpCnt = BUFFER_SIZE - bufferReadPos;
            System.arraycopy(buf, bufferReadPos, array, 0, tmpCnt);
            System.arraycopy(buf, 0, array, tmpCnt, cnt - tmpCnt);
            bufferReadPos = cnt - tmpCnt;
         } else {
            System.arraycopy(buf, bufferReadPos, array, 0, cnt);
            bufferReadPos += cnt;

         }
         if(bufferReadPos == BUFFER_SIZE) {
            bufferReadPos = 0;
         }
         size -= cnt;
         context.remainingDataBytes -= cnt;
         context.remainingStreamBytes -= cnt;
         in.append(new String(array, "ISO-8859-1"));
         context.done = context.remainingDataBytes == 0;
      }
      invalidateContext(context);
      return buffer;
   }

   public byte readByte(SDRContext context) throws SDRException, IOException {
      byte value = readRawValue(context);
      invalidateContext(context);
      return value;

   }

   public byte readUByte(SDRContext context) throws SDRException, IOException {
      byte value = readRawValue(context);
      invalidateContext(context);
      return value;
   }

   public short readShort(SDRContext context) throws SDRException, IOException {
      short signedShort = (short) readInt(context);
      if ((signedShort >>> 15) == 1) {
         signedShort |= 0x80000000;
         signedShort &= 0xFFFF7FFF;
      }
      return signedShort;
   }

   public short readUShort(SDRContext context) throws SDRException, IOException {
      return (short) readUInt(context);
   }

   public byte[] readBytes(byte[] b, int length, SDRContext context)
         throws SDRException, IOException {
      byte[] array = b;
      if (array == null) {
         array = new byte[length];
      }
      if (length == 0) {
         context.done = true;
      } else {
         if (context.remainingDataBytes == -1) {
            context.remainingDataBytes = length;
         }
         context.done = context.remainingStreamBytes > 0;

         if (context.done) {
            int cnt = Math.min(context.remainingDataBytes,
                  context.remainingStreamBytes);
            ensureAvailableBytes(cnt);

            if (bufferReadPos + cnt > BUFFER_SIZE) {
               int tmpCnt = BUFFER_SIZE - bufferReadPos;
               System.arraycopy(buf, bufferReadPos, array, length
                     - context.remainingDataBytes, tmpCnt);

               
               System.arraycopy(buf, 0, array, length
                     - context.remainingDataBytes + tmpCnt, cnt - tmpCnt);
               bufferReadPos = cnt - tmpCnt;
            } else {
               System.arraycopy(buf, bufferReadPos, array, length
                     - context.remainingDataBytes, cnt);
               bufferReadPos += cnt;

            }
            if(bufferReadPos == BUFFER_SIZE) {
               bufferReadPos = 0;
            }
            size -= cnt;
            context.remainingDataBytes -= cnt;
            context.remainingStreamBytes -= cnt;
            context.done = context.remainingDataBytes == 0;
         }
      }
      invalidateContext(context);
      return array;
   }

   private byte readRawValue(SDRContext context) throws IOException {
      byte value = -1;
      if (context.byteCnt == context.bufferReadIdx) {
         // nothing to read from the contextbuffer
         if (context.remainingStreamBytes == 0) {
            context.done = false;
         } else {

            readBuffer(1);
            value = buf[bufferReadPos];
            bufferReadPos++;
            if(bufferReadPos == BUFFER_SIZE) {
               bufferReadPos=0;
            }
            context.remainingStreamBytes--;
            size--;
            context.buffer[context.byteCnt] = value;
            context.byteCnt++;
            context.bufferReadIdx++;
            context.done = true;
         }
      } else {
         value = context.buffer[context.bufferReadIdx];
         context.bufferReadIdx++;
         context.done = true;
      }
      return value;
   }

   public byte readRawValue() throws IOException {
      byte value = -1;

      readBuffer(1);
      value = buf[bufferReadPos];
      bufferReadPos++;
      if(bufferReadPos == BUFFER_SIZE) {
         bufferReadPos=0;
      }
      size--;
      return value;
   }

 
   private void readBuffer(int min) throws IOException {
      while (size == 0 || size < min) {
         int cnt = 0;
         if(size != 0) {
            if(bufferEnd < bufferReadPos) {
               cnt = m_inStream.read(buf, bufferEnd, bufferReadPos - bufferEnd);
            } else {
               cnt = m_inStream.read(buf, bufferEnd, BUFFER_SIZE - bufferEnd);
            }
         } else {
            cnt = m_inStream.read(buf, bufferReadPos, BUFFER_SIZE - bufferEnd);   
         }
         if(cnt == -1){
            throw new IOException("socket closed by peer");
         }
         size += cnt;
         bufferEnd += cnt;
         if (bufferEnd == BUFFER_SIZE) {
            bufferEnd = 0;
         }
      }
   }

   public void ensureAvailableBytes(int length) throws IOException {
      readBuffer(length);
   }

}
