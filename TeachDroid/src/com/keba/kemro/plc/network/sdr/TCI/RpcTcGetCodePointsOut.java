package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetCodePointsOut implements SDR {
   /** Array[TCI.rpcMaxCodePoints] of Int */
   public int[] lineNrs;
   /** Array[TCI.rpcMaxCodePoints] of RpcTcCodePointKind */
   public RpcTcCodePointKind[] kinds;
   /** Array[TCI.rpcMaxCodePoints] of Bool */
   public boolean[] active;
   /** Int */
   public int nrCodePoints;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetCodePointsOut() {
      lineNrs = new int[TCI.rpcMaxCodePoints];
      kinds = new RpcTcCodePointKind[TCI.rpcMaxCodePoints];
      for (int i21 = 0; i21 < TCI.rpcMaxCodePoints; i21++) {
         kinds[i21] = new RpcTcCodePointKind();
      }
      active = new boolean[TCI.rpcMaxCodePoints];
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = TCI.rpcMaxCodePoints != 0 ? startOffset1 % TCI.rpcMaxCodePoints : 0;
      if (startOffset1 >= TCI.rpcMaxCodePoints)
         startIdx11 = TCI.rpcMaxCodePoints;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < TCI.rpcMaxCodePoints; i11++) {
         if (mMemberDone == actMember) {
            lineNrs[i11] = in.readInt(context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = TCI.rpcMaxCodePoints != 0 ? startOffset2 % TCI.rpcMaxCodePoints : 0;
      if (startOffset2 >= TCI.rpcMaxCodePoints)
         startIdx21 = TCI.rpcMaxCodePoints;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < TCI.rpcMaxCodePoints; i21++) {
         if (mMemberDone == actMember) {
            kinds[i21].read(in, context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx21 = 0;
      int startOffset3 = mMemberDone - actMember;
      int startIdx31 = TCI.rpcMaxCodePoints != 0 ? startOffset3 % TCI.rpcMaxCodePoints : 0;
      if (startOffset3 >= TCI.rpcMaxCodePoints)
         startIdx31 = TCI.rpcMaxCodePoints;
      actMember = mMemberDone;
      for (int i31 = startIdx31; i31 < TCI.rpcMaxCodePoints; i31++) {
         if (mMemberDone == actMember) {
            active[i31] = in.readBool(context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx31 = 0;
      if (mMemberDone == actMember) {
         nrCodePoints = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
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

      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = TCI.rpcMaxCodePoints != 0 ? startOffset1 % TCI.rpcMaxCodePoints : 0;
      if (startOffset1 >= TCI.rpcMaxCodePoints)
         startIdx11 = TCI.rpcMaxCodePoints;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < TCI.rpcMaxCodePoints; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(lineNrs[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = TCI.rpcMaxCodePoints != 0 ? startOffset2 % TCI.rpcMaxCodePoints : 0;
      if (startOffset2 >= TCI.rpcMaxCodePoints)
         startIdx21 = TCI.rpcMaxCodePoints;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < TCI.rpcMaxCodePoints; i21++) {
         if (mMemberDone == actMember) {
            kinds[i21].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            kinds[i21].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx21 = 0;
      int startOffset3 = mMemberDone - actMember;
      int startIdx31 = TCI.rpcMaxCodePoints != 0 ? startOffset3 % TCI.rpcMaxCodePoints : 0;
      if (startOffset3 >= TCI.rpcMaxCodePoints)
         startIdx31 = TCI.rpcMaxCodePoints;
      actMember = mMemberDone;
      for (int i31 = startIdx31; i31 < TCI.rpcMaxCodePoints; i31++) {
         if (mMemberDone == actMember) {
            out.writeBool(active[i31], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx31 = 0;
      if (mMemberDone == actMember) {
         out.writeInt(nrCodePoints, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
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
      for (int i11 = 0; i11 < TCI.rpcMaxCodePoints; i11++) {
         size += SDRUtil.sizeInt(lineNrs[i11]);
      }
      for (int i21 = 0; i21 < TCI.rpcMaxCodePoints; i21++) {
         size += kinds[i21].size();
      }
      for (int i31 = 0; i31 < TCI.rpcMaxCodePoints; i31++) {
         size += SDRUtil.sizeBool(active[i31]);
      }
      size += SDRUtil.sizeInt(nrCodePoints);
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i21 = 0; i21 < TCI.rpcMaxCodePoints; i21++) {
         kinds[i21].reset();
      }
   }
}
