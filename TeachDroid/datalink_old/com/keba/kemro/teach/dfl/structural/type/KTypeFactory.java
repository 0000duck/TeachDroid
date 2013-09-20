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
package com.keba.kemro.teach.dfl.structural.type;

import java.util.*;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.network.*;


/**
 * Diese Klasse ist für die Verarbeitung von Typen des Strukturbaumes verantwortlich.
 */
public class KTypeFactory {
   /**
    * Lädt alle Typen.
    */
   public void loadTypes(KStructNode parent) {
      Enumeration e = parent.dfl.client.structure.getNodes(parent.getTcStructuralNode(),
            TcStructuralNode.TYPE);
      while (e.hasMoreElements()) {
         TcStructuralTypeNode t = (TcStructuralTypeNode) e.nextElement();
         addNode(parent, t);
      }
   }

	public KStructType addNode(KStructNode parent, TcStructuralTypeNode t) {
		KStructType type;
		KStructProject project;
		if (parent instanceof KStructProject) {
			project = (KStructProject) parent;
		   type = createNode(t.getName(), t, KStructNode.GLOBAL, 0, parent.dfl);
		   if (((t.getTypeKind() == TcStructuralTypeNode.UNIT_TYPE) && (t.getBaseType() == null))) {
		      // Is Unit file
		      project.units.addChild(type);
		      addDirEntry(type, project);
		   } else {
		      project.types.addChild(type);
		   }
		} else {
			project = parent.getKStructProject();
		   type = createNode(t.getName(), t, KStructNode.PRIVATE, 0, parent.dfl);
		   ((KStructProgram) parent).types.addChild(type);
		}
		project.allTypes.put(t, type);
		return type;
	}

   /**
    * Erzeugt ein Typobjekt.
    * 
    * @param type
    *           TeachControl - Repräsentation des Typs
    * @return Typ
    */
   public KStructType createKStructType (TcStructuralTypeNode type, KTcDfl dfl) {
      KStructType t = null;
      KStructProject p = null;
      TcStructuralNode dirEntry = null;
      if ((type.getTypeKind() == TcStructuralTypeNode.MAPTO_TYPE) && (type.getBaseType() != null)) {
      	TcStructuralNode n = type.getBaseType().getParent();
      	while ((n != null) && (n.getKind() == TcStructuralNode.PROGRAM)) {
      		n = n.getParent();
      	}
      	if (n != null) {
      		if (n.getKind() == TcStructuralNode.ROOT) {
      			p = dfl.structure.getRoot();
      		} else {
      			p = dfl.structure.getKStructProject(n);
      		}
      	}
      } else {
      	dirEntry = type.getDeclarationNode();
         if (dirEntry != null) {
            p = dfl.structure.getKStructProject(dirEntry.getParent());
         }
      }
      if (p != null) {
         t = createNode(type.getName(), type, KStructNode.PUBLIC, 0, dfl);
         p.allTypes.put(type, t);
      } else {
         t = createNode(type.getName(), type, KStructNode.PUBLIC, 0, dfl);         	
      }
      if (t == null) {
         System.out.println("KTypeAdministrator - createKStructType " + type.getName());
      }
      return t;
   }

   
   /**
    * Lädt alle einfachen Typen.
    *
    * @param root globales Projekt
    */
   public static void loadSimpleTypes (KStructRoot root) {
      TcStructuralNode tcRoot = (root.getTcStructuralNode() != null) ? root.getTcStructuralNode(): null;
      if (tcRoot == null) {
         return;
      }
      Enumeration e = root.dfl.client.structure.getNodes(tcRoot, TcStructuralNode.TYPE);
      TcStructuralNode tn;
      while (e.hasMoreElements()) {
         tn = (TcStructuralNode) e.nextElement();
         if (tn instanceof TcStructuralTypeNode) {
         	KStructType child = null;
            switch (((TcStructuralTypeNode) tn).getTypeKind()) {
            case TcStructuralTypeNode.BOOL_TYPE:
            	child = new KStructTypeBool(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
            case TcStructuralTypeNode.DINT_TYPE:
            	child = new KStructTypeDInt(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
            case TcStructuralTypeNode.SINT_TYPE:
            	child = new KStructTypeSInt(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
            case TcStructuralTypeNode.INT_TYPE:
            	child = new KStructTypeInt(tn.getName(), KStructNode.GLOBAL, root.dfl);
              break;
            case TcStructuralTypeNode.LINT_TYPE:
            	child = new KStructTypeLInt(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
            case TcStructuralTypeNode.BYTE_TYPE:
            	child = new KStructTypeByte(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
            case TcStructuralTypeNode.WORD_TYPE:
            	child = new KStructTypeWord(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
            case TcStructuralTypeNode.DWORD_TYPE:
            	child = new KStructTypeDWord(tn.getName(), KStructNode.GLOBAL, root.dfl);
                break;
            case TcStructuralTypeNode.LWORD_TYPE:
            	child = new KStructTypeLWord(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
           case TcStructuralTypeNode.REAL_TYPE:
           		child = new KStructTypeReal(tn.getName(), KStructNode.GLOBAL, root.dfl);
               break;
           case TcStructuralTypeNode.ANY_TYPE:
          		child = new KStructTypeAny(tn.getName(), KStructNode.GLOBAL, false, root.dfl);
              break;
            case TcStructuralTypeNode.STRING_TYPE:
            	child = new KStructTypeString(tn.getName(), KStructNode.GLOBAL, root.dfl);
              break;
            case TcStructuralTypeNode.LREAL_TYPE:
            	child= new KStructTypeLReal(tn.getName(), KStructNode.GLOBAL, root.dfl);
            }
            if (child != null) {
            	child.setTcStructuralNode(tn);
            	root.dfl.structure.typeCounter++;
               root.allTypes.put(tn, child);
               root.types.addChild(child);
            }
         }
      }
   }

   private KStructType createNode (String key, TcStructuralTypeNode currentType,
                                          int visibility, int typeKindIfNoCurrentType, KTcDfl dfl) {
      KStructType child = null;
      dfl.structure.typeCounter++;
      int typeKind;
      if (currentType == null) {
         typeKind = typeKindIfNoCurrentType;
      } else {
         typeKind = currentType.getTypeKind();
      }
      TcStructuralTypeNode base = null;
      switch (typeKind) {
      case TcStructuralTypeNode.MAPTO_TYPE:
         child = new KStructTypeMapTo(dfl);
         break;
      case TcStructuralTypeNode.BOOL_TYPE:
         child = new KStructTypeBool(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.LINT_TYPE:
         child = new KStructTypeLInt(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.DINT_TYPE:
         child = new KStructTypeDInt(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.INT_TYPE:
         child = new KStructTypeInt(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.SINT_TYPE:
         child = new KStructTypeSInt(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.BYTE_TYPE:
         child = new KStructTypeByte(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.WORD_TYPE:
         child = new KStructTypeWord(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.DWORD_TYPE:
         child = new KStructTypeDWord(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.LWORD_TYPE:
         child = new KStructTypeLWord(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.REAL_TYPE:
         child = new KStructTypeReal(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.STRING_TYPE:
         child = new KStructTypeString(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.SUBRANGE_TYPE:
         child = new KStructTypeSubrange(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.ARRAY_TYPE:
         child = new KStructTypeArray(key, visibility, dfl);
         break;
      case TcStructuralTypeNode.ENUM_TYPE:
         if (currentType != null) {
            base = currentType.getBaseType();
         }
         child = new KStructTypeEnum(key, visibility, (base != null) && (base.getName().length() <= 0), dfl);
         break;
      case TcStructuralTypeNode.STRUCT_TYPE:
         if (currentType != null) {
            base = currentType.getBaseType();
         }
         child = new KStructTypeStruct(key, visibility, (base != null) && (base.getName().length() <= 0), dfl);
         break;
      case TcStructuralTypeNode.UNIT_TYPE:
         if (currentType != null) {
            base = currentType.getBaseType();
         }
         child = new KStructTypeUnit(key, visibility, base == null, dfl);
         break;
      case TcStructuralTypeNode.ROUTINE_TYPE:
         if (currentType != null) {
            base = currentType.getBaseType();
         }
         child = new KStructTypeRoutine(key, visibility, base == null, dfl);
         break;
      case TcStructuralTypeNode.ANY_TYPE:
         child = new KStructTypeAny(key, visibility, false, dfl);
         break;
      case TcStructuralTypeNode.LREAL_TYPE:
    	  child= new KStructTypeLReal(key, visibility,  dfl);
    	  break;
      default:
         child = new KStructTypeInvalid(key, visibility, dfl);
      }
      child.setTcStructuralNode(currentType);
      return child;
   }

   /**
    * Lädt die Typen eines Bausteins.
    *
    * @param parent Baustein
    */
   protected void loadUnitTypes (KStructTypeUnit parent) {
      TcStructuralTypeNode node = (TcStructuralTypeNode) parent.getTcStructuralNode();
      KStructType child = null;
      Enumeration e = parent.dfl.client.structure.getNodes(node, TcStructuralNode.TYPE);
      KStructProject project = parent.getKStructProject();
      TcStructuralTypeNode t;
      while (e.hasMoreElements()) {
         t = (TcStructuralTypeNode) e.nextElement();
         child =
            createNode(t.getName(), t, KStructNode.PRIVATE, 0, parent.dfl);
         parent.types.addChild(child);
         project.allTypes.put(t, child);
      }
   }

   private void addDirEntry (KStructNode node, KStructProject parent) {
      TcStructuralNode dirEntryNode = node.getTcStructuralNode().getDeclarationNode();
      if (dirEntryNode != null) {
         if (parent.programsAndUnits.containsKey(dirEntryNode)) {
            return;
         }
         parent.programsAndUnits.put(dirEntryNode, node);
      }
   }
}
