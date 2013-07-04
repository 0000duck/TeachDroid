package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.base.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcStructuralRoutineNode extends TcRpcStructuralNode implements TcStructuralRoutineNode {
   private static final RpcTcGetRoutineInfoIn rpcTcGetRoutineInfoIn = new RpcTcGetRoutineInfoIn();
   private static final RpcTcGetRoutineInfoOut rpcTcGetRoutineInfoOut = new RpcTcGetRoutineInfoOut();
   private static final int BIT_MASK_IS_LOADED = TcRpcStructuralNode.BIT_MASK_LAST * 2;
   private static final int BIT_MASK_IS_PUBLIC = TcRpcStructuralNode.BIT_MASK_LAST * 4;
   private byte routineKind;
   private int nrOfStatements;
   private TcRpcStructuralNode node;
  
	TcRpcStructuralRoutineNode (int handle, TcRpcClient client) {
		super(handle, client);
   }

   /**
    * Liefert die Routinenart (UNNAMED_ROUTINE, NAMED_ROUTINE oder AT_ROUTINE) zurück.
    *
    * @return Routinenart
    */
   public byte getRoutineKind () {
      if (isRoutineInfoLoaded() || loadRoutineInfo()) {
         return routineKind;
      }
      return UNNAMED_ROUTINE;
   }

   /**
    * Liefert den Rückgabetyp der Routine zurück.
    *
    * @return Typ
    */
   public TcStructuralTypeNode getReturnType () {
      if (isRoutineInfoLoaded() || loadRoutineInfo()) {
         return ((node != null) && (routineKind != AT_ROUTINE)) ? (TcStructuralTypeNode) node : null;
      }
      return null;
   }

   /**
    * Liefert die Ereignisvariable einer Ereignisroutine zurück.
    *
    * @return Ereignisvariable
    */
   public TcStructuralVarNode getEventVariable () {
      if (isRoutineInfoLoaded() || loadRoutineInfo()) {
         return ((node != null) && (routineKind == AT_ROUTINE)) ? (TcStructuralVarNode) node : null;
      }
      return null;
   }

   /**
    * Liefert die Anzahl der Statements zurück.
    *
    * @return Anzahl der Statements
    */
   public int getNrOfStatements () {
      if (isRoutineInfoLoaded() || loadRoutineInfo()) {
         return nrOfStatements;
      }
      return 0;
   }

   /**
    * Gibt die Sichtbarkeit an.
    *
    * @return true für eine Public - Routine
    */
   public boolean isPublic () {
      if (isRoutineInfoLoaded() || loadRoutineInfo()) {
         return isBitSet(BIT_MASK_IS_PUBLIC);
      }
      return false;
   }

   private void setPublic (boolean b) {
      setBit(BIT_MASK_IS_PUBLIC, b);
   }

   private boolean isRoutineInfoLoaded () {
      return isBitSet(BIT_MASK_IS_LOADED);
   }

   private void setRoutineInfoLoaded (boolean b) {
      setBit(BIT_MASK_IS_LOADED, b);
   }

   private boolean loadRoutineInfo () {
      try {
         synchronized (rpcTcGetRoutineInfoIn) {
            rpcTcGetRoutineInfoIn.routineScopeHnd = getHandle();
            client.client.RpcTcGetRoutineInfo_1(rpcTcGetRoutineInfoIn, rpcTcGetRoutineInfoOut);
            if (rpcTcGetRoutineInfoOut.retVal) {
               setRoutineInfo(rpcTcGetRoutineInfoOut.info);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralRoutineNode - loadInfo: ");
      }
      return false;
   }

   /**
    * Setzt die Routineinfo.
    *
    * @param info Routineinfo
    */
   void setRoutineInfo (RpcTcRoutineInfo info) {
      setRoutineInfoLoaded(true);
      routineKind = (byte) info.kind.value;
      nrOfStatements = info.nrOfStmts;
      if (routineKind == AT_ROUTINE) {
         node = (info.eventVarHnd != 0) ? new TcRpcStructuralVarNode(info.eventVarHnd, client) : null;
      } else {
         TcStructuralAbstractNode osn;
         if (info.retTypeHnd != 0) {
            osn = client.cache.getFromCache(info.retTypeHnd);
            if ((osn != null) && (osn instanceof TcRpcStructuralTypeNode)) {
               node = (TcRpcStructuralTypeNode) osn;
            } else {
               node = new TcRpcStructuralTypeNode(info.retTypeHnd, client);
               client.cache.putToCache(node);
            }
         }
      }
      setPublic(!info.isPrivate);
   }

}
