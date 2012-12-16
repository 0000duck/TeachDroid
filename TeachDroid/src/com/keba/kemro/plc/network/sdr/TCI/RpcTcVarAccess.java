package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcVarAccess implements SDR {
   /** Int */
   public int varHandle;
   /** Int */
   public int typeHandle;
   /** Int */
   public int index;
   public static final int cMaxLen_offsets = TCI.cMaxOffsets;
   /** Array<TCI.cMaxOffsets> of Int */
   public int[] offsets;
   /** Array length member added by SdrGen */
   public int offsets_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcVarAccess() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         varHandle = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         typeHandle = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         index = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         offsets_count = in.readUInt(context);
         if (context.done)
            offsets = new int[offsets_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = offsets_count != 0 ? startOffset1 % offsets_count : 0;
      if (startOffset1 >= offsets_count)
         startIdx11 = offsets_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < offsets_count; i11++) {
         if (mMemberDone == actMember) {
            offsets[i11] = in.readInt(context);
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
         out.writeInt(varHandle, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(typeHandle, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(index, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((offsets == null) || (offsets.length < offsets_count) || (TCI.cMaxOffsets < offsets_count)) &&
             ((offsets != null) || (offsets_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(offsets_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = offsets_count != 0 ? startOffset1 % offsets_count : 0;
      if (startOffset1 >= offsets_count)
         startIdx11 = offsets_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < offsets_count; i11++) {
         if (mMemberDone == actMember) {
            out.writeInt(offsets[i11], context);
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
      size += SDRUtil.sizeInt(varHandle);
      size += SDRUtil.sizeInt(typeHandle);
      size += SDRUtil.sizeInt(index);
      size += SDRUtil.sizeUInt(offsets_count);
      for (int i11 = 0; i11 < offsets_count; i11++) {
         size += SDRUtil.sizeInt(offsets[i11]);
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
