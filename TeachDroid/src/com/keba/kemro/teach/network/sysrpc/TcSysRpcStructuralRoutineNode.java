package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.base.*;
import com.keba.kemro.plc.network.sysrpc.TCI.*;

public class TcSysRpcStructuralRoutineNode extends TcSysRpcStructuralNode implements TcStructuralRoutineNode {
   private static final SysRpcTcGetRoutineInfoIn SysRpcTcGetRoutineInfoIn = new SysRpcTcGetRoutineInfoIn();
   private static final SysRpcTcGetRoutineInfoOut SysRpcTcGetRoutineInfoOut = new SysRpcTcGetRoutineInfoOut();
   private static final int BIT_MASK_IS_LOADED = TcSysRpcStructuralNode.BIT_MASK_LAST * 2;
   private static final int BIT_MASK_IS_PUBLIC = TcSysRpcStructuralNode.BIT_MASK_LAST * 4;
   private byte routineKind;
   private int nrOfStatements;
   private TcSysRpcStructuralNode node;
  
	TcSysRpcStructuralRoutineNode (int handle, TcSysRpcClient client) {
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
         synchronized (SysRpcTcGetRoutineInfoIn) {
         	SysRpcTcGetRoutineInfoIn.routineScopeHnd = getHandle();
            client.client.SysRpcTcGetRoutineInfo_1(SysRpcTcGetRoutineInfoIn, SysRpcTcGetRoutineInfoOut);
            if (SysRpcTcGetRoutineInfoOut.retVal) {
               setRoutineInfo(SysRpcTcGetRoutineInfoOut.info);
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
   void setRoutineInfo (SysRpcTcRoutineInfo info) {
      setRoutineInfoLoaded(true);
      routineKind = (byte) info.kind.value;
      nrOfStatements = info.nrOfStmts;
      if (routineKind == AT_ROUTINE) {
         node = (info.eventVarHnd != 0) ? new TcSysRpcStructuralVarNode(info.eventVarHnd, client) : null;
      } else {
         TcStructuralAbstractNode osn;
         if (info.retTypeHnd != 0) {
            osn = client.cache.getFromCache(info.retTypeHnd);
            if ((osn != null) && (osn instanceof TcSysRpcStructuralTypeNode)) {
               node = (TcSysRpcStructuralTypeNode) osn;
            } else {
               node = new TcSysRpcStructuralTypeNode(info.retTypeHnd, client);
               client.cache.putToCache(node);
            }
         }
      }
      setPublic(!info.isPrivate);
   }

}
