package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcSetWatchPointVarIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int routineScopeHnd;
   /** Int */
   public int lineNr;
   /** Int */
   public int exeUnitHnd;
   /** RpcTcInstancePath */
   public RpcTcInstancePath instPath;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcSetWatchPointVarIn() {
      instPath = new RpcTcInstancePath();
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
         routineScopeHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         lineNr = in.readInt(context);
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
         instPath.read(in, context);
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
         out.writeInt(routineScopeHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(lineNr, context);
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
         instPath.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         instPath.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeInt(routineScopeHnd);
      size += SDRUtil.sizeInt(lineNr);
      size += SDRUtil.sizeInt(exeUnitHnd);
      size += instPath.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      instPath.reset();
   }
}
