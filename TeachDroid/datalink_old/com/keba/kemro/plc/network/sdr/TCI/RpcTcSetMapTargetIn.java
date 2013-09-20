package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcSetMapTargetIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int exeUnitHnd;
   /** RpcTcVarAccess */
   public RpcTcVarAccess varAccess;
   /** RpcTcMapTarget */
   public RpcTcMapTarget target;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcSetMapTargetIn() {
      varAccess = new RpcTcVarAccess();
      target = new RpcTcMapTarget();
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
         exeUnitHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         varAccess.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         target.read(in, context);
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
         out.writeInt(clientId, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(exeUnitHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         varAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         varAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         target.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         target.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeInt(exeUnitHnd);
      size += varAccess.size();
      size += target.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      varAccess.reset();
      target.reset();
   }
}
