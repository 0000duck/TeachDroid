package com.keba.teachdroid.app;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;

import com.keba.teachdroid.data.InitializationTask;
import com.keba.teachdroid.data.InitializationTask.InitializationListener;

public class MainActivity extends Activity implements InitializationListener {

	private String					m_host					= "10.0.0.5";
	final String					m_connectFormatString	= "Connecting attempt {0}";
	protected final ProgressDialog	m_dlg					= ProgressDialog.show(this, "Connecting...", "Connecting to " + m_host, true, true);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// show the progress dialog
		InitializationTask con = new InitializationTask(this);
		try {
			Boolean result = con.execute(m_host).get();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationBegin()
	 */
	public void initializationBegin() {

		runOnUiThread(new Runnable() {

			public void run() {
				m_dlg.show();
				m_dlg.setMessage("Begin initialization...");

			}

		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationComplete()
	 */
	public void initializationComplete(final boolean _success) {

		runOnUiThread(new Runnable() {
			public void run() {
				// m_dlg.dismiss();

				String msg = "Connection " + (_success ? "established" : "failed!!") + "\nPress back button to dismiss dialog.";

				m_dlg.setMessage(msg);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				m_dlg.cancel();

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #setInitializationProgress(int)
	 */
	public void setInitializationProgress(final int _progress) {
		runOnUiThread(new Runnable() {
			public void run() {
				m_dlg.setMessage(MessageFormat.format(m_connectFormatString, _progress));
			}
		});
	}

}
