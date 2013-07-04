package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcCopyDirEntryIn implements SDR {
   /** Int */
   public int clientId;
   /** String */
   public StringBuffer srcDirEntryPath;
   /** String */
   public StringBuffer destDirPath;
   /** String */
   public StringBuffer destdirEntryName;
   /** RpcTcDirEntryKind */
   public RpcTcDirEntryKind kind;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcCopyDirEntryIn() {
      srcDirEntryPath = new StringBuffer();
      destDirPath = new StringBuffer();
      destdirEntryName = new StringBuffer();
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
         srcDirEntryPath = in.readString(srcDirEntryPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         destDirPath = in.readString(destDirPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         destdirEntryName = in.readString(destdirEntryName, context);
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
         if (srcDirEntryPath.length() > TCI.rpcMaxPathLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(srcDirEntryPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (destDirPath.length() > TCI.rpcMaxPathLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(destDirPath, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (destdirEntryName.length() > TCI.rpcMaxNameLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(destdirEntryName, context);
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
      size += SDRUtil.sizeString(srcDirEntryPath);
      size += SDRUtil.sizeString(destDirPath);
      size += SDRUtil.sizeString(destdirEntryName);
      size += kind.size();
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
   }
}
