/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Project : Kemro.teachview.r2
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.value;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.edit.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.var.*;
import com.keba.kemro.teach.network.*;

/**
 * This class represents a internal teachtalk variable map target.
 */
public class KMapTargetInternal extends KMapTarget {
   protected Object[] componentPath;
   Object[] tcComponentPath;
   String tcFullPath;

   /**
    * Constructor for a internal variable.
    *     
    * fullPath = [project + '.' ] + [program + '.' ] + variable + {'.' + type
    * component | '[ + array index + ']'} return path = variable + {component | array index}.
    * program is omitted for global variables.
    * 
    * @param fullPath represents the variable or variable component node in the structural tree
    */
   KMapTargetInternal (String fullPath, KTcDfl dfl) {
   	super(dfl);
		this.fullPath = fullPath;
		componentPath = (fullPath != null) ? dfl.structure.getVariableComponentPath(fullPath): null;
		tcComponentPath = (componentPath != null) ? convertKStructNodePath(componentPath) : null;
		tcFullPath = (tcComponentPath != null) ? createFullPathOfTcComponentPath() : null;
   }
   
   /**
    * Constructor for a internal variable.
	 * variableComponentPath = KStructVar + {+ KStructVar (type
	 * component) | + Integer (array index)}
    * 
    * @param variableComponentPath  variable component path
    */
   KMapTargetInternal (Object[] variableComponentPath, KTcDfl dfl) {
   	super(dfl);
   	fullPath = (variableComponentPath != null) ? dfl.structure.getFullPath(variableComponentPath): null;
   	componentPath = variableComponentPath;
   	tcComponentPath = (componentPath != null) ? convertKStructNodePath(componentPath) : null;
   	tcFullPath = (tcComponentPath != null) ? createFullPathOfTcComponentPath() : null;
   }
   
   /**
    * Constructor for a internal variable.
	 * 
    * @param mapTarget  current map target
    */
   KMapTargetInternal (TcMapTarget mapTarget, KTcDfl dfl) {
   	super(dfl);
   	if (mapTarget != null && mapTarget.getPath() != null) {
   		tcComponentPath = mapTarget.getPath();
   		tcFullPath = (tcComponentPath != null) ? createFullPathOfTcComponentPath() : null;
   		componentPath = convertTcStructuralNodePath(tcComponentPath);
   		fullPath = (componentPath != null) ? dfl.structure.getFullPath(componentPath) : null;
   	} else {
   		tcComponentPath = componentPath = null;
   		fullPath = tcFullPath = null;
   	}
   }
   
   /**
    * Returns the variable component path.
	 * variableComponentPath = KStructVar + {+ KStructVar (type
	 * component) | + Integer (array index)}
	 * 
    * @return variable component path
    */
   public Object[] getVariableComponentPath () {
	  return componentPath;
   }
   
   public void updateComponentPath(){
	   if (tcComponentPath != null){
	     componentPath = convertTcStructuralNodePath(tcComponentPath);
	   }
   }
   
   
   /**
    * Returns the TeachControl component path.
    * tcComponentPath = TcStructuralNode + {+ TcStructuralNode (type
	 * component) | + Integer (array index)}
    * 
    * @return TeachControl component path
    */
   public Object[] getTcComponentPath() {
   	return tcComponentPath;
   }

   /**
    * Returns the representation of the variable or variable component node in the structural tree.
    * fullPath = [project + '.' ] + [program + '.' ] + variable + {'.' + type
    * component | '[ + array index + ']'} return path = variable + {component | array index}.
    * program is omitted for global variables.
    *
    * @return fullPath represents the variable or variable component node in the structural tree
    */
   public String getFullPath () {
      return fullPath;
   }
   
   /**
    * Returns the representation of the variable in TcStructuralNode .
    * tcFullPath = variable + {'.' + type component | '[ + array index + ']'} 
    * return path = variable + {component | array index}.
    *
    * @return tcFullPath path of the variable in TcStructuralNode
    */
   public String getTcFullPath() {
   	return tcFullPath;
   }
   
   
   /**
    * Converts the TcStructualNodePath into a KStructNodePath
    * @param tcComponentPath	TcStructualNodePath which has to be converted
    *
    * @return KStructNodePath of the TcStructualNodePath
    */
   private Object[] convertTcStructuralNodePath (Object[] tcComponentPath) {
      if (tcComponentPath == null || 0 == tcComponentPath.length) {
         return null;
      }
      
      KStructNode n = dfl.structure.getKStructNode((TcStructuralNode) tcComponentPath[0]);
      if (n != null) {
      	Object[] p = null;
      	int offset = 0;
      	if (n instanceof KStructProgram) {
         	p = new Object[tcComponentPath.length - 1];
      		offset = 1;
      	} else {
         	p = new Object[tcComponentPath.length];
         	p[0] = n;
      	}
         for (int i = 1; i < tcComponentPath.length; i++) {
            if (tcComponentPath[i] instanceof Integer) {
               p[i - offset] = tcComponentPath[i];
            } else {
               n = dfl.structure.getKStructVar((TcStructuralVarNode) tcComponentPath[i]);
               if (n == null) {
                  return null;
               }
               p[i - offset] = n;
            }
         }
         return p;
      }
      return null;
   }
   
   /**
    * Converts the KStructNodePath into a TcStructualNodePath
    * @param kStructNodePath	KStructNodePath which has to be converted
    * 
    * @return	TcStructualNodePath of the KStructNodePath
    */
   private Object[] convertKStructNodePath (Object[] kStructNodePath) {
   	if (kStructNodePath == null || 0 == kStructNodePath.length) {
         return null;
      }
   	
   	Object[] p = null;
		int offset = 0;
		if (((KStructVar) kStructNodePath[0]).getParent() instanceof KStructProgram) {
			p = new Object[kStructNodePath.length + 1];
			p[0] = ((KStructVar) kStructNodePath[0]).getParent().getTcStructuralNode();
			offset = 1;
		} else {
			p = new Object[kStructNodePath.length];
		}
		for (int i = 0; i < kStructNodePath.length; i++) {
			if (kStructNodePath[i] instanceof Integer) {
				p[i + offset] = kStructNodePath[i];
			} else {
				p[i + offset] = ((KStructNode) kStructNodePath[i]).getTcStructuralNode();
			}
		}
		return p;
	}
   
   /**
    * Compares two TcStructualNodePaths on equality. 
    * They are equal, if the length of the two arrays and the content are equal.
    * @param tcPath	TcStructualNodePath which should be compared
    * 
    * @return true, if the two TcStructualNodePaths are equal
    */
   boolean compareTcStructuralNodePath (Object[] tcPath) {
   	if (((tcComponentPath == null || tcComponentPath.length == 0) && (tcPath != null && 0 < tcPath.length))
				|| ((tcComponentPath != null && 0 < tcComponentPath.length) && (tcPath == null || tcPath.length == 0))) {
			return false;
		}
		if (((tcComponentPath == null || tcComponentPath.length == 0) && (tcPath == null || tcPath.length == 0))) {
			return true;
		}
		int offset = (tcPath[0] instanceof TcStructuralVarNode) ? 0 : 1;
		if ((tcComponentPath.length - offset) != (tcPath.length - offset)) {
			return false;
}
   	for (int i = offset; i < tcComponentPath.length; i++) {
   		if (!(((tcComponentPath[i] instanceof TcStructuralNode) && (tcPath[i] instanceof TcStructuralNode) && 
   					(tcComponentPath[i].equals(tcPath[i]))) || 
   			 ((tcComponentPath[i] instanceof Integer) && (tcPath[i] instanceof Integer) && 
   					 (tcComponentPath[i].equals(tcPath[i]))))) {
   			return false;
   		}
   	}
   	return true;
   }
   
   private String createFullPathOfTcComponentPath() {
   	if (tcComponentPath == null) {
   		return null;
   	}
   	
   	String path = "";
		String point = "";
		for (int i = 0; i < tcComponentPath.length; i++) {
			Object obj = tcComponentPath[i];
			if (obj == null) {
				return null;
			}
			if (obj instanceof TcStructuralNode) {
				path += (point + ((TcStructuralNode)obj).getName());
			} 
			else if (obj instanceof Integer) {
				path += KEditKW.KW_BRACKET_LEFT + ((Integer)obj).toString() + KEditKW.KW_BRACKET_RIGHT;
			} else {
				return null;
			}
			point = KEditKW.KW_POINT ;
		}
		return path;
   }

}
