package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcStructuralVarNode extends TcRpcStructuralNode implements TcStructuralVarNode {
   private static final RpcTcGetVarInfoIn rpcTcGetVarInfoIn = new RpcTcGetVarInfoIn();
   private static final RpcTcGetVarInfoOut rpcTcGetVarInfoOut = new RpcTcGetVarInfoOut();
   private static final int BIT_MASK_IS_LOADED = TcRpcStructuralNode.BIT_MASK_LAST * 2;
   private static final int BIT_MASK_IS_PUBLIC = TcRpcStructuralNode.BIT_MASK_LAST * 4;
   private static final int BIT_MASK_IS_SAVE = TcRpcStructuralNode.BIT_MASK_LAST * 8;
   protected static final int BIT_MASK_IS_OPTIONAL = TcRpcStructuralNode.BIT_MASK_LAST * 16;
   protected static final int BIT_MASK_IS_READONLY = TcRpcStructuralNode.BIT_MASK_LAST * 32;
   private byte varKind;
   private TcRpcStructuralTypeNode type;
   
	TcRpcStructuralVarNode (int handle, TcRpcClient client) {
		super(handle, client);
   }
   /**
    * Liefert die Variablenart (VAR, PARAM, CONST_PARAM oder VALUE_PARAM) zurück.
    *
    * @return Variablenart
    */
   public byte getVarKind () {
      if (isVarInfoLoaded() || loadVarInfo()) {
         return varKind;
      }
      return -1;
   }

   /**
    * Liefert den Typ der Variable zurück.
    *
    * @return Variablentyp
    */
   public TcStructuralTypeNode getType () {
      if (isVarInfoLoaded() || loadVarInfo()) {
         return type;
      }
      return null;
   }

   /**
    * Gibt die Sichtbarkeit an.
    *
    * @return true für eine Pulic - Variable
    */
   public boolean isPublic () {
      if (isVarInfoLoaded() || loadVarInfo()) {
         return isBitSet(BIT_MASK_IS_PUBLIC);
      }
      return false;
   }

   private void setPublic (boolean b) {
      setBit(BIT_MASK_IS_PUBLIC, b);
   }

   /**
    * Liefert true für eine Save-Variable zurück.
    *
    * @return true für eine Save-Variable
    */
   public boolean isSave () {
      if (isVarInfoLoaded() || loadVarInfo()) {
         return isBitSet(BIT_MASK_IS_SAVE);
      }
      return false;
   }

   private void setSave (boolean b) {
      setBit(BIT_MASK_IS_SAVE, b);
   }
   
   /**
    * Returns true if the variable is a optional parameter.
    * @return true for a optional parameter
    */
   public boolean isOptional () {
      if (isVarInfoLoaded() || loadVarInfo()) {
         return isBitSet(BIT_MASK_IS_OPTIONAL);
      }
      return false;
   }

   private void setOptional (boolean b) {
      setBit(BIT_MASK_IS_OPTIONAL, b);
   }
   
   
   /**
    * Returns true if the variable is a optional parameter.
    * @return true for a optional parameter
    */
   public boolean isReadOnly () {
      if (isVarInfoLoaded() || loadVarInfo()) {
         return isBitSet(BIT_MASK_IS_READONLY);
      }
      return false;
   }

   private void setReadOnly (boolean b) {
      setBit(BIT_MASK_IS_READONLY, b);
   }

   /**
    * Returns true if the variable is referenced.
    * @return true if the variable is referenced
    */
   public boolean isReferenced () {
      try {
         synchronized (rpcTcGetVarInfoIn) {
            rpcTcGetVarInfoIn.varScopeHnd = getHandle();
            client.client.RpcTcGetVarInfo_1(rpcTcGetVarInfoIn, rpcTcGetVarInfoOut);
            if (rpcTcGetVarInfoOut.retVal) {
         		return (rpcTcGetVarInfoOut.info.attr & RpcTcVarAttr.rpcVarAttrIsReferenced) == RpcTcVarAttr.rpcVarAttrIsReferenced;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralVarNode - loadInfo: ");
      }
      return false;
  }


   private boolean isVarInfoLoaded () {
      return isBitSet(BIT_MASK_IS_LOADED);
   }

   private void setVarInfoLoaded (boolean b) {
      setBit(BIT_MASK_IS_LOADED, b);
   }

   private boolean loadVarInfo () {
      try {
         synchronized (rpcTcGetVarInfoIn) {
            rpcTcGetVarInfoIn.varScopeHnd = getHandle();
            client.client.RpcTcGetVarInfo_1(rpcTcGetVarInfoIn, rpcTcGetVarInfoOut);
            if (rpcTcGetVarInfoOut.retVal) {
               setVarInfo(rpcTcGetVarInfoOut.info);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralVarNode - loadInfo: ");
      }
      return false;
   }
   

   /**
    * Setzt die Variableninformation.
    *
    * @param info Variableninformation
    */
   void setVarInfo (RpcTcVarInfo info) {
      setVarInfoLoaded(true);
      setSave(info.isSave);
      setReadOnly((info.attr & RpcTcVarAttr.rpcVarAttrIsReadonly) == RpcTcVarAttr.rpcVarAttrIsReadonly);
      varKind = (byte) info.kind.value;
      setOptional((info.attr & RpcTcVarAttr.rpcVarAttrIsOptional) == RpcTcVarAttr.rpcVarAttrIsOptional);
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
      setPublic(!info.isPrivate);
   }
}
