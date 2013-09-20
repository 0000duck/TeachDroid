package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetExeUnitStatusListOut implements SDR {
   /** Int */
   public int nrOfExeUnitHnd;
   public static final int cMaxLen_status = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcExeUnitStatus */
   public RpcTcExeUnitStatus[] status;
   /** Array length member added by SdrGen */
   public int status_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetExeUnitStatusListOut() {
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
         status_count = in.readUInt(context);
         if (context.done)
            status = new RpcTcExeUnitStatus[status_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = status_count != 0 ? startOffset1 % status_count : 0;
      if (startOffset1 >= status_count)
         startIdx11 = status_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < status_count; i11++) {
         if (mMemberDone == actMember) {
            if (status[i11] == null)
               status[i11] = new RpcTcExeUnitStatus();
            status[i11].read(in, context);
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
         if (((status == null) || (status.length < status_count) || (TCI.rpcChunkLen < status_count)) &&
             ((status != null) || (status_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(status_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = status_count != 0 ? startOffset1 % status_count : 0;
      if (startOffset1 >= status_count)
         startIdx11 = status_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < status_count; i11++) {
         if (mMemberDone == actMember) {
            status[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            status[i11].reset(); //done for multiple use of input parameters
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
      size += SDRUtil.sizeUInt(status_count);
      for (int i11 = 0; i11 < status_count; i11++) {
         size += status[i11].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < status_count; i11++) {
         status[i11].reset();
      }
   }
}
