package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcNodeInfo implements SDR {
   /** RpcTcNodeKind */
   public RpcTcNodeKind kind;
   /** String */
   public StringBuffer elemName;
   /** Int */
   public int incCnt;
   /** Int */
   public int attr;
   /** Int */
   public int upperHnd;
   /** Int */
   public int declHnd;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcNodeInfo() {
      kind = new RpcTcNodeKind();
      elemName = new StringBuffer();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         kind.read(in, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         elemName = in.readString(elemName, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         incCnt = in.readInt(context);
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
         upperHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         declHnd = in.readInt(context);
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
         kind.write(out, context);
         if (!context.done)
            return;
         mMemberDone++;
         kind.reset(); //done for multiple use of input parameters
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (elemName.length() > TCI.rpcMaxNameLen)
            throw new java.lang.IllegalArgumentException();
         out.writeString(elemName, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(incCnt, context);
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
         out.writeInt(upperHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(declHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += kind.size();
      size += SDRUtil.sizeString(elemName);
      size += SDRUtil.sizeInt(incCnt);
      size += SDRUtil.sizeInt(attr);
      size += SDRUtil.sizeInt(upperHnd);
      size += SDRUtil.sizeInt(declHnd);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
   }
}
