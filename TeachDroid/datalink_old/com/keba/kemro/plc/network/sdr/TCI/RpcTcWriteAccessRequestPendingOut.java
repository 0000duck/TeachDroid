package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcWriteAccessRequestPendingOut implements SDR {
   /** Bool */
   public boolean requestPending;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcWriteAccessRequestPendingOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         requestPending = in.readBool(context);
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
         out.writeBool(requestPending, context);
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
      size += SDRUtil.sizeBool(requestPending);
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
