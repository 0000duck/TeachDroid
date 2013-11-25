package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcVarAccessInfo implements SDR {
   /** RpcTcTypeKind */
   public RpcTcTypeKind typeKind;
   /** Int */
   public int attr;
   /** Int */
   public int reserved;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcVarAccessInfo() {
      typeKind = new RpcTcTypeKind();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         typeKind.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         attr = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         reserved = in.readInt(context);
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
         typeKind.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         typeKind.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(attr, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(reserved, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += typeKind.size();
      size += SDRUtil.sizeInt(attr);
      size += SDRUtil.sizeInt(reserved);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      typeKind.reset();
   }
}
