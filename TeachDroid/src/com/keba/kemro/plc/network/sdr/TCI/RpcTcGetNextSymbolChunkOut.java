package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class RpcTcGetNextSymbolChunkOut implements SDR {
   /** Int */
   public int iterHnd;
   /** Int */
   public int nrOfSymbols;
   public static final int cMaxLen_symbols = TCI.rpcChunkLen;
   /** Array<TCI.rpcChunkLen> of RpcTcSymbol */
   public RpcTcSymbol[] symbols;
   /** Array length member added by SdrGen */
   public int symbols_count;
   /** Bool */
   public boolean retVal;

   /** Added by SdrGen */
   private int mMemberDone = 0;

   public RpcTcGetNextSymbolChunkOut() {
   }

   public void read(SDRInputStream in, SDRContext context) throws SDRException, IOException {
      /** Added by SdrGen */
      int actMember = 0;

      if (mMemberDone == actMember) {
         iterHnd = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         nrOfSymbols = in.readInt(context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         symbols_count = in.readUInt(context);
         if (context.done)
            symbols = new RpcTcSymbol[symbols_count];
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = symbols_count != 0 ? startOffset1 % symbols_count : 0;
      if (startOffset1 >= symbols_count)
         startIdx11 = symbols_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < symbols_count; i11++) {
         if (mMemberDone == actMember) {
            if (symbols[i11] == null)
               symbols[i11] = new RpcTcSymbol();
            symbols[i11].read(in, context);
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
         out.writeInt(iterHnd, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         out.writeInt(nrOfSymbols, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      if (mMemberDone == actMember) {
         if (((symbols == null) || (symbols.length < symbols_count) || (TCI.rpcChunkLen < symbols_count)) &&
             ((symbols != null) || (symbols_count != 0)))
            throw new java.lang.IllegalArgumentException();
         out.writeUInt(symbols_count, context);
         if (!context.done)
            return;
         mMemberDone++;
      }
      actMember++;
      int startOffset1 = mMemberDone - actMember;
      int startIdx11 = symbols_count != 0 ? startOffset1 % symbols_count : 0;
      if (startOffset1 >= symbols_count)
         startIdx11 = symbols_count;
      actMember = mMemberDone;
      for (int i11 = startIdx11; i11 < symbols_count; i11++) {
         if (mMemberDone == actMember) {
            symbols[i11].write(out, context);
            if (!context.done)
               return;
            mMemberDone++;
            symbols[i11].reset(); //done for multiple use of input parameters
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
      size += SDRUtil.sizeInt(iterHnd);
      size += SDRUtil.sizeInt(nrOfSymbols);
      size += SDRUtil.sizeUInt(symbols_count);
      for (int i11 = 0; i11 < symbols_count; i11++) {
         size += symbols[i11].size();
      }
      size += SDRUtil.sizeBool(retVal);
      return size;
   }

   public void reset() {
      mMemberDone = 0;
      for (int i11 = 0; i11 < symbols_count; i11++) {
         symbols[i11].reset();
      }
   }
}
