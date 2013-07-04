package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcExeUnitKind implements SDR {
   /** Int */
   public static final int rpcInvalidExeUnit = -1;
   /** Int */
   public static final int rpcGlobalExeUnit = 0;
   /** Int */
   public static final int rpcProjectExeUnit = 1;
   /** Int */
   public static final int rpcProgramExeUnit = 2;
   /** Int */
   public static final int rpcRoutineExeUnit = 3;
   /** Int */
   public static final int rpcFilterAllExeUnit = 4;
   /** Int */
   public static final int rpcFilterUserProgramExeUnit = 5;
   /** Int */
   public static final int rpcFilterUserRoutineExeUnit = 6;
   /** Int */
   public static final int rpcFilterAllUserExeUnit = 7;
   /** Int */
   public static final int rpcSystemExeUnit = 8;

   /** Int */
   public int value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcExeUnitKind() {
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
