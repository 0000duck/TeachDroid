package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcDirEntryKind implements SDR {
   /** Int */
   public static final int rpcProjectEntry = 0;
   /** Int */
   public static final int rpcUnitEntry = 1;
   /** Int */
   public static final int rpcProgEntry = 2;
   /** Int */
   public static final int rpcDataEntry = 3;
   /** Int */
   public static final int rpcFilterAllTcEntry = 4;
   /** Int */
   public static final int rpcNoTcEntry = 5;
   /** Int */
   public static final int rpcUserProgEntry = 6;
   /** Int */
   public static final int rpcUserDataEntry = 7;
   /** Int */
   public static final int rpcFilterAllUserEntry = 8;
   /** Int */
   public static final int rpcFilterAllProgEntry = 9;
   /** Int */
   public static final int rpcFilterAllEntry = 10;
   /** Int */
   public static final int rpcArchiveEntry = 11;

   /** Int */
   public int value;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcDirEntryKind() {
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
