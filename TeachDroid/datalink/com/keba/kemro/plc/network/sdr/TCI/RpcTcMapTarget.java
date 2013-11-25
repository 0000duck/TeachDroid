package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcMapTarget implements SDR {
   /** RpcTcMapKind */
   public RpcTcMapKind kind;
   /** Int */
   public int exeUnit;
   /** RpcTcInstancePath */
   public RpcTcInstancePath instancePath;
   /** Int */
   public int routineHdl;
   /** String */
   public StringBuffer external;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcMapTarget() {
      kind = new RpcTcMapKind();
      instancePath = new RpcTcInstancePath();
      external = new StringBuffer();
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
         exeUnit = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         instancePath.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         routineHdl = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         external = in.readString(external, context);
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
         out.writeInt(exeUnit, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         instancePath.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         instancePath.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(routineHdl, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (external.length() > TCI.rpcMaxPathLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(external, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += kind.size();
      size += SDRUtil.sizeInt(exeUnit);
      size += instancePath.size();
      size += SDRUtil.sizeInt(routineHdl);
      size += SDRUtil.sizeString(external);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
      instancePath.reset();
   }
}
