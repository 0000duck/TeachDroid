package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcRenameDirEntryIn implements SDR {
   /** Int */
   public int clientId;
   /** String */
   public StringBuffer dirEntryPath;
   /** String */
   public StringBuffer newDirEntryName;
   /** RpcTcDirEntryKind */
   public RpcTcDirEntryKind kind;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcRenameDirEntryIn() {
      dirEntryPath = new StringBuffer();
      newDirEntryName = new StringBuffer();
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
         dirEntryPath = in.readString(dirEntryPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         newDirEntryName = in.readString(newDirEntryName, context);
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
         if (newDirEntryName.length() > TCI.rpcMaxNameLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(newDirEntryName, context);
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
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(clientId);
      size += SDRUtil.sizeString(dirEntryPath);
      size += SDRUtil.sizeString(newDirEntryName);
      size += kind.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
   }
}
