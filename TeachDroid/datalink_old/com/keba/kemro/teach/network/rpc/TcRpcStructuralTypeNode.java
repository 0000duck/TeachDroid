package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcStructuralTypeNode extends TcRpcStructuralNode implements TcStructuralTypeNode {
	static final int VARIANT_TYPE = RpcTcNodeKind.rpcVariableNode;
   private static final RpcTcGetTypeInfoIn rpcTcGetTypeInfoIn = new RpcTcGetTypeInfoIn();
   private static final RpcTcGetTypeInfoOut rpcTcGetTypeInfoOut = new RpcTcGetTypeInfoOut();
   private static final int BIT_MASK_IS_LOADED = TcRpcStructuralNode.BIT_MASK_LAST * 2;
   private static final int BIT_MASK_IS_SIZE_CONST = TcRpcStructuralNode.BIT_MASK_LAST * 4;
   /** Typart */
   byte typeKind;
   private int lowerBound;
   private int upperBound;
   /** Basistyp */
   private TcRpcStructuralTypeNode baseType;
   private TcRpcStructuralNode returnOrArrayElemOrUnit;
   private TcRpcStructuralConstNode lowerBoundConst;
   private TcRpcStructuralConstNode upperBoundConst;
   
	TcRpcStructuralTypeNode (int handle, TcRpcClient client) {
		super(handle, client);
   }

   /**
    * Liefert die Typart zurück.
    *
    * @return Typart
    */
   public byte getTypeKind () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return typeKind;
      }
      return UNKOWN_TYPE;
   }

   /**
    * Liefert die Unteregrenze eines Array oder Subrange - Typs zurück
    *
    * @return Untergrenze
    */
   public int getLowerBound () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return lowerBound;
      }
      return 0;
   }

   /**
    * Liefert die Oberegrenze eines Array oder Subrange - Typs zurück.
    *
    * @return Oberegrenze
    */
   public int getUpperBound () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return upperBound;
      }
      return 0;
   }

   /**
    * Liefert die untere Grenzkonstante eines Array oder Subrange - Typs zurück.
    *
    * @return untere Grenzkonstante
    */
   public TcStructuralConstNode getLowerBoundConst () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return lowerBoundConst;
      }
      return null;
   }

   /**
    * Liefert die obere Grenzkonstante eines Array oder Subrange - Typs zurück.
    *
    * @return obere Grenzkonstante
    */
   public TcStructuralConstNode getUpperBoundConst () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return (!isSizeConst()) ? upperBoundConst : null;
      }
      return null;
   }

   /**
    * Liefert die Größenkonstante eines Array - Typs zurück.
    *
    * @return Größenkonstante
    */
   public TcStructuralConstNode getArraySizeConst () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return isSizeConst() ? upperBoundConst : null;
      }
      return null;
   }

   /**
    * Liefert den Bausteintyp, von dem dieser Bausteintyp abgeleitet wurde, zurück.
    *
    * @return Basisbaustein
    */
   public TcStructuralTypeNode getBaseUnit () {
      if ((isTypeInfoLoaded() || loadTypeInfo()) && (typeKind == UNIT_TYPE)) {
         return (returnOrArrayElemOrUnit != null)
                ? (TcStructuralTypeNode) returnOrArrayElemOrUnit : null;
      }
      return null;
   }
   
   /**
    * Liefert den Bausteintyp, von dem dieser Bausteintyp abgeleitet wurde, zurück.
    *
    * @return Basisbaustein
    */
   public TcStructuralNode getVariantNode() {
      if ((isTypeInfoLoaded() || loadTypeInfo()) && (typeKind == STRUCT_TYPE)) {
    	  return returnOrArrayElemOrUnit ;
      }
      return null;
   }


   /**
    * Liefert den Basistyp (z.B. Array) zurück.
    *
    * @return Basistyp
    */
   public TcStructuralTypeNode getBaseType () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return baseType;
      }
      return null;
   }

   /**
    * Liefert den Rückgabetyp des Routinen - Types zurück.
    *
    * @return Rückgabetyp
    */
   public TcStructuralTypeNode getReturnType () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return ((typeKind == ROUTINE_TYPE) && (returnOrArrayElemOrUnit != null))
                ? (TcStructuralTypeNode) returnOrArrayElemOrUnit : null;
      }
      return null;
   }

   /**
    * Liefert den Typ des Array-Elementes zurück.
    *
    * @return Array-Elementtyp
    */
   public TcStructuralTypeNode getArrayElementType () {
      if (isTypeInfoLoaded() || loadTypeInfo()) {
         return ((typeKind == ARRAY_TYPE) && (returnOrArrayElemOrUnit != null))
                ? (TcStructuralTypeNode) returnOrArrayElemOrUnit : null;
      }
      return null;
   }

   private boolean isTypeInfoLoaded () {
      return isBitSet(BIT_MASK_IS_LOADED);
   }

   private void setTypeInfoLoaded (boolean b) {
      setBit(BIT_MASK_IS_LOADED, b);
   }

   private boolean isSizeConst () {
      return isBitSet(BIT_MASK_IS_SIZE_CONST);
   }

   private void setSizeConst (boolean b) {
      setBit(BIT_MASK_IS_SIZE_CONST, b);
   }

   private boolean loadTypeInfo () {
      try {
         synchronized (rpcTcGetTypeInfoIn) {
            rpcTcGetTypeInfoIn.typeScopeHnd = getHandle();
            client.client.RpcTcGetTypeInfo_1(rpcTcGetTypeInfoIn, rpcTcGetTypeInfoOut);
            if (rpcTcGetTypeInfoOut.retVal) {
               setTypeInfo(rpcTcGetTypeInfoOut.info);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralTypeNode - loadInfo: ");
      }
      return false;
   }

   /**
    * Setzt die Typinformation.
    *
    * @param info Typinformation
    */
   void setTypeInfo (RpcTcTypeInfo info) {
      setTypeInfoLoaded(true);
      typeKind = (byte) info.kind.value;
      lowerBound = info.lowerBound;
      upperBound = info.upperBound;
      TcStructuralNode osn;
      if (typeKind == ARRAY_TYPE) {
         if (info.arrayElemHnd != 0) {
            osn = client.cache.getFromCache(info.arrayElemHnd);
            if ((osn != null) && (osn instanceof TcRpcStructuralTypeNode)) {
            	returnOrArrayElemOrUnit = (TcRpcStructuralTypeNode) osn;
            } else {
            	returnOrArrayElemOrUnit = new TcRpcStructuralTypeNode(info.arrayElemHnd, client);
               client.cache.putToCache(returnOrArrayElemOrUnit);
            }
         }
      } else if (typeKind == UNIT_TYPE) {
         if ((info.baseTypeHnd == 0) && (info.baseUnitHnd != 0)) {
            osn = client.cache.getFromCache(info.baseUnitHnd);
            if ((osn != null) && (osn instanceof TcRpcStructuralTypeNode)) {
            	returnOrArrayElemOrUnit = (TcRpcStructuralTypeNode) osn;
            } else {
            	returnOrArrayElemOrUnit = new TcRpcStructuralTypeNode(info.baseUnitHnd, client);
               client.cache.putToCache(returnOrArrayElemOrUnit);
               client.cache.putToDirEntryCache(returnOrArrayElemOrUnit);
            }
         }
      } else if (typeKind == ROUTINE_TYPE) {
         if (info.returnTypeHnd != 0) {
            osn = client.cache.getFromCache(info.returnTypeHnd);
            if ((osn != null) && (osn instanceof TcRpcStructuralTypeNode)) {
            	returnOrArrayElemOrUnit = (TcRpcStructuralTypeNode) osn;
            } else {
            	returnOrArrayElemOrUnit = new TcRpcStructuralTypeNode(info.returnTypeHnd, client);
               client.cache.putToCache(returnOrArrayElemOrUnit);
            }
         }
      } else if ((info.variantVarHnd != 0) && ((typeKind == STRUCT_TYPE) || (typeKind == VARIANT_TYPE))) {
//        typeKind = VARIANT_TYPE;
         typeKind = STRUCT_TYPE;
         returnOrArrayElemOrUnit = new TcRpcStructuralVarNode(info.variantVarHnd, client);
      }
      if (info.baseTypeHnd != 0) {
         osn = client.cache.getFromCache(info.baseTypeHnd);
         if ((osn != null) && (osn instanceof TcRpcStructuralTypeNode)) {
            baseType = (TcRpcStructuralTypeNode) osn;
         } else {
            baseType = new TcRpcStructuralTypeNode(info.baseTypeHnd, client);
            client.cache.putToCache(baseType);
         }
      }
      if (info.lowerBoundConstHnd != 0) {
         osn = client.cache.getFromCache(info.lowerBoundConstHnd);
         if ((osn != null) && (osn instanceof TcRpcStructuralConstNode)) {
            lowerBoundConst = (TcRpcStructuralConstNode) osn;
         } else {
            lowerBoundConst = new TcRpcStructuralConstNode(info.lowerBoundConstHnd, client);
            client.cache.putToCache(lowerBoundConst);
         }
      }
      if (info.upperBoundConstHnd != 0) {
         osn = client.cache.getFromCache(info.upperBoundConstHnd);
         if ((osn != null) && (osn instanceof TcStructuralConstNode)) {
            upperBoundConst = (TcRpcStructuralConstNode) osn;
         } else {
            upperBoundConst = new TcRpcStructuralConstNode(info.upperBoundConstHnd, client);
            client.cache.putToCache(upperBoundConst);
         }
      }
      if (info.arraySizeConstHnd != 0) {
         setSizeConst(true);
         osn = client.cache.getFromCache(info.arraySizeConstHnd);
         if ((osn != null) && (osn instanceof TcRpcStructuralConstNode)) {
            upperBoundConst = (TcRpcStructuralConstNode) osn;
         } else {
            upperBoundConst = new TcRpcStructuralConstNode(info.arraySizeConstHnd, client);
            client.cache.putToCache(upperBoundConst);
         }
      }
   }

}
