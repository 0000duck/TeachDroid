package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcInstancePath implements SDR {
   /** Int */
   public int nrOfElems;
   public static final int cMaxLen_elems = TCI.rpcMaxInstancePathElems;
   /** Array<TCI.rpcMaxInstancePathElems> of RpcTcInstancePathElem */
   public RpcTcInstancePathElem[] elems;
   /** Array length member added by SdrGen */
   public int elems_count;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcInstancePath() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         nrOfElems = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         elems_count = in.readUInt(context);
         if (context.done)
            elems = new RpcTcInstancePathElem[elems_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = elems_count != 0 ? startOffset1 % elems_count : 0;
      if (startOffset1 >= elems_count)
         startIdx11 = elems_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < elems_count; i11++) {
         if (mMemberDone == actMember) {
            if (elems[i11] == null)
               elems[i11] = new RpcTcInstancePathElem();
            elems[i11].read(in, context);
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
         out.writeInt(nrOfElems, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((elems == null) || (elems.length < elems_count) || (TCI.rpcMaxInstancePathElems < elems_count)) &&
             ((elems != null) || (elems_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(elems_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = elems_count != 0 ? startOffset1 % elems_count : 0;
      if (startOffset1 >= elems_count)
         startIdx11 = elems_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < elems_count; i11++) {
         if (mMemberDone == actMember) {
            elems[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            elems[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(nrOfElems);
      size += SDRUtil.sizeUInt(elems_count);
      for (int i11 = 0; i11 < elems_count; i11++) {
         size += elems[i11].size();
      }
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < elems_count; i11++) {
         elems[i11].reset();
      }
   }
}
