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
package com.keba.kemro.teach.dfl.codepoint;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.execution.*;
import com.keba.kemro.teach.dfl.structural.var.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.network.*;

import java.util.*;


/**
 * Diese Klasse verwaltet CodePoints. CodePoints sind Breakpoints und Watchpoints.
 */
public class KCodePointAdministrator {
	/**  execution breakpoint */
	public static final int BREAKPOINT = TcCodePoint.BREAKPOINT;
	/**  execution watchpoint */
	public static final int WATCHPOINT = TcCodePoint.WATCHPOINT;
	/**  execution main flow breakpoint */
	public static final int MAIN_FLOW_BREAKPOINT = TcCodePoint.MAIN_FLOW_BREAKPOINT;
   private final Vector listeners = new Vector();
   private final Vector cpRoutineList = new Vector();
   private final TcValue value = new TcValue();
   private boolean isActivated;
   private KTcDfl dfl;
   
   protected KCodePointAdministrator (KTcDfl dfl) {
   	this.dfl = dfl;
	}
	protected void init () {  	
      dfl.structure.addStructAdministratorListener(new KStructAdministratorListener() {
            public void treeChanged (KStructNode parent) {
               if (isActivated && (parent instanceof KStructProject)) {
                  checkProject((KStructProject) parent);
                  fireAllCodePointsChanged();
               }
            }
            public void nodeInserted (KStructNode parent, KStructNode node) {
               if (isActivated && (node instanceof KStructProject)) {
                  checkProject((KStructProject) node);
                  fireAllCodePointsChanged();
               }
            }
            public void nodeRemoved (KStructNode parent, KStructNode node) {
               if (isActivated && (node instanceof KStructProject)) {
                  resetProject((KStructProject) node);
                  fireAllCodePointsChanged();
               }
            }
         });

      dfl.execution.addListener(new KExecAdministratorListener() {
            public void execUnitsRemovedAdded (Vector toRemove, Vector toInsert) {
               if (isActivated) {
                  for (int i = 0; i < toRemove.size(); i++) {
                     KExecUnitNode eun = (KExecUnitNode) toRemove.elementAt(i);
                     if ((eun instanceof KExecUnitProject)) {
                        resetWatchpointVariables((KExecUnitProject) eun);
                     }
                  }
                  for (int i = 0; i < toInsert.size(); i++) {
                     KExecUnitNode eun = (KExecUnitNode) toInsert.elementAt(i);
                     if (eun instanceof KExecUnitProject) {
                        checkWatchpointVariables((KExecUnitProject) eun);
                     }
                  }
               }
            }
            public void updateState () {
         		// interface method not used
            }
         });
   }
   /**
    * Liefert true, wenn der KCodePointAdministrator aktiv ist.
    *
    * @return ture wenn der KCodePointAdministrator aktiv ist
    */
   public boolean isActivated () {
      return isActivated;
   }

   /**
    * Setzt den KCodePointAdministrator aktiv. Bei bestehender Verbindung zum TeachControl 
    * werden alle CodePoints im TeachControl gelöscht und die CodePoints des 
    * KCodePointAdministrators gesetzt. Anschließend wird die Methode allChanged der 
    * CodePoint - Listener aufgerufen. Ein CodePoint ist gültig, wenn er erfolgreich 
    * im TeachControl gesetzt werden konnte.
    *
    * @param b true für aktiv
    */
   public void setActivated (boolean b) {
      if (b) {
         isActivated = true;
         synchronized (dfl.getLockObject()) {
            KStructRoot root = dfl.structure.getRoot();
            checkProject(root);
            fireAllCodePointsChanged();
         }

         // check watchpoint variables
         synchronized (dfl.getLockObject()) {
            KExecUnitScope root = dfl.execution.getRoot();
            checkWatchpointVariables(root);
            for (int i = 0; i < root.getExecUnitProjectCount(); i++) {
               checkWatchpointVariables(root.getExecUnitProject(i));
            }
         }
      } else {
         isActivated = false;
         synchronized (dfl.getLockObject()) {
            checkProject(dfl.structure.getRoot());
            fireAllCodePointsChanged();
         }
      }
   }

   private void checkProject (KStructProject project) {
      if (project instanceof KStructRoot) {
         resetRoot((KStructRoot) project);
      } else {
         resetProject(project);
      }

      checkCodePointRoutineNodes();

      if (isActivated) {
         for (int i = 0; i < cpRoutineList.size(); i++) {
            CodePointRoutineNode cprn = (CodePointRoutineNode) cpRoutineList.elementAt(i);
            if (cprn.getStructRoutine() != null) {
               KStructNode sn = cprn.getStructRoutine().getParent();
               while ((sn != null) && !sn.equals(project)) {
                  sn = sn.getParent();
               }
               if ((sn != null) && sn.equals(project)) {
                  setCodePoints(cprn);
               }
            }
         }
      }
   }

   private void resetRoot (KStructRoot root) {
      // remove all CodePoints
      for (int i = 0; i < root.projects.getChildCount(); i++) {
         KStructProject p = (KStructProject) root.projects.getChild(i);
         resetProject(p);
      }
   }

   private void resetProject (KStructProject project) {
      // remove all CodePoints
      dfl.client.structure.removeAllCodePoints(project.getTcStructuralNode());
      for (int i = 0; i < cpRoutineList.size(); i++) {
         CodePointRoutineNode cprn = (CodePointRoutineNode) cpRoutineList.elementAt(i);
         if (cprn.routine != null) {
            KStructNode p = cprn.routine.getParent();
            while (!(p instanceof KStructRoot) && !project.equals(p)) {
               p = p.getParent();
            }
            if (project.equals(p)) {
               for (int j = 0; j < cprn.codePointList.size(); j++) {
                  CodePoint cp = (CodePoint) cprn.codePointList.elementAt(j);
                  for (int k = 0; k < cp.variables.size(); k++) {
                     WatchpointVariableNode wpvn =
                        (WatchpointVariableNode) cp.variables.elementAt(k);
                     wpvn.clearComponentPath();
                     wpvn.setTcWatchpointVariableNode(null);
                  }
                  cp.setValid(false);
               }
               cprn.routine = null;
            }
         }
      }
   }

   private void checkCodePointRoutineNodes () {
      for (int i = 0; i < cpRoutineList.size(); i++) {
         CodePointRoutineNode cprn = (CodePointRoutineNode) cpRoutineList.elementAt(i);
         if (cprn.getStructRoutine() == null) {
            String fullPath = cprn.getPath();
            KStructNode sn = dfl.structure.getRoot();
            int beg = 0;
            int end = 0;
            while ((sn != null) && (beg < fullPath.length())) {
               end = fullPath.indexOf(".", beg);
               if (end == -1) {
                  end = fullPath.length();
               }
               String name = fullPath.substring(beg, end);
               beg = end + 1;
               KStructNode child = null;
               if (sn instanceof KStructScope) {
                  for (int j = 0; j < ((KStructScope) sn).projects.getChildCount(); j++) {
                     child = ((KStructScope) sn).projects.getChild(j);
                     if (child.getKey().equalsIgnoreCase(name)) {
                        break;
                     }
                     child = null;
                  }
               }
               if ((child == null) && (sn instanceof KStructProject)) {
                  for (int j = 0; j < ((KStructProject) sn).programs.getChildCount(); j++) {
                     child = ((KStructProject) sn).programs.getChild(j);
                     if (child.getKey().equalsIgnoreCase(name)) {
                        break;
                     }
                     child = null;
                  }
                  if (child == null) {
                     for (int j = 0; j < ((KStructProject) sn).units.getChildCount(); j++) {
                        child = ((KStructProject) sn).units.getChild(j);
                        if (child.getKey().equalsIgnoreCase(name)) {
                           break;
                        }
                        child = null;
                     }
                  }
               }
               sn = child;
            }
            if ((sn != null) && (beg == (fullPath.length() + 1))) {
               KStructNodeVector children = null;
               if (sn instanceof KStructProject) {
                  children = ((KStructProject) sn).routines;
               } else if (sn instanceof KStructProgram) {
                  children = ((KStructProgram) sn).routines;
               } else if (sn instanceof KStructTypeUnit) {
                  children = ((KStructTypeUnit) sn).routines;
               }
               if (children != null) {
                  for (int j = 0; j < children.getChildCount(); j++) {
                     KStructRoutine child = (KStructRoutine) children.getChild(j);
                     if (child.getKey().equals(cprn.getName())) {
                        sn = child;
                        cprn.routine = child;
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private void setCodePoints (CodePointRoutineNode cprn) {
      // set all codepoints
      for (int j = 0; j < cprn.codePointList.size(); j++) {
         CodePoint cp = (CodePoint) cprn.codePointList.elementAt(j);
         if (!cp.isValid()) {
            if (
            		dfl.client.structure.setCodePoint(
                                              (TcStructuralRoutineNode) cp.cpRoutineNode.routine.getTcStructuralNode(),
                                              cp.getLineNr(), null, null, cp.getKind())) {
               cp.setValid(true);
               if (!cp.isEnabled()) {
                  if (
                      !dfl.client.structure.enableCodePoint(
                                                        (TcStructuralRoutineNode) cp.cpRoutineNode.getStructRoutine()
                                                                                                  .getTcStructuralNode(),
                                                        cp.getLineNr(), false)) {
                     cp.setValid(false);
                  }
               }
            }
         }
      }
   }

   private void checkWatchpointVariables (KExecUnitProject project) {
      KStructProject sp = (KStructProject) project.getKStructNode();
      for (int i = 0; i < cpRoutineList.size(); i++) {
         CodePointRoutineNode cprn = (CodePointRoutineNode) cpRoutineList.elementAt(i);
         for (int j = 0; j < cprn.codePointList.size(); j++) {
            CodePoint cp = (CodePoint) cprn.codePointList.elementAt(j);
            if (cp.isValid() && (cp.getKind() == WATCHPOINT) && (0 < cp.variables.size())) {
               for (int k = 0; k < cp.variables.size(); k++) {
                  WatchpointVariableNode wpn = (WatchpointVariableNode) cp.variables.elementAt(k);
                  if (
                      (wpn.getTcWatchpointVariableNode() == null) &&
                      (wpn.getComponentPath() != null)) {
                     Object[] path = wpn.getComponentPath();
                     KStructVar sv = (KStructVar) path[0];
                     KStructNode sn = sv.getParent();
                     while ((sn != null) && !sn.equals(sp)) {
                        sn = sn.getParent();
                     }
                     if ((sn != null) && (sn.equals(sp))) {
                        TcWatchpointVarNode ortsWpvn;
                        ortsWpvn =
                        	dfl.client.structure.setWatchpointVariable(
                                                                  (TcStructuralRoutineNode) cp.cpRoutineNode.getStructRoutine()
                                                                                                            .getTcStructuralNode(),
                                                                  cp.getLineNr(),
                                                                  null,
                                                                  wpn.getTcComponentPath());
                        if (ortsWpvn != null) {
                           wpn.setTcWatchpointVariableNode(ortsWpvn);
                           fireWatchpointVariableChanged(cp, wpn);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void resetWatchpointVariables (KExecUnitProject project) {
      KStructProject sp = (KStructProject) project.getKStructNode();
      for (int i = 0; i < cpRoutineList.size(); i++) {
         CodePointRoutineNode cprn = (CodePointRoutineNode) cpRoutineList.elementAt(i);
         for (int j = 0; j < cprn.codePointList.size(); j++) {
            CodePoint cp = (CodePoint) cprn.codePointList.elementAt(j);
            if (cp.isValid() && (cp.getKind() == WATCHPOINT) && (0 < cp.variables.size())) {
               for (int k = 0; k < cp.variables.size(); k++) {
                  WatchpointVariableNode wpn = (WatchpointVariableNode) cp.variables.elementAt(k);
                  if (
                      (wpn.getTcWatchpointVariableNode() != null) &&
                      (wpn.getComponentPath() != null)) {
                     Object[] path = wpn.getComponentPath();
                     KStructVar sv = (KStructVar) path[0];
                     KStructNode sn = sv.getParent();
                     while ((sn != null) && !sn.equals(sp)) {
                        sn = sn.getParent();
                     }
                     if ((sn != null) && (sn.equals(sp))) {
                     	dfl.client.structure.removeWatchpointVariable(
                                                                  (TcStructuralRoutineNode) cp.cpRoutineNode.getStructRoutine()
                                                                                                            .getTcStructuralNode(),
                                                                  cp.getLineNr(),
                                                                  wpn.getTcWatchpointVariableNode());
                        wpn.setTcWatchpointVariableNode(null);
                        fireWatchpointVariableChanged(cp, wpn);
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Fügt einen CodePoint hinzu. Wurde der CodePoint erfolgreich hinzugefügt, dann wird die
    * Methode added vom KCodePointListner aufgerufen. Ist der KCodePointAdministrator aktiv 
    * geschaltet und besteht die Verbindung zum TeachControl, dann wird der CodePoint auch im 
    * TeachControll gestetzt.
    *
    * @param routine Routine, in der der CodePoint hinzugefügt werden soll 
    * @param lineNr Zeilennummer für den CodePoint
    * @param kind BREAKPOINT, WATCHPOINT or MAIN_FLOW_BREAKPOINT
    */
   public void addCodePoint (KStructRoutine routine, int lineNr, int kind) {
      CodePointRoutineNode cprn = getCodePointRoutineNode(routine, true);
      addCodePointTo(cprn, lineNr, kind);
   }

   /**
    * Fügt eine Watchpoint - Variable hinzu. Wurde der Watchpoint - Variable erfolgreich 
    * hinzugefügt, dann wird die Methode watchpointVariableAdded vom KCodePointListner 
    * aufgerufen. Ist der KCodePointAdministrator aktiv geschaltet und besteht die Verbindung 
    * zum TeachControl, dann wird die Watchpoint - Variable auch im TeachControll gestetzt.
    *
    * @param cp Watchpoint
    * @param name Name der Variablen bzw. der Komponente
    * @param fullPath fullpath = [Projektname + '.'] + [Programmname + '.'] +
    * Variablename + { '.' + Komponentenname | '[' + Array - Index + ']'}.
    */
   public void addWatchpointVariable (CodePoint cp, String name, String fullPath) {
      if (
          (cp != null) && (cp.getKind() == WATCHPOINT) && (name != null) && (0 < name.length()) &&
          (fullPath != null) && (0 < fullPath.length())) {
         WatchpointVariableNode wpvn = new WatchpointVariableNode(name, fullPath, cp);

         for (int i = 0; i < cp.variables.size(); i++) {
            WatchpointVariableNode wpvnc = (WatchpointVariableNode) cp.variables.elementAt(i);
            if (wpvnc.equals(wpvn)) {
               return;
            }
         }
         addWatchpointVariable(cp, wpvn);
      }
   }

   /**
    * Fügt eine Watchpoint - Variable hinzu. Wurde die Watchpoint - Variable erfolgreich 
    * hinzugefügt, dann wird die Methode watchpointVariableAdded vom KCodePointListner 
    * aufgerufen. Ist der KCodePointAdministrator aktiv geschaltet und besteht die Verbindung zum 
    * TeachControl, dann wird die Watchpoint - Variable auch im TeachControll gestetzt.
    *
    * @param cp Watchpoint
    * @param fullNodePath componentPath = [KStructProject] + [KStructProgram] +
    * KStructVar + {  KStructVar | Integer (Array -Index) }.
    */
   public void addWatchpointVariable (CodePoint cp, Vector fullNodePath) {
      if (
          (cp != null) && (cp.getKind() == WATCHPOINT) && (fullNodePath != null) &&
          (0 < fullNodePath.size())) {
         // add variable
         Object[] path = new Object[fullNodePath.size()];
         fullNodePath.copyInto(path);
         WatchpointVariableNode wpvn = new WatchpointVariableNode(path, cp);

         for (int i = 0; i < cp.variables.size(); i++) {
            WatchpointVariableNode wpvnc = (WatchpointVariableNode) cp.variables.elementAt(i);
            if (wpvnc.equals(wpvn)) {
               return;
            }
         }
         addWatchpointVariable(cp, wpvn);
      }
   }

   private void addWatchpointVariable (CodePoint cp, WatchpointVariableNode wpvn) {
      cp.variables.addElement(wpvn);
      Object[] path = wpvn.getComponentPath();
      if (isActivated && (path != null)) {
         TcWatchpointVarNode ortsWpvn;
         ortsWpvn =
         	dfl.client.structure.setWatchpointVariable(
                                                   (TcStructuralRoutineNode) cp.cpRoutineNode.getStructRoutine()
                                                                                             .getTcStructuralNode(),
                                                   cp.getLineNr(), null,
                                                   wpvn.getTcComponentPath());
         if (ortsWpvn != null) {
            wpvn.setTcWatchpointVariableNode(ortsWpvn);
         }
      }
      fireWatchpointVariableAdded(cp, wpvn);
   }

   /**
    * Entfernt die angegebene Watchpoint - Variable. Wurde die Watchpoint - Variable erfolgreich
    * entfernt, dann wird die Methode watchpointVariableRemoved vom KCodePointListner aufgerufen.
    * Ist der KCodePointAdministrator aktiv geschaltet und besteht die Verbindung zum 
    * TeachControl, dann wird die Watchpoint - Variable auch im TeachControll entfernt.
    *
    * @param variable Watchpoint - Variable
    */
   public void removeWatchpointVariable (WatchpointVariableNode variable) {
      CodePoint cp = variable.getCodePoint();

      // remove
      cp.variables.removeElement(variable);
      if (isActivated) {
      	dfl.client.structure.removeWatchpointVariable(
                                                   (TcStructuralRoutineNode) cp.cpRoutineNode.getStructRoutine()
                                                                                             .getTcStructuralNode(),
                                                   cp.getLineNr(),
                                                   variable.getTcWatchpointVariableNode());
         variable.setTcWatchpointVariableNode(null);
      }
      fireWatchpointVariableRemoved(cp, variable);
   }

   private CodePointRoutineNode getCodePointRoutineNode (
                                                                KStructRoutine routine,
                                                                boolean create) {
      CodePointRoutineNode cprn;
      String name = routine.getKey();
      String progPath = "";
      KStructNode parent = routine.getParent();
      boolean first = true;
      while (!(parent instanceof KStructRoot)) {
         if (first) {
            first = false;
         } else {
            progPath = "." + progPath;
         }
         if (
             (parent instanceof KStructProject) || (parent instanceof KStructProgram) ||
             (parent instanceof KStructTypeUnit)) {
            progPath = parent.getKey().toLowerCase() + progPath;
         } else {
            progPath = parent.getKey() + progPath;
         }
         parent = parent.getParent();
      }
      cprn = getCodePointRoutineNode(progPath, name, create);
      if (create && (cprn != null)) {
         cprn.routine = routine;
      }
      return cprn;
   }

   private CodePointRoutineNode getCodePointRoutineNode (
                                                                String path, String name,
                                                                boolean create) {
      CodePointRoutineNode cprn = null;
      for (int i = 0; i < cpRoutineList.size(); i++) {
         cprn = (CodePointRoutineNode) cpRoutineList.elementAt(i);
         if (cprn.name.equals(name) && cprn.path.equals(path)) {
            return cprn;
         }
      }

      //add
      if (create) {
         cprn = new CodePointRoutineNode();
         cprn.name = name;
         cprn.path = path;
         cpRoutineList.addElement(cprn);
         return cprn;
      }
      return null;
   }

   private void addCodePointTo (
                                       CodePointRoutineNode cpRoutineNode, int lineNr,
                                       int kind) {
      int i = 0;
      while (
             (i < cpRoutineNode.codePointList.size()) &&
             (((CodePoint) cpRoutineNode.codePointList.elementAt(i)).lineNr != lineNr)) {
         i++;
      }
      if (i == cpRoutineNode.codePointList.size()) {
         // add new codepoint
         CodePoint cp = new CodePoint(cpRoutineNode, lineNr, kind, true);
         cp.setValid(false);
         cpRoutineNode.codePointList.addElement(cp);
         if (
             isActivated &&
             dfl.client.structure.setCodePoint(
                                           (TcStructuralRoutineNode) cpRoutineNode.routine.getTcStructuralNode(),
                                           lineNr, null, null, kind)) {
            cp.setValid(true);
         }
         fireCodePointAdded(cp);
      }
   }

   /**
    * Entfernt den angegebenen CodePoint. Wurde der CodePoint erfolgreich entfernt,
    * dann wird die Methode removed vom KCodePointListner aufgerufen. Ist der 
    * KCodePointAdministrator aktiv geschaltet und besteht die Verbindung zum TeachControl, dann 
    * wird der CodePoint auch im TeachControll entfernt.
    *
    * @param cp CodePoint
    */
   public void removeCodePoint (CodePoint cp) {
      if (isActivated) {
      	dfl.client.structure.removeCodePoint(
                                          (TcStructuralRoutineNode) cp.cpRoutineNode.getStructRoutine()
                                                                                    .getTcStructuralNode(),
                                          cp.lineNr);
      }
      cp.cpRoutineNode.codePointList.removeElement(cp);
      fireCodePointRemoved(cp);
   }

   /**
    * Schaltet den angegebenen Codepoint frei, anschließend wird die Methode 
    * changed vom KCodePointListner aufgerufen. Ist der KCodePointAdministrator aktiv 
    * geschaltet und besteht die Verbindung zum TeachControl, dann wird der CodePoint 
    * auch im TeachControll freigeschaltet.
    *
    * @param cp CodePoint
    * @param b true für freischalten
    */
   public void setCodePointEnabled (CodePoint cp, boolean b) {
      cp.setEnabled(b);
      cp.setValid(dfl.client.structure.enableCodePoint(
                                                   (TcStructuralRoutineNode) cp.cpRoutineNode.routine.getTcStructuralNode(),
                                                   cp.getLineNr(), b));
      fireCodePointChanged(cp);
   }

   /**
    * Setzt den Watchpoint - Zähler bei aktiven KCodePointAdministrator und bestehender
    * Verbindung zum TeachControl zurück , anschließend wird die Methode 
    * changed vom KCodePointListner aufgerufen.
    *
    * @param cp Watchpoint
    */
   public void resetWatchpointCounter (CodePoint cp) {
      if (
          isActivated &&
          dfl.client.structure.resetWatchpointCounter(
                                                  (TcStructuralRoutineNode) cp.cpRoutineNode.routine.getTcStructuralNode(),
                                                  cp.getLineNr())) {
         fireCodePointChanged(cp);
      }
   }

   /**
    * Fügt einen Listener hinzu.
    *
    * @param l Listener
    */
   public void addCodePointListener (KCodePointListener l) {
      listeners.addElement(l);
   }

   /**
    * Entfernt den Listener.
    *
    * @param l Listener
    */
   public void removeCodePointListener (KCodePointListener l) {
      listeners.removeElement(l);
   }

   /**
    * Gibt die Änderung aller CodePoints bekannt.
    */
   protected void fireAllCodePointsChanged () {
      for (int i = 0; i < listeners.size(); i++) {
         ((KCodePointListener) listeners.elementAt(i)).allChanged();
      }
   }

   /**
    * Gibt das Hinzufügen eines CodePoints bekannt.
    *
    * @param codePoint CodePoint
    */
   protected void fireCodePointAdded (CodePoint codePoint) {
      for (int i = 0; i < listeners.size(); i++) {
         ((KCodePointListener) listeners.elementAt(i)).added(codePoint);
      }
   }

   /**
    * Gibt das Entfernen eines CodePoints bekannt.
    *
    * @param codePoint CodePoint
    */
   protected void fireCodePointRemoved (CodePoint codePoint) {
      for (int i = 0; i < listeners.size(); i++) {
         ((KCodePointListener) listeners.elementAt(i)).removed(codePoint);
      }
   }

   /**
    * Gibt die Änderung eines CodePoints bekannt.
    *
    * @param codePoint CodePoint
    */
   protected void fireCodePointChanged (CodePoint codePoint) {
      for (int i = 0; i < listeners.size(); i++) {
         ((KCodePointListener) listeners.elementAt(i)).changed(codePoint);
      }
   }

   /**
    * Gibt das Hinzufügen einer Watchpoint - Variablen bekannt.
    *
    * @param codePoint Watchpoint
    * @param variable Watchpoint - Variablen
    */
   protected void fireWatchpointVariableAdded (
                                                      CodePoint codePoint,
                                                      WatchpointVariableNode variable) {
      for (int i = 0; i < listeners.size(); i++) {
         ((KCodePointListener) listeners.elementAt(i)).watchpointVariableAdded(codePoint, variable);
      }
   }

   /**
    * Gibt das Entfernen einer Watchpoint - Variablen bekannt.
    *
    * @param codePoint Watchpoint
    * @param variable Watchpoint - Variablen
    */
   protected void fireWatchpointVariableRemoved (
                                                        CodePoint codePoint,
                                                        WatchpointVariableNode variable) {
      for (int i = 0; i < listeners.size(); i++) {
         ((KCodePointListener) listeners.elementAt(i)).watchpointVariableRemoved(
                                                                                 codePoint, variable);
      }
   }

   /**
    * Gibt die Änderung einer Watchpoint - Variablen bekannt.
    *
    * @param codePoint Watchpoint
    * @param variable Watchpoint - Variablen
    */
   protected void fireWatchpointVariableChanged (
                                                        CodePoint codePoint,
                                                        WatchpointVariableNode variable) {
      for (int i = 0; i < listeners.size(); i++) {
         ((KCodePointListener) listeners.elementAt(i)).watchpointVariableChanged(
                                                                                 codePoint, variable);
      }
   }

   /**
    * Liefert alle CodePoints einer Routine zurück.
    *
    * @param routine Routine, welche die CodePoints enthält
    *
    * @return CodePoints
    */
   public Enumeration getCodePoints (KStructRoutine routine) {
      return new CodePointRoutineEnumeration(getCodePointRoutineNode(routine, false));
   }

   /**
    * Liefert alle CodePoints zurück.
    *
    * @return CodePoints
    */
   public Enumeration getCodePoints () {
      return new CodePointEnumeration();
   }

   /**
    * Repräsentiert eine Routine mit all ihren CodePoints.
    */
   private static class CodePointRoutineNode {
      /** Pfad der Routine im Strukturbaum */
      private String path;
      /** Name der Routine */
      private String name;
      /** Strukturbaumknoten */
      private KStructRoutine routine;
      /** Enthält die Codepoints der Routine */
      private final Vector codePointList = new Vector();

      /**
       * Liefert den Strukturbaumpfad der Routine zurück.
       *
       * @return Strukturbaumpfad
       */
      private String getPath () {
         return path;
      }

      /**
       * Liefert den Name der Routine zurück.
       *
       * @return Routinenname
       */
      private String getName () {
         return name;
      }

      /**
       * Liefert den Strukturbaumknoten der Routine zurück.
       *
       * @return Strukturbaumknoten
       */
      private KStructRoutine getStructRoutine () {
         return routine;
      }
   }
   /**
    * Repräsentation eines CodePoints.
    */
   public class CodePoint {
      private CodePointRoutineNode cpRoutineNode;
      private int lineNr;
      private int kind;
      private boolean isEnabled;
      private boolean isValid;
      private final Vector variables = new Vector();
      private int counter;

      /**
       * Konstruktor
       *
       * @param cpRoutineNode Routine
       * @param lineNr Zeilennumer
       * @param kind BREAKPOINT, WATCHPOINT or MAIN_FLOW_BREAKPOINT
       * @param enabled true, wenn freigeschaltet
       */
      private CodePoint (
                           CodePointRoutineNode cpRoutineNode, int lineNr, int kind,
                           boolean enabled) {
         this.cpRoutineNode = cpRoutineNode;
         this.lineNr = lineNr;
         this.kind = kind;
         this.isEnabled = enabled;
      }

      /**
       * Liefert den Name der Routine, welche diesen CodePoint enthält, zurück.
       *
       * @return Name der Routine
       */
      public String getName () {
         return cpRoutineNode.getName();
      }

      /**
       * Liefert den Strukturbaumknoten der Routine.
       *
       * @return Strukturbaumknoten
       */
      public KStructRoutine getStructRoutine () {
         return cpRoutineNode.getStructRoutine();
      }

      /**
       * Liefert die Zeilennummer des CodePoints.
       *
       * @return Zeilennummer
       */
      public int getLineNr () {
         return lineNr;
      }

      /**
       * Liefert den Status der Freischaltung zurück.
       *
       * @return true für freigeschaltet
       */
      public boolean isEnabled () {
         return isEnabled;
      }

      /**
       * Liefert true zurück, wenn der CodePoint erfolgreich im TeachControl gesetzt
       * werden konnte.
       *
       * @return true wenn gültig
       */
      public boolean isValid () {
         return isValid;
      }

      /**
       * Returns the codepoint kind.
       *
       * @return BREAKPOINT, WATCHPOINT or MAIN_FLOW_BREAKPOINT
       */
      public int getKind () {
         return kind;
      }
      
      /**
       * Liefert die Anzahl der Watchpoint - Variablen zurück.
       *
       * @return Anzahl der Watchpoint - Variablen
       */
      public int getWatchpointVarialeCount () {
         return (kind == WATCHPOINT) ? variables.size(): 0;
      }
      
      /**
       * Liefert die Watchpoint - Variable für den angegebenen Index zurück.
       * 
       * @param index der Variable
       * @return Watchpoint - Variable
       */
      public WatchpointVariableNode getWatchpointVariable (int index) {
         return (kind == WATCHPOINT) ? (WatchpointVariableNode) variables.elementAt(index): null;
      }

      /**
       * Liefert den Wert des Watchpoint - Zählers zurück.
       *
       * @return Wert des Watchpoint - Zählers
       */
      public int getWatchpointCounter () {
         if (isValid() && isEnabled() && (kind == WATCHPOINT)) {
            counter =
            	dfl.client.structure.getWatchpointCounter(
                                                     (TcStructuralRoutineNode) this.cpRoutineNode.getStructRoutine()
                                                                                                 .getTcStructuralNode(),
                                                     this.getLineNr());
         } else {
            counter = -1;
         }
         return counter;
      }

      private void setEnabled (boolean b) {
         isEnabled = b;
      }

      private void setValid (boolean b) {
         isValid = b;
      }
   }
   
   /**
    * Repräsentation einer Watchpoint - Variable.
    */
   public class WatchpointVariableNode {
      /**
      * fullpath = [Projektname] + '.' + Programmname + '.' +
      * Variablename + { '.' + Komponentenname | '[' + Array - Index + ']'}.
      */
      private String fullPath;
      private String name;
      /** 
       * instancePath = KStructVar + {  KStructVar | Integer (Array -Index) }.
       */
      private Object[] componentPath;
      private Object[] tcComponentPath;
      private CodePoint cp;
      private Object actualValue;
      private TcWatchpointVarNode wvn;
      private KStructType type;

      /**
       * Konstruktor.
       *
       * @param fullNodePath  Komponentenpfad componentPath = [KStructProject] + [KStructProgram] +
       * KStructVar + {  KStructVar | Integer (Array -Index) }.
       * @param cp Watchpoint
       */
      protected WatchpointVariableNode (Object[] fullNodePath, CodePoint cp) {
         int index = getIndex(fullNodePath);
         componentPath = new Object[fullNodePath.length - index];
         System.arraycopy(fullNodePath, index, componentPath, 0, componentPath.length);

         Object o = fullNodePath[fullNodePath.length - 1];
         if (o instanceof Integer) {
            // array index
            name = "[" + o.toString() + "]";
         } else {
            name = ((KStructVar) o).getKey();
         }
         this.cp = cp;
         createFullPath(fullNodePath);
      }

      /**
       * Konstruktor.
       *
       * @param name Variablenname
       * @param fullPath fullpath = [Projektname] + '.' + Programmname + '.' +
      * Variablename + { '.' + Komponentenname | '[' + Array - Index + ']'}.
       * @param cp Watchpoint
       */
      protected WatchpointVariableNode (String name, String fullPath, CodePoint cp) {
         this.name = name;
         this.fullPath = fullPath;
         this.cp = cp;
      }

      /**
       * Liefert true zurück, wenn die Watchpoint - Variable erfolgreich im TeachControl gesetzt
       * werden konnte.
       *
       * @return true wenn gültig
       */
      public boolean isValid () {
         return getTcWatchpointVariableNode() != null;
      }

      private void createFullPath (Object[] fullNodePath) {
         if (fullPath == null) {
            fullPath = dfl.structure.convertPath(fullNodePath);
         }
      }

      /**
       * Liefert den Strukturbaumpfad für die Watchpoint - Variable zurück.
       *
       * @return Strukturbaumpfad
       */
      public String getFullPath () {
         return fullPath;
      }

      /**
       * Liefert den Variablenname zurück.
       *
       * @return Variablenname
       */
      public String getName () {
         return name;
      }

      /**
       * Liefert den Watchpoint zurück.
       *
       * @return Watchpoint
       */
      public CodePoint getCodePoint () {
         return cp;
      }

      private void clearComponentPath () {
         componentPath = null;
         type = null;
      }

      /**
       * Liefert den Strukturbaumpfad der Watchpoint - Variable zurück.
       *
       * @return Strukturbaumpfad
       */
      public Object[] getComponentPath () {
         if ((componentPath == null) && (fullPath != null)) {
            // create path from fullPath
            componentPath = dfl.structure.getVariableComponentPath(fullPath);
         }
         return componentPath;
      }

      private int getIndex (Object[] path) {
         int i = -1;
         if (path != null) {
            i = 0;
            while ((i < path.length) && !(path[i] instanceof KStructVar)) {
               i++;
            }
            if (i < path.length) {
               return i;
            }
         }
         return i;
      }

      /**
       * Liefert den Watchpoint - Variablentyp zurück.
       *
       * @return Typ
       */
      protected KStructType getType () {
         if ((type == null) && (getComponentPath() != null)) {
            int i = componentPath.length - 1;
            if (componentPath[i] instanceof Integer) {
               i--;
               while (componentPath[i] instanceof Integer) {
                  i--;
               }
               if (componentPath[i] instanceof KStructVarArray) {
                  KStructType t =
                     (KStructTypeArray) ((KStructVarArray) componentPath[i]).getKStructType();
                  while (t.isAliasType() && (t instanceof KStructTypeMapTo)) {
                     t = t.getKStructType();
                  }
                  KStructType elementType = ((KStructTypeArray) t).getArrayElementKStructType();
                  while (elementType.isAliasType() && (elementType instanceof KStructTypeMapTo)) {
                     elementType = elementType.getKStructType();
                  }
                  while (elementType instanceof KStructTypeArray) {
                     elementType = ((KStructTypeArray) elementType).getArrayElementKStructType();
                     while (elementType.isAliasType() && (elementType instanceof KStructTypeMapTo)) {
                        elementType = elementType.getKStructType();
                     }
                  }
                  type = elementType;
               }
            } else if (componentPath[i] instanceof KStructVar) {
               type = ((KStructVar) componentPath[i]).getKStructType();
               while (type.isAliasType() && (type instanceof KStructTypeMapTo)) {
                  type = type.getKStructType();
               }
            }
         }
         return type;
      }

      private Object[] getTcComponentPath () {
         if ((tcComponentPath == null) && (componentPath != null)) {
            tcComponentPath = new Object[componentPath.length];
            for (int i = 0; i < componentPath.length; i++) {
               if (componentPath[i] instanceof KStructVar) {
                  tcComponentPath[i] = ((KStructVar) componentPath[i]).getTcStructuralNode();
               } else if (componentPath[i] instanceof Integer) {
                  tcComponentPath[i] = componentPath[i];
               } else {
                  // error
                  System.out.println("KCodePointAdministrator - WatchpointVariableNode - getTcComponentPath");
                  tcComponentPath = null;
                  break;
               }
            }
         }
         return tcComponentPath;
      }

      /**
       * @see java.lang.Object#equals(java.lang.Object)
       */
      public boolean equals (Object o) {
        if (this == o) {
        	return true;
        }
      	if (!(o instanceof WatchpointVariableNode)) {
            return false;
        }
        String p = ((WatchpointVariableNode) o).getFullPath();
        if (fullPath == null) {
           return p == null;
        }
        return fullPath.equals(p);
      }

      /* (non-Javadoc)
       * @see java.lang.Object#hashCode()
       */
      public int hashCode () {
      	return (fullPath != null) ? fullPath.hashCode(): -1;
      }
      
      /**
       * Liefert den Aktualwert der Watchpoint - Variablen.
       *
       * @return Aktualwert
       */
      public Object getValue () {
         if (cp.isValid() && cp.isEnabled() && (componentPath != null) && (wvn != null)) {
            synchronized (value) {
               if (
                   dfl.client.structure.getWatchpointVariableValue(
                                                               (TcStructuralRoutineNode) cp.getStructRoutine()
                                                                                           .getTcStructuralNode(),
                                                               cp.getLineNr(), wvn, value)) {
                  valueChanged(value);
               } else {
                  actualValue = null;
               }
            }
         } else {
            actualValue = null;
         }
         return actualValue;
      }

      private TcWatchpointVarNode getTcWatchpointVariableNode () {
         return wvn;
      }

      private void setTcWatchpointVariableNode (TcWatchpointVarNode ortsWatchpointVarNode) {
         wvn = ortsWatchpointVarNode;
      }

      private boolean valueChanged (TcValue value) {
         boolean changed = false;
         KStructType t = getType();
         if (t != null) {
            if (t instanceof KStructTypeBool) {
               changed =
                  ((actualValue == null) && value.isValid) ||
                  ((actualValue != null) && !value.isValid) ||
                  ((actualValue != null) &&
                  (((Boolean) actualValue).booleanValue() != value.boolValue));
               if (changed && value.isValid) {
                  actualValue = value.boolValue ? Boolean.TRUE : Boolean.FALSE;
               } else if (!value.isValid) {
                  actualValue = null;
               }
            } else if (t instanceof KStructTypeSInt) {
               changed =
                  ((actualValue == null) && value.isValid) ||
                  ((actualValue != null) && !value.isValid) ||
                  ((actualValue != null) && (((Byte) actualValue).byteValue() != value.int8Value));
               if (changed && value.isValid) {
                  actualValue = new Byte(value.int8Value);
               } else if (!value.isValid) {
                  actualValue = null;
               }
            } else if (t instanceof KStructTypeInt) {
               changed =
                  ((actualValue == null) && value.isValid) ||
                  ((actualValue != null) && !value.isValid) ||
                  ((actualValue != null) &&
                  (((Short) actualValue).shortValue() != value.int16Value));
               if (changed && value.isValid) {
                  actualValue = new Short(value.int16Value);
               } else if (!value.isValid) {
                  actualValue = null;
               }
            } else if (
                       (t instanceof KStructTypeDInt) || (t instanceof KStructTypeEnum) ||
                       (t instanceof KStructTypeSubrange)) {
               changed =
                  ((actualValue == null) && value.isValid) ||
                  ((actualValue != null) && !value.isValid) ||
                  ((actualValue != null) &&
                  (((Integer) actualValue).intValue() != value.int32Value));
               if (changed && value.isValid) {
                  actualValue = new Integer(value.int32Value);
               } else if (!value.isValid) {
                  actualValue = null;
               }
            } else if (t instanceof KStructTypeLInt) {
               changed =
                  ((actualValue == null) && value.isValid) ||
                  ((actualValue != null) && !value.isValid) ||
                  ((actualValue != null) && (((Long) actualValue).longValue() != value.int64Value));
               if (changed && value.isValid) {
                  actualValue = new Long(value.int64Value);
               } else if (!value.isValid) {
                  actualValue = null;
               }
            } else if (t instanceof KStructTypeReal) {
               changed =
                  ((actualValue == null) && value.isValid) ||
                  ((actualValue != null) && !value.isValid) ||
                  ((actualValue != null) &&
                  (((Float) actualValue).floatValue() != value.floatValue)); // $codepro.audit.disable floatComparison
               if (changed && value.isValid) {
                  actualValue = new Float(value.floatValue);
               } else if (!value.isValid) {
                  actualValue = null;
               }
            } else if (t instanceof KStructTypeString) {
               changed =
                  ((actualValue == null) && value.isValid) ||
                  ((actualValue != null) && !value.isValid) ||
                  ((actualValue != null) && !actualValue.equals(value.stringValue));
               if (changed && value.isValid) {
                  actualValue = value.stringValue;
               } else if (!value.isValid) {
                  actualValue = null;
               }
            } else {
               actualValue = null;
            }
         }
         return changed;
      }
   }

   /**
    * Enumeration der Codepoints einer Routine.
    */
   protected static class CodePointRoutineEnumeration implements Enumeration {
      private int codePointIndex = 0;
      private CodePoint next = null;
      private CodePointRoutineNode cprn;

      private CodePointRoutineEnumeration (CodePointRoutineNode cpRoutineNode) {
         this.cprn = cpRoutineNode;
      }

      /**
       * Liefert true zurück, wenn noch CodePoints vorhanden sind.
       *
       * @return true für weitere CodePoints
       */
      public boolean hasMoreElements () {
         if (cprn == null) {
            return false;
         }
         if (next == null) {
            if (codePointIndex < cprn.codePointList.size()) {
               next = (CodePoint) cprn.codePointList.elementAt(codePointIndex);
               codePointIndex++;
               return true;
            }
            return false;
         }
         return true;
      }

      /**
       * Liefert den nächsten CodePoint zurück.
       *
       * @return nächster CodePoint
       */
      public Object nextElement () {
         if (hasMoreElements()) {
            CodePoint help = next;
            next = null;
            return help;
         }
         return null;
      }
   }
   /**
    * Enumeration aller Codepoints.
    */
   protected class CodePointEnumeration implements Enumeration {
      private int routineIndex = 0;
      private int codePointIndex = 0;
      private CodePoint next = null;

      private CodePointEnumeration () {
      	// do nothing
      }

      /**
       * Liefert true zurück, wenn noch weitere CodePoints vorhanden sind.
       *
       * @return true für weitere CodePoints
       */
      public boolean hasMoreElements () {
         if (next == null) {
            while (routineIndex < cpRoutineList.size()) {
               CodePointRoutineNode cprn =
                  (CodePointRoutineNode) cpRoutineList.elementAt(routineIndex);
               if (codePointIndex < cprn.codePointList.size()) {
                  next = (CodePoint) cprn.codePointList.elementAt(codePointIndex);
                  codePointIndex++;
                  return true;
               }
               codePointIndex = 0;
               routineIndex++;
            }
            return false;
         }
         return true;
      }

      /**
       * Liefert den nächsten CodePoint zurück.
       *
       * @return nächster CodePoint
       */
      public Object nextElement () {
         if (hasMoreElements()) {
            CodePoint help = next;
            next = null;
            return help;
         }
         return null;
      }
   }
}
