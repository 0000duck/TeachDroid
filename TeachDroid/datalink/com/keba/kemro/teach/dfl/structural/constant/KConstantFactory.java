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
package com.keba.kemro.teach.dfl.structural.constant;

import java.util.*;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.network.*;

/**
 * Diese Klasse ist für die Verarbeitung von Konstanten des Strukturbaumes verantwortlich.
 */
public class KConstantFactory {

	/**
	 * Lädt die Konstanten.
	 *
	 * @param parent Projekt, Programm, Baustein oder Enumerationstyp 
	 */
	public void loadConstants(KStructNode parent) {
		Enumeration e = parent.dfl.client.structure.getNodes(parent.getTcStructuralNode(), TcStructuralNode.CONST);
		while (e.hasMoreElements()) {
			TcStructuralConstNode c = (TcStructuralConstNode) e.nextElement();
			addNode(parent, c);
		}
	}

	public KStructConst addNode(KStructNode parent, TcStructuralConstNode tcc) {
		TcStructuralTypeNode t = tcc.getType();
		KStructType tn = parent.dfl.structure.getKStructType(t);
		if ((tn != null) && (!(tn instanceof KStructTypeEnum) || (parent instanceof KStructTypeEnum))) {
			String key = tcc.getName();
			int visibility;
			KStructProject project;
			if (parent instanceof KStructProject) {
				project = (KStructProject) parent;
				visibility = KStructNode.GLOBAL;
			} else {
				project = parent.getKStructProject();
				if ((tn instanceof KStructTypeEnum) && (!parent.isPrivate())) {
					visibility = KStructNode.GLOBAL;
				} else {
					visibility = KStructNode.PRIVATE;
				}
			}
			KStructConst constant = createNode(key, visibility, tcc, tn, parent.dfl);
			if (parent instanceof KStructProgram) {
				((KStructProgram) parent).constants.addChild(constant);
			} else if (parent instanceof KStructTypeUnit) {
				((KStructTypeUnit) parent).constants.addChild(constant);
			} else if (parent instanceof KStructTypeEnum) {
				((KStructTypeEnum) parent).constants.addChild(constant);
			} else if (parent instanceof KStructProject) {
				((KStructProject) parent).constants.addChild(constant);
			}
			project.allConstants.put(tcc, constant);
			return constant;
		}
		return null;
	}

	protected KStructConst createNode(
		String key,
		int visibility,
		TcStructuralConstNode c,
		KStructType type,
		KTcDfl dfl) {
		dfl.structure.constCounter++;
		KStructConst node = null;
		if (type instanceof KStructTypeBool) {
			node = new KStructConstBool(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeByte) {
			node = new KStructConstByte(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeWord) {
			node = new KStructConstWord(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeDWord) {
			node = new KStructConstDWord(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeLWord) {
			node = new KStructConstLWord(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeSInt) {
			node = new KStructConstSInt(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeInt) {
			node = new KStructConstInt(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeDInt) {
			node = new KStructConstDInt(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeLInt) {
			node = new KStructConstLInt(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeReal) {
			node = new KStructConstReal(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeString) {
			node = new KStructConstString(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeEnum) {
			node = new KStructConstEnum(key, type, visibility, dfl);
		} else if (type instanceof KStructTypeInvalid) {
			node = new KStructConstInvalid(key, type, visibility, dfl);
		}
		else if( type instanceof KStructTypeLReal) {
			node= new KStructConstLReal(key,type,visibility,dfl);
		}
		node.setTcStructuralNode(c);
		return node;
	}
}
