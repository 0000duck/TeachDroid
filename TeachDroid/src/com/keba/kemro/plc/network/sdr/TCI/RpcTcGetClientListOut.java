package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetClientListOut implements SDR {
   public static final int cMaxLen_clients = 10;
   /** Array<10> of RpcTcClientInfo */
   public RpcTcClientInfo[] clients;
   /** Array length member added by SdrGen */
   public int clients_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetClientListOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         clients_count = in.readUInt(context);
         if (context.done)
            clients = new RpcTcClientInfo[clients_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = clients_count != 0 ? startOffset1 % clients_count : 0;
      if (startOffset1 >= clients_count)
         startIdx11 = clients_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < clients_count; i11++) {
         if (mMemberDone == actMember) {
            if (clients[i11] == null)
               clients[i11] = new RpcTcClientInfo();
            clients[i11].read(in, context);
            if (!context.done)
               return;
            mMemberDone++;
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         retVal = in.readBool(context);
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
         if (((clients == null) || (clients.length < clients_count) || (10 < clients_count)) &&
             ((clients != null) || (clients_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(clients_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = clients_count != 0 ? startOffset1 % clients_count : 0;
      if (startOffset1 >= clients_count)
         startIdx11 = clients_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < clients_count; i11++) {
         if (mMemberDone == actMember) {
            clients[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            clients[i11].reset(); //done for multiple use of input parameters
         }
         actMember++;
      }
      startIdx11 = 0;
      if (mMemberDone == actMember) {
         out.writeBool(retVal, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
   }

   public int size() {
      int size = 0;
      size += SDRUtil.sizeUInt(clients_count);
      for (int i11 = 0; i11 < clients_count; i11++) {
         size += clients[i11].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < clients_count; i11++) {
         clients[i11].reset();
      }
   }
}
