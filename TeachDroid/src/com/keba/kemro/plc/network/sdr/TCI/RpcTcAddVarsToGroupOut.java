package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcAddVarsToGroupOut implements SDR {
   public static final int cMaxLen_varId = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] varId;
   /** Array length member added by SdrGen */
   public int varId_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcAddVarsToGroupOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         varId_count = in.readUInt(context);
         if (context.done)
            varId = new int[varId_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varId_count != 0 ? startOffset1 % varId_count : 0;
      if (startOffset1 >= varId_count)
         startIdx11 = varId_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varId_count; i11++) {
         if (mMemberDone == actMember) {
            varId[i11] = in.readInt(context);
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
         if (((varId == null) || (varId.length < varId_count) || (TCI.rpcChunkLen < varId_count)) &&
             ((varId != null) || (varId_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(varId_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varId_count != 0 ? startOffset1 % varId_count : 0;
      if (startOffset1 >= varId_count)
         startIdx11 = varId_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varId_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(varId[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
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
      size += SDRUtil.sizeUInt(varId_count);
      for (int i11 = 0; i11 < varId_count; i11++) {
         size += SDRUtil.sizeInt(varId[i11]);
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
