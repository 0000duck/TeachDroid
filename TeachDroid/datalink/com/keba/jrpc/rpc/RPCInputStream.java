package com.keba.jrpc.rpc;

import java.io.*;

public class RPCInputStream {
   protected static final String EMPTY_STRING = "";
   protected static final byte[] EMPTY_ARRAY = new byte[0];
   protected InputStream in;
   protected byte[] buf;
   protected int pos, end;
   protected int size;
   protected int remainder;
   protected boolean isLastFragment;
   protected boolean newRun;


   public RPCInputStream (InputStream in, int bufferSize) {
      this.in = in;
      size = bufferSize;
      buf = new byte[bufferSize];
      end = 0;
      pos = 0;
      remainder = 0;
      isLastFragment = true;
      newRun = true;
   }

   protected boolean isEmpty () throws IOException  {
      if (!isLastFragment && (remainder == 0)) {
         // read next record fragment
         int rm = (((((int) in.read())  & 0xff) << 24) | ((((int) in.read()) & 0xff) << 16)
                  | ((((int) in.read()) & 0xff) << 8) | (((int) in.read()) & 0xff));
         if (rm == -1) {
            throw new IOException("socket closed by peer");
         }
         isLastFragment = (rm & 0x80000000) == 0x80000000;
         remainder = rm & 0x7fffffff;
         return isLastFragment && (remainder == 0);
      }
      return isLastFragment && (remainder == 0);
   }

   protected void readBuffer (int min) throws RPCException, IOException {
      int r = end - pos;
      if (r > 0) {
         System.arraycopy(buf, pos, buf, 0, r);
         pos = 0;
         end = r;
      } else {
         pos = 0;
         end = 0;
      }
      while (min > end - pos) {
         if (remainder > 0) {
            r = size - end;
            if (remainder < r) {
               r = remainder;
            }
            r = in.read(buf, end, r);
            if (r == -1) {
               throw new IOException("socket closed by peer");
            }
            end += r;
            remainder -= r;
         } else {
            if (newRun || !isLastFragment) {
               newRun = false;
               // read next record fragment
               int rm = (((((int) in.read())  & 0xff) << 24) | ((((int) in.read()) & 0xff) << 16)
                        | ((((int) in.read()) & 0xff) << 8) | (((int) in.read()) & 0xff));
               if (rm == -1) {
                  throw new IOException("socket closed by peer");
               }
               isLastFragment = (rm & 0x80000000) == 0x80000000;
               remainder = rm & 0x7fffffff;
            } else {
               // error: data expected
               throw new RPCException(RPCException.RECORD_ERROR, "record marking standard error");
            }
         }
      }
   }

   public void skip () throws IOException  {
      end = 0;
      pos = 0;
      remainder = 0;
      isLastFragment = true;
      newRun = true;
      in.skip(in.available());
   }

   public int readInt () throws RPCException, IOException {
      try {
         if (pos + 4 > end) {
            readBuffer(4);
         }
         return (((((int) buf[pos++])  & 0xff) << 24) | ((((int) buf[pos++]) & 0xff) << 16)
                | ((((int) buf[pos++]) & 0xff) << 8) | (((int) buf[pos++]) & 0xff));
      } catch (RPCException rpce) {
         // error: data expected
         throw new RPCException(RPCException.RECORD_ERROR, "readInt(): more data expected -> record marking standard error");
      }
   }

   public long readHyper () throws RPCException, IOException {
      try {
         if (pos + 8 > end) {
            readBuffer(8);
         }
         return (((((long) buf[pos++]) & 0xff) << 56) | ((((long) buf[pos++]) & 0xff) << 48)
                | ((((long) buf[pos++]) & 0xff) << 40) | ((((long) buf[pos++]) & 0xff) << 32)
                | ((((long) buf[pos++]) & 0xff) << 24) | ((((long) buf[pos++]) & 0xff) << 16)
                | ((((long) buf[pos++]) & 0xff) << 8) | (((long) buf[pos++]) & 0xff));
      } catch (RPCException rpce) {
         // error: data expected
         throw new RPCException(RPCException.RECORD_ERROR, "readHyper(): more data expected -> record marking standard error");
      }
   }

   public float readFloat () throws RPCException, IOException {
      try {
         return Float.intBitsToFloat(readInt());
      } catch (RPCException rpce) {
         // error: data expected
         throw new RPCException(RPCException.RECORD_ERROR, "readFloat(): more data expected -> record marking standard error");
      }
   }
   public double readDouble () throws RPCException, IOException {
      try {
         return Double.longBitsToDouble(readHyper());
      } catch (RPCException rpce) {
         // error: data expected
         throw new RPCException(RPCException.RECORD_ERROR, "readDouble(): more data expected -> record marking standard error");
      }
   }
   public boolean readBool () throws RPCException, IOException {
      try {
         return readInt() == 1;
      } catch (RPCException rpce) {
         // error: data expected
         throw new RPCException(RPCException.RECORD_ERROR, "readBool(): more data expected -> record marking standard error");
      }
   }
   public String readString () throws RPCException, IOException {
      try {
         byte[] s = readBytes();
         return (0 < s.length) ? new String(s, "ISO-8859-1"): EMPTY_STRING;
      } catch (RPCException rpce) {
         // error: data expected
         throw new RPCException(RPCException.RECORD_ERROR,
         "readString(): more data expected -> record marking standard error/rread string: ");
      }
   }

   public byte[] readBytes () throws RPCException, IOException {
       try {
        int len = readInt();
         if (len > 0) {
            return readBytes(len);
         }
         return EMPTY_ARRAY;
      } catch (RPCException rpce) {
         // error: data expected
         throw new RPCException(RPCException.RECORD_ERROR, "readBytes(): more data expected -> record marking standard error");
      }
   }
   public byte[] readBytes (int len) throws RPCException, IOException {
      byte[] buffer;
      int x = end - pos;
      int i = 0;
      int r = len;
      try {
         buffer = new byte[len];
      } catch (java.lang.OutOfMemoryError err) {
         throw new RPCException(RPCException.RECORD_ERROR, err.toString()
         + ", readBytes(int len) len=" +len + " "
         + "pos: " + pos + ", end: " +end + ", remainder: " + remainder);
      }
      if (x > 0) {
         if (x > len) {
            x = len;
         }
         System.arraycopy(buf, pos, buffer, 0, x);
         pos += x;
         r -= x;
         i = x;
      }
      while (r > 0) {
         if (remainder > 0) {
            x = r;
            if (r > remainder) {
               x = remainder;
            }
            x = in.read(buffer, i, x);
            if (x == -1) {
               throw new IOException("socket closed by peer");
            }
            i += x;
            remainder -= x;
            r -= x;
         } else {
            if (newRun || !isLastFragment) {
               newRun = false;
               // read next record fragment
               int rm = (((((int) in.read())  & 0xff) << 24) | ((((int) in.read()) & 0xff) << 16)
                        | ((((int) in.read()) & 0xff) << 8) | (((int) in.read()) & 0xff));
               if (rm == -1) {
                  throw new IOException("socket closed by peer");
               }
               isLastFragment = (rm & 0x80000000) == 0x80000000;
               remainder = rm & 0x7fffffff;
            } else {
               // error: data expected
               throw new RPCException(RPCException.RECORD_ERROR, "readBytes(int len): more data expected -> record marking standard error");
            }
         }
      }

      int min = (4 - (len % 4)) % 4;
      if (min > end - pos) {
         readBuffer(min);
      }
      pos += min;
      return buffer;
   }

}