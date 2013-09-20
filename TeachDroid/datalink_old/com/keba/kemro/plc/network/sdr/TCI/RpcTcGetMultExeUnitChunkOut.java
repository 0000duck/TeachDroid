package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetMultExeUnitChunkOut implements SDR {
   /** Int */
   public int iterHnd;
   /** Int */
   public int nrOfExeUnitHnd;
   public static final int cMaxLen_upperExeUnitHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] upperExeUnitHnd;
   /** Array length member added by SdrGen */
   public int upperExeUnitHnd_count;
   public static final int cMaxLen_exeUnitHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] exeUnitHnd;
   /** Array length member added by SdrGen */
   public int exeUnitHnd_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetMultExeUnitChunkOut() {
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
         nrOfExeUnitHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         upperExeUnitHnd_count = in.readUInt(context);
         if (context.done)
            upperExeUnitHnd = new int[upperExeUnitHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = upperExeUnitHnd_count != 0 ? startOffset1 % upperExeUnitHnd_count : 0;
      if (startOffset1 >= upperExeUnitHnd_count)
         startIdx11 = upperExeUnitHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < upperExeUnitHnd_count; i11++) {
         if (mMemberDone == actMember) {
            upperExeUnitHnd[i11] = in.readInt(context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         exeUnitHnd_count = in.readUInt(context);
         if (context.done)
            exeUnitHnd = new int[exeUnitHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = exeUnitHnd_count != 0 ? startOffset2 % exeUnitHnd_count : 0;
      if (startOffset2 >= exeUnitHnd_count)
         startIdx21 = exeUnitHnd_count;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < exeUnitHnd_count; i21++) {
         if (mMemberDone == actMember) {
            exeUnitHnd[i21] = in.readInt(context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx21 = 0;
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
         out.writeInt(nrOfExeUnitHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((upperExeUnitHnd == null) || (upperExeUnitHnd.length < upperExeUnitHnd_count) || (TCI.rpcChunkLen < upperExeUnitHnd_count)) &&
             ((upperExeUnitHnd != null) || (upperExeUnitHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(upperExeUnitHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = upperExeUnitHnd_count != 0 ? startOffset1 % upperExeUnitHnd_count : 0;
      if (startOffset1 >= upperExeUnitHnd_count)
         startIdx11 = upperExeUnitHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < upperExeUnitHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(upperExeUnitHnd[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         if (((exeUnitHnd == null) || (exeUnitHnd.length < exeUnitHnd_count) || (TCI.rpcChunkLen < exeUnitHnd_count)) &&
             ((exeUnitHnd != null) || (exeUnitHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(exeUnitHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = exeUnitHnd_count != 0 ? startOffset2 % exeUnitHnd_count : 0;
      if (startOffset2 >= exeUnitHnd_count)
         startIdx21 = exeUnitHnd_count;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < exeUnitHnd_count; i21++) {
         if (mMemberDone == actMember) {
            out.writeInt(exeUnitHnd[i21], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx21 = 0;
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
      size += SDRUtil.sizeInt(nrOfExeUnitHnd);
      size += SDRUtil.sizeUInt(upperExeUnitHnd_count);
      for (int i11 = 0; i11 < upperExeUnitHnd_count; i11++) {
         size += SDRUtil.sizeInt(upperExeUnitHnd[i11]);
      }
      size += SDRUtil.sizeUInt(exeUnitHnd_count);
      for (int i21 = 0; i21 < exeUnitHnd_count; i21++) {
         size += SDRUtil.sizeInt(exeUnitHnd[i21]);
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
