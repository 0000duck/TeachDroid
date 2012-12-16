package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetExeUnitInfoListIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int nrOfExeUnitHnd;
   public static final int cMaxLen_exeUnitHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] exeUnitHnd;
   /** Array length member added by SdrGen */
   public int exeUnitHnd_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetExeUnitInfoListIn() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         clientId = in.readInt(context);
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
         exeUnitHnd_count = in.readUInt(context);
         if (context.done)
            exeUnitHnd = new int[exeUnitHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = exeUnitHnd_count != 0 ? startOffset1 % exeUnitHnd_count : 0;
      if (startOffset1 >= exeUnitHnd_count)
         startIdx11 = exeUnitHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < exeUnitHnd_count; i11++) {
         if (mMemberDone == actMember) {
            exeUnitHnd[i11] = in.readInt(context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public void write(SDROutputStream out, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         out.writeInt(clientId, context);
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
         if (((exeUnitHnd == null) || (exeUnitHnd.length < exeUnitHnd_count) || (TCI.rpcChunkLen < exeUnitHnd_count)) &&
             ((exeUnitHnd != null) || (exeUnitHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(exeUnitHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = exeUnitHnd_count != 0 ? startOffset1 % exeUnitHnd_count : 0;
      if (startOffset1 >= exeUnitHnd_count)
         startIdx11 = exeUnitHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < exeUnitHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(exeUnitHnd[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeInt(nrOfExeUnitHnd);
      size += SDRUtil.sizeUInt(exeUnitHnd_count);
      for (int i11 = 0; i11 < exeUnitHnd_count; i11++) {
         size += SDRUtil.sizeInt(exeUnitHnd[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
