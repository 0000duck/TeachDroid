package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcErrorElem implements SDR {
   /** RpcTcErrorKind */
   public RpcTcErrorKind errorKind;
   /** Int */
   public int errorNr;
   /** Int */
   public int nrParams;
   /** Array[TCI.cNrErrorParams] of RpcTcErrorParam */
   public RpcTcErrorParam[] errorParams;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcErrorElem() {
      errorKind = new RpcTcErrorKind();
      errorParams = new RpcTcErrorParam[TCI.cNrErrorParams];
      for (int i11 = 0; i11 < TCI.cNrErrorParams; i11++) {
         errorParams[i11] = new RpcTcErrorParam();
      }
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         errorKind.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         errorNr = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         nrParams = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = TCI.cNrErrorParams != 0 ? startOffset1 % TCI.cNrErrorParams : 0;
      if (startOffset1 >= TCI.cNrErrorParams)
         startIdx11 = TCI.cNrErrorParams;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < TCI.cNrErrorParams; i11++) {
         if (mMemberDone == actMember) {
            errorParams[i11].read(in, context);
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
         errorKind.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         errorKind.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(errorNr, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(nrParams, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = TCI.cNrErrorParams != 0 ? startOffset1 % TCI.cNrErrorParams : 0;
      if (startOffset1 >= TCI.cNrErrorParams)
         startIdx11 = TCI.cNrErrorParams;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < TCI.cNrErrorParams; i11++) {
         if (mMemberDone == actMember) {
            errorParams[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            errorParams[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public int size() {
      int size = 0;
      size += errorKind.size();
      size += SDRUtil.sizeInt(errorNr);
      size += SDRUtil.sizeInt(nrParams);
      for (int i11 = 0; i11 < TCI.cNrErrorParams; i11++) {
         size += errorParams[i11].size();
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      errorKind.reset();
      for (int i11 = 0; i11 < TCI.cNrErrorParams; i11++) {
         errorParams[i11].reset();
      }
   }
}
