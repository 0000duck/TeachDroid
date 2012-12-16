package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcExecuteMethodExtIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int routineHnd;
   /** RpcTcVarAccess */
   public RpcTcVarAccess instanceVarAccess;
   public static final int cMaxLen_routParams = TCI.rpcMaxParams;
   /** Array<TCI.rpcMaxParams> of RpcTcMethodParam */
   public RpcTcMethodParam[] routParams;
   /** Array length member added by SdrGen */
   public int routParams_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcExecuteMethodExtIn() {
      instanceVarAccess = new RpcTcVarAccess();
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
         routineHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         instanceVarAccess.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         routParams_count = in.readUInt(context);
         if (context.done)
            routParams = new RpcTcMethodParam[routParams_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = routParams_count != 0 ? startOffset1 % routParams_count : 0;
      if (startOffset1 >= routParams_count)
         startIdx11 = routParams_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < routParams_count; i11++) {
         if (mMemberDone == actMember) {
            if (routParams[i11] == null)
               routParams[i11] = new RpcTcMethodParam();
            routParams[i11].read(in, context);
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
         out.writeInt(routineHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         instanceVarAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         instanceVarAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((routParams == null) || (routParams.length < routParams_count) || (TCI.rpcMaxParams < routParams_count)) &&
             ((routParams != null) || (routParams_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(routParams_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = routParams_count != 0 ? startOffset1 % routParams_count : 0;
      if (startOffset1 >= routParams_count)
         startIdx11 = routParams_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < routParams_count; i11++) {
         if (mMemberDone == actMember) {
            routParams[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            routParams[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeInt(routineHnd);
      size += instanceVarAccess.size();
      size += SDRUtil.sizeUInt(routParams_count);
      for (int i11 = 0; i11 < routParams_count; i11++) {
         size += routParams[i11].size();
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      instanceVarAccess.reset();
      for (int i11 = 0; i11 < routParams_count; i11++) {
         routParams[i11].reset();
      }
   }
}
