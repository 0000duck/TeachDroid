package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetVarInfoListIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int nrOfVarScopeHnd;
   public static final int cMaxLen_varScopeHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] varScopeHnd;
   /** Array length member added by SdrGen */
   public int varScopeHnd_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetVarInfoListIn() {
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
         nrOfVarScopeHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         varScopeHnd_count = in.readUInt(context);
         if (context.done)
            varScopeHnd = new int[varScopeHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varScopeHnd_count != 0 ? startOffset1 % varScopeHnd_count : 0;
      if (startOffset1 >= varScopeHnd_count)
         startIdx11 = varScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            varScopeHnd[i11] = in.readInt(context);
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
         out.writeInt(nrOfVarScopeHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((varScopeHnd == null) || (varScopeHnd.length < varScopeHnd_count) || (TCI.rpcChunkLen < varScopeHnd_count)) &&
             ((varScopeHnd != null) || (varScopeHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(varScopeHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varScopeHnd_count != 0 ? startOffset1 % varScopeHnd_count : 0;
      if (startOffset1 >= varScopeHnd_count)
         startIdx11 = varScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(varScopeHnd[i11], context);
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
      size += SDRUtil.sizeInt(nrOfVarScopeHnd);
      size += SDRUtil.sizeUInt(varScopeHnd_count);
      for (int i11 = 0; i11 < varScopeHnd_count; i11++) {
         size += SDRUtil.sizeInt(varScopeHnd[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
