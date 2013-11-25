package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcExecuteMethodIn implements SDR {
   /** Int */
   public int clientId;
   /** String */
   public StringBuffer methodName;
   /** Int */
   public int instanceExeUnitHnd;
   /** RpcTcVarAccess */
   public RpcTcVarAccess instanceVarAccess;
   /** Int */
   public int paramExeUnitHnd;
   /** RpcTcVarAccess */
   public RpcTcVarAccess paramVarAccess;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcExecuteMethodIn() {
      methodName = new StringBuffer();
      instanceVarAccess = new RpcTcVarAccess();
      paramVarAccess = new RpcTcVarAccess();
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
         methodName = in.readString(methodName, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         instanceExeUnitHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         instanceVarAccess.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         paramExeUnitHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         paramVarAccess.read(in, context);
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
         if (methodName.length() > TCI.rpcMaxNameLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(methodName, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(instanceExeUnitHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         instanceVarAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         instanceVarAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(paramExeUnitHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         paramVarAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         paramVarAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeString(methodName);
      size += SDRUtil.sizeInt(instanceExeUnitHnd);
      size += instanceVarAccess.size();
      size += SDRUtil.sizeInt(paramExeUnitHnd);
      size += paramVarAccess.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      instanceVarAccess.reset();
      paramVarAccess.reset();
   }
}
