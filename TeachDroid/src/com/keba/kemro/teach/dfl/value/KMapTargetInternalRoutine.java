package com.keba.kemro.teach.dfl.value;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.network.*;

/**
 * This class represents a internal tachtalk program or unit routine map target. 
 */
public class KMapTargetInternalRoutine extends KMapTargetInternal {
   private KStructRoutine routine;

   /**
    * Constructor for a internal routine map target.
    *     
    * fullPath = [project + '.' ] + [program + '.' ] + variable + {'.' + type
    * component | '[ + array index + ']'} return path = variable + {component | array index}.
    * program is omitted for global variables.
    * 
    * routineFullPath = [project + '.' ] + [(program | unit) + '.' ] + routine .
    * program is omitted for global routines.
    * 
    * @param fullPath represents the unit variable or unit variable component node in the structural tree
    * @param routineFullPath represents the routine in the structural tree
    */
   KMapTargetInternalRoutine (String fullPath, String routineFullPath, KTcDfl dfl) {
 		super(fullPath, dfl);
		if (routineFullPath != null) {
			KStructNode node = dfl.structure.getKStructNode(routineFullPath);
			if (node instanceof KStructRoutine) {
				routine = (KStructRoutine) node;
			}
		}
   }
   
   /**
    * Constructor for a internal routine map target. 
	 * variableComponentPath = KStructVar + {+ KStructVar (type
	 * component) | + Integer (array index)}
    * 
    * @param variableComponentPath  unit variable component path
    * @param routine routine map target
    */
   KMapTargetInternalRoutine (Object[] variableComponentPath, KStructRoutine routine, KTcDfl dfl) {
   	super(variableComponentPath, dfl);
   	this.routine = routine;
   }
   
   KMapTargetInternalRoutine (TcMapTarget mapTarget, KStructRoutine routine, KTcDfl dfl) {
   	super(mapTarget, dfl);
   	this.routine = routine;
   }

   /**
    * @return routine map target
    */
   public KStructRoutine getRoutine () {
      return routine;
   }
}
