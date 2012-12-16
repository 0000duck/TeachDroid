package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetFirstLineChunkOut implements SDR {
   /** Int */
   public int iterHnd;
   public static final int cMaxLen_lines = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of String */
   public StringBuffer[] lines;
   /** Array length member added by SdrGen */
   public int lines_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetFirstLineChunkOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         iterHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         lines_count = in.readUInt(context);
         if (context.done)
            lines = new StringBuffer[lines_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = lines_count != 0 ? startOffset1 % lines_count : 0;
      if (startOffset1 >= lines_count)
         startIdx11 = lines_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < lines_count; i11++) {
         if (mMemberDone == actMember) {
            if (lines[i11] == null)
               lines[i11] = new StringBuffer();
            lines[i11] = in.readString(lines[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         retVal = in.readBool(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public void write(SDROutputStream out, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         out.writeInt(iterHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((lines == null) || (lines.length < lines_count) || (TCI.rpcChunkLen < lines_count)) &&
             ((lines != null) || (lines_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(lines_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = lines_count != 0 ? startOffset1 % lines_count : 0;
      if (startOffset1 >= lines_count)
         startIdx11 = lines_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < lines_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeString(lines[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         out.writeBool(retVal, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(iterHnd);
      size += SDRUtil.sizeUInt(lines_count);
      for (int i11 = 0; i11 < lines_count; i11++) {
         size += SDRUtil.sizeString(lines[i11]);
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
