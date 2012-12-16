package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetConstInfoListOut implements SDR {
   /** Int */
   public int nrOfInfos;
   public static final int cMaxLen_infos = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcConstInfo */
   public RpcTcConstInfo[] infos;
   /** Array length member added by SdrGen */
   public int infos_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetConstInfoListOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         nrOfInfos = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         infos_count = in.readUInt(context);
         if (context.done)
            infos = new RpcTcConstInfo[infos_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = infos_count != 0 ? startOffset1 % infos_count : 0;
      if (startOffset1 >= infos_count)
         startIdx11 = infos_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < infos_count; i11++) {
         if (mMemberDone == actMember) {
            if (infos[i11] == null)
               infos[i11] = new RpcTcConstInfo();
            infos[i11].read(in, context);
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
         out.writeInt(nrOfInfos, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((infos == null) || (infos.length < infos_count) || (TCI.rpcChunkLen < infos_count)) &&
             ((infos != null) || (infos_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(infos_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = infos_count != 0 ? startOffset1 % infos_count : 0;
      if (startOffset1 >= infos_count)
         startIdx11 = infos_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < infos_count; i11++) {
         if (mMemberDone == actMember) {
            infos[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            infos[i11].reset(); //done for multiple use of input parameters
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
      size += SDRUtil.sizeInt(nrOfInfos);
      size += SDRUtil.sizeUInt(infos_count);
      for (int i11 = 0; i11 < infos_count; i11++) {
         size += infos[i11].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < infos_count; i11++) {
         infos[i11].reset();
      }
   }
}
