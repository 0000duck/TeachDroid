package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcAddTypeNodeIn implements SDR {
   /** Int */
   public int clientId;
   /** RpcTcNodeInfo */
   public RpcTcNodeInfo nodeInfo;
   /** RpcTcTypeInfo */
   public RpcTcTypeInfo typeInfo;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcAddTypeNodeIn() {
      nodeInfo = new RpcTcNodeInfo();
      typeInfo = new RpcTcTypeInfo();
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
         typeInfo.read(in, context);
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
         typeInfo.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         typeInfo.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += nodeInfo.size();
      size += typeInfo.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      nodeInfo.reset();
      typeInfo.reset();
   }
}
