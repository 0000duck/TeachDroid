package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcReadNodeChangeOut implements SDR {
   public static final int cMaxLen_changes = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcNodeChange */
   public RpcTcNodeChange[] changes;
   /** Array length member added by SdrGen */
   public int changes_count;
   /** Int */
   public int nrChanges;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcReadNodeChangeOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         changes_count = in.readUInt(context);
         if (context.done)
            changes = new RpcTcNodeChange[changes_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = changes_count != 0 ? startOffset1 % changes_count : 0;
      if (startOffset1 >= changes_count)
         startIdx11 = changes_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < changes_count; i11++) {
         if (mMemberDone == actMember) {
            if (changes[i11] == null)
               changes[i11] = new RpcTcNodeChange();
            changes[i11].read(in, context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         nrChanges = in.readInt(context);
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

      if (mMemberDone == actMember) {
         if (((changes == null) || (changes.length < changes_count) || (TCI.rpcChunkLen < changes_count)) &&
             ((changes != null) || (changes_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(changes_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = changes_count != 0 ? startOffset1 % changes_count : 0;
      if (startOffset1 >= changes_count)
         startIdx11 = changes_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < changes_count; i11++) {
         if (mMemberDone == actMember) {
            changes[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            changes[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         out.writeInt(nrChanges, context);
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
      size += SDRUtil.sizeUInt(changes_count);
      for (int i11 = 0; i11 < changes_count; i11++) {
         size += changes[i11].size();
      }
      size += SDRUtil.sizeInt(nrChanges);
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < changes_count; i11++) {
         changes[i11].reset();
      }
   }
}
