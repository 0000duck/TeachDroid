package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.plc.network.sysrpc.TCI.*;

public class TcSysRpcExecutionUnit implements TcExecutionUnit {
   private static final String EMPTY_STRING = "";

   private int handle;
   private TcSysRpcClient client;
   private int kind;
   private int priority;
   private boolean isMainFlow;
   private String callPath = EMPTY_STRING;
   private TcSysRpcStructuralNode ortsStructuralNode;
   private TcSysRpcExecutionUnit parent;
   

   /**
    * Konstruktor
    *
    * @param handle ist die Identifikation einer Ausführungseinheit im TeachControl.
    */
   TcSysRpcExecutionUnit (int handle, TcSysRpcClient client) {
      this.handle = handle;
		this.client = client;
  }

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object o) {
     if (this == o) {
    	 return true;
     }
     if (!(o instanceof TcSysRpcExecutionUnit)) {
    	 return false;
     }
  	 return getHandle() == ((TcSysRpcExecutionUnit) o).getHandle();
   }

   /**
    * @see java.lang.Object#hashCode()
    */
   public int hashCode () {
      return handle;
   }

   /**
    * Liefert den Ausführungseinheit - Handle zurück.
    *
    * @return Ausführungseinheit - Handle
    */
   public int getHandle () {
      return handle;
   }

   /**
    * Liefert die übergeordnente Ausführungseinheit zurück. 
    *
    * @return Ausführungseinheit
    */
   public TcExecutionUnit getParent () {
      return parent;
   }

   /**
    * Liefert die Art der Ausführungseinheit zurück.
    *
    * @return Art der Ausführungseinheit
    */
   public int getKind () {
      return kind;
   }

   /**
    * Liefert die Ausführungspriorität zurück.
    *
    * @return Ausführungspriorität
    */
   public int getPriority () {
      return priority;
   }

   /**
    * Liefert den Aufrufpfad der Ausführungseinheit (nur gültig für Routinen) zurück.
    *
    * @return Aufrufpfad
    */
   public String getCallPath () {
      return callPath;
   }

   /**
    * Liefert den Wert true zurück, wenn der Ablauf durch den Start eines Programmes
    * (TcExecutionModel.startProgram) hervorgegangen ist.
    *
    * @see TcExecutionModel
    */
   public boolean isMainFlow () {
      return isMainFlow;
   }

   /**
    * Liefert den Strukturbaumknoten (TcStructuralNode) zurück, welches die
    * Ausführungseinheit repräsentiert.
    *
    * @see TcStructuralNode
    */
   public TcStructuralNode getTcStructuralNode () {
      return ortsStructuralNode;
   }

   /**
    * Setzt die übergeordnente Ausführungseinheit. 
    *
    * @param parent übergeordnente Ausführungseinheit
    */
   protected void setParent (TcSysRpcExecutionUnit parent) {
      this.parent = parent;
   }

   /**
    * Setzt die Werte (Art, Aufrufpfad, ...) der Ausführungseinheit
    *
    * @param info Werte
    */
   protected void setInfo (SysRpcTcExeUnitInfo info) {
      kind = info.kind.value;
      priority = info.priority;
      isMainFlow = info.isMainFlow;
      callPath = info.callPath.toString();
      if (kind == TcExecutionUnit.ROUTINE) {
         ortsStructuralNode = null;
         if (info.scopeHnd != 0) {
            ortsStructuralNode = (TcSysRpcStructuralNode) client.cache.getFromCache(info.scopeHnd);
            if (
                (ortsStructuralNode == null) ||
                !(ortsStructuralNode instanceof TcSysRpcStructuralRoutineNode)) {
               ortsStructuralNode = new TcSysRpcStructuralRoutineNode(info.scopeHnd, client);
               client.cache.putToCache(ortsStructuralNode);
            }
         }      	
      } else if (kind == TcExecutionUnit.INVALID) {
      	// there isn't a valid scope handle
      } else {
         ortsStructuralNode = null;
         if (info.scopeHnd != 0) {
            ortsStructuralNode = (TcSysRpcStructuralNode) client.cache.getFromDirEntryCache(info.scopeHnd);
            if (
                (ortsStructuralNode == null) ||
                (ortsStructuralNode.getKind() != TcStructuralNode.PROGRAM)) {
               ortsStructuralNode = new TcSysRpcStructuralNode(info.scopeHnd, client);
               client.cache.putToDirEntryCache(ortsStructuralNode);
            }
         }
      }
   }

}
