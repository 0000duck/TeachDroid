package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcOpenVarAccessListOut implements SDR {
   public static final int cMaxLen_varAccess = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcVarAccess */
   public RpcTcVarAccess[] varAccess;
   /** Array length member added by SdrGen */
   public int varAccess_count;
   public static final int cMaxLen_info = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcVarAccessInfo */
   public RpcTcVarAccessInfo[] info;
   /** Array length member added by SdrGen */
   public int info_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcOpenVarAccessListOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         varAccess_count = in.readUInt(context);
         if (context.done)
            varAccess = new RpcTcVarAccess[varAccess_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varAccess_count != 0 ? startOffset1 % varAccess_count : 0;
      if (startOffset1 >= varAccess_count)
         startIdx11 = varAccess_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varAccess_count; i11++) {
         if (mMemberDone == actMember) {
            if (varAccess[i11] == null)
               varAccess[i11] = new RpcTcVarAccess();
            varAccess[i11].read(in, context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         info_count = in.readUInt(context);
         if (context.done)
            info = new RpcTcVarAccessInfo[info_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = info_count != 0 ? startOffset2 % info_count : 0;
      if (startOffset2 >= info_count)
         startIdx21 = info_count;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < info_count; i21++) {
         if (mMemberDone == actMember) {
            if (info[i21] == null)
               info[i21] = new RpcTcVarAccessInfo();
            info[i21].read(in, context);
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
         if (((varAccess == null) || (varAccess.length < varAccess_count) || (TCI.rpcChunkLen < varAccess_count)) &&
             ((varAccess != null) || (varAccess_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(varAccess_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varAccess_count != 0 ? startOffset1 % varAccess_count : 0;
      if (startOffset1 >= varAccess_count)
         startIdx11 = varAccess_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varAccess_count; i11++) {
         if (mMemberDone == actMember) {
            varAccess[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            varAccess[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         if (((info == null) || (info.length < info_count) || (TCI.rpcChunkLen < info_count)) &&
             ((info != null) || (info_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(info_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = info_count != 0 ? startOffset2 % info_count : 0;
      if (startOffset2 >= info_count)
         startIdx21 = info_count;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < info_count; i21++) {
         if (mMemberDone == actMember) {
            info[i21].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            info[i21].reset(); //done for multiple use of input parameters
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
      size += SDRUtil.sizeUInt(varAccess_count);
      for (int i11 = 0; i11 < varAccess_count; i11++) {
         size += varAccess[i11].size();
      }
      size += SDRUtil.sizeUInt(info_count);
      for (int i21 = 0; i21 < info_count; i21++) {
         size += info[i21].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < varAccess_count; i11++) {
         varAccess[i11].reset();
      }
      for (int i21 = 0; i21 < info_count; i21++) {
         info[i21].reset();
      }
   }
}
