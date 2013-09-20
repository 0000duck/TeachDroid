package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcExeStepState implements SDR {
   /** Int */
   public static final int rpcStepOff = 0;
   /** Int */
   public static final int rpcStepBreak = 1;
   /** Int */
   public static final int rpcStepInto = 2;
   /** Int */
   public static final int rpcStepOver = 3;
   /** Int */
   public static final int rpcStepOut = 4;
   /** Int */
   public static final int rpcStepWait = 5;
   /** Int */
   public static final int rpcStepGo = 6;

   /** Int */
   public int value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcExeStepState() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      if (mMemberDone == 0) {
         value = in.readInt(context);
         if (context.done)
            mMemberDone++;
      }
   }
   public void write(SDROutputStream out, SDRContext context) throws SDRException, IOException {
      if (mMemberDone == 0) {
         out.writeInt(value, context);
         if (context.done)
            mMemberDone++;
      }
   }
   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(value);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
