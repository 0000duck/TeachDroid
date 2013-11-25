package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcDirEntryInfo implements SDR {
   /** String */
   public StringBuffer name;
   /** RpcTcDirEntryKind */
   public RpcTcDirEntryKind kind;
   /** Int */
   public int size;
   /** Int */
   public int attr;
   /** Int */
   public int createTime;
   /** Int */
   public int modifyTime;
   /** Int */
   public int accessTime;
   /** Bool */
   public boolean isGlobal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcDirEntryInfo() {
      name = new StringBuffer();
      kind = new RpcTcDirEntryKind();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         name = in.readString(name, context);
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
         size = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         attr = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         createTime = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         modifyTime = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         accessTime = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         isGlobal = in.readBool(context);
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
         if (name.length() > TCI.rpcMaxNameLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(name, context);
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
         out.writeInt(size, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(attr, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(createTime, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(modifyTime, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(accessTime, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeBool(isGlobal, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeString(name);
      size += kind.size();
      size += SDRUtil.sizeInt(size);
      size += SDRUtil.sizeInt(attr);
      size += SDRUtil.sizeInt(createTime);
      size += SDRUtil.sizeInt(modifyTime);
      size += SDRUtil.sizeInt(accessTime);
      size += SDRUtil.sizeBool(isGlobal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
   }
}
