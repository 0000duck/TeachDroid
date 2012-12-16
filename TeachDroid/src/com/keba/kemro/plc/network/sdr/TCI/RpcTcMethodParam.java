package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcMethodParam implements SDR {
   /** RpcTcVarAccess */
   public RpcTcVarAccess varAccess;
   /** RpcTcValue */
   public RpcTcValue value;
   /** Bool */
   public boolean isValue;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcMethodParam() {
      varAccess = new RpcTcVarAccess();
      value = new RpcTcValue();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         varAccess.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         value.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         isValue = in.readBool(context);
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
         varAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         varAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         value.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         value.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeBool(isValue, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += varAccess.size();
      size += value.size();
      size += SDRUtil.sizeBool(isValue);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      varAccess.reset();
      value.reset();
   }
}
