package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetAttributesOut implements SDR {
   public static final int cMaxLen_attributes = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of String */
   public StringBuffer[] attributes;
   /** Array length member added by SdrGen */
   public int attributes_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetAttributesOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         attributes_count = in.readUInt(context);
         if (context.done)
            attributes = new StringBuffer[attributes_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = attributes_count != 0 ? startOffset1 % attributes_count : 0;
      if (startOffset1 >= attributes_count)
         startIdx11 = attributes_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < attributes_count; i11++) {
         if (mMemberDone == actMember) {
            if (attributes[i11] == null)
               attributes[i11] = new StringBuffer();
            attributes[i11] = in.readString(attributes[i11], context);
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
         if (((attributes == null) || (attributes.length < attributes_count) || (TCI.rpcChunkLen < attributes_count)) &&
             ((attributes != null) || (attributes_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(attributes_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = attributes_count != 0 ? startOffset1 % attributes_count : 0;
      if (startOffset1 >= attributes_count)
         startIdx11 = attributes_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < attributes_count; i11++) {
         if (mMemberDone == actMember) {
            if (attributes[i11].length() > TCI.rpcMaxAttrLen)
               throw new java.lang.IllegalArgumentException();
            out.writeString(attributes[i11], context);
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
      size += SDRUtil.sizeUInt(attributes_count);
      for (int i11 = 0; i11 < attributes_count; i11++) {
         size += SDRUtil.sizeString(attributes[i11]);
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
