package com.keba.kemro.teach.network;

/**
 * Map - Ziel - Objekt
 */
public class TcMapTarget {
   /** zeigt auf kein Objekt */
   public static final int MAP_TARGET_VOID = 0;
   /** zeigt auf eine TeachTalk - Routine oder - Variable */
   public static final int MAP_TARGET_INTERNAL = 1;
   /** zeigt auf eine Systemroutine oder -variable */
   public static final int MAP_TARGET_EXTERNAL = 2;

   protected boolean isInternal;
   protected Object[] path;
   protected TcStructuralRoutineNode routine;
   protected String external;
   protected boolean isValid;

   /**
    * 
    */
   public TcMapTarget () {
   	// do nothing
   }
   
   public void setValid (boolean valid) {
   	this.isValid = valid;
   }
   
   public void setValues (boolean isInternal, Object[] path,
                      TcStructuralRoutineNode routine, String external) {
      this.isInternal = isInternal;
      this.path = path;
      this.routine = routine;
      this.external = external;
   }
  
   /**
    * @return true if valid
    */
   public boolean isValid () {
   	return isValid;
   }
   /**
    * Liefert true für eine TeachTalk - Variable oder Routine zurück.
    *
    * @return true für eine TeachTalk - Variable oder Routine.
    */
   public boolean isInternal () {
      return isInternal;
   }

   /**
    * Liefert true für eine TachTalk - Routine zurück.
    *
    * @return true für eine TachTalk - Routine
    */
   public boolean isInternalMapToRoutine () {
      return isInternal && (routine != null);
   }

   /**
    * Liefert den Strukturbaumknoten der Routine zurück.
    *
    * @return Strukturbaumknoten
    */
   public TcStructuralRoutineNode getRoutine () {
      return routine;
   }

   /**
    * Liefert den Instanzpfad der Variable bzw. Variablekomponente zurück.
    * Pfad = Wurzelvariable {array index | Variablekomponente}.
    *
    * @return Instanzpfad
    */
   public Object[] getPath () {
      return path;
   }

   /**
    * Liefert die Zeichenkette für das externe Ziel (Systemvariable oder Systemroutine) zurück.
    *
    * @return Zeichenkette
    */
   public String getExternalMap () {
      return external;
   }
}