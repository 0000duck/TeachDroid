package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcCopyActValueIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int destExeUnitHnd;
   /** RpcTcVarAccess */
   public RpcTcVarAccess destVarAccess;
   /** Int */
   public int srcExeUnitHnd;
   /** RpcTcVarAccess */
   public RpcTcVarAccess srcVarAccess;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcCopyActValueIn() {
      destVarAccess = new RpcTcVarAccess();
      srcVarAccess = new RpcTcVarAccess();
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
         destExeUnitHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         destVarAccess.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         srcExeUnitHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         srcVarAccess.read(in, context);
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
         out.writeInt(destExeUnitHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         destVarAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         destVarAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(srcExeUnitHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         srcVarAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         srcVarAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeInt(destExeUnitHnd);
      size += destVarAccess.size();
      size += SDRUtil.sizeInt(srcExeUnitHnd);
      size += srcVarAccess.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      destVarAccess.reset();
      srcVarAccess.reset();
   }
}
