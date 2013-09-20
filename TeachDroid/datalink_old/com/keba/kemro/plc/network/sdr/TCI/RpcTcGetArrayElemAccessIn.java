package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetArrayElemAccessIn implements SDR {
   /** Int */
   public int clientId;
   /** RpcTcVarAccess */
   public RpcTcVarAccess varAccess;
   /** Int */
   public int index;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetArrayElemAccessIn() {
      varAccess = new RpcTcVarAccess();
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
         varAccess.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         index = in.readInt(context);
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
         varAccess.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         varAccess.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(index, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += varAccess.size();
      size += SDRUtil.sizeInt(index);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      varAccess.reset();
   }
}
