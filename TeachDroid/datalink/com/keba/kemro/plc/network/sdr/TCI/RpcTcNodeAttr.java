package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcNodeAttr implements SDR {
   /** Int */
   public static final int rpcUserNodeAttr = 1;
   /** Int */
   public static final int rpcHasAttributesAttr = 2;
   /** Int */
   public static final int rpcIsReferencedAttr = 4;
   /** Int */
   public static final int rpcIsAbstractAttr = 8;
   /** Int */
   public static final int rpcIsDeprecatedAttr = 16;
   /** Int */
   public static final int rpcIsExportVarAttr = 32;
   /** Int */
   public static final int rpcNormalProgAttr = 64;

   /** Int */
   public int value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcNodeAttr() {
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
