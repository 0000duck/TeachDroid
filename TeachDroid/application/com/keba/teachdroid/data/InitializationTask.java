/**
 * 
 */
package com.keba.teachdroid.data;

import android.os.AsyncTask;

import com.keba.kemro.kvs.teach.data.program.KvtStatementAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMultiKinematikAdministrator;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.serviceclient.alarm.KMessageService;

public class InitializationTask extends AsyncTask<String, Integer, Boolean> {

	/**
	 * 
	 */
	private final InitializationListener	m_listener;

	/**
	 * @param _mainTeachView
	 */
	public InitializationTask(InitializationListener _listener) {
		m_listener = _listener;
	}

	@Override
	protected Boolean doInBackground(String... _params) {
		final String host = _params[0];

		KvtProjectAdministrator.init();
		KvtStatementAdministrator.init();
		KvtMultiKinematikAdministrator.init();
		KvtMotionModeAdministrator.init();
		KvtMainModeAdministrator.init();
		KvtPositionMonitor.init();
		KvtDriveStateMonitor.init();
		KvtProgramStateMonitor.init();
		KMessageService.connect(host, 5000);

		RobotControlProxy.startup();

		for (int i = 1; i < 10; ++i) {
			try {
				// if (i % 10 == 0) {
				publishProgress(i);
				// }
				boolean isConnected = KvtSystemCommunicator.connectOnce(host, 5000, "_global");

				if (isConnected) {
					i = 100;
				}

				Thread.sleep(100);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return KvtSystemCommunicator.isConnected();
	}

	@Override
	protected void onCancelled(Boolean result) {
		super.onCancelled(result);
	}

	@Override
	protected void onPostExecute(final Boolean result) {

		super.onPostExecute(result);
		m_listener.initializationComplete(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		m_listener.initializationBegin();
	}

	@Override
	protected void onProgressUpdate(final Integer... values) {
		super.onProgressUpdate(values);
		// runOnUiThread(new Runnable() {
		// public void run() {
		// m_mainTeachView.m_dlg.setMessage(MessageFormat.format(MainTeachView.m_connectFormatString,
		// values[0]));
		m_listener.setInitializationProgress(values[0]);
		// }
		// });

	}

	public void progress(int _p) {
		publishProgress(_p);
	}

	public static interface InitializationListener {

		public void initializationBegin();

		public void setInitializationProgress(int _progress);

		public void initializationComplete(boolean _success);
	};
}