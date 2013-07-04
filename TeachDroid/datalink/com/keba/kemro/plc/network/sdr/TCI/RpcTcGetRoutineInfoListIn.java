package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetRoutineInfoListIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int nrOfRoutineScopeHnd;
   public static final int cMaxLen_routineScopeHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] routineScopeHnd;
   /** Array length member added by SdrGen */
   public int routineScopeHnd_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetRoutineInfoListIn() {
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
         nrOfRoutineScopeHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         routineScopeHnd_count = in.readUInt(context);
         if (context.done)
            routineScopeHnd = new int[routineScopeHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = routineScopeHnd_count != 0 ? startOffset1 % routineScopeHnd_count : 0;
      if (startOffset1 >= routineScopeHnd_count)
         startIdx11 = routineScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < routineScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            routineScopeHnd[i11] = in.readInt(context);
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
         out.writeInt(nrOfRoutineScopeHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((routineScopeHnd == null) || (routineScopeHnd.length < routineScopeHnd_count) || (TCI.rpcChunkLen < routineScopeHnd_count)) &&
             ((routineScopeHnd != null) || (routineScopeHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(routineScopeHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = routineScopeHnd_count != 0 ? startOffset1 % routineScopeHnd_count : 0;
      if (startOffset1 >= routineScopeHnd_count)
         startIdx11 = routineScopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < routineScopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(routineScopeHnd[i11], context);
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
      size += SDRUtil.sizeInt(nrOfRoutineScopeHnd);
      size += SDRUtil.sizeUInt(routineScopeHnd_count);
      for (int i11 = 0; i11 < routineScopeHnd_count; i11++) {
         size += SDRUtil.sizeInt(routineScopeHnd[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
