package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetNextNodeChunkOut implements SDR {
   /** Int */
   public int iterHnd;
   /** Int */
   public int nrOfHnd;
   public static final int cMaxLen_nodeHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] nodeHnd;
   /** Array length member added by SdrGen */
   public int nodeHnd_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetNextNodeChunkOut() {
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
         nrOfHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         nodeHnd_count = in.readUInt(context);
         if (context.done)
            nodeHnd = new int[nodeHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = nodeHnd_count != 0 ? startOffset1 % nodeHnd_count : 0;
      if (startOffset1 >= nodeHnd_count)
         startIdx11 = nodeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < nodeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            nodeHnd[i11] = in.readInt(context);
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
         out.writeInt(nrOfHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((nodeHnd == null) || (nodeHnd.length < nodeHnd_count) || (TCI.rpcChunkLen < nodeHnd_count)) &&
             ((nodeHnd != null) || (nodeHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(nodeHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = nodeHnd_count != 0 ? startOffset1 % nodeHnd_count : 0;
      if (startOffset1 >= nodeHnd_count)
         startIdx11 = nodeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < nodeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(nodeHnd[i11], context);
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
      size += SDRUtil.sizeInt(nrOfHnd);
      size += SDRUtil.sizeUInt(nodeHnd_count);
      for (int i11 = 0; i11 < nodeHnd_count; i11++) {
         size += SDRUtil.sizeInt(nodeHnd[i11]);
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
