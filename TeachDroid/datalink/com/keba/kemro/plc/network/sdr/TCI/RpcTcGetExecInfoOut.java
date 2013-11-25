package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetExecInfoOut implements SDR {
   /** Bool */
   public boolean retVal;
   /** Int */
   public int nrOfInfos;
   public static final int cMaxLen_execInfos = 10;
   /** Array<10> of RpcTcExecInfoElem */
   public RpcTcExecInfoElem[] execInfos;
   /** Array length member added by SdrGen */
   public int execInfos_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetExecInfoOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         retVal = in.readBool(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         nrOfInfos = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         execInfos_count = in.readUInt(context);
         if (context.done)
            execInfos = new RpcTcExecInfoElem[execInfos_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = execInfos_count != 0 ? startOffset1 % execInfos_count : 0;
      if (startOffset1 >= execInfos_count)
         startIdx11 = execInfos_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < execInfos_count; i11++) {
         if (mMemberDone == actMember) {
            if (execInfos[i11] == null)
               execInfos[i11] = new RpcTcExecInfoElem();
            execInfos[i11].read(in, context);
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
         out.writeBool(retVal, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(nrOfInfos, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((execInfos == null) || (execInfos.length < execInfos_count) || (10 < execInfos_count)) &&
             ((execInfos != null) || (execInfos_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(execInfos_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = execInfos_count != 0 ? startOffset1 % execInfos_count : 0;
      if (startOffset1 >= execInfos_count)
         startIdx11 = execInfos_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < execInfos_count; i11++) {
         if (mMemberDone == actMember) {
            execInfos[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            execInfos[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeBool(retVal);
      size += SDRUtil.sizeInt(nrOfInfos);
      size += SDRUtil.sizeUInt(execInfos_count);
      for (int i11 = 0; i11 < execInfos_count; i11++) {
         size += execInfos[i11].size();
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < execInfos_count; i11++) {
         execInfos[i11].reset();
      }
   }
}
