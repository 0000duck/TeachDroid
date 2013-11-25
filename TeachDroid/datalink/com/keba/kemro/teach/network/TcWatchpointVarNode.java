package com.keba.kemro.teach.network;

/**
 * Watchpoint - Variable
 */
public class TcWatchpointVarNode {
   /** Variablen - Handle des TeachControls */
   protected int handle;
   /** Instanzpfad */
   protected Object[] path;
   /** Ausführungseinheit für das Programm, in dem die Variable deklariert wurde */
   protected TcExecutionUnit execUnit;

   /**
    * Konstruktor
    *
    * @param handle Variablen - Handle
    * @param path Instanzpfad
    * @param execUnit Ausführungseinheit für das Programm, in dem die Variable deklariert wurde
    */
   protected TcWatchpointVarNode (int handle, Object[] path, TcExecutionUnit execUnit) {
      this.handle = handle;
      this.path = path;
      this.execUnit = execUnit;
   }

   /**
    * Liefert den Variablen - Handle zurück.
    *
    * @return Variablen - Handle
    */
   protected int getHandle () {
      return handle;
   }

   /**
    * Liefert den Instanzpfad zurück
    *
    * @return Instanzpfad
    */
   public Object[] getPath () {
      return path;
   }

   /**
    * Liefert die Ausführungseinheit des Programms zurück.
    *
    * @return Ausführungseinheit
    */
   public TcExecutionUnit getTcExecutionUnit () {
      return execUnit;
   }
   
   /**
    * @see java.lang.Object#hashCode()
    */
   public int hashCode () {
      return handle;
   }
   
   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object o) {
     if (this == o) {
     	return true;
     }
     if (!(o instanceof TcWatchpointVarNode)) {
     	return false;
     }
   	return getHandle() == ((TcWatchpointVarNode) o).getHandle();
   }
}