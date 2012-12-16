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
    * Liefert true f�r eine TeachTalk - Variable oder Routine zur�ck.
    *
    * @return true f�r eine TeachTalk - Variable oder Routine.
    */
   public boolean isInternal () {
      return isInternal;
   }

   /**
    * Liefert true f�r eine TachTalk - Routine zur�ck.
    *
    * @return true f�r eine TachTalk - Routine
    */
   public boolean isInternalMapToRoutine () {
      return isInternal && (routine != null);
   }

   /**
    * Liefert den Strukturbaumknoten der Routine zur�ck.
    *
    * @return Strukturbaumknoten
    */
   public TcStructuralRoutineNode getRoutine () {
      return routine;
   }

   /**
    * Liefert den Instanzpfad der Variable bzw. Variablekomponente zur�ck.
    * Pfad = Wurzelvariable {array index | Variablekomponente}.
    *
    * @return Instanzpfad
    */
   public Object[] getPath () {
      return path;
   }

   /**
    * Liefert die Zeichenkette f�r das externe Ziel (Systemvariable oder Systemroutine) zur�ck.
    *
    * @return Zeichenkette
    */
   public String getExternalMap () {
      return external;
   }
}