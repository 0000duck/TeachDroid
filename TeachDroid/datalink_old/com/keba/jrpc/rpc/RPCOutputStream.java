package com.keba.jrpc.rpc;

import java.io.*;

public class RPCOutputStream {
   protected byte[] buf;
   protected OutputStream out;
   int count;

   public RPCOutputStream(OutputStream out, int bufferSize) {
      this.out = out;
      buf = new byte[bufferSize];
      count = 4;
   }

   protected void flushBuffer() throws IOException {
      if (count > 4) {
         // send one record fragment
         int i = count;
         count = 0;
         writeInt(i - 4); // write length of record fragment
	      out.write(buf, 0, i);
         out.flush();
	      count = 4;
      }
   }

   public void flush () throws IOException {
      // send last record fragment
      int i = count;
      count = 0;
      writeInt((i - 4) | 0x80000000); // write length of record fragment
      out.write(buf, 0, i);
      out.flush();
      count = 4;
   }
   
   public void writeInt (int v) throws IOException {
      if (count + 4 >= buf.length) {
          flushBuffer();
      }
      buf[count++] = (byte)(v >>> 24);
      buf[count++] = (byte)(v >>> 16);
      buf[count++] = (byte)(v >>> 8);
      buf[count++] = (byte)v;
   }

   public void writeHyper (long v) throws IOException {
      if (count + 8 >= buf.length) {
          flushBuffer();
      }
      buf[count++] = (byte)(v >>> 56);
      buf[count++] = (byte)(v >>> 48);
      buf[count++] = (byte)(v >>> 40);
      buf[count++] = (byte)(v >>> 32);
      buf[count++] = (byte)(v >>> 24);
      buf[count++] = (byte)(v >>> 16);
      buf[count++] = (byte)(v >>> 8);
      buf[count++] = (byte) v;
   }

   public void writeFloat (float v) throws IOException {
      writeInt(Float.floatToIntBits(v));
   }

   public void writeDouble (double v) throws IOException {
      writeHyper(Double.doubleToLongBits(v));
   }

   public void writeBool (boolean v) throws IOException {
      writeInt(v ? 1: 0);
   }

   public void writeString (String v) throws IOException {
      if(v != null)
          writeBytes(v.getBytes("ISO-8859-1"));
      else
          writeInt(0);  // String mit Laenge 0
   }

   public void writeBytes (byte[] b) throws IOException  {
      int l = b.length;
      writeInt(l);
      writeBytes(b, l);
   }
   public void writeBytes (byte[] b, int len) throws IOException  {
      int r = (4 - (len % 4)) % 4;
      int pos = 0;

      if (len + r > buf.length - count) {
         int i = count;
         count = 0;
         writeInt(i - 4 + len + r); // write length of record fragment
         out.write(buf, 0, i);
         out.write(b, 0, len);
         switch (r) {
         case 3: out.write(0);
         case 2: out.write(0);
         case 1: out.write(0);
         }
         out.flush();
         count = 4;
      } else {
         System.arraycopy(b, 0, buf, count, len);
         count += len;
         switch (r) {
         case 3: buf[count++] = 0;
         case 2: buf[count++] = 0;
         case 1: buf[count++] = 0;
         }
      }
   }

}