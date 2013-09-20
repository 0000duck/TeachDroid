package com.keba.kemro.kvs.teach.util;

import java.util.Hashtable;

import com.keba.kemro.kvs.teach.data.rc.KvtRcAdministrator;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.KMultikinematicListener;
import com.keba.kemro.teach.dfl.structural.KStructAdministratorListener;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructRoot;
import com.keba.kemro.teach.dfl.structural.KStructSystem;
import com.keba.kemro.teach.dfl.structural.var.KStructVarLReal;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;
import com.keba.kemro.teach.dfl.value.KVariableGroup;
import com.keba.kemro.teach.dfl.value.KVariableGroupListener;

public class KvtMultiKinematikAdministrator implements KMultikinematicListener, KStructAdministratorListener, KVariableGroupListener, KvtTeachviewConnectionListener {
	private static KStructVarWrapper kinematic;
	private static KVariableGroup varGroup;
	private static KvtMultiKinematikAdministrator admin;
	private static boolean mInitialized;
	private String filter;
	private Hashtable masks = new Hashtable(19);
	private String oldKinematic;
	private KTcDfl mDfl;
	private static Object mLck = new Object();

	public static void init() {
		synchronized (mLck) {
			if (!mInitialized) {
				if (admin == null) {
					admin = new KvtMultiKinematikAdministrator();
				}
				mInitialized = true;
			}

		}
		// return admin;
	}

	private KvtMultiKinematikAdministrator() {
		KvtSystemCommunicator.addConnectionListener(this);
	}

	public static String readKinematicFilter() {
		init();
		return admin.setFilterString();
	}

	public static boolean isMultiKinematik() {
		init();
		KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
		if (dfl != null) {
			KStructVarWrapper wrappi = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcData.bIsMultiKinematicSystem");
			if (wrappi != null) {
				Object o = wrappi.readActualValue(null);
				return (!(o instanceof Boolean)) || ((Boolean) o).booleanValue();
			}
		}
		return false;
	}

	public static String getKinematicFilter() {
		init();
		return admin.getFilterString();
	}

	public static int getKinematicIndex() {
		init();
		int kin = -1;
		if (kinematic != null) {
			Object o = kinematic.readActualValue(null);
			if (o instanceof Integer) {
				kin = ((Integer) o).intValue();
			}
		}
		return kin;
	}

	public void teachviewConnected() {
		synchronized (this) {
			mDfl = KvtSystemCommunicator.getTcDfl();
			if (mDfl != null) {
				synchronized (mDfl.getLockObject()) {
					varGroup = mDfl.variable.createVariableGroup("masksettingadmin");
					varGroup.addListener(this);
					varGroup.setPollInterval(500);
					mDfl.structure.addMultikinematikListener(this);
					mDfl.structure.addStructAdministratorListener(this);
					treeChanged(mDfl.structure.getRoot());
				}
			}
		}
	}

	public void teachviewDisconnected() {
		// mdfl = KvtSystemCommunicator.getTcDfl();
		if (mDfl != null) {
			mDfl.structure.removeMultikinematicListener(this);
			mDfl.structure.removeStructAdministratorListener(this);
			if (varGroup != null) {
				varGroup.release();
			}
			kinematic = null;
			varGroup = null;
		}
	}

	public void treeChanged(KStructNode parent) {
		if ((parent instanceof KStructRoot) || (parent instanceof KStructSystem)) {
			createVariable();
		}
	}

	public void nodeInserted(KStructNode parent, KStructNode node) {
		if (node instanceof KStructSystem) {
			createVariable();
		}
	}

	public void nodeRemoved(KStructNode parent, KStructNode node) {
		if (node instanceof KStructSystem) {
			createVariable();
		}
	}

	private synchronized void createVariable() {
		if (varGroup != null) {
			varGroup.release();
			KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
			if (dfl != null) {
				kinematic = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcData.selectedRobot");
				if (kinematic != null) {
					varGroup.add(kinematic);
					varGroup.activate();
				}
			}
		}
	}

	private synchronized String setFilterString() {
		filter = "_global";
		// KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
		int kin = -1;
		if (kinematic != null) {
			Object o = kinematic.readActualValue(null);
			if (o instanceof Integer) {
				kin = ((Integer) o).intValue();
			}
		}
		if (mDfl != null) {
			if (0 <= kin) {
				KStructVarWrapper w = mDfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcData.robotName[" + kin + "]");
				if (w != null) {
					filter = (String) w.readActualValue(null);
					if ((filter == null) || (filter.length() == 0)) {
						filter = "_global";
					}
				} else {
					filter = "_global";
				}
			}
			new Thread(new Runnable() {
				public void run() {
					KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
					if (dfl != null) {
						dfl.setGlobalFilter(filter);
					}
					// KvtTeachViewController.getController().reloadMenu();
				}
			}).start();
		}
		return filter;
	}

	public synchronized String getFilterString() {
		return filter;
	}

	private synchronized void saveControllerData() {
		// if ((filter != null) && (!filter.equals("_global"))) {
		// String mask = ((KvtMainTeachView)
		// KvtMainTeachView.getInstance()).getActMask();
		// if (mask != null) {
		// masks.put(filter, mask);
		// }
		// Enumeration en = KvtAbstractController.getRegisteredControllers();
		// while (en.hasMoreElements()) {
		// Object data = en.nextElement();
		// if (data instanceof KvtAbstractController) {
		// ((KvtAbstractController) data).saveControllerData(filter);
		// }
		// }
		// }
	}

	private synchronized void restoreControllerData() {
		// if ((filter != null) && (!filter.equals("_global"))) {
		// Enumeration en = KvtAbstractController.getRegisteredControllers();
		// while (en.hasMoreElements()) {
		// Object data = en.nextElement();
		// if (data instanceof KvtAbstractController) {
		// ((KvtAbstractController) data).restoreControllerData(filter);
		// }
		// }
		// }
	}

	public void allActualValuesUpdated() {
		// TODO Auto-generated method stub
	}

	public void changed(KStructVarWrapper variable) {
		if (variable == kinematic) {
			// KvToolkit.closePopup();
			// KvToolkit.closePopupMenu();
			// KvtDialogFactory.getDialogFactory().closeAllOpenDialogs();
			saveControllerData();
			oldKinematic = filter;
			setFilterString();
			// if (KvtMainTeachView.bootupfinished() && (filter != null) &&
			// (!filter.equals("_global")) && (!filter.equals(oldKinematic))) {
			// String title =
			// KvtTeachViewController.getController().getTranslationText("Main.msg.changeKinematic");
			// KvtProgressMonitor.showProgressMonitor(title, filter, 0, 5000);
			// KvtProgressMonitor.ignoreComands(true);
			// }
		}
	}

	public void kinematikChanged() {
		if (/* KvtMainTeachView.bootupfinished() && */(filter != null) && (!filter.equals("_global")) && (!filter.equals(oldKinematic))) {
			oldKinematic = filter;
			Object mask = masks.get(filter);

			// if (mask == null) {
			// mask = KvtMainTeachView.getInstance().getInitMask();
			// }
			// if (mask instanceof String) {
			// KvtMainTeachView.getInstance().showMask((String) mask);
			// }
			// KvUtilities.invokeLater(new Runnable() {
			// public void run() {
			// restoreControllerData();
			// try {
			// if (!KvtSystem.isKetop()) {
			// Thread.sleep(1000);
			// } else {
			// Thread.sleep(100);
			// }
			// } catch (Exception e) {
			// TODO Auto-generated catch block
			// }
			// KvUtilities.invokeLater(new Runnable() {
			// public void run() {
			// KvtProgressMonitor.ignoreComands(false);
			// KvtProgressMonitor.closeProgressMonitor();
			// /*
			// * new Thread(){ public void run(){ try {
			// * Thread.sleep(3000); } catch (Exception e) { //
			// * TODO Auto-generated catch block }
			// * KvtProgressMonitor.ignoreComands(false);
			// * KvtProgressMonitor.closeProgressMonitor(); }
			// * }.start();
			// */
			// }
			// });
			// }
			// });
		}
	}
}