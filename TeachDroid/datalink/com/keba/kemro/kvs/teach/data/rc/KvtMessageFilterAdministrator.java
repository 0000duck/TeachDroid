package com.keba.kemro.kvs.teach.data.rc;

import java.util.Vector;

import com.keba.kemro.kvs.teach.controller.KvtMessageFilter;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.KStructAdministratorListener;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructRoot;
import com.keba.kemro.teach.dfl.structural.KStructSystem;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;
import com.keba.kemro.teach.dfl.value.KVariableGroup;
import com.keba.kemro.teach.dfl.value.KVariableGroupListener;

//import com.keba.kemro.kvs.teach.framework.util.KvtLogger;

/**
 * Message filter administrator manages the message filter.
 * 
 */
public class KvtMessageFilterAdministrator {
	private static Vector				m_listeners			= new Vector(5);
	private static KVariableGroup		generalVarGroup;
	private static KStructVarWrapper	kinematic;
	private static KStructVarWrapper	changeCounter;
	private static int					kinematicNr			= -1;
	private static int					changeCnt;
	private static int[]				componentNumbers;
	private static Object[]				instanceNumbers;
	private static boolean				kinematicChanged	= false;
	private static boolean				rebuildFilter		= false;
	private static KvtMessageFilter		applicationFilter;
	private static KTcDfl				dfl;

	/**
	 * Initializes the administrator.
	 */
	@SuppressWarnings("unused")
	public static void init() {
		try {
			// String className = Config.getStringProperty("messageFilterClass",
			// null);
			String className = null; // attaching a message filter class is not
										// supported on android!
			if (className != null) {
				Class classObject = Class.forName(className);
				if (classObject != null) {
					Object classInstance = classObject.newInstance();
					if (classInstance instanceof KvtMessageFilter) {
						applicationFilter = (KvtMessageFilter) classInstance;
					}
				}
				if (applicationFilter == null) {
					// KvtLogger.error(KvtMessageFilterAdministrator.class,
					// "can't load application message filter :" + className);
					Log.e("KvtMEssageFilterAdministrator", "can't load application message filter :" + className);
				}
			}
		} catch (Exception e) {
			// option not set
			e.printStackTrace();
		}

		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {
			@Override
			public void teachviewConnected() {
				dfl = KvtSystemCommunicator.getTcDfl();
				synchronized (dfl.getLockObject()) {
					generalVarGroup = dfl.variable.createVariableGroup("Message Filter Gourp");
					generalVarGroup.setPollInterval(500);
					generalVarGroup.addListener(new KVariableGroupListener() {
						@Override
						public void changed(KStructVarWrapper variable) {
							if (variable.equals(kinematic)) {
								Object value = kinematic.getActualValue();
								if ((value instanceof Integer) && (0 <= ((Integer) value).intValue())) {
									kinematicChanged = true;
								}
							}
							if (variable.equals(changeCounter)) {
								Object value = changeCounter.getActualValue();
								if ((value instanceof Integer) && (changeCnt != ((Integer) value).intValue())) {
									changeCnt = ((Integer) value).intValue();
									rebuildFilter = true;
								}
							}
						}

						@Override
						public void allActualValuesUpdated() {
							final boolean rFilter = rebuildFilter;
							final boolean kChanged = kinematicChanged;
							if (rFilter || kChanged) {
								Thread r = new Thread("rebuild message filter") {
									@Override
									public void run() {
										if (rFilter) {
											rebuildFilter();
										}
										if (kChanged) {
											kinematicChanged();
										}
										if (rFilter || kChanged) {
											fireFilterChanged();
										}
									}
								};
								r.setPriority(Thread.MIN_PRIORITY);
								r.start();
							}
							rebuildFilter = false;
							kinematicChanged = false;
						}
					});

					dfl.structure.addStructAdministratorListener(new KStructAdministratorListener() {
						@Override
						public void treeChanged(KStructNode parent) {
							if ((parent instanceof KStructSystem) || (parent instanceof KStructRoot)) {
								clear();
								createGeneralVariablen();
							}
						}

						@Override
						public void nodeInserted(KStructNode parent, KStructNode node) {
							if (node instanceof KStructSystem) {
								createGeneralVariablen();
							}
						}

						@Override
						public void nodeRemoved(KStructNode parent, KStructNode node) {
							if (node instanceof KStructSystem) {
								clear();
								fireFilterChanged();
							}
						}
					});
					clear();
					createGeneralVariablen();
				}

			}

			@Override
			public void teachviewDisconnected() {
				synchronized (dfl.getLockObject()) {
					generalVarGroup.release();
					dfl = null;
					generalVarGroup = null;
				}
			}
		});
	}

	/**
	 * Checks the message against the filter. In general all message are valid
	 * until a filter criterion could applied. For messages with a component
	 * number lesser then 1000 the application filter will be called. All other
	 * messages with a component number contained in the filter will be checked
	 * against the filter instance numbers. If the message instance number is
	 * contained in the current kinematic instance numbers or isn't contained in
	 * any other kinematic instance numbers the message is valid.
	 * 
	 * @param messageClass
	 *            message class
	 * @param componentNr
	 *            message component number
	 * @param messageNr
	 *            message number
	 * @param instanceNr
	 *            message instance number
	 * @return returns true when the message is valid
	 */
	public static synchronized boolean isMessageValid(int messageClass, int componentNr, int messageNr, int instanceNr) {
		boolean valid = true;
		// if (componentNr < 1000) {
		// is a application message
		if (applicationFilter != null) {
			try {
				valid = applicationFilter.isMessageValid(kinematicNr, messageClass, componentNr, messageNr, instanceNr);
			} catch (Exception e) {
				// KvtLogger.error(KvtMessageFilterAdministrator.class,
				// "message filter error", e);
				Log.e("KvtMEssageFilterAdministrator", "message filter error " + e);
			}
		}
		// return true;
		// }
		if ((componentNumbers == null) || (instanceNumbers == null) || (kinematicNr < 0) || (instanceNumbers.length <= kinematicNr)) {
			return valid;
		}
		if (contains(componentNr, componentNumbers)) {
			if (contains(instanceNr, (int[]) instanceNumbers[kinematicNr])) {
				valid = true;
				// return true;
			}
			for (int i = 0; i < instanceNumbers.length; i++) {
				if (i != kinematicNr) {
					if (contains(instanceNr, (int[]) instanceNumbers[i])) {
						// return false;
						valid = false;
						break;
					}
				}
			}
		}
		return valid;
	}

	public static void addCoordinateListener(KvtMessageFilterListener listener) {
		if (!m_listeners.contains(listener)) {
			m_listeners.addElement(listener);
		}
	}

	public static void removeCoordinateListener(KvtMessageFilterListener listener) {
		m_listeners.removeElement(listener);
	}

	private static void fireFilterChanged() {
		for (int i = 0; i < m_listeners.size(); i++) {
			((KvtMessageFilterListener) m_listeners.elementAt(i)).messageFilterChanged();
		}
	}

	private synchronized static void clear() {
		if (generalVarGroup != null) {
			generalVarGroup.release();
		}
		changeCnt = 0;
		kinematicNr = -1;
		kinematic = null;
		changeCounter = null;
		componentNumbers = null;
		instanceNumbers = null;
	}

	private static void createGeneralVariablen() {
		KTcDfl d = dfl;
		if ((d != null) && (generalVarGroup != null)) {
			generalVarGroup.deactivate();

			kinematic = d.variable.createKStructVarWrapper("_system.gRcData.selectedRobot");
			if (kinematic != null) {
				generalVarGroup.add(kinematic);
			}
			changeCounter = d.variable.createKStructVarWrapper("_system.gRcData.messageFilterData.changeCnt");
			if (changeCounter != null) {
				generalVarGroup.add(changeCounter);
			}
			generalVarGroup.activate();
		}

	}

	private static void kinematicChanged() {
		int kinNr = -1;
		KStructVarWrapper kin = kinematic;
		if (kin != null) {
			Object value = kin.getActualValue();
			if (value instanceof Integer) {
				kinNr = ((Integer) value).intValue();
			}
		}
		synchronized (KvtMessageFilterAdministrator.class) {
			kinematicNr = kinNr;
		}
	}

	private static void rebuildFilter() {
		KTcDfl d = dfl;
		if (d != null) {
			// read all component numbers
			KStructVarWrapper cmpNrVar = d.variable.createKStructVarWrapper("_system.gRcData.messageFilterData.nrOfComponents");
			Object value = (cmpNrVar != null) ? cmpNrVar.readActualValue(null) : null;
			int cmpNr = (value instanceof Integer) ? ((Integer) value).intValue() : 0;
			int[] comptNumbers = new int[cmpNr];
			for (int i = 0; i < cmpNr; i++) {
				KStructVarWrapper cmpVar = d.variable.createKStructVarWrapper("_system.gRcData.messageFilterData.componentNr[" + i + "]");
				value = (cmpVar != null) ? cmpVar.readActualValue(null) : null;
				comptNumbers[i] = (value instanceof Integer) ? ((Integer) value).intValue() : 0;
			}
			sort(comptNumbers);
			KStructVarWrapper kinNrVar = d.variable.createKStructVarWrapper("_system.gRcData.nrOfRobots");
			value = (kinNrVar != null) ? kinNrVar.readActualValue(null) : null;
			int kinNr = (value instanceof Integer) ? ((Integer) value).intValue() : 0;
			Object[] instNumbers = new Object[kinNr];
			for (int i = 0; i < kinNr; i++) {
				KStructVarWrapper nrVar = d.variable.createKStructVarWrapper("_system.gRcData.messageFilterData.kinInstanceNumbers[" + i
						+ "].nrOfInstanceNrs");
				value = (nrVar != null) ? nrVar.readActualValue(null) : null;
				int nr = (value instanceof Integer) ? ((Integer) value).intValue() : 0;
				instNumbers[i] = new int[nr];
				// read instance numbers
				String prefix = "_system.gRcData.messageFilterData.kinInstanceNumbers[" + i + "].instanceNumbers[";
				for (int j = 0; j < nr; j++) {
					KStructVarWrapper iNrVar = d.variable.createKStructVarWrapper(prefix + j + "]");
					value = (iNrVar != null) ? iNrVar.readActualValue(null) : null;
					int iNr = (value instanceof Integer) ? ((Integer) value).intValue() : 0;
					((int[]) instNumbers[i])[j] = iNr;
				}
				sort(((int[]) instNumbers[i]));
			}
			synchronized (KvtMessageFilterAdministrator.class) {
				componentNumbers = comptNumbers;
				instanceNumbers = instNumbers;
			}
		}
	}

	private static boolean contains(int nr, int[] sortedNumbers) {
		int beg = 0;
		int end = sortedNumbers.length - 1;
		while (beg < end) {
			if ((sortedNumbers[beg] <= nr) && (nr <= sortedNumbers[end])) {
				int middle = beg + (end - beg) / 2;
				if (nr <= sortedNumbers[middle]) {
					end = middle;
				} else {
					beg = middle + 1;
				}
			} else {
				return false;
			}
		}
		return (beg < sortedNumbers.length) && (sortedNumbers[beg] == nr);
	}

	private static void sort(int[] numbers) {
		int r;
		int gap;
		int counter;
		int pointer;
		boolean greater;
		int temp;
		r = numbers.length;
		gap = r / 2;
		while (gap > 0) {
			for (counter = gap; counter < r; counter++) {
				pointer = counter - gap;
				while (pointer >= 0) {
					greater = numbers[pointer] > numbers[gap + pointer];
					if (greater) {
						temp = numbers[pointer];
						numbers[pointer] = numbers[gap + pointer];
						numbers[gap + pointer] = temp;
						pointer = pointer - gap;
					} else {
						pointer = -1;
					}
				}
			}
			gap = gap / 2;
		}
	}
}
