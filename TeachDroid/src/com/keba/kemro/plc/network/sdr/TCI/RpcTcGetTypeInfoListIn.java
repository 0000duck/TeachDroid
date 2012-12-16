package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetTypeInfoListIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int nrOfTypeScopeHnd;
   public static final int cMaxLen_typeScopeHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] typeScopeHnd;
   /** Array length member added by SdrGen */
   public int typeScopeHnd_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetTypeInfoListIn() {
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
         nrOfTypeScopeHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         typeScopeHnd_count = in.readUInt(context);
         if (context.done)
            typeScopeHnd = new int[typeScopeHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = typeScopeHnd_count != 0 ? startOffset1 % typeScopeHnd_count : 0;
      if (startOffset1 >= typeScopeHnd_count)
         startIdx11 = typeScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < typeScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            typeScopeHnd[i11] = in.readInt(context);
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
         out.writeInt(nrOfTypeScopeHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((typeScopeHnd == null) || (typeScopeHnd.length < typeScopeHnd_count) || (TCI.rpcChunkLen < typeScopeHnd_count)) &&
             ((typeScopeHnd != null) || (typeScopeHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(typeScopeHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = typeScopeHnd_count != 0 ? startOffset1 % typeScopeHnd_count : 0;
      if (startOffset1 >= typeScopeHnd_count)
         startIdx11 = typeScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < typeScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(typeScopeHnd[i11], context);
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
      size += SDRUtil.sizeInt(nrOfTypeScopeHnd);
      size += SDRUtil.sizeUInt(typeScopeHnd_count);
      for (int i11 = 0; i11 < typeScopeHnd_count; i11++) {
         size += SDRUtil.sizeInt(typeScopeHnd[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
