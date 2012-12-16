/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Auftragsnr: 5500395
 *    Erstautor : tur
 *    Datum     : 01.04.2003
 *--------------------------------------------------------------------------
 *      Revision:
 *        Author:
 *          Date:
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.value;

import java.util.*;

//import com.keba.kemro.kvs.teach.framework.util.KvtLogger;
//import com.keba.kemro.kvs.teach.util.KvtErrMsgAdministrator;
import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.util.*;
import com.keba.kemro.teach.network.*;

/**
 * Der Variablen-Server ist ein Singelton und verwaltet die Variablen in Form
 * von Variablengruppen. Er aktualisiert zyklisch die Variablenwerte in den
 * einzelnen Variablengruppen und steuert den dazu verwendeten Thread.
 */
public class KVariableServer implements KVariableGroupListener {
	private static final int			LOW_PRIORITY		= Thread.NORM_PRIORITY - 1;
	private static final int			HIGH_PRIORITY		= Thread.NORM_PRIORITY + 1;
	private static final int			SLEEP_TIME			= 200;
	private static final int			SLEEP_MIN_TIME		= 30;
	// private static final Integer ACTIVATE = new Integer(0);
	// private static final Integer CHECK_VALUES = new Integer(1);
	// private static final Integer DEACTIVATE = new Integer(2);
	private long						nextPollTime;
	private PollThread					pollThread;
	private final KvtVariableRegistry	variableRegistry;
	Vector								updGroupsStatistics	= new Vector();

	private final Object				varAccessLock		= new Object();			
	// TcVarAnalyzer va;

	KTcDfl								dfl;

	protected KVariableServer(KTcDfl dfl) {
		this.dfl = dfl;
		variableRegistry = new KvtVariableRegistry(this, dfl);
		// va = new TcVarAnalyzer();
		// KvSplashScreen.setText("Starting Pollthread");
		pollThread = new PollThread();
		pollThread.setPriority(LOW_PRIORITY);
		pollThread.start();

		dfl.client.addConnectionListener(new TcConnectionListener() {
			public void connectionStateChanged(boolean isConnected) {
				if (isConnected) {
				} else {
					pollThread.stopPoll();
				}
			}
		});
	}

	protected void stop() {
		pollThread.stopPoll();
	}

	/**
	 * Aktiviert eine Variablengruppe beim Variablen-Server
	 * 
	 * @param _group
	 *            Variablengruppe
	 */
	void activateGroup(KVariableGroup _group) {

		variableRegistry.addGroup(_group);
		// setDirty(true);
		pollThread.interrupt();
	}

	/**
	 * Deaktiviert eine Variablengruppe
	 * 
	 * @param _group
	 *            Variablengruppe
	 */
	void deactivateGroup(KVariableGroup _group) {
		synchronized (varAccessLock) {
			variableRegistry.removeGroup(_group);
			// setDirty(true);
		}
		pollThread.interrupt();
	}

	/**
	 * Ist die übergebene Variablengruppe aktiviert, wird der Abfrage-Thread
	 * unmittelbar aufgeweckt und die Gruppe aktualisiert.
	 * 
	 * @param group
	 *            Variablengruppe
	 */
	void checkGroup(KVariableGroup group) {
		// do nothing
		// setDirty(true);
		pollThread.interrupt();
	}

	private class PollThread extends Thread {
		private boolean	m_bDoPoll	= true;
		private long	sleep;
		private long	t0;
		private long	difference;
		private boolean	doIt		= true;

		private PollThread() {
			super("KVariableServer.PollThread");

		}

		/**
		 * Main processing method for the PollThread object
		 */
		public void run() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException ie) {

			}
			while (m_bDoPoll) {
				try {
					t0 = System.currentTimeMillis();
					long t1 = System.currentTimeMillis();

					if (nextPollTime <= t1) {
						try {
							// synchronized (varAccessLock) {
							if (variableRegistry.isDirty()) {
								variableRegistry.refresh();
								// setDirty(false);

							}
							variableRegistry.update();
							setPriority(HIGH_PRIORITY);
							variableRegistry.notifyChanges();
							setPriority(LOW_PRIORITY);
							// va.setNewSample(variableRegistry);
							// }

						} catch (Exception e) {
							e.printStackTrace();
							setPriority(LOW_PRIORITY);
							KDflLogger.error(this, "KVariableServer.PollThread: update values", e);
						}

						difference = System.currentTimeMillis() - t0;

						if (SLEEP_TIME < difference) {
							sleep = SLEEP_MIN_TIME;
						} else if ((SLEEP_TIME - difference) < SLEEP_MIN_TIME) {
							sleep = SLEEP_MIN_TIME;
						} else {
							sleep = SLEEP_TIME - difference;
						}
					} else {
						sleep = nextPollTime - t1;
					}

					// sleep = SLEEP_MIN_TIME;

					nextPollTime = System.currentTimeMillis() + sleep;
					Thread.sleep(sleep);
				} catch (InterruptedException ie) {
				}
			}
		}

		/**
		 * stops poll
		 */
		public void stopPoll() {
			m_bDoPoll = false;
		}
	}

	/**
	 * @return
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.value.KVariableGroupListener#changed(com.keba
	 * .kemro.teach.dfl.value.KStructVarWrapper)
	 */
	public void changed(KStructVarWrapper variable) {
		if (variableRegistry.isRegistered(variable)) {
			dispatch(variable);
		} else {
//			System.out.println("UNREGISTERED: " + variable.getRootPathString() + " -> " + variable.getActualValue());
		}

	}

	/**
	 * @param variable
	 */
	private void dispatch(KStructVarWrapper variable) {
		// System.out.println("dispatching "+variable+" with value "+variable.getActualValue());
		Vector groups = new Vector();
		synchronized (varAccessLock) {
			if (variableRegistry == null || variableRegistry.size() <= 0) {
				return;
			}

			// tell each listener that "their" variable has changed
			groups = variableRegistry.getGroupsForVar(variable);
		}
		if(groups == null) return;
		for (int i = 0; i < groups.size(); i++) {
			KVariableGroup g = (KVariableGroup) groups.elementAt(i);
			if (g != null) {
				if (g.m_listener == null) {
					System.err.println("CAUTION: variable " + variable + " has no actual listener!!");
//					KvtLogger.error(getClass(), "CAUTION: variable " + variable + " has no actual listener!!");
				} else
					g.m_listener.changed(variable);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.value.KVariableGroupListener#allActualValuesUpdated
	 * ()
	 */
	public void allActualValuesUpdated() {
		Enumeration l = variableRegistry.getAllListeners();
		while (l.hasMoreElements()) {
			KVariableGroupListener elem = ((KVariableGroupListener) (l.nextElement()));
			if (elem != null)
				elem.allActualValuesUpdated();
		}

	}
}
