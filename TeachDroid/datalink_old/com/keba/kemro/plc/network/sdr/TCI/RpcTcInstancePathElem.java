package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcInstancePathElem implements SDR {
   /** Int */
   public int structComponent;
   /** Int */
   public int arrayIndex;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcInstancePathElem() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         structComponent = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         arrayIndex = in.readInt(context);
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
         out.writeInt(structComponent, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(arrayIndex, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(structComponent);
      size += SDRUtil.sizeInt(arrayIndex);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
