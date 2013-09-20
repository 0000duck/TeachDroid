package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgParamUnion implements XDR {
   public TRepSysMsgParamKind mKind;
   // case null.eRepSysFloatParam
   public float mFltVal;
   // case null.eRepSysIntParam
   // case null.eRepSysStringParam
   // case null.eRepSysWStringParam
   // case null.eRepSysMemoryParam
   // case null.eRepSysUINT32Param
   // case null.eRepSysUINT64Param
   // case null.eRepSysRealParam
   public int mIntVal;
   // default

   public TRepSysMsgParamUnion () {
      mKind = new TRepSysMsgParamKind();
   }

   public void write (RPCOutputStream out) throws RPCException, IOException {
      mKind.write(out);
      switch (mKind.value) {
      case TRepSysMsgParamKind.eRepSysFloatParam: 
         out.writeFloat(mFltVal);
         break;
      case TRepSysMsgParamKind.eRepSysIntParam: 
      case TRepSysMsgParamKind.eRepSysStringParam: 
      case TRepSysMsgParamKind.eRepSysWStringParam: 
      case TRepSysMsgParamKind.eRepSysMemoryParam: 
      case TRepSysMsgParamKind.eRepSysUINT32Param: 
      case TRepSysMsgParamKind.eRepSysUINT64Param: 
      case TRepSysMsgParamKind.eRepSysRealParam: 
         out.writeInt(mIntVal);
         break;
      default: 
         
         break;
      }
   }

   public void read (RPCInputStream in) throws RPCException, IOException {
      mKind.read(in);
      switch (mKind.value) {
      case TRepSysMsgParamKind.eRepSysFloatParam: 
      mFltVal = in.readFloat();
   break;
      case TRepSysMsgParamKind.eRepSysIntParam: 
      case TRepSysMsgParamKind.eRepSysStringParam: 
      case TRepSysMsgParamKind.eRepSysWStringParam: 
      case TRepSysMsgParamKind.eRepSysMemoryParam: 
      case TRepSysMsgParamKind.eRepSysUINT32Param: 
      case TRepSysMsgParamKind.eRepSysUINT64Param: 
      case TRepSysMsgParamKind.eRepSysRealParam: 
      mIntVal = in.readInt();
   break;
      default: 
   break;
      }
   }
}