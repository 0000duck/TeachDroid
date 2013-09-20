package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcDeleteTextIn implements SDR {
   /** Int */
   public int clientId;
   /** Int */
   public int editHnd;
   /** RpcTcPos */
   public RpcTcPos beg;
   /** RpcTcPos */
   public RpcTcPos end;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcDeleteTextIn() {
      beg = new RpcTcPos();
      end = new RpcTcPos();
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
         editHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         beg.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         end.read(in, context);
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
         out.writeInt(editHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         beg.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         beg.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         end.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         end.reset(); //done for multiple use of input parameters
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeInt(editHnd);
      size += beg.size();
      size += end.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      beg.reset();
      end.reset();
   }
}
