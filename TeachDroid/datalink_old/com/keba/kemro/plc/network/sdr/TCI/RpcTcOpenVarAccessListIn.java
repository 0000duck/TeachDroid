package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcOpenVarAccessListIn implements SDR {
   /** Int */
   public int clientId;
   public static final int cMaxLen_varAccessPaths = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of String */
   public StringBuffer[] varAccessPaths;
   /** Array length member added by SdrGen */
   public int varAccessPaths_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcOpenVarAccessListIn() {
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
         varAccessPaths_count = in.readUInt(context);
         if (context.done)
            varAccessPaths = new StringBuffer[varAccessPaths_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varAccessPaths_count != 0 ? startOffset1 % varAccessPaths_count : 0;
      if (startOffset1 >= varAccessPaths_count)
         startIdx11 = varAccessPaths_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varAccessPaths_count; i11++) {
         if (mMemberDone == actMember) {
            if (varAccessPaths[i11] == null)
               varAccessPaths[i11] = new StringBuffer();
            varAccessPaths[i11] = in.readString(varAccessPaths[i11], context);
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
         if (((varAccessPaths == null) || (varAccessPaths.length < varAccessPaths_count) || (TCI.rpcChunkLen < varAccessPaths_count)) &&
             ((varAccessPaths != null) || (varAccessPaths_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(varAccessPaths_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = varAccessPaths_count != 0 ? startOffset1 % varAccessPaths_count : 0;
      if (startOffset1 >= varAccessPaths_count)
         startIdx11 = varAccessPaths_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < varAccessPaths_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeString(varAccessPaths[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeUInt(varAccessPaths_count);
      for (int i11 = 0; i11 < varAccessPaths_count; i11++) {
         size += SDRUtil.sizeString(varAccessPaths[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
