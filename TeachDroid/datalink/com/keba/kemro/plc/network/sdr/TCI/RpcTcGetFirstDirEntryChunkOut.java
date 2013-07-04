package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetFirstDirEntryChunkOut implements SDR {
   /** Int */
   public int iterHnd;
   public static final int cMaxLen_dirEntryPaths = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of String */
   public StringBuffer[] dirEntryPaths;
   /** Array length member added by SdrGen */
   public int dirEntryPaths_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetFirstDirEntryChunkOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         iterHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         dirEntryPaths_count = in.readUInt(context);
         if (context.done)
            dirEntryPaths = new StringBuffer[dirEntryPaths_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = dirEntryPaths_count != 0 ? startOffset1 % dirEntryPaths_count : 0;
      if (startOffset1 >= dirEntryPaths_count)
         startIdx11 = dirEntryPaths_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < dirEntryPaths_count; i11++) {
         if (mMemberDone == actMember) {
            if (dirEntryPaths[i11] == null)
               dirEntryPaths[i11] = new StringBuffer();
            dirEntryPaths[i11] = in.readString(dirEntryPaths[i11], context);
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
         out.writeInt(iterHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((dirEntryPaths == null) || (dirEntryPaths.length < dirEntryPaths_count) || (TCI.rpcChunkLen < dirEntryPaths_count)) &&
             ((dirEntryPaths != null) || (dirEntryPaths_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(dirEntryPaths_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = dirEntryPaths_count != 0 ? startOffset1 % dirEntryPaths_count : 0;
      if (startOffset1 >= dirEntryPaths_count)
         startIdx11 = dirEntryPaths_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < dirEntryPaths_count; i11++) {
         if (mMemberDone == actMember) {
            if (dirEntryPaths[i11].length() > TCI.rpcMaxPathLen)
               throw new java.lang.IllegalArgumentException();
            out.writeString(dirEntryPaths[i11], context);
            if (!context.done)
               return;
            mMemberDone++;
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
      size += SDRUtil.sizeInt(iterHnd);
      size += SDRUtil.sizeUInt(dirEntryPaths_count);
      for (int i11 = 0; i11 < dirEntryPaths_count; i11++) {
         size += SDRUtil.sizeString(dirEntryPaths[i11]);
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
