/**
 * 
 */
package com.keba.kemro.kvs.teach.data.program;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;

/**
 * @author ltz
 * 
 */
public class ProgramStarter implements Runnable {
	private static final int	LOADTIMEOUT	= 1000;
	private String				mProgName;
	private String				mProjName;

	/**
	 * create new runnable for starting a program
	 */
	public ProgramStarter(KvtProgram prog) {
		mProgName = prog.toString();
		mProjName = prog.getParent().toString();
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int remainingTime = LOADTIMEOUT;
		KvtProject project = null;
		project = KvtProjectAdministrator.getProject(mProjName);
		while ((0 < remainingTime) && (project != null) && (project.getProjectState() < KvtProject.SUCCESSFULLY_LOADED)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// ignore
			}
			remainingTime -= 100;
			project = KvtProjectAdministrator.getProject(mProjName);
		}
		KTcDfl d = KvtSystemCommunicator.getTcDfl();
		if (d != null) {
			synchronized (d.getLockObject()) {
				project = KvtProjectAdministrator.getProject(mProjName);
				if ((project != null) && (project.getProjectState() == KvtProject.SUCCESSFULLY_LOADED)) {
					KvtProgram prog = project.getProgram(mProgName);
					// KvtProjectAdministrator.startProgram(prog);

					// String varname =
					// "_system.RcHtControl.simuKeys.progStart";
					String varname = "_system.RcHtControl.uiKeys.progStart";
					KStructVarWrapper wrp = d.variable.createKStructVarWrapper(varname);
					if (wrp != null) {
						wrp.setActualValue(Boolean.TRUE); // set
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// do nothing
						}
						wrp.setActualValue(Boolean.FALSE); // reset
					}
				}
			}
		} else
			Log.w("ProgramStarter", "Could not start " + mProjName + "." + mProgName + ", DFL was null!");
	}
}
