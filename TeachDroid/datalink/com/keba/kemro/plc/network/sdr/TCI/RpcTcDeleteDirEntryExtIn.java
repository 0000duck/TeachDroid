package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcDeleteDirEntryExtIn implements SDR {
   /** Int */
   public int clientId;
   /** String */
   public StringBuffer dirEntryPath;
   /** Bool */
   public boolean forced;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcDeleteDirEntryExtIn() {
      dirEntryPath = new StringBuffer();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         clientId = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         dirEntryPath = in.readString(dirEntryPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         forced = in.readBool(context);
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
         out.writeInt(clientId, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (dirEntryPath.length() > TCI.rpcMaxPathLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(dirEntryPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeBool(forced, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeString(dirEntryPath);
      size += SDRUtil.sizeBool(forced);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
   }
}
