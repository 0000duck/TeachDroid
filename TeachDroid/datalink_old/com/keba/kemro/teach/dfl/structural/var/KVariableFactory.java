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
package com.keba.kemro.teach.dfl.structural.var;

import java.util.*;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.dfl.util.KDflLogger;
import com.keba.kemro.teach.network.*;


/**
 * Der Variablen-Administrator verwaltet die Variablen des Teachtalk-Systems.
 * Er bietet eine Schnittstelle zum Anlegen, Ändern und Löschen von Variablen.
 */
public class KVariableFactory {

   private final KStructNode[] variables = new KStructNode[250];
   private final KStructNode[] parameters = new KStructNode[20]; 

   /**
    * Loads all variables from teachcontrol.
    * @param parent scope
    */
   public void loadVariables (KStructNode parent) {
      Enumeration e = parent.dfl.client.structure.getNodes(parent.getTcStructuralNode(), TcStructuralNode.VAR);
      while (e.hasMoreElements()) {
         TcStructuralVarNode v = (TcStructuralVarNode) e.nextElement();
         addNode(parent, v);
      }
   }

   public KStructVar addNode (KStructNode parent, TcStructuralVarNode variable) {
      KStructVar child = null;
      String key = variable.getName();
      if (!(parent instanceof KStructTypeUnit) || !key.equalsIgnoreCase("SUPER")) {
         byte kind = variable.getVarKind();
         int visibility;
         if (parent instanceof KStructTypeRoutine) {
            visibility = KStructNode.PRIVATE;
         } else if (parent instanceof KStructRoutine) {
            visibility = KStructNode.PRIVATE;
         } else if (parent instanceof KStructProject) {
            visibility = KStructNode.GLOBAL;
         } else {
            if (variable.isPublic()) {
               visibility = KStructNode.PUBLIC;
            } else {
               visibility = KStructNode.PRIVATE;
            }
         }
         TcStructuralTypeNode t = variable.getType();
         KStructType tn = parent.dfl.structure.getKStructType(t);
         if (tn == null) {
            tn = parent.dfl.structure.typeFactory.createKStructType(t, parent.dfl);
         }
         if (tn != null) {
            boolean isMapTo = tn instanceof KStructTypeMapTo;
            child = createNode(key, visibility, kind, isMapTo, false, tn, variable, parent.dfl);
            switch (kind) {
            case TcStructuralVarNode.VAR_KIND:
            case TcStructuralVarNode.CONST_VAR_KIND:
               if (parent instanceof KStructProject) {
                  ((KStructProject) parent).variables.addChild(child);
               } else if (parent instanceof KStructProgram) {
                  ((KStructProgram) parent).variables.addChild(child);
               } else if (parent instanceof KStructTypeUnit) {
                  ((KStructTypeUnit) parent).components.addChild(child);
               } else {
                  ((KStructRoutine) parent).variables.addChild(child);
               }
               break;
            case TcStructuralVarNode.PARAM_KIND:
            case TcStructuralVarNode.CONST_PARAM_KIND:
            case TcStructuralVarNode.VALUE_PARAM_KIND:
               if (parent instanceof KStructRoutine) {
                  ((KStructRoutine) parent).parameters.addChild(child);
               } else {
                  ((KStructTypeRoutine) parent).parameters.addChild(child);
               }
               break;
            }
         }
      }
      return child;
   }

	public void removeNode(KStructVar variable) {
      TcStructuralVarNode v = (TcStructuralVarNode) variable.getTcStructuralNode();
      KStructNode parent = variable.getParent();
		byte kind = v.getVarKind();
		switch (kind) {
		case TcStructuralVarNode.VAR_KIND:
		case TcStructuralVarNode.CONST_VAR_KIND:
		   if (parent instanceof KStructProject) {
		      ((KStructProject) parent).variables.removeChild(variable);
		   } else if (parent instanceof KStructProgram) {
		      ((KStructProgram) parent).variables.removeChild(variable);
		   } else if (parent instanceof KStructTypeUnit) {
		      ((KStructTypeUnit) parent).components.removeChild(variable);
		   } else {
		      ((KStructRoutine) parent).variables.removeChild(variable);
		   }
		   break;
		case TcStructuralVarNode.PARAM_KIND:
		case TcStructuralVarNode.CONST_PARAM_KIND:
		case TcStructuralVarNode.VALUE_PARAM_KIND:
		   if (parent instanceof KStructRoutine) {
		      ((KStructRoutine) parent).parameters.removeChild(variable);
		   } else {
		      ((KStructTypeRoutine) parent).parameters.removeChild(variable);
		   }
		   break;
		}
	}

   /**
    * Lädt routinen-lokale Variablen und Parameter in den Sturkturbaum.
    *
    * @param parent Strukturknoten der Routine
    */
   public void loadRoutineVariables (KStructRoutine parent) {
      synchronized (variables) {
         synchronized (parameters) {
            int varCounter = 0;
            int paramCounter = 0;
            int visibility = KStructNode.PRIVATE;
            Enumeration e =
               parent.dfl.client.structure.getNodes(parent.getTcStructuralNode(), TcStructuralNode.VAR);
            while (e.hasMoreElements()) {
               TcStructuralVarNode v = (TcStructuralVarNode) e.nextElement();
               String key = v.getName();
               byte kind = v.getVarKind();
               TcStructuralTypeNode t = v.getType();
               KStructType tn = parent.dfl.structure.getKStructType(t);
               if (tn == null) {
                  tn = parent.dfl.structure.typeFactory.createKStructType(t, parent.dfl);
               }
               if (tn != null) {
                  boolean isMapTo = tn instanceof KStructTypeMapTo;
                  KStructVar child =
                     createNode(key, visibility, kind, isMapTo, false, tn, v, parent.dfl);
                  switch (kind) {
                  case TcStructuralVarNode.VAR_KIND:
                  case TcStructuralVarNode.CONST_VAR_KIND:
                     variables[varCounter] = child;
                     varCounter++;
                     break;
                  case TcStructuralVarNode.PARAM_KIND:
                  case TcStructuralVarNode.CONST_PARAM_KIND:
                  case TcStructuralVarNode.VALUE_PARAM_KIND:
                     parameters[paramCounter] = child;
                     paramCounter++;
                     break;
                  }
               } else {
                  KDflLogger.error(KVariableFactory.class, "loadRoutineVariables: can't create type for" + key);
               }
            }
            if (0 < varCounter) {
               parent.variables.addChildren(variables, varCounter);
               for (int i = 0; i < varCounter; i++) {
                  variables[i] = null;
               }
            }
            if (0 < paramCounter) {
               parent.parameters.addChildren(parameters, paramCounter);
               for (int i = 0; i < paramCounter; i++) {
                  parameters[i] = null;
               }
            }
         }
      }
   }

   /**
    * Lädt die Komponenten eines Strukturtyps.
    *
    * @param parent Strukturtyp
    */
   public void loadComponents (KStructTypeStruct parent) {
      TcStructuralTypeNode node = (TcStructuralTypeNode) parent.getTcStructuralNode();
      Enumeration e = parent.dfl.client.structure.getNodes(node, TcStructuralNode.VAR);
      while (e.hasMoreElements()) {
         TcStructuralVarNode v = (TcStructuralVarNode) e.nextElement();
         TcStructuralTypeNode t = (v).getType();
         String key = v.getName();
         byte kind = v.getVarKind();
         KStructType tn = parent.dfl.structure.getKStructType(t);
         if (tn == null) {
            if (t.getTypeKind() == TcStructuralTypeNode.ARRAY_TYPE) {
               tn = parent.dfl.structure.typeFactory.createKStructType(t, parent.dfl);
            } else {
               tn = parent.dfl.structure.typeFactory.createKStructType(t, parent.dfl);
            }
         }
         if (tn != null) {
            boolean isMapTo = tn instanceof KStructTypeMapTo;
            KStructVar child =
               createNode(key, KStructNode.PRIVATE, kind, isMapTo, true, tn, v, parent.dfl);
            parent.components.addChild(child);
         } else {
            System.out.println("EVariableAdministrator - loadComponents Error: " + key);
         }
      }
   }

   /**
    * Erzeugt einen Variablenknoten im Strukturbaum und liefert diesen zurück.
    *
    * @param key Name der Variable
    * @param visibility Sichtbarkeit
    * @param kind Variablenkennung (VAR, PARAM,...)
    * @param isMapTo Wahrheitswert für MapTo-Variable
    * @param isComponent Wahrheitswert für eine Komponentenvariable
    * @param nodeType Variablentyp
    * @param ortsStructuralNode Strukturknoten des TeachControl-Systems
    *
    * @return angelegter Variablenknoten
    */
   public KStructVar createNode (String key, int visibility, byte kind,
                                        boolean isMapTo, boolean isComponent, KStructType nodeType,
                                        TcStructuralNode ortsStructuralNode, KTcDfl dfl) {
      dfl.structure.varCounter++;
      KStructVar node = null;
      TcStructuralTypeNode t = null;
      if (isMapTo) {
         // use base type because it is already loaded
         t = ((TcStructuralTypeNode) nodeType.getTcStructuralNode()).getBaseType();
         //t = (TcStructuralTypeNode) nodeType.getKStructType().getTcStructuralNode();
      } else {
         t = (TcStructuralTypeNode) nodeType.getTcStructuralNode();
      }
      int typeKind = -1;
      if (t != null) {
         typeKind = t.getTypeKind();
      } else if (nodeType != null) {
         if (nodeType instanceof KStructTypeBool) {
            typeKind = TcStructuralTypeNode.BOOL_TYPE;
         } else if (nodeType instanceof KStructTypeByte) {
            typeKind = TcStructuralTypeNode.BYTE_TYPE;
         } else if (nodeType instanceof KStructTypeWord) {
            typeKind = TcStructuralTypeNode.WORD_TYPE;
         } else if (nodeType instanceof KStructTypeDWord) {
            typeKind = TcStructuralTypeNode.DWORD_TYPE;
         } else if (nodeType instanceof KStructTypeLWord) {
            typeKind = TcStructuralTypeNode.LWORD_TYPE;
         } else if (nodeType instanceof KStructTypeSInt) {
            typeKind = TcStructuralTypeNode.SINT_TYPE;
         } else if (nodeType instanceof KStructTypeInt) {
            typeKind = TcStructuralTypeNode.INT_TYPE;
         } else if (nodeType instanceof KStructTypeDInt) {
            typeKind = TcStructuralTypeNode.DINT_TYPE;
         } else if (nodeType instanceof KStructTypeLInt) {
            typeKind = TcStructuralTypeNode.LINT_TYPE;
         } else if (nodeType instanceof KStructTypeReal) {
            typeKind = TcStructuralTypeNode.REAL_TYPE;
         } else if (nodeType instanceof KStructTypeString) {
            typeKind = TcStructuralTypeNode.STRING_TYPE;
         } else if (nodeType instanceof KStructTypeEnum) {
            typeKind = TcStructuralTypeNode.ENUM_TYPE;
         } else if (nodeType instanceof KStructTypeSubrange) {
            typeKind = TcStructuralTypeNode.SUBRANGE_TYPE;
         } else if (nodeType instanceof KStructTypeArray) {
            typeKind = TcStructuralTypeNode.ARRAY_TYPE;
         } else if (nodeType instanceof KStructTypeStruct) {
            typeKind = TcStructuralTypeNode.STRUCT_TYPE;
         } else if (nodeType instanceof KStructTypeUnit) {
            typeKind = TcStructuralTypeNode.UNIT_TYPE;
         } else if (nodeType instanceof KStructTypeRoutine) {
            typeKind = TcStructuralTypeNode.ROUTINE_TYPE;
         } else if (nodeType instanceof KStructTypeAny) {
            typeKind = TcStructuralTypeNode.ANY_TYPE;
         }else if(nodeType instanceof KStructTypeLReal) {
        	 typeKind= TcStructuralTypeNode.LREAL_TYPE;
         }
      }
      switch (typeKind) {
      case TcStructuralTypeNode.BOOL_TYPE:
         node = new KStructVarBool(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.LINT_TYPE:
         node = new KStructVarLInt(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.DINT_TYPE:
         node = new KStructVarDInt(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.INT_TYPE:
         node = new KStructVarInt(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.SINT_TYPE:
         node = new KStructVarSInt(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.BYTE_TYPE:
         node = new KStructVarByte(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.WORD_TYPE:
         node = new KStructVarWord(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.DWORD_TYPE:
         node = new KStructVarDWord(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.LWORD_TYPE:
         node = new KStructVarLWord(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.REAL_TYPE:
         node = new KStructVarReal(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.STRING_TYPE:
         node = new KStructVarString(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.ENUM_TYPE:
         node = new KStructVarEnum(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.SUBRANGE_TYPE:
         node =
            new KStructVarSubrange(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.ARRAY_TYPE:
         node = new KStructVarArray(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.STRUCT_TYPE:
         node = new KStructVarStruct(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.UNIT_TYPE:
         node = new KStructVarUnit(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.ROUTINE_TYPE:
         node =
            new KStructVarRoutine(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.ANY_TYPE:
         node =
            new KStructVarAny(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      case TcStructuralTypeNode.LREAL_TYPE:
    	  node= new KStructVarLReal(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
    	  break;
      default:
         node =
            new KStructVarInvalid(key, visibility, kind, isMapTo, isComponent, nodeType, dfl);
         break;
      }
      node.setTcStructuralNode(ortsStructuralNode);
      if (ortsStructuralNode != null) {
         node.setSave(((TcStructuralVarNode) ortsStructuralNode).isSave());
      }
      return node;
   }

}
