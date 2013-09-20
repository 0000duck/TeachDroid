/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : sinn
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:  sinn
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.network;

import java.util.*;

/**
 * TcExecutionModel erm�glicht das Steuern und Auslesen TeachControl - Ausf�hrungsteils.
 *
 * @see TcExecutionUnit
 */
public interface TcExecutionModel {   
	public static final int FILTER_ALL = 4;

   /**
    * Copies the actual value from srouce variable to the destination variable
    * @param srcAccessHandle access handle of the source variable
    * @param srcExeUnit executioin handle of the routine, only necessary if the source variable is a routine variable
    * @param destAccessHandle access handle of the destination variable
    * @param destExeUnit executioin handle of the routine, only necessary if the destination variable is a routine variable
    * @return true if the value was successfully copied
    */
   public boolean copyActualValue(TcAccessHandle srcAccessHandle, TcExecutionUnit srcExecUnit,
   		TcAccessHandle destAccessHandle, TcExecutionUnit destExecUnit);

   	
  /**
    * Liefert f�r den angegebene Zugriffs - Handle der Variable den Aktualwert.
    *
    * @param accessHandle Zugriffs - Handle
    * @param exeUnit Ausf�hrungseinheit des Programms, in der die Variable deklariert wurde
    * @param value Aktualwert
    *
    * @return true f�r einen erfolgreichen Aufruf
    */
   public abstract boolean getActualValue (
                                         TcAccessHandle accessHandle, TcExecutionUnit execUnit,
                                         TcValue value);
 
   /**
    * Lefert f�r die angegebenen Zugriffs - Handles der Variablen die Aktualwerte.
    *
    * @param accessHandles enth�lt Zugriffs - Handles
    * @param execUnits Ausf�hrungseinheiten der Programme, in denen die Variablen deklariert wurden
    * @param values Aktualwerte
    *
    * @return true f�r einen erfolgreichen Aufruf
    */
   public abstract boolean getActualValues (Vector accessHandles, Vector execUnits, Vector values);

   /**
    * Setzt f�r den angegebene Zugriffs - Handle der Variable den Aktualwert.
    *
    * @param accessHandle Zugriffs - Handle
    * @param unit Ausf�hrungseinheit des Programms, in der die Variable deklariert wurde
    * @param value Aktualwert
    *
    * @return true f�r einen erfolgreichen Aufruf
    */
   public abstract boolean setActualValue (TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcValue value);
 	
  /**
    * Gets the map target.
    * @param accessHandle access handle of the variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param target map target
    * @return true if the target is successfully gotten
    */
   public abstract boolean getMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcMapTarget target);
   
   /**
    * Sets the map target. Target is a teachtalk variable or component.
    * @param accessHandle map to variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param path instance path of the destination
    * @return true if the target is successfully set
    */
   public abstract boolean setMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, Object[] path);

   /**
    * Sets the map target. Target is program routine or a unit routine.
    * @param accessHandle map to variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param path if routine is a routine of the unit then the the instance path is the instance of the unit variable
    * @param routine new routine destination 
    * @return true if the target is successfully set
    */
   public abstract boolean setMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, Object[] path, TcStructuralRoutineNode routine);

   /**
    * Sets the map target.
    * @param accessHandle map to variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param external new external variable destination
    * @return true if the target is successfully set
    */
   public abstract boolean setMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, String external);

   public abstract TcVariableGroup createVariableGroup (String groupName);
   public abstract boolean removeVariableGroup (TcVariableGroup group);
   public abstract int[] addVariablesToGroup (TcVariableGroup group, Vector accessHandles);
   public abstract boolean removeVariablesFromGroup (TcVariableGroup group, int[] variableIds);
   public abstract int[] getVariableGroupValues (TcVariableGroup group, Vector values);
   public abstract int[] getChangedVariableGroupValues (TcVariableGroup group, Vector values);
   
   /**
    * Liefert die Enumeration der Ausf�hrungseinheiten, die unter der Ausf�hrungseinheit parent
    * liegen.
    *
    * @param parent
    * @param kind ist die Art der Ausf�hrungeinheiten, welche bei der Enumeration geliefert werden
    *        sollen.
    * @param loadInfos gibt an, ob auch die Knoteninformationen der Ausf�hrungseinheiten gelesen
    *        werden sollen.
    *
    * @return Enumeration
    */
   public Enumeration getExecutionUnits (TcExecutionUnit parent, int kind, boolean loadInfos);

   /**
    * Liefert die Enumeration der Ausf�hrungseinheiten, die unter den Ausf�hrungseinheiten im
    * Vektor parent liegen.
    *
    * @param parents Ausf�hrungseinheiten
    * @param kind ist die Art der Ausf�hrungeinheiten, welche bei der Enumeration geliefert werden
    *        sollen.
    * @param loadInfos gibt an, ob auch die Knoteninformationen der Ausf�hrungseinheiten gelesen
    *        werden sollen.
    *
    * @return Enumeration
    */
   public Enumeration getExecutionUnits (Vector parents, int kind, boolean loadInfos);

   /**
    * Liefert die Wurzel aller Ausf�hrungseinheiten.
    *
    * @return Wurzel
    */
   public TcExecutionUnit getRoot ();

   /**
    * Bringt das angegebene Projekt project in Ausf�hrung.
    *
    * @param project Projekt, welches geladen werden soll.
    *
    * @return Ausf�hrungseinheit f�r das geladene Projekt
    */
   public TcExecutionUnit loadProject (TcStructuralNode project);
   
   /**
    * Entl�dt das Projekt project.
    *
    * @param project Projekt, welches abgeladen werden soll.
    *
    * @return true f�r das erfolgreiche Abladen
    */
   public boolean unloadProject (TcExecutionUnit project);

   /**
    * Startet die unbenannte Routine des Programms program.
    *
    * @param program Programm, das die unbenannte Routine enth�lt.
    *
    * @return Ausf�hrungseinheit f�r die unbenannte Routine
    */
   public TcExecutionUnit startProgram (TcStructuralNode program);

   /**
    * Starts the unnamed routine of the program.
    * @param program program to start
    * @param interrupt if true the unnamed routine will be stopped at the first statement 
    * @param restart if true the unnamed routine will be automatically restarted
    * @return execution unit for the unnamed routine
    */
   public TcExecutionUnit startProgram (TcStructuralNode program, boolean interrupt, boolean restart);

   /**
    * Unterbricht alle Routine des Ablaufes.
    *
    * @param exeUnit Wurzel eines Ablaufs
    *
    * @return true f�r das erfolgreiche Unterbrechen
    */
   public boolean interruptExeUnit (TcExecutionUnit execUnit);
   
   /**
    * Setzt den Ablauf fort.
    *
    * @param exeUnit Wurzel eines Ablaufs
    *
    * @return true f�r das erfolgreiche Fortsetzen
    */
   public boolean continueExeUnit (TcExecutionUnit execUnit);
   /**
    * Stopt den Ablauf.
    *
    * @param exeUnit Wurzel eines Ablaufs
    *
    * @return true f�r das erfolgreiche Stoppen
    */
   public boolean stopExeUnit (TcExecutionUnit execUnit);

   /**
    * Liest und setzt die Ausf�hrungseinheiteninformation (z.B. Art, Instanzpfad, 
    * Strukturbaumknoten, ...) 
    *
    * @param execUnits Ausf�hrungseinheiten
    *
    * @return true f�r den erfolgreichen Aufruf
    */
   public boolean setExecUnitInfos (Vector execUnits);
   
   /**
    * Sets the execution mode.
    * @param exeUnit execution unit
    * @param mode {EXECUTION_MODE_FLOW, EXECUTION_MODE_ROUTINE, EXECUTION_MODE_OFF} 
    * @return true if the mode was successfully set
    */
   public boolean setExecutionMode (TcExecutionUnit execUnit, int mode);
   
   /**
    * Switchs the main flow stepping on or off
    * @param routine root of the execution flow
    * @param enable true for on and false for off
    * @return returns true if the main flow stepping is successfully switched
    */
   public boolean setMainFlowStepping (TcExecutionUnit routine, boolean enable);

   /**
    * Liefert den Zustand der Ausf�hrungseinheit.
    *
    * @param execUnit Ausf�hrungseinheit
    * @param s Zustandsobjekt, welches nach dem Aufruf die Informationen enth�lt
    *
    * @return true f�r das erfolgreiche Lesen
    */
   public boolean getState (TcExecutionUnit execUnit, TcExecutionState s);

   /**
    * Liefert die Zust�nde der Ausf�hrungseinheiten.
    *
    * @param execUnits Ausf�hrungseinheiten
    * @param states Zustandsobjekte
    *
    * @return true f�r den erfolgreichen Aufruf
    */
   public boolean getStates (Vector execUnits, Vector states);
   
   /**
    * F�hrt ein oder mehrer Anweisungen mit der angegebenen Schrittart aus.
    *
    * @param execUnit Ausf�hrungseinheit
    * @param stepKind Schrittart
    *
    * @return true f�r den erfolgreichen Aufruf
    */
   public boolean step (TcExecutionUnit execUnit, int stepKind);

   /**
    * F�hrt ein oder mehrer Anweisungen mit der angegebenen Schrittart aus.
    *
    * @param execUnits Ausf�hrungseinheiten
    * @param stepKind Schrittart
    *
    * @return true f�r den erfolgreichen Aufruf
    */
   public boolean step (Vector execUnits, int stepKind);
   
   /**
    * Setzt den Ausf�hrungszeiger auf die angegebene Zeilenposition.
    *
    * @param execUnit Ausf�hrungseinheit
    * @param line Zeilenposition
    *
    * @return true f�r das erfolgreiche Setzen
    */
   public boolean setInstructionPointer (TcExecutionUnit execUnit, int line);

   
   /**
    * Executes the given routine. 
    *
    * @param routine routine which should be executed
    * @param instanceAccessHandle variable instance or null if the routine is program routine
    * @param parameter parameter of the routine (Access Handle, Boolean, Byte, Short, Integer, Long, Float or String)
    *
    * @return returns the routine return value if it was successfully executed otherwise null.
    * If the routine does not have a return value a Object will be retruned
    */
   public Object executeRoutine (TcStructuralRoutineNode routine,
                                         TcAccessHandle instanceAccessHandle,
                                         Object[] parameter);


public abstract Object[] readVarArray(TcExecutionUnit _execUnit, TcStructuralVarNode _tcArrayVarNode, TcAccessHandle _varAccess, TcStructuralTypeNode _elemType);


public abstract int[] getTcMemdump(TcStructuralNode _structTypeNode, TcStructuralNode _structVarNode, TcAccessHandle _accessHandle, TcExecutionUnit _execUnit) throws NullPointerException;


public abstract int[] getTcMemdump(int _memoffset, int _buffersize, TcAccessHandle _accessHandle);

}
