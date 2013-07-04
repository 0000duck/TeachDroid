/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : ede
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.structural.routine;

import java.util.*;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.network.*;


/**
 *  Administrator für die Verwaltung der Routinen im Strukturbaum. Bietet Möglichkeiten
 * zum Erzeugen, Löschen und Ändern von Routinen. 
 */
public class KRoutineFactory {
  
   /**
    * Lädt alle Routinen des übergebenen Knoten.
    *
    * @param parent Program/ Baustein dessen Routine geladen werden sollen 
    */
   public void loadRoutines (KStructNode parent) {
      Enumeration e = parent.dfl.client.structure.getNodes(parent.getTcStructuralNode(), TcStructuralNode.ROUTINE);
      while (e.hasMoreElements()) {
         TcStructuralRoutineNode r = (TcStructuralRoutineNode) e.nextElement();
         addNode(parent, r);
      }
   }

	public KStructRoutine addNode(KStructNode parent, TcStructuralRoutineNode tcr) {
		int visibility;
      KStructProject project;
      if (parent instanceof KStructProject) {
         project = (KStructProject) parent;
         visibility = KStructNode.GLOBAL;
      } else if (tcr.isPublic()) {
         project = parent.getKStructProject();
        visibility = KStructNode.PUBLIC;
      } else {
         project = parent.getKStructProject();
      	visibility = KStructNode.PRIVATE;
      }
      KStructRoutine routine = createNode(tcr.getName(), tcr, visibility, parent.dfl);
      if (parent instanceof KStructProgram) {
         ((KStructProgram) parent).routines.addChild(routine);
      } else if (parent instanceof KStructTypeUnit) {
         ((KStructTypeUnit) parent).routines.addChild(routine);
      } else {
         ((KStructProject) parent).routines.addChild(routine);
      }
      project.allRoutines.put(tcr, routine);
      return routine;
   }

   /**
    * Description of the Method
    *
    * @param key Description of the Parameter
    * @param node Description of the Parameter
    * @param visibility Description of the Parameter
    *
    * @return Description of the Return Value
    */
   protected KStructRoutine createNode (String key, TcStructuralRoutineNode node,
   		int visibility, KTcDfl dfl) {
      dfl.structure.routineCounter++;
      KStructRoutine child = new KStructRoutine(key, visibility, node.getRoutineKind(), dfl);
      child.setTcStructuralNode(node);
      return child;
   }
 
}
