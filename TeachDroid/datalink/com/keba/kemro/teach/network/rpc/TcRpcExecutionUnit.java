package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcExecutionUnit implements TcExecutionUnit {
   private static final String EMPTY_STRING = "";

   private int handle;
   private TcRpcClient client;
   private int kind;
   private int priority;
   private boolean isMainFlow;
   private String callPath = EMPTY_STRING;
   private TcRpcStructuralNode ortsStructuralNode;
   private TcRpcExecutionUnit parent;
   

   /**
    * Konstruktor
    *
    * @param handle ist die Identifikation einer Ausf�hrungseinheit im TeachControl.
    */
   TcRpcExecutionUnit (int handle, TcRpcClient client) {
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
     if (!(o instanceof TcRpcExecutionUnit)) {
    	 return false;
     }
  	 return getHandle() == ((TcRpcExecutionUnit) o).getHandle();
   }

   /**
    * @see java.lang.Object#hashCode()
    */
   public int hashCode () {
      return handle;
   }

   /**
    * Liefert den Ausf�hrungseinheit - Handle zur�ck.
    *
    * @return Ausf�hrungseinheit - Handle
    */
   public int getHandle () {
      return handle;
   }

   /**
    * Liefert die �bergeordnente Ausf�hrungseinheit zur�ck. 
    *
    * @return Ausf�hrungseinheit
    */
   public TcExecutionUnit getParent () {
      return parent;
   }

   /**
    * Liefert die Art der Ausf�hrungseinheit zur�ck.
    *
    * @return Art der Ausf�hrungseinheit
    */
   public int getKind () {
      return kind;
   }

   /**
    * Liefert die Ausf�hrungspriorit�t zur�ck.
    *
    * @return Ausf�hrungspriorit�t
    */
   public int getPriority () {
      return priority;
   }

   /**
    * Liefert den Aufrufpfad der Ausf�hrungseinheit (nur g�ltig f�r Routinen) zur�ck.
    *
    * @return Aufrufpfad
    */
   public String getCallPath () {
      return callPath;
   }

   /**
    * Liefert den Wert true zur�ck, wenn der Ablauf durch den Start eines Programmes
    * (TcExecutionModel.startProgram) hervorgegangen ist.
    *
    * @see TcExecutionModel
    */
   public boolean isMainFlow () {
      return isMainFlow;
   }

   /**
    * Liefert den Strukturbaumknoten (TcStructuralNode) zur�ck, welches die
    * Ausf�hrungseinheit repr�sentiert.
    *
    * @see TcStructuralNode
    */
   public TcStructuralNode getTcStructuralNode () {
      return ortsStructuralNode;
   }

   /**
    * Setzt die �bergeordnente Ausf�hrungseinheit. 
    *
    * @param parent �bergeordnente Ausf�hrungseinheit
    */
   protected void setParent (TcRpcExecutionUnit parent) {
      this.parent = parent;
   }

   /**
    * Setzt die Werte (Art, Aufrufpfad, ...) der Ausf�hrungseinheit
    *
    * @param info Werte
    */
   protected void setInfo (RpcTcExeUnitInfo info) {
      kind = info.kind.value;
      priority = info.priority;
      isMainFlow = info.isMainFlow;
      callPath = info.callPath;
      if (kind == TcExecutionUnit.ROUTINE) {
         ortsStructuralNode = null;
         if (info.scopeHnd != 0) {
            ortsStructuralNode = (TcRpcStructuralNode) client.cache.getFromCache(info.scopeHnd);
            if (
                (ortsStructuralNode == null) ||
                !(ortsStructuralNode instanceof TcRpcStructuralRoutineNode)) {
               ortsStructuralNode = new TcRpcStructuralRoutineNode(info.scopeHnd, client);
               client.cache.putToCache(ortsStructuralNode);
            }
         }      	
      } else if (kind == TcExecutionUnit.INVALID) {
      	// there isn't a valid scope handle
      } else {
         ortsStructuralNode = null;
         if (info.scopeHnd != 0) {
            ortsStructuralNode = (TcRpcStructuralNode) client.cache.getFromDirEntryCache(info.scopeHnd);
            if (
                (ortsStructuralNode == null) ||
                (ortsStructuralNode.getKind() != TcStructuralNode.PROGRAM)) {
               ortsStructuralNode = new TcRpcStructuralNode(info.scopeHnd, client);
               client.cache.putToDirEntryCache(ortsStructuralNode);
            }
         }
      }
   }

}
