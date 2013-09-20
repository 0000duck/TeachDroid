package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcErrorParam implements SDR {
   /** RpcTcErrorParamType */
   public RpcTcErrorParamType errorType;
   /** Int */
   public int iValue;
   /** Float */
   public float fValue;
   /** String */
   public StringBuffer sValue;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcErrorParam() {
      errorType = new RpcTcErrorParamType();
      sValue = new StringBuffer();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         errorType.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         iValue = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         fValue = in.readFloat(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         sValue = in.readString(sValue, context);
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
         errorType.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         errorType.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(iValue, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeFloat(fValue, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (sValue.length() > 76)
            throw new java.lang.IllegalArgumentException();
         out.writeString(sValue, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += errorType.size();
      size += SDRUtil.sizeInt(iValue);
      size += SDRUtil.sizeFloat(fValue);
      size += SDRUtil.sizeString(sValue);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      errorType.reset();
   }
}
