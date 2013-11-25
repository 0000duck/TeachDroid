package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcExeUnitStatus implements SDR {
   /** RpcTcExeUnitState */
   public RpcTcExeUnitState state;
   /** RpcTcExeStepState */
   public RpcTcExeStepState step;
   /** RpcTcExeFlags */
   public RpcTcExeFlags exeFlags;
   /** Int */
   public int lineOrStatus;
   /** Int */
   public int mainFlowLine;
   /** Int */
   public int nrChilds;
   /** Int */
   public int changeCnt;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcExeUnitStatus() {
      state = new RpcTcExeUnitState();
      step = new RpcTcExeStepState();
      exeFlags = new RpcTcExeFlags();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         state.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         step.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         exeFlags.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         lineOrStatus = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         mainFlowLine = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         nrChilds = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         changeCnt = in.readInt(context);
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
         state.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         state.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         step.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         step.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         exeFlags.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         exeFlags.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(lineOrStatus, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(mainFlowLine, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(nrChilds, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(changeCnt, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += state.size();
      size += step.size();
      size += exeFlags.size();
      size += SDRUtil.sizeInt(lineOrStatus);
      size += SDRUtil.sizeInt(mainFlowLine);
      size += SDRUtil.sizeInt(nrChilds);
      size += SDRUtil.sizeInt(changeCnt);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      state.reset();
      step.reset();
      exeFlags.reset();
   }
}
