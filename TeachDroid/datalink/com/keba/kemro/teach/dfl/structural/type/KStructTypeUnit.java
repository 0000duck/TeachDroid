package com.keba.kemro.teach.dfl.structural.type;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.network.*;

/**
 *  Dieser Typ unterscheidet prinzipiell 2 Arten von Bausteinen. 
 * AliasTypen sind Bausteine die wie normale Datentypen verwendet werden
 * können. Hier kann nicht auf Konstante, Typen , Routinen, Componenten
 * und Basisunits zugegriffen werden. Die 2 Art sind die NonAlias Typen
 * die physikalisch eine Datei repräsentieren.
 */
public class KStructTypeUnit extends KStructTypeAny {
   /**
    *  Liste mit Constanten
    */
   public KStructNodeVector constants;
   /**
    *  Liste mit Typen
    */
   public KStructNodeVector types;
   /**
    *  Liste mit Routinen
    */
   public KStructNodeVector routines;
   /**
    *  Liste mit Komponenten
    */
   public KStructNodeVector components;
   /**
    *  Flag für abgeleitete  
    */
   protected final static int IS_EXTENDED = BIT_MASK_LAST_KSTRUCT_TYPE_NODE * 2;


   KStructTypeUnit(String key, int visibility, boolean allowsChildren, KTcDfl dfl) {
      super(key.length() == 0 ? "UNIT": key, visibility, allowsChildren, dfl);
      if (allowsChildren) {
         constants = new KStructNodeVector(this);
         types = new KStructNodeVector(this);
         routines = new KStructNodeVector(this);
         components = new KStructNodeVector(this);
      }
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.KStructNode#getDirEntryPath()
    */
   public String getDirEntryPath() {
      if (storedDirEntryPath != null) {
         return storedDirEntryPath;
      }
      if (isAliasType()) {
         storedDirEntryPath = getParent().getDirEntryPath();
      }
      else {
         storedDirEntryPath = getParent().getDirEntryPath() + m_separator + getKey().toUpperCase() + KTcDfl.OBJECT_FILE_EXTENSION;
      }
      return storedDirEntryPath;
   }


   /**
    * Liefert die BasisUnit falls der Baustein von einem anderen 
    * anderen Baustein abgeleitet wurde. Funktioniert natürlich nur
    * für Non Alias Typen
    *
    *@return    The baseUnit value
    */
   public KStructTypeUnit getBaseUnit() {
      checkReference();
      if (isAliasType()) {
         return null;
      }
      return (KStructTypeUnit) baseType;
   }

   
   /**
    * Returns the routine of the given name and searchs in the base unit if the routine isn't
    * declared in this unit.
    * 
    * @param name routine name
    * @return routine of the given name otherwise null
    */
   public KStructRoutine getRoutine (String name) {
   	KStructRoutine routine = (KStructRoutine) ((routines != null) ? routines.getChild(name, false): null);
   	if (routine == null) {
   		KStructTypeUnit baseUnit = getBaseUnit();
   		if (baseUnit != null) {
   			return baseUnit.getRoutine(name);
   		}
   	}
   	return routine;
   }
   
   /**
    * Liefert true wenn der Baustein als ABSTRACT gekennzeichnet ist und nicht 
    * instanziert werden darf.
    *
    *@return    isAbstract
    */
   public boolean isAbstract() {
  		return (ortsStructuralNode instanceof TcStructuralTypeNode) && ((TcStructuralTypeNode)ortsStructuralNode).isAbstract();
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.KStructNode#loadChildren()
    */
   protected void loadChildren() {
      if (!isLoaded() && this.getAllowsChildren() && !isAliasType() && !(parent instanceof KStructTypeUnit)) {
	      setLoaded(true);
	      dfl.structure.typeFactory.loadUnitTypes(this);
	      dfl.structure.constFactory.loadConstants(this);
	      dfl.structure.variableFactory.loadVariables(this);
	      dfl.structure.routineFactory.loadRoutines(this);
      }
   }
   
   protected void checkReference () {
      if (!isBaseChecked()) {
      	super.checkReference();
         if (!isAliasType()) {
         	TcStructuralTypeNode baseTypeNode = ((TcStructuralTypeNode) ortsStructuralNode).getBaseUnit();
         	baseType = dfl.structure.getKStructType(baseTypeNode);
         }
      }
   }
}
