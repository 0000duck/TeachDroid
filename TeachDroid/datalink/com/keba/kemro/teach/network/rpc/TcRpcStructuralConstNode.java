package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcStructuralConstNode extends TcRpcStructuralNode implements TcStructuralConstNode {
   private static final RpcTcGetConstInfoIn rpcTcGetConstInfoIn = new RpcTcGetConstInfoIn();
   private static final RpcTcGetConstInfoOut rpcTcGetConstInfoOut = new RpcTcGetConstInfoOut();
   private static final int BIT_MASK_IS_LOADED = TcRpcStructuralNode.BIT_MASK_LAST * 2;
   protected TcRpcStructuralTypeNode type;
   protected TcValue value;
  
   TcRpcStructuralConstNode (int handle, TcRpcClient client) {
      super(handle, client);
   }

   /**
    * Liefert den Wert der Konstanten zurück.
    *
    * @return Wert
    */
   public TcValue getValue () {
      if (isConstInfoLoaded() || loadConstInfo()) {
         return value;
      }
      return null;
   }

   /**
    * Liefert den Typ der Konstanten zurück.
    *
    * @return Typ
    */
   public TcStructuralTypeNode getType () {
      if (isConstInfoLoaded() || loadConstInfo()) {
         return type;
      }
      return null;
   }

   protected boolean isConstInfoLoaded () {
      return isBitSet(BIT_MASK_IS_LOADED);
   }

   protected void setConstInfoLoaded (boolean b) {
      setBit(BIT_MASK_IS_LOADED, b);
   }
 
   protected boolean loadConstInfo () {
      try {
         synchronized (rpcTcGetConstInfoIn) {
            rpcTcGetConstInfoIn.constScopeHnd = getHandle();
            client.client.RpcTcGetConstInfo_1(rpcTcGetConstInfoIn, rpcTcGetConstInfoOut);
            if (rpcTcGetConstInfoOut.retVal) {
               setConstInfo(rpcTcGetConstInfoOut.info);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralConstNode - loadInfo: ");
      }
      return false;
   }

   void setConstInfo (RpcTcConstInfo info) {
      setConstInfoLoaded(true);
      TcStructuralNode osn;
      if (info.typeHnd != 0) {
         osn = client.cache.getFromCache(info.typeHnd);
         if ((osn != null) && (osn instanceof TcRpcStructuralTypeNode)) {
            type = (TcRpcStructuralTypeNode) osn;
         } else {
            type = new TcRpcStructuralTypeNode(info.typeHnd, client);
            client.cache.putToCache(type);
         }
      }
      value = new TcValue();
      value.boolValue = info.value.bValue;
      value.int8Value = (byte) info.value.i8Value;
      value.int16Value = (short) info.value.i16Value;
      value.int32Value = info.value.i32Value;
      value.int64Value = info.value.i64Value;
      value.floatValue = info.value.fValue;
      value.stringValue = info.value.sValue;
   }

}
