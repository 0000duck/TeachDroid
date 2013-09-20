package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetExeUnitInfoListOut implements SDR {
   /** Int */
   public int nrOfExeUnitHnd;
   public static final int cMaxLen_info = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcExeUnitInfo */
   public RpcTcExeUnitInfo[] info;
   /** Array length member added by SdrGen */
   public int info_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetExeUnitInfoListOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         nrOfExeUnitHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         info_count = in.readUInt(context);
         if (context.done)
            info = new RpcTcExeUnitInfo[info_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = info_count != 0 ? startOffset1 % info_count : 0;
      if (startOffset1 >= info_count)
         startIdx11 = info_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < info_count; i11++) {
         if (mMemberDone == actMember) {
            if (info[i11] == null)
               info[i11] = new RpcTcExeUnitInfo();
            info[i11].read(in, context);
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
         out.writeInt(nrOfExeUnitHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
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
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = info_count != 0 ? startOffset1 % info_count : 0;
      if (startOffset1 >= info_count)
         startIdx11 = info_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < info_count; i11++) {
         if (mMemberDone == actMember) {
            info[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            info[i11].reset(); //done for multiple use of input parameters
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
      size += SDRUtil.sizeInt(nrOfExeUnitHnd);
      size += SDRUtil.sizeUInt(info_count);
      for (int i11 = 0; i11 < info_count; i11++) {
         size += info[i11].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < info_count; i11++) {
         info[i11].reset();
      }
   }
}
