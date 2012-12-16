package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcVarInfo implements SDR {
   /** Int */
   public int incCnt;
   /** Int */
   public int dataSize;
   /** Int */
   public int typeHnd;
   /** RpcTcVarKind */
   public RpcTcVarKind kind;
   /** Int */
   public int attr;
   /** Bool */
   public boolean isPrivate;
   /** Bool */
   public boolean isSave;
   /** Bool */
   public boolean isProjectSave;
   /** Int */
   public int variantConstHnd;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcVarInfo() {
      kind = new RpcTcVarKind();
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         incCnt = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         dataSize = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         typeHnd = in.readInt(context);
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
         attr = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         isPrivate = in.readBool(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         isSave = in.readBool(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         isProjectSave = in.readBool(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         variantConstHnd = in.readInt(context);
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
         out.writeInt(incCnt, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(dataSize, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(typeHnd, context);
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
         out.writeInt(attr, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeBool(isPrivate, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeBool(isSave, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeBool(isProjectSave, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(variantConstHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeInt(incCnt);
      size += SDRUtil.sizeInt(dataSize);
      size += SDRUtil.sizeInt(typeHnd);
      size += kind.size();
      size += SDRUtil.sizeInt(attr);
      size += SDRUtil.sizeBool(isPrivate);
      size += SDRUtil.sizeBool(isSave);
      size += SDRUtil.sizeBool(isProjectSave);
      size += SDRUtil.sizeInt(variantConstHnd);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      kind.reset();
   }
}
