package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcNodeKind implements SDR {
   /** Int */
   public static final int rpcRootNode = 0;
   /** Int */
   public static final int rpcGlobalNode = 1;
   /** Int */
   public static final int rpcProjectNode = 2;
   /** Int */
   public static final int rpcProgramNode = 3;
   /** Int */
   public static final int rpcTypeNode = 4;
   /** Int */
   public static final int rpcRoutineNode = 5;
   /** Int */
   public static final int rpcVariableNode = 6;
   /** Int */
   public static final int rpcConstantNode = 7;
   /** Int */
   public static final int rpcFilterAllNode = 8;
   /** Int */
   public static final int rpcFilterProgramUserNode = 9;
   /** Int */
   public static final int rpcFilterTypeUserNode = 10;
   /** Int */
   public static final int rpcFilterRoutineUserNode = 11;
   /** Int */
   public static final int rpcFilterVariableUserNode = 12;
   /** Int */
   public static final int rpcFilterConstantUserNode = 13;
   /** Int */
   public static final int rpcFilterAllUserNode = 14;
   /** Int */
   public static final int rpcSystemNode = 15;

   /** Int */
   public int value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcNodeKind() {
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
