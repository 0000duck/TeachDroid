package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetSaveValueListIn implements SDR {
   /** Int */
   public int clientId;
   public static final int cMaxLen_varAccess = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcVarAccess */
   public RpcTcVarAccess[] varAccess;
   /** Array length member added by SdrGen */
   public int varAccess_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetSaveValueListIn() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         clientId = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         varAccess_count = in.readUInt(context);
         if (context.done)
            varAccess = new RpcTcVarAccess[varAccess_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varAccess_count != 0 ? startOffset1 % varAccess_count : 0;
      if (startOffset1 >= varAccess_count)
         startIdx11 = varAccess_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varAccess_count; i11++) {
         if (mMemberDone == actMember) {
            if (varAccess[i11] == null)
               varAccess[i11] = new RpcTcVarAccess();
            varAccess[i11].read(in, context);
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
         out.writeInt(clientId, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((varAccess == null) || (varAccess.length < varAccess_count) || (TCI.rpcChunkLen < varAccess_count)) &&
             ((varAccess != null) || (varAccess_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(varAccess_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varAccess_count != 0 ? startOffset1 % varAccess_count : 0;
      if (startOffset1 >= varAccess_count)
         startIdx11 = varAccess_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varAccess_count; i11++) {
         if (mMemberDone == actMember) {
            varAccess[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            varAccess[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeUInt(varAccess_count);
      for (int i11 = 0; i11 < varAccess_count; i11++) {
         size += varAccess[i11].size();
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < varAccess_count; i11++) {
         varAccess[i11].reset();
      }
   }
}
