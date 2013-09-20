/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Auftragsnr: 5500395
 *    Erstautor : wies
 *    Datum     : 01.04.2003
 *--------------------------------------------------------------------------
 *      Revision:
 *        Author:
 *          Date:
 *------------------------------------------------------------------------*/

package com.keba.kemro.kvs.teach.data.program;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.KStructGlobal;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.KStructProject;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;
import com.keba.kemro.teach.dfl.structural.var.KStructVar;
import com.keba.kemro.teach.dfl.value.KMapTarget;
import com.keba.kemro.teach.dfl.value.KMapTargetInternal;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;

/**
 * This class administers the consecutive numbering of variables with a certain
 * name prefix.
 */
public class KvtVarManager {
	private static final Hashtable	varLists	= new Hashtable();

	/**
	 * Adds a new variable to the variable list with the given prefix.
	 * 
	 * @param prefix
	 *            prefix of the variable
	 * @param var
	 *            variable to add
	 */
	public static void addVariable(String prefix, KStructVar var) {
		Vector varList = (Vector) varLists.get(prefix);
		if (varList != null) {
			varList.addElement(var.getKey().substring(prefix.length()));
		}
	}

	/**
	 * write save and init values for a variable after it has been changed (new,
	 * teach, ...)
	 * 
	 * @param var
	 *            variable whose value shall be stored
	 * @return true on success
	 */
	public static boolean writeBackValue(KStructVarWrapper var, KStructProject prj) {
		return writeBackValue(var, prj, true);
	}

	public static boolean writeBackValue(KStructVarWrapper var, KStructProject prj, boolean includeMapTarget) {
		// only for user variables the value should be saved
		boolean result = false;
		if (var != null) {
			result = writeBackValuePrivate(var, prj);
		}
		KTcDfl d = KvtSystemCommunicator.getTcDfl();
		if (includeMapTarget && (d != null)) {
			String v = var.getRootPathString();
			int i = v.lastIndexOf(".");
			if (i > 0) {
				var = d.variable.createKStructVarWrapper(v.substring(0, i));
			}
			if (var != null) {
				KMapTarget target = var.readMapTarget();
				if (target instanceof KMapTargetInternal) {
					KStructVarWrapper wrapper = d.variable.createKStructVarWrapper(((KMapTargetInternal) target).getVariableComponentPath());
					if (wrapper != null) {
						result = writeBackValuePrivate(wrapper, prj) && result;
					}
				}
			}
		}
		return result;
	}

	private static boolean writeBackValuePrivate(KStructVarWrapper var, KStructProject prj) {
		if (var.isUserVar()) {
			if (var.isDeclaredInUserProgram()) {
				if (!var.writeBackInitValue()) {
					KTcDfl d = KvtSystemCommunicator.getTcDfl();
					if (d != null) {
						d.error.checkIncCompilerError();
					}
					// if (KvtErrMsgAdministrator.projectChangedErrorActive()) {
					// KvtProject p = (prj != null) ?
					// KvtProjectAdministrator.getProject(prj.getKey()) : null;
					// new KvtProjectChangedErrorDialog(cont, p);
					// return false;
					// }
					//
					// KvtErrMsgAdministrator.showError(KvtErrMsgAdministrator.ERR_TITLE,
					// KvtErrMsgAdministrator.WRITE_BACK_INIT_VALUES_ERR_MSG,
					// prj);
					return false;
				}
			} else if (var.isSaveVar()) {
				if (!var.writeBackSaveValue()) {
					KTcDfl d = KvtSystemCommunicator.getTcDfl();
					if (d != null) {
						d.error.checkIncCompilerError();
					}
					// if (KvtErrMsgAdministrator.projectChangedErrorActive()) {
					// new KvtProjectChangedErrorDialog(cont,
					// KvtProjectAdministrator.getProject(prj.getKey()));
					// return false;
					// }
					//
					// KvtErrMsgAdministrator.showError(KvtErrMsgAdministrator.ERR_TITLE,
					// KvtErrMsgAdministrator.WRITE_BACK_SAVE_VALUES_ERR_MSG,
					// prj);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Removes the given variable.
	 * 
	 * @param var
	 *            variable to remove
	 */
	public static void removeVariable(KStructVar var) {
		Enumeration e = varLists.keys();
		while (e.hasMoreElements()) {
			String elem = (String) e.nextElement();
			if (var.getKey().startsWith(elem)) {
				try {
					Integer seq = new Integer(var.getKey().substring(elem.length()));
					Vector v = ((Vector) varLists.get(elem));
					v.removeElement(seq); // even works when seq does not
					// exist in v
				} catch (NumberFormatException ne) {
					// remove only variables with a number as postfix
				}
			}
		}
	}

	/**
	 * Removes all stored variables.
	 */
	public static void removeAllVariables() {
		varLists.clear();
	}

	private static Vector buildVarListForPrefix(String prefix, KStructNode node, Vector varList) {
		KStructNodeVector nv = null;
		if (node instanceof KStructProject) {
			if (!(node instanceof KStructGlobal)) {
				varList = buildVarListForPrefix(prefix, node.getParent(), varList);
			}

			KStructProject p = (KStructProject) node;
			nv = p.variables;
		} else if (node instanceof KStructProgram) {
			varList = buildVarListForPrefix(prefix, node.getParent(), varList);
			KStructProgram p = (KStructProgram) node;
			nv = p.variables;
		} else if (node instanceof KStructRoutine) {
			varList = buildVarListForPrefix(prefix, node.getParent(), varList);
			KStructRoutine p = (KStructRoutine) node;
			nv = p.variables;
		}

		if (node instanceof KStructRoutine) {
			KStructRoutine p = (KStructRoutine) node;
			nv = p.variables;
		}

		if (nv != null) {
			for (int i = 0; i < nv.getChildCount(); i++) {
				KStructVar elem = (KStructVar) nv.getChild(i);
				if (elem.getKey().startsWith(prefix)) {
					try {
						Integer seq = new Integer(elem.getKey().substring(prefix.length()));
						varList.addElement(seq);
					} catch (NumberFormatException ne) {
						// add only variables with a number as postfix
					}
				}
			}
		}
		return varList;
	}

	private static Vector sort(Vector src) {
		int r;
		int gap;
		int counter;
		int pointer;
		boolean greater;
		Object temp;
		r = src.size();
		gap = r / 2;
		while (gap > 0) {
			for (counter = gap; counter < r; counter++) {
				pointer = counter - gap;
				while (pointer >= 0) {
					greater = ((Integer) src.elementAt(pointer)).intValue() > ((Integer) src.elementAt(gap + pointer)).intValue();
					if (greater) {
						temp = src.elementAt(pointer);
						src.setElementAt(src.elementAt(gap + pointer), pointer);
						src.setElementAt(temp, gap + pointer);
						pointer = pointer - gap;
					} else {
						pointer = -1;
					}
				}
			}
			gap = gap / 2;
		}
		return src;
	}

	/**
	 * starts to count at lowest possible number
	 * 
	 * @see KvtVarManager#getNextVarSequenceNumber(String, KStructNode)
	 * @param prefix
	 *            variable prefix for which to restart counting
	 */
	public static void resetVarSequenceNumber(String prefix) {
		varLists.remove(prefix);
	}

	/**
	 * gets next possibly free variable name. this simply counts up and
	 * remembers the returned variable names for each prefix. result will be
	 * "prefixXX" with XX being a number
	 * 
	 * @param prefix
	 *            variable prefix as configured in usertypes.properties
	 * @param node
	 *            specifies the scope for searching existing variables
	 * @return prefixXX
	 */
	public static int getNextVarSequenceNumber(String prefix, KStructNode node) {
		varLists.clear();
		Vector varList = (Vector) varLists.get(prefix);
		if (varList == null) {
			varList = new Vector();
			varList = buildVarListForPrefix(prefix, node, varList);
		}

		sort(varList);
		int i = 0;
		int j = 0;
		while ((i < varList.size())) {
			while ((i < varList.size()) && ((Integer) varList.elementAt(i)).intValue() < j) {
				i++;
			}
			if (i >= varList.size() || ((Integer) varList.elementAt(i)).intValue() > j) {
				varList.addElement(new Integer(j));
				varLists.put(prefix, varList);
				return j;
			}
			j++;
		}
		if (varList.size() == 0) {
			varList.addElement(new Integer(j));
			varLists.put(prefix, varList);
		}
		varLists.put(prefix, varList);
		return j;
	}

	public static boolean teach(KStructVarWrapper _teachVar, Object[] _optParameters) {
		return teach(_teachVar, _optParameters, null, false);
	}

	/**
	 * teach variable, write back values, open teach ok dialog for a short time
	 * 
	 * @param wrap
	 *            variable that should be taught
	 * @param additionalParameter
	 *            caller KStructVarWrapper variable or program name and project
	 *            name
	 * @param desc
	 *            description for the success dialog
	 * @param dropAhead
	 *            drops the ahead execution
	 * @return true if successfully teach
	 */
	public static boolean teach(KStructVarWrapper wrap, Object[] additionalParameter, String desc, boolean dropAhead) {
		KStructVar var = wrap.getKStructVar();
		if ((var == null) && (wrap.isArrayField())) {
			var = wrap.getRootVariable();
		}
		KStructProject prj = var.getKStructProject();
		boolean result = false;
		if (additionalParameter != null) {
			result = wrap.teachVariable(additionalParameter);
		} else {
			result = wrap.teachVariable();
		}
		if (result) {
			if (dropAhead) {
				// KvtProgramControllerFactory.getProgramController().dropAheadExecution();
			}
			KvtVarManager.writeBackValue(wrap, prj);
			// new CloseThread(cont, desc);
		} else {
			// KvtErrMsgAdministrator.showError(KvtErrMsgAdministrator.ERR_TITLE,
			// KvtErrMsgAdministrator.TEACH_ERR_MSG, prj);
			Log.e("KvtVarManager", "Teaching variable FAILED");
		}
		return result;
	}

	/**
	 * dialog displayed on teach command
	 */
	// public static class CloseThread extends Thread {
	// private static final int TEACH_OK_DIALOG_OPEN_TIME = 500;
	// private static int dialogOpenTime = -1;
	// private KvtTempDialog dialog;
	// private KvtAbstractController controller;
	//
	// /**
	// * create new close thread
	// *
	// * @param makro
	// * teached unit
	// */
	// public CloseThread(KvtAbstractController cont, String unit) {
	// if (dialogOpenTime == -1) {
	// dialogOpenTime = Config.getIntProperty("TeachOKDialogOpenTime",
	// TEACH_OK_DIALOG_OPEN_TIME);
	// }
	// controller = cont;
	// String title =
	// KvtTranslation.getTranslationText("VarManager.msg.titleTeachOk");
	// String text = unit + " " +
	// KvtTranslation.getTranslationText("VarManager.msg.contentTeachOk");
	// dialog = new KvtTempDialog(controller, title, text);
	// dialog.show();
	// start();
	// }
	//
	// /**
	// * @see java.lang.Runnable#run()
	// */
	// @Override
	// public void run() {
	// try {
	// sleep(dialogOpenTime);
	// } catch (InterruptedException e) {
	// // ignore
	// }
	// dialog.close();
	// }
	// }
}
