/**
 * 
 */
package com.keba.teachdroid.data;

import android.os.AsyncTask;

import com.keba.kemro.kvs.teach.data.program.KvtStatementAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMultiKinematikAdministrator;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.serviceclient.alarm.KMessageService;

/**
 * A task which is responsible for initializing all data-loading classes to the
 * PLC. Since this task class derives from {@link AsyncTask}, it runs in a
 * separate thread. This is necessary because in Android, network access on the
 * main thread is not allowed.
 * 
 * Pass the hostname/IP address as parameter, get an integer as progress and
 * receive a boolean as return value!
 * 
 * Use a {@link InitializationListener} if you need to be notified about
 * Initialization start, progress and end
 * 
 * @author ltz
 * 
 */
public class InitializationTask extends AsyncTask<String, Object, Boolean> {

	private final InitializationListener	mListener;

	/**
	 * @param _listener
	 */
	public InitializationTask(InitializationListener _listener) {
		mListener = _listener;
	}

	public InitializationTask() {
		mListener = null;
	}

	@Override
	protected Boolean doInBackground(String... _params) {
		final String host = _params[0];

		KvtProjectAdministrator.init();

		publishProgress("adminstrators");
		KvtStatementAdministrator.init();
		KvtMultiKinematikAdministrator.init();
		KvtMotionModeAdministrator.init();
		KvtMainModeAdministrator.init();
		KvtPositionMonitor.init();
		KvtDriveStateMonitor.init();
		KvtProgramStateMonitor.init();
		KvtExecutionMonitor.init();
		publishProgress("message service");
		KMessageService.connect(host, 5000);
		RobotControlProxy.startup();

		// fake progress bar :-)
		// new Thread(new Runnable() {
		//
		// public void run() {
		//
		// for (int i = 20; i < 99; i += 5) {
		// int sleepTime = (int) (new Random().nextDouble() * 900);
		// try {
		// Thread.sleep(sleepTime);
		// publishProgress(i);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		//
		// }).start();

		publishProgress("teachcontrol");
		KvtSystemCommunicator.connectOnce(host, 5000, "_global");
		publishProgress(100);

		return KvtSystemCommunicator.isConnected();
	}

	@Override
	protected void onCancelled(Boolean result) {
		super.onCancelled(result);
	}

	@Override
	protected void onPostExecute(final Boolean result) {

		super.onPostExecute(result);
		if (mListener != null)
			mListener.initializationComplete(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mListener != null)
			mListener.initializationBegin();
	}

	@Override
	protected void onProgressUpdate(final Object... values) {
		super.onProgressUpdate(values);
		// runOnUiThread(new Runnable() {
		// public void run() {
		// m_mainTeachView.m_dlg.setMessage(MessageFormat.format(MainTeachView.m_connectFormatString,
		// values[0]));
		if (mListener != null)
			mListener.setInitializationProgress(values[0]);
		// }
		// });

	}

	/**
	 * Publishes the progress to the AsyncTask
	 * 
	 * @param _p
	 *            an integer indicating progress.
	 */
	public void progress(int _p) {
		publishProgress(_p);
	}

	/**
	 * Listener class which can be used to acknowledge the initialization
	 * progress of the application.
	 * 
	 * @author ltz
	 * 
	 */
	public static interface InitializationListener {

		/**
		 * The initialization of the connect-process is about to begin. This
		 * method is called <em>before</em> the initialization begins.
		 */
		public void initializationBegin();

		/**
		 * Called whenever the initialization thread needs to publish a progress
		 * update, e.g. for updating a progress bar
		 * 
		 * @param _progress
		 */
		public void setInitializationProgress(Object _progress);

		/**
		 * Called after the initialization task has completed.
		 * 
		 * @param _success
		 *            Indicates whether a connection to the robot controller was
		 *            established or not
		 */
		public void initializationComplete(boolean _success);
	};
}