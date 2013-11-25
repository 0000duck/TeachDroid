package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetSaveValueListOut implements SDR {
   public static final int cMaxLen_value = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcValue */
   public RpcTcValue[] value;
   /** Array length member added by SdrGen */
   public int value_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetSaveValueListOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         value_count = in.readUInt(context);
         if (context.done)
            value = new RpcTcValue[value_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = value_count != 0 ? startOffset1 % value_count : 0;
      if (startOffset1 >= value_count)
         startIdx11 = value_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < value_count; i11++) {
         if (mMemberDone == actMember) {
            if (value[i11] == null)
               value[i11] = new RpcTcValue();
            value[i11].read(in, context);
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
         if (((value == null) || (value.length < value_count) || (TCI.rpcChunkLen < value_count)) &&
             ((value != null) || (value_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(value_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = value_count != 0 ? startOffset1 % value_count : 0;
      if (startOffset1 >= value_count)
         startIdx11 = value_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < value_count; i11++) {
         if (mMemberDone == actMember) {
            value[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            value[i11].reset(); //done for multiple use of input parameters
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
      size += SDRUtil.sizeUInt(value_count);
      for (int i11 = 0; i11 < value_count; i11++) {
         size += value[i11].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < value_count; i11++) {
         value[i11].reset();
      }
   }
}
