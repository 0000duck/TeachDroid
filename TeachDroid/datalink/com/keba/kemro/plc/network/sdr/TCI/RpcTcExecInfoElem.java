package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcExecInfoElem implements SDR {
   /** String */
   public StringBuffer infoText;
   /** Int */
   public int infoValue1;
   /** Int */
   public int infoValue2;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcExecInfoElem() {
      infoText = new StringBuffer();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         infoText = in.readString(infoText, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         infoValue1 = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         infoValue2 = in.readInt(context);
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
         if (infoText.length() > 16)
            throw new java.lang.IllegalArgumentException();
         out.writeString(infoText, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(infoValue1, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(infoValue2, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeString(infoText);
      size += SDRUtil.sizeInt(infoValue1);
      size += SDRUtil.sizeInt(infoValue2);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
