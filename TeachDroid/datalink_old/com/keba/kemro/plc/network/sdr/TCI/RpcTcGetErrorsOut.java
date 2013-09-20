package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetErrorsOut implements SDR {
   public static final int cMaxLen_errors = TCI.cMaxErrors;
   /** Array<TCI.cMaxErrors> of RpcTcErrorElem */
   public RpcTcErrorElem[] errors;
   /** Array length member added by SdrGen */
   public int errors_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetErrorsOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         errors_count = in.readUInt(context);
         if (context.done)
            errors = new RpcTcErrorElem[errors_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = errors_count != 0 ? startOffset1 % errors_count : 0;
      if (startOffset1 >= errors_count)
         startIdx11 = errors_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < errors_count; i11++) {
         if (mMemberDone == actMember) {
            if (errors[i11] == null)
               errors[i11] = new RpcTcErrorElem();
            errors[i11].read(in, context);
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
         if (((errors == null) || (errors.length < errors_count) || (TCI.cMaxErrors < errors_count)) &&
             ((errors != null) || (errors_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(errors_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = errors_count != 0 ? startOffset1 % errors_count : 0;
      if (startOffset1 >= errors_count)
         startIdx11 = errors_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < errors_count; i11++) {
         if (mMemberDone == actMember) {
            errors[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            errors[i11].reset(); //done for multiple use of input parameters
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
      size += SDRUtil.sizeUInt(errors_count);
      for (int i11 = 0; i11 < errors_count; i11++) {
         size += errors[i11].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < errors_count; i11++) {
         errors[i11].reset();
      }
   }
}
