package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetSymbolTextRangeOut implements SDR {
   /** RpcTcPos */
   public RpcTcPos beg;
   /** RpcTcPos */
   public RpcTcPos end;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetSymbolTextRangeOut() {
      beg = new RpcTcPos();
      end = new RpcTcPos();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

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
      if (mMemberDone == actMember) {
         retVal = in.readBool(context);
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
      if (mMemberDone == actMember) {
         out.writeBool(retVal, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += beg.size();
      size += end.size();
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      beg.reset();
      end.reset();
   }
}
