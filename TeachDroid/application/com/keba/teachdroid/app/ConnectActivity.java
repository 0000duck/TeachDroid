package com.keba.teachdroid.app;

import java.io.Serializable;
import java.text.MessageFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.teachdroid.data.InitializationTask;
import com.keba.teachdroid.data.InitializationTask.InitializationListener;
import com.keba.teachdroid.data.RobotControlProxy;
import com.keba.teachdroid.util.PreferenceManager;

public class ConnectActivity extends Activity implements InitializationListener, IConnectCallback, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3440662095768050268L;
	private long mStartTime;
	protected volatile ProgressDialog m_dlg;
	final String m_connectFormatString = "Connecting... ";
	private boolean mConnected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (RobotControlProxy.isConnected() /*|| trueDEBUG*/) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		setContentView(R.layout.activity_connect);

		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {

			public void teachviewDisconnected() {
				runOnUiThread(new Runnable() {

					public void run() {
						Button connect = (Button) findViewById(R.id.connectButton);
						connect.setText(getString(R.string.action_connect));
						connect.invalidate();
						mConnected = false;
					}
				});

			}

			public void teachviewConnected() {
				runOnUiThread(new Runnable() {

					public void run() {
						Button connect = (Button) findViewById(R.id.connectButton);
						connect.setText(getString(R.string.action_disconnect));
						connect.invalidate();
						mConnected = true;
					}
				});

			}
		});

		// set ip address
		EditText ip = (EditText) findViewById(R.id.ipAddress);
		ip.setText(PreferenceManager.getInstance().getHostname());

		// add button callback
		Button connect = (Button) findViewById(R.id.connectButton);
		connect.setOnClickListener(new OnClickListener() {

			public void onClick(View _v) {
				// connect

				if (RobotControlProxy.isConnected())
					disconnect();
				else {
					String ip = ((EditText) findViewById(R.id.ipAddress)).getText().toString();
					PreferenceManager.getInstance().setHostname(ip);
					connect(ip);
				}

				// switch to next section
			}
		});
		int id = mConnected ? R.string.action_disconnect : R.string.action_connect;
		connect.setText(getString(id));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
			startActivity(settingsActivity);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * @param _host
	 * 
	 */
	private void checkWifiAndConnect(final String _host) {
		if (!RobotControlProxy.isConnected()) {
			final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (!wifi.isWifiEnabled()) {

				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

				dlgAlert.setMessage("Click OK to enable WiFi, Cancel to dismiss!");

				dlgAlert.setTitle("Info");
				dlgAlert.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface _dialog, int _which) {
						wifi.setWifiEnabled(true);
						connectToPlc(_host);
					}
				});
				dlgAlert.setNegativeButton("Cancel", /*
													 * new android.content.
													 * DialogInterface
													 * .OnClickListener() {
													 * 
													 * public void
													 * onClick(DialogInterface
													 * _dialog, int _which) {
													 * finish(); } }
													 */null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();

			} else {
				connectToPlc(_host);
			}
		}
	}

	/**
	 * @param _host
	 * 
	 */
	private void connectToPlc(String _host) {
		// always read host from preferences, just in case someone has modified
		// it in the meantime
		// String host = PreferenceManager.getInstance().getHostname();
		mStartTime = System.currentTimeMillis();
		InitializationTask itask = new InitializationTask(this);
		itask.execute(_host);

	}

	private void disconnectFromPLC() {
		KvtSystemCommunicator.disconnect();
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

				// m_dlg = ProgressDialog.show(MainActivity.this,
				// "Connecting...", "Connecting to " + m_host, true, true);
				m_dlg = new ProgressDialog(ConnectActivity.this, ProgressDialog.STYLE_SPINNER);
				m_dlg.setTitle("Connecting...");
				// m_dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

				m_dlg.setCancelable(true);
				m_dlg.setCanceledOnTouchOutside(false);
				m_dlg.setMessage("Connecting to " + PreferenceManager.getInstance().getHostname());

				m_dlg.setIndeterminate(true);
				m_dlg.show();

				// m_dlg.setMessage("Begin initialization...");

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
				long duration = System.currentTimeMillis() - mStartTime;
				Toast.makeText(getBaseContext(),"Connecting took me " + duration / 1000 + " seconds", Toast.LENGTH_SHORT).show();
				m_dlg.dismiss();
				Toast.makeText(getBaseContext(), "Connection established", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
				startActivity(intent);
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
	public void setInitializationProgress(final Object _progress) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (_progress instanceof Integer)
					m_dlg.setProgress((Integer) _progress);
				else
					m_dlg.setTitle(MessageFormat.format(m_connectFormatString + "{0}"/*
																					 * %
																					 * complete
																					 * "
																					 */, _progress));

			}
		});
	}

	public void connect(String _host) {
		checkWifiAndConnect(_host);

	}

	public void disconnect() {
		disconnectFromPLC();

	}
}
