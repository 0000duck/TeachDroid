package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetNextDirEntryChunkIn implements SDR {
   /** Int */
   public int clientId;
   /** String */
   public StringBuffer dirPath;
   /** RpcTcDirEntryKind */
   public RpcTcDirEntryKind kind;
   /** Int */
   public int iterHnd;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetNextDirEntryChunkIn() {
      dirPath = new StringBuffer();
      kind = new RpcTcDirEntryKind();
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
         dirPath = in.readString(dirPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         kind.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         iterHnd = in.readInt(context);
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
         if (dirPath.length() > TCI.rpcMaxPathLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(dirPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         kind.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         kind.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(iterHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeString(dirPath);
      size += kind.size();
      size += SDRUtil.sizeInt(iterHnd);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
   }
}
