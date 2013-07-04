package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcOpenTeachControlIn implements SDR {
   /** Int */
   public int clientId;
   /** RpcTcClientType */
   public RpcTcClientType clType;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcOpenTeachControlIn() {
      clType = new RpcTcClientType();
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
         clType.read(in, context);
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
         clType.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         clType.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += clType.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      clType.reset();
   }
}
