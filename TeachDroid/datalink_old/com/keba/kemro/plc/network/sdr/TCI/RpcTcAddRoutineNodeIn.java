package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcAddRoutineNodeIn implements SDR {
   /** Int */
   public int clientId;
   /** RpcTcNodeInfo */
   public RpcTcNodeInfo nodeInfo;
   /** RpcTcRoutineInfo */
   public RpcTcRoutineInfo routInfo;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcAddRoutineNodeIn() {
      nodeInfo = new RpcTcNodeInfo();
      routInfo = new RpcTcRoutineInfo();
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
         nodeInfo.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         routInfo.read(in, context);
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
         nodeInfo.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         nodeInfo.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         routInfo.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         routInfo.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += nodeInfo.size();
      size += routInfo.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      nodeInfo.reset();
      routInfo.reset();
   }
}
