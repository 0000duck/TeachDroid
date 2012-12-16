package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.plc.network.sysrpc.TCI.*;

public class TcSysRpcStructuralConstNode extends TcSysRpcStructuralNode implements TcStructuralConstNode {
   private static final SysRpcTcGetConstInfoIn SysRpcTcGetConstInfoIn = new SysRpcTcGetConstInfoIn();
   private static final SysRpcTcGetConstInfoOut SysRpcTcGetConstInfoOut = new SysRpcTcGetConstInfoOut();
   private static final int BIT_MASK_IS_LOADED = TcSysRpcStructuralNode.BIT_MASK_LAST * 2;
   protected TcSysRpcStructuralTypeNode type;
   protected TcValue value;
  
   TcSysRpcStructuralConstNode (int handle, TcSysRpcClient client) {
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
         synchronized (SysRpcTcGetConstInfoIn) {
         	SysRpcTcGetConstInfoIn.constScopeHnd = getHandle();
            client.client.SysRpcTcGetConstInfo_1(SysRpcTcGetConstInfoIn, SysRpcTcGetConstInfoOut);
            if (SysRpcTcGetConstInfoOut.retVal) {
               setConstInfo(SysRpcTcGetConstInfoOut.info);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralConstNode - loadInfo: ");
      }
      return false;
   }

   void setConstInfo (SysRpcTcConstInfo info) {
      setConstInfoLoaded(true);
      TcStructuralNode osn;
      if (info.typeHnd != 0) {
         osn = client.cache.getFromCache(info.typeHnd);
         if ((osn != null) && (osn instanceof TcSysRpcStructuralTypeNode)) {
            type = (TcSysRpcStructuralTypeNode) osn;
         } else {
            type = new TcSysRpcStructuralTypeNode(info.typeHnd, client);
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
      value.stringValue = info.value.sValue.toString();
   }

}
