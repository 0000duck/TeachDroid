package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetWatchPointVarsOut implements SDR {
   /** Array[TCI.rpcMaxWatchVars] of Int */
   public int[] exeUnitHnds;
   /** Array[TCI.rpcMaxWatchVars] of RpcTcInstancePath */
   public RpcTcInstancePath[] instancePaths;
   /** Array[TCI.rpcMaxWatchVars] of Int */
   public int[] wpVarHnds;
   /** Int */
   public int nrWPVars;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetWatchPointVarsOut() {
      exeUnitHnds = new int[TCI.rpcMaxWatchVars];
      instancePaths = new RpcTcInstancePath[TCI.rpcMaxWatchVars];
      for (int i21 = 0; i21 < TCI.rpcMaxWatchVars; i21++) {
         instancePaths[i21] = new RpcTcInstancePath();
      }
      wpVarHnds = new int[TCI.rpcMaxWatchVars];
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = TCI.rpcMaxWatchVars != 0 ? startOffset1 % TCI.rpcMaxWatchVars : 0;
      if (startOffset1 >= TCI.rpcMaxWatchVars)
         startIdx11 = TCI.rpcMaxWatchVars;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < TCI.rpcMaxWatchVars; i11++) {
         if (mMemberDone == actMember) {
            exeUnitHnds[i11] = in.readInt(context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = TCI.rpcMaxWatchVars != 0 ? startOffset2 % TCI.rpcMaxWatchVars : 0;
      if (startOffset2 >= TCI.rpcMaxWatchVars)
         startIdx21 = TCI.rpcMaxWatchVars;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < TCI.rpcMaxWatchVars; i21++) {
         if (mMemberDone == actMember) {
            instancePaths[i21].read(in, context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx21 = 0;
      int startOffset3 = mMemberDone - actMember;
      int startIdx31 = TCI.rpcMaxWatchVars != 0 ? startOffset3 % TCI.rpcMaxWatchVars : 0;
      if (startOffset3 >= TCI.rpcMaxWatchVars)
         startIdx31 = TCI.rpcMaxWatchVars;
      actMember = mMemberDone;
      for (int i31 = startIdx31; i31 < TCI.rpcMaxWatchVars; i31++) {
         if (mMemberDone == actMember) {
            wpVarHnds[i31] = in.readInt(context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx31 = 0;
      if (mMemberDone == actMember) {
         nrWPVars = in.readInt(context);
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
      int startIdx11 = TCI.rpcMaxWatchVars != 0 ? startOffset1 % TCI.rpcMaxWatchVars : 0;
      if (startOffset1 >= TCI.rpcMaxWatchVars)
         startIdx11 = TCI.rpcMaxWatchVars;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < TCI.rpcMaxWatchVars; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(exeUnitHnds[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      int startOffset2 = mMemberDone - actMember;
      int startIdx21 = TCI.rpcMaxWatchVars != 0 ? startOffset2 % TCI.rpcMaxWatchVars : 0;
      if (startOffset2 >= TCI.rpcMaxWatchVars)
         startIdx21 = TCI.rpcMaxWatchVars;
      actMember = mMemberDone;
      for (int i21 = startIdx21; i21 < TCI.rpcMaxWatchVars; i21++) {
         if (mMemberDone == actMember) {
            instancePaths[i21].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            instancePaths[i21].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx21 = 0;
      int startOffset3 = mMemberDone - actMember;
      int startIdx31 = TCI.rpcMaxWatchVars != 0 ? startOffset3 % TCI.rpcMaxWatchVars : 0;
      if (startOffset3 >= TCI.rpcMaxWatchVars)
         startIdx31 = TCI.rpcMaxWatchVars;
      actMember = mMemberDone;
      for (int i31 = startIdx31; i31 < TCI.rpcMaxWatchVars; i31++) {
         if (mMemberDone == actMember) {
            out.writeInt(wpVarHnds[i31], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx31 = 0;
      if (mMemberDone == actMember) {
         out.writeInt(nrWPVars, context);
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
      for (int i11 = 0; i11 < TCI.rpcMaxWatchVars; i11++) {
         size += SDRUtil.sizeInt(exeUnitHnds[i11]);
      }
      for (int i21 = 0; i21 < TCI.rpcMaxWatchVars; i21++) {
         size += instancePaths[i21].size();
      }
      for (int i31 = 0; i31 < TCI.rpcMaxWatchVars; i31++) {
         size += SDRUtil.sizeInt(wpVarHnds[i31]);
      }
      size += SDRUtil.sizeInt(nrWPVars);
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i21 = 0; i21 < TCI.rpcMaxWatchVars; i21++) {
         instancePaths[i21].reset();
      }
   }
}
