package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcConstInfo implements SDR {
   /** Int */
   public int typeHnd;
   /** RpcTcValue */
   public RpcTcValue value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcConstInfo() {
      value = new RpcTcValue();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         typeHnd = in.readInt(context);
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
   }

   public void write(SDROutputStream out, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         out.writeInt(typeHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
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
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(typeHnd);
      size += value.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      value.reset();
   }
}
