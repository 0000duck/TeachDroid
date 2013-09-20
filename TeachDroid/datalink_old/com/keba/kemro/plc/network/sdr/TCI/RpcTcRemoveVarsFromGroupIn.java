package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcRemoveVarsFromGroupIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int varGroupHnd;
   public static final int cMaxLen_varId = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] varId;
   /** Array length member added by SdrGen */
   public int varId_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcRemoveVarsFromGroupIn() {
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
         varGroupHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         varId_count = in.readUInt(context);
         if (context.done)
            varId = new int[varId_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varId_count != 0 ? startOffset1 % varId_count : 0;
      if (startOffset1 >= varId_count)
         startIdx11 = varId_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varId_count; i11++) {
         if (mMemberDone == actMember) {
            varId[i11] = in.readInt(context);
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
         out.writeInt(varGroupHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((varId == null) || (varId.length < varId_count) || (TCI.rpcChunkLen < varId_count)) &&
             ((varId != null) || (varId_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(varId_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varId_count != 0 ? startOffset1 % varId_count : 0;
      if (startOffset1 >= varId_count)
         startIdx11 = varId_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varId_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(varId[i11], context);
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
      size += SDRUtil.sizeInt(varGroupHnd);
      size += SDRUtil.sizeUInt(varId_count);
      for (int i11 = 0; i11 < varId_count; i11++) {
         size += SDRUtil.sizeInt(varId[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
