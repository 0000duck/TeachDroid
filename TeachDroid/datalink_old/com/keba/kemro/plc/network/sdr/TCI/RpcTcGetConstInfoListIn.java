package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetConstInfoListIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int nrOfConstScopeHnd;
   public static final int cMaxLen_constScopeHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] constScopeHnd;
   /** Array length member added by SdrGen */
   public int constScopeHnd_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetConstInfoListIn() {
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
         nrOfConstScopeHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         constScopeHnd_count = in.readUInt(context);
         if (context.done)
            constScopeHnd = new int[constScopeHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = constScopeHnd_count != 0 ? startOffset1 % constScopeHnd_count : 0;
      if (startOffset1 >= constScopeHnd_count)
         startIdx11 = constScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < constScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            constScopeHnd[i11] = in.readInt(context);
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
         out.writeInt(nrOfConstScopeHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((constScopeHnd == null) || (constScopeHnd.length < constScopeHnd_count) || (TCI.rpcChunkLen < constScopeHnd_count)) &&
             ((constScopeHnd != null) || (constScopeHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(constScopeHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = constScopeHnd_count != 0 ? startOffset1 % constScopeHnd_count : 0;
      if (startOffset1 >= constScopeHnd_count)
         startIdx11 = constScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < constScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(constScopeHnd[i11], context);
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
      size += SDRUtil.sizeInt(nrOfConstScopeHnd);
      size += SDRUtil.sizeUInt(constScopeHnd_count);
      for (int i11 = 0; i11 < constScopeHnd_count; i11++) {
         size += SDRUtil.sizeInt(constScopeHnd[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
