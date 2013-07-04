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
 * TcStructuralModel liefert die Zurgriffsfunktionen für den Strukturbaum des TeachControls. Beim
 * Kompilieren von Bausteindatein und Programmdateien wird für jeden Typ, Variable, Baustein, 
 * Programm, Routine, usw. ein Knoten erzeugt und im Strukturbaum abgelegt. Die Wurzel ist Global,
 * darunter können Projekte, Programme, Bausteine, Konstanten, Typen Variablen und Routinen sein.
 * Programme und Bausteine können Konstanten, Typen Variablen und Routinen enthalten. Routinen 
 * können Parameter(Variablen) und Variablen enthalten. Jede Hierarchieebene wird als
 * Sichtbarkeitsbereich bezeichnet (scope).
 *
 * @see TcStructuralNode
 * @see TcStructuralRoutineNode
 * @see TcStructuralTypeNode
 * @see TcStructuralVarNode
 */
public interface TcStructuralModel {
     
   /**
    * Kompiliert das angegebene Projekt. Die Kompilationsfehlermeldungen können mit der Methode
    * getErrorMessage ausgelesen werden.
    *
    * @param dirEntry des Projekts
    *
    * @return true für einen erfolgreichen Aufruf
    */
   public TcStructuralNode build (TcDirEntry dirEntry);
   public boolean destroy (TcStructuralNode project);
   
   /**
    * Liefert den Verzeichniseintrag für den Strukturbaumknoten (Projekt, Programm oder Baustein) 
    * oder für die Deklarationsdatei, in der der Strukturbaumknoten deklariert wurde, zurück.
    *
    * @return Verzeichniseintrag
    */
   public TcDirEntry getDirEntry (TcStructuralNode node);

   	/**
	 * Liefert den entsprechende Strukturbaumknoten (TcStructuralNode) zurück, welches den
	 * Verzeichniseintrag repräsentiert. Nur Projektverzeichnisse, Programm-, Bausteineinträge
	 * besitzen einen Strukturbaumknoten.
	 *
	 * @return Strukturbaumknoten
	 * @see TcStructuralNode
	 */
   public TcStructuralNode getNode (TcDirEntry entry);
   /**
    * Liefert alle Knoten der angegebenen Art im Sichbarkeitsbereich scope.
    *
    * @param scope der Sichtbarkeitsbereich unter dem enumeriert wird.
    * @param kind Art des Knotens
    *
    * @return Enumeration der Strukturbaumknoten
    *
    * @see TcStructuralNode
    */
   public Enumeration getNodes (TcStructuralNode scope, int kind);
   
   
   /**
    * Liefert den Wurzelknoten des Strukturbaums.
    *
    * @return Wurzelknoten
    */
   public TcStructuralNode getRoot();
   
   public boolean supportsChangeList ();
   /**
    * Returns changes that happened after the startCounter.
    * @param startCounter counter from which the changes should be read
    * @return changes that happened after startCounter otherwise null if there wasn't any changes
    */
   public TcStructuralChanges getChanges (int startCounter);

   /**
    * Returns true is the source files of the project have changed since last project build.
    * @param project project structural node
    * @return true if the source files of the project have changed
    */
   public boolean projectSourcesChanged (TcStructuralNode project);
   /**
    * Returns the attributes for the given node.
    * @param node node
    * @return attributes
    */
   public String getAttributes (TcStructuralNode node);

   /**
    * Returns the attributes for the given nodes.
    * @param nodes nodes
    * @return attributes
    */
   public String[] getAttributes (Vector nodes);

   /**
    * Sets the attributes for the given node.
    * @param node node
    * @param attributes attributes
    * @return true if the attributes has successfully set
    */
   public boolean setAttirbutes (TcStructuralNode node, String attributes);

   /**
    * Erzeugt eine Variablenknoten im Strukturbaum.
    *
    * @param dirEntryNode Programm oder Baustein, in der die Variable deklariert werden soll 
    * @param parent bestimmt die Sichtbarkeit, für eine globale Variable ist parent der Wurzelknoten 
    *               oder ein Projekt, für eine public oder private Variable ist 
    *               parent ein Programm oder Bautstein und für einen Parameter oder 
    *               Routinenvariable ist parent eine Routine
    * @param name Variablenname
    * @param varKind VAR, PARAM, CONST_PARAM oder CONST_VAR deklariert in TcStructuralVarNode
    * @param type Variablentyp
    * @param isProjectSave true für eine Project-Save-Variable
    * @param isSave true für eine Save-Variable
    * @param isPrivate true für eine private Variable
    * @param isDynamic true für Variable ohne Deklaration in Sourcefile  
    *
    * @return TcStructuralVarNode    neue Variable
    *
    * @see TcStructuralVarNode
    */
   public TcStructuralVarNode addVariable (
                                                  TcStructuralNode declarationNode,
                                                  TcStructuralNode parent, String name, byte varKind,
                                                  TcStructuralTypeNode type, boolean isProjectSave,
                                                  boolean isSave, boolean isPrivate, boolean isDynamic);
    
	/**
	 * Creates a program.
	 * 
	 * @param parent global or project node
	 * @param name name of the program
	 * @return program node
	 */
	public TcStructuralNode addProgram (TcStructuralNode parent, String name);

   /**
    * Erzeugt einen Routinenknoten im Strukturbaum.
    *
    * @param dirEntryNode Programm oder Baustein, in der die Routine deklariert werden soll 
    * @param parent bestimmt die Sichtbarkeit, für eine globale Routine ist parent der Wurzelknoten 
    *               oder ein Projekt, für eine public oder private Routine ist 
    *               parent ein Programm oder Bautstein
    * @param name Routinenname
    * @param routineKind UNNAMED_ROUTINE, NAMED_ROUTINE oder AT_ROUTINE, sind deklariert in TcStructuralRoutineNode
    * @param returnType Rückgabetyp der Routine
    * @param eventVariable Ereignisvariable
    * @param isPrivate true für eine private Routine
    *
    * @return TcStructuralRoutineNode    neue Routine
    *
    * @see TcStructuralRoutineNode
	 */
	public TcStructuralRoutineNode addRoutine (
												  TcStructuralNode declarationNode,
												  TcStructuralNode parent, String name, byte routineKind,
												  TcStructuralTypeNode returnType, 
												  TcStructuralVarNode eventVariable,
												  boolean isPrivate);
	

   /**
    * Renames the variable.
    * @param var variable to rename
    * @param name new name
    * @return new variable node if the renaming of the variable was successfully
    */
   public TcStructuralVarNode renameVariable (TcStructuralVarNode var, String name);
   /**
    * Moves a variable to the destination scope.
    * @param var variable to move
    * @param dest destination scope
    * @return the new variable was successfully moved
    */
   public TcStructuralVarNode moveVariable (TcStructuralVarNode var, TcStructuralNode dest);

	/**
    * Entfernt eine Strukturbaumknoten.
    *
    * @param node Strukturbaumknoten
    *
    * @return boolean    true für das erfolgreiche Entfernen
    */
   public boolean removeNode (TcStructuralNode node);

   /**
    * Liefert den Zugriffs-Handle für den angegebenen Komponentenpfad. Der Zurgriffs-
    * Handle ist für das Auslesen der Aktualwerte und Watchpoint - Variablenwerte 
    * notwendig.
    * fullPath = project  + program + variable + {'.' + type component | '[ + array index + ']'}
    * program is only necessary for public or private variables.  
    *
    * @param fullPath Variable
    *
    * @return Zugriffs-Handle
    */
   public TcAccessHandle getVarAccessHandle (String fullPath);
   
   
   public TcAccessHandle getVoidAccessHandle ();
   
   public TcAccessHandle getVarAccessHandle (Object[] instancePath);


   /**
    * Speichert alle Save-Werte im angegebenen Sichtbarkeitsbereich.
    *
    * @param scope Projekt oder Programm
    *
    * @return true für einen erfolgreichen Aufruf
    */
   public boolean writeBackSaveValues (TcStructuralNode scope);
   
   /**
    * Writes the new variable declaration with the new init values. For the new init
    * values are taken the actual values.
    * @param variable variable which decaration should be written
    * @return true if the declaration is written successfully
    */
   public boolean writeBackInitValues (TcStructuralVarNode variable);

   public TcEditorModel getEditorModel (TcStructuralNode node);
   
   /**
    * Fügt das angegeben Text text an der Postion pos der Routine routine ein und 
    * speichert den Editorinhalt. Das Einfügen erfolgt inkrementell und kann zur 
    * Ausführungszeit durchgeführt werden. Die Prüfung erfolgt im TeachControl, bei 
    * einem Misserfolg können die Kompilerfehlermeldung mit der Methode
    * TcStructuralModel.getErrorMessage ausgelesen werden.
    *
    * @param routine Routine, in die das Statement eingefügt werden soll
    * @param pos Einfügeposition
    * @param text Statement
    *
    * @return true für das erfolgreiche Einfügen 
    */
   public boolean insertTextInc (TcStructuralRoutineNode routine, int line, String text);

   /**
    * Löscht den angegeben Textbereich. Das Löschen erfolgt inkrementell und kann zur 
    * Ausführungszeit durchgeführt werden. Die Prüfung erfolgt im TeachControl, bei 
    * einem Misserfolg können die Kompilerfehlermeldung mit der Methode
    * TcStructuralModel.getErrorMessage ausgelesen werden.
    *
    * @param routine Routine, in die das Statement enthält
    * @param range Textbereich
    *
    * @return true für das erfolgreiche Löschen
    */
   public boolean deleteTextInc (TcStructuralRoutineNode routine, int line, int count);
   
   /**
    * Ersetzt den angegeben Textbereich range durch den Text text. Das Löschen erfolgt
    * inkrementell und kann zur Ausführungszeit durchgeführt werden. Die Prüfung erfolgt
    * im TeachControl, bei einem Misserfolg können die Kompilerfehlermeldung mit der Methode
    * TcStructuralModel.getErrorMessage ausgelesen werden.
    * 
    * @param routine Routine, in die das Statement enthält
    * @param range der zu ersetzende Textbereich
    * @param text	neuer Text
    * @return true für das erfolgreiche Ersetzen
    */
   public boolean replaceTextInc (TcStructuralRoutineNode routine, int line, int count, String text);

   
   /**
    * Liefert die Fehlermeldungen des letzten build - Aufrufes bzw. der inkrementellen
    * Editieroperation zurück.
    *
    * @return Fehlermeldung
    */
   public TcErrorMessage getErrorMessage ();
	/**
	 * Returns the compiler error messages of the last build form the project.
	 * @param project project to check
	 * @return true if the target is successfully gotten
	 */
	public TcErrorMessage[] getErrorMessages (TcStructuralNode project);
	
   /**
    * Setzt einen Breakpoint oder einen Watchpoint. Für execUnit und varPath null angeben, außer es
    * ist ein Breakpoint bzw. Watchpoint bezogen auf eine Bausteininstanz notwendig.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    * @param execUnit Programmausführungseinheit, in der die Bausteininstanz deklariert wurde.
    * @param varPath Bausteininstanzpfad
    * @param kind BREAKPOINT, WATCHPOINT or MAIN_FLOW_BREAKPOINT
     *
    * @return true für das erfolgreiche Stoppen
    */
   public boolean setCodePoint (
                                       TcStructuralRoutineNode routine, int lineNr,
                                       TcExecutionUnit execUnit, Object[] varPath,
                                       int kind);
   /**
    * Aktivieren eines Breakpoints oder Watchpoints.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    * @param enable true für einen freigeschalteten Breakpoint bzw. Watchpoint
    *
    * @return true für den erfolgreichen Aufruf
    */
   public boolean enableCodePoint (TcStructuralRoutineNode routine, int lineNr,
                                          boolean enable);

   /**
    * Entfernt einen Breakpoint oder Watchpoint.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    *
    * @return true für das erfolgreiche Entfernen
    */
   public boolean removeCodePoint (TcStructuralRoutineNode routine, int lineNr);

   /**
    * Entfernt alle Breakpoints und Watchpoints im angegeben TeachTalk -  Project, - Programm und -
    * Bausteine.
    *
    * @param structNode Strukturbaumknoten
    *
    * @return true für das erfolgreiche Stoppen
    */
   public boolean removeAllCodePoints (TcStructuralNode structNode);

   /**
    * Liefert den Wert des Watchpoints - Zählers
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    *
    * @return true für das erfolgreiche Lesen
    */
   public int getWatchpointCounter (TcStructuralRoutineNode routine, int lineNr);

   /**
    * Setzt den Watchpoints - Zähler auf 0.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    *
    * @return true für das erfolgreiche zurücksetzen
    */
   public boolean resetWatchpointCounter (TcStructuralRoutineNode routine, int lineNr);

   /**
    * Setzt eine Watchpoint - Variable.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    * @param execUnit Ausführungseinheit des Programms, in der die Variable deklariert wurde
    * @param path Variableninstanzpfad, enthält TcStructuralVarNode oder Integer Object
    *
    * @return true für das erfolgreiche Setzten
    */
   public TcWatchpointVarNode setWatchpointVariable (TcStructuralRoutineNode routine,
                                                            int lineNr, TcExecutionUnit execUnit,
                                                            Object[] path);

   /**
    * Löscht die angegebene Watchpoint - Variable.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    * @param variable Watchpoint - Variable
    *
    * @return true für das erfolgreiche Löschen
    */
   public boolean removeWatchpointVariable (
                                                   TcStructuralRoutineNode routine, int lineNr,
                                                   TcWatchpointVarNode variable);

   /**
    * Löscht alle Watchpoint - Variable des Watchpoints.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    *
    * @return true für das erfolgreiche Löschen
    */
   public boolean removeAllWatchpointVariable (TcStructuralRoutineNode routine, int lineNr);
   
   /**
    * Liest den Wert der Watchpoint - Variable.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    * @param variable Watchpoint - Variable
    * @param value Wertobjekt
    *
    * @return true für das erfolgreiche Lesen
    */
   public boolean getWatchpointVariableValue (
                                                     TcStructuralRoutineNode routine, int lineNr,
                                                     TcWatchpointVarNode variable,
                                                     TcValue value);
   /**
    * Liefert die Watchpoint - Variablen des angegebenen Watchpoints.
    *
    * @param routine Routine
    * @param lineNr Zeilennummer
    *
    * @return true für den erfolgreiche Aufruf
    */
   public TcWatchpointVarNode[] getWatchpointVariables (
                                                               TcStructuralRoutineNode routine,
                                                               int lineNr);
   /**
    * Returns a list of routines which have a codepoint set.
    * @param force ture to get a list anyway
    * @return list of routines
    */
   public TcCodePointRoutineList getCodePointRoutines (int lastChangeCount);
   /**
    * Liefert die CodePoints für die angegebene Routine.
    *
    * @param routine Routine
    *
    * @return true für den erfolgreichen Aufruf
    */
   public TcCodePoint[] getCodePoints (TcStructuralRoutineNode routine);


}
