package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcTypeKind implements SDR {
   /** Int */
   public static final int rpcNoType = 0;
   /** Int */
   public static final int rpcBoolType = 1;
   /** Int */
   public static final int rpcInt8Type = 2;
   /** Int */
   public static final int rpcInt16Type = 3;
   /** Int */
   public static final int rpcInt32Type = 4;
   /** Int */
   public static final int rpcInt64Type = 5;
   /** Int */
   public static final int rpcRealType = 6;
   /** Int */
   public static final int rpcStringType = 7;
   /** Int */
   public static final int rpcSubRangeType = 8;
   /** Int */
   public static final int rpcEnumType = 9;
   /** Int */
   public static final int rpcArrayType = 10;
   /** Int */
   public static final int rpcStructType = 11;
   /** Int */
   public static final int rpcUnitType = 12;
   /** Int */
   public static final int rpcMapToType = 13;
   /** Int */
   public static final int rpcRoutineType = 14;
   /** Int */
   public static final int rpcAnyType = 15;
   /** Int */
   public static final int rpcByteType = 20;
   /** Int */
   public static final int rpcWordType = 21;
   /** Int */
   public static final int rpcDWordType = 22;
   /** Int */
   public static final int rpcLWordType = 23;

   /** Int */
   public int value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcTypeKind() {
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
