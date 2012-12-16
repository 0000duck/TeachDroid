package com.keba.kemro.teach.network;

/**
 * Zustandsobjekt
 */
public class TcExecutionState {
   /** TC Version < 3.00: Kein Debug - Modus */
   public static final int EXECUTION_MODE_OFF = 0;
   /**
    * Execution mode flow
    */
   public static final int EXECUTION_MODE_FLOW = 1;
   /**
    * Routien - Debug - Modus, in diesem Modus sind nur einzelne Routinen im Debug - Modus.
    * Routinen die von einer in Debug - Modus befindlichen Routine entstehen sind automatisch im
    * Debug - Modus. Jeder beliebigen Routinen eines Ablaufs kann der Debug - Modus  gesetzt oder
    * zur�ckgesetzt werden.
    */
   public static final int EXECUTION_MODE_ROUTINE = 2;
   

   
   /** Ausf�hrungszustand - Invalid, d.h. die Routine ist nicht mehr in Ausf�hrung */
   public static final int STATE_INVALID = -1;
   /** Ausf�hrungszustand - Waiting, d.h. die Routine steht auf einer WAIT - Anweisung */
   public static final int STATE_WAITING = 0;
   /** Ausf�hrungszustand - Running, d.h. die Routine wird ausgef�hrt */
   public static final int STATE_RUNNING = 1;
   /** Ausf�hrungszustand - Stepping, d.h. die Routine ist im Debug - Modus und angehalten */
   public static final int STATE_STEPPING = 2;
   /**
    * Ausf�hrungszustand - Finished, d.h. die Routine ist beendet, aber eine oder mehrere
    * CO-Routinen sind noch in Ausf�hrung.
    */
   public static final int STATE_FINISHED = 3;
   /** Ausf�hrungszustand - final interrupted, d.h. der Ablauf wurde unterbrochen */
   public static final int STATE_INTERRUPTED = 4;
   /** execution state - project loading, means that at least one new routine isn't finished */
   public static final int STATE_PROJECT_LOADING = 100;
   /** execution state - project loading, means that at least one delete routine isn't finished */
   public static final int STATE_PROJECT_UNLOADING = 101;

   /** Kein Debug - Zustand */
   public static final int STEP_OFF = 0;
   /** Debug - Zustand und Ausf�hrungsschritt: Routine angehalten */
   public static final int STEP_BREAK = 1;
   /** Debug - Zustand und Ausf�hrungsschritt: Routine verfolgen */
   public static final int STEP_INTO = 2;
   /** Debug - Zustand und Ausf�hrungsschritt: Routine �berspringen */
   public static final int STEP_OVER = 3;
   /** Debug - Zustand und Ausf�hrungsschritt: Routine verlassen */
   public static final int STEP_OUT = 4;
   /** Debug - Zustand und Ausf�hrungsschritt: Routine ausf�hren bis zur n�chsten WAIT - Anweisung */
   public static final int STEP_GOTO_WAIT = 5;
   /** Debug - Zustand und Ausf�hrungsschritt: Routine ausf�hren bis zum n�chsten Breakpoint */
   public static final int STEP_GOTO_BREAKPOINT = 6;


   /** Start flag isn't supported */
   public static final int START_FLAG_NOT_SUPPORTED = 0;
   /** Start flag is set to disabled which means that the flow won't be started with the START hard key */
   public static final int START_FLAG_DISABLED = 1;
   /** Start flag is set to enabled which means that the flow will be started with the START hard key */
   public static final int START_FLAG_ENABLED = 2;

	/** 
    * execution state 
    * {STATE_WAITING, STATE_RUNNING, STATE_STEPPING, STATE_FINISHED, STATE_INTERRUPTED,
    * STATE_INVALID, STATE_PROJECT_LOADING, STATE_PROJECT_UNLOADING} */
   public int executionState = STATE_INVALID;
   /**
    * stepping state
    * {STEP_OFF, STEP_BREAK, STEP_INTO, STEP_OVER, STEP_OUT, STEP_GOTO_WAIT, STEP_GOTO_BREAKPOINT }
    */
   public int steppingState;
   /** 
    * {EXECUTION_MODE_FLOW, EXECUTION_MODE_ROUTINE, EXECUTION_MODE_OFF} 
    */
   public int executionMode;
   /** main flow stepping is on (true) or off (false) */
   public boolean  isMainFlowStepping;
   
   /** Zeilennummer des Ausf�hrungszeigers */
   public int line;
   /** Zeilennummer des Hauptlaufzeigers */
   public int mainFlowLine;
   /** Anzahl der Ausf�hrungseinheit die unter diesem Ausf�hrungsknoten vorhanden sind */
   public int childCount;
   /** �nderungsz�hler */
   public int changeCount;
   /** Letzer �nderungsz�hlerwert */
   public int oldChangeCount = -1;
   
   public int startFlag;

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object obj) {
     if (this == obj) {
     	return true;
     }
     if (!(obj instanceof TcExecutionState)) {
     	return false;
     }
     TcExecutionState s = (TcExecutionState)obj;
   	return (s.executionState == executionState) &&
            (s.steppingState == steppingState) && (s.executionMode == executionMode) &&
            (s.isMainFlowStepping == isMainFlowStepping) &&
            (s.line == line) && (s.mainFlowLine == mainFlowLine) &&
            (s.childCount == childCount) &&
            (s.changeCount == changeCount);
   }
   
   /**
    * @see java.lang.Object#hashCode()
    */
   public int hashCode () {
   	return ((executionState & 0x7) << 29) | ((steppingState & 0x7) << 26) 
   					| ((executionMode & 0x3) << 24) | ((line & 0xf) << 20)
   					| ((mainFlowLine & 0xf) << 16) | ((childCount & 0xf) << 12)
   					| (mainFlowLine & 0xfff);
   }

   /**
    * Gibt die �nderung des �nderungsz�hlers bekannt.
    *
    * @return true f�r die �nderung des �nderungsz�hlers
    */
   public boolean hasChanged () {
      return oldChangeCount != changeCount;
   }

   /**
    * Speichert den �nderungsz�hlerwert zwischen.
    */
   public void resetChangeFlag () {
      oldChangeCount = changeCount;
   }
   
   /**
    * 
    */
   public void setChangeFlag () {
      oldChangeCount = -1;
   }
   
   /**
    * @return START_FLAG_NOT_SUPPORTED, START_FLAG_DISABLED or START_FLAG_ENABLED
    */
   public int getStartFlag () {
   	return startFlag;
   }
}