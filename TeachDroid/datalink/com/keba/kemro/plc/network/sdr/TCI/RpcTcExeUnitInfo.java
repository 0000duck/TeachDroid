package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcExeUnitInfo implements SDR {
   /** RpcTcExeUnitKind */
   public RpcTcExeUnitKind kind;
   /** Bool */
   public boolean isMainFlow;
   /** String */
   public StringBuffer callPath;
   /** Int */
   public int scopeHnd;
   /** Int */
   public int priority;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcExeUnitInfo() {
      kind = new RpcTcExeUnitKind();
      callPath = new StringBuffer();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         kind.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         isMainFlow = in.readBool(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         callPath = in.readString(callPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         scopeHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         priority = in.readInt(context);
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
         kind.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         kind.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeBool(isMainFlow, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (callPath.length() > 80)
            throw new java.lang.IllegalArgumentException();
         out.writeString(callPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(scopeHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(priority, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += kind.size();
      size += SDRUtil.sizeBool(isMainFlow);
      size += SDRUtil.sizeString(callPath);
      size += SDRUtil.sizeInt(scopeHnd);
      size += SDRUtil.sizeInt(priority);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
   }
}
