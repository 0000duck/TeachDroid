package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetAttributesIn implements SDR {
   /** Int */
   public int clientId;
   public static final int cMaxLen_scopeHnd = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of Int */
   public int[] scopeHnd;
   /** Array length member added by SdrGen */
   public int scopeHnd_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetAttributesIn() {
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
         scopeHnd_count = in.readUInt(context);
         if (context.done)
            scopeHnd = new int[scopeHnd_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = scopeHnd_count != 0 ? startOffset1 % scopeHnd_count : 0;
      if (startOffset1 >= scopeHnd_count)
         startIdx11 = scopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < scopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            scopeHnd[i11] = in.readInt(context);
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
         if (((scopeHnd == null) || (scopeHnd.length < scopeHnd_count) || (TCI.rpcChunkLen < scopeHnd_count)) &&
             ((scopeHnd != null) || (scopeHnd_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(scopeHnd_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = scopeHnd_count != 0 ? startOffset1 % scopeHnd_count : 0;
      if (startOffset1 >= scopeHnd_count)
         startIdx11 = scopeHnd_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < scopeHnd_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(scopeHnd[i11], context);
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
      size += SDRUtil.sizeUInt(scopeHnd_count);
      for (int i11 = 0; i11 < scopeHnd_count; i11++) {
         size += SDRUtil.sizeInt(scopeHnd[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
