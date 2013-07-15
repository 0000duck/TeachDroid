package com.keba.teachdroid.app;

import java.text.MessageFormat;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.teachdroid.app.projectview.ProjectListActivity;
import com.keba.teachdroid.data.InitializationTask;
import com.keba.teachdroid.data.InitializationTask.InitializationListener;
import com.keba.teachdroid.data.RobotControlProxy;
import com.keba.teachdroid.util.PreferenceManager;

public class MainActivity extends Activity implements InitializationListener, KvtDriveStateListener, KvtPositionMonitorListener {

	// private String m_host = "10.0.0.5";s
	final String				m_connectFormatString	= "Connecting... ";
	protected ProgressDialog	m_dlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// add listeners for rc data
		KvtPositionMonitor.addListener(this);
		KvtDriveStateMonitor.addListener(this);
		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {
			
			public void teachviewDisconnected() {
				runOnUiThread(new Runnable() {

					public void run() {
						ImageView v = (ImageView) findViewById(R.id.connStateIcon);
						if (v != null) {
							Resources res = getResources();
							// v.setImageResource(R.drawable.conn);
							v.setImageDrawable(res.getDrawable(R.drawable.disconn));
							v.invalidate();
						}
					}
				});

			}
			
			public void teachviewConnected() {
				runOnUiThread(new Runnable() {

					public void run() {
						ImageView v = (ImageView) findViewById(R.id.connStateIcon);
						if (v != null) {
							Resources res = getResources();
							v.setImageDrawable(res.getDrawable(R.drawable.conn));
							// v.setImageResource(R.drawable.disconn);
							v.invalidate();
						}
					}
				});
			}
		});
		setContentView(R.layout.activity_main);

		// setup override seekbar
		SeekBar s = (SeekBar) findViewById(R.id.overrideBar);
		s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar _seekBar) {
				int progress = _seekBar.getProgress();
				if (progress >= 0 && progress <= 100)
					RobotControlProxy.setOverride(progress);
			}

			public void onStartTrackingTouch(SeekBar _seekBar) {
			}

			public void onProgressChanged(SeekBar _seekBar, int _progress, boolean _fromUser) {
				TextView t = (TextView) findViewById(R.id.overrideLabel);
				t.setText("Override " + _progress + "%");
			}
		});

		
		// drives power switch
		Switch sw = (Switch) findViewById(R.id.switch1);
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
				// if (RobotControlProxy.isConnected()) {
				// RobotControlProxy.toggleDrivesPower();
				// }
			}
		});
		sw.setClickable(false);

		checkWifiAndConnect();

	}

	/**
	 * 
	 */
	private void checkWifiAndConnect() {
		if (!RobotControlProxy.isConnected()) {
			final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (!wifi.isWifiEnabled()) {

				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

				dlgAlert.setMessage("Click OK to enable WiFi, Cancel to dismiss!");

				dlgAlert.setTitle("Info");
				dlgAlert.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface _dialog, int _which) {
						wifi.setWifiEnabled(true);
						connectToPlc();
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

			} else
				connectToPlc();
		}
	}

	/**
	 * 
	 */
	private void connectToPlc() {
		// always read host from preferences, just in case someone has modified
		// it in the meantime
		String host = PreferenceManager.getInstance().getHostname();
		InitializationTask itask = new InitializationTask(this);
		itask.execute(host);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem _item) {

		if (_item.getTitle().equals(getString(R.string.menu_settings))) {
			Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
			startActivity(settingsActivity);

		} else if (_item.getTitle().equals(getString(R.string.action_connect))) {
			if (!RobotControlProxy.isConnected()) {
				checkWifiAndConnect();
			} else {
				disconnectFromPLC();
			}
		}
		return super.onOptionsItemSelected(_item);
	}

	/**
	 * 
	 */
	private void disconnectFromPLC() {
		KvtSystemCommunicator.disconnect();
	}

	public void onConnStateClick(View _v) {
		if (RobotControlProxy.isConnected()) {
			disconnectFromPLC();
		} else
			checkWifiAndConnect();
	}

	public void onPowerToggle(View _v) {
		RobotControlProxy.toggleDrivesPower();
	}

	public void onShowProjects(View _v) {
		Intent projectsActivity = new Intent(this, ProjectListActivity.class);
		// projectsActivity.putExtra("projects",
		// RobotControlProxy.getProjects());
		startActivity(projectsActivity);
	}

	public void onGenericButtonClick(View _view) {
		RobotControlProxy.setRefsysName("foo");
		List rs = KvtPositionMonitor.getAvailableRefsys();
		List ts = KvtPositionMonitor.getAvailableTools();

		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ts);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner toolSel = (Spinner) findViewById(R.id.toolSelectionCB);
		toolSel.setAdapter(adapter);

		ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, rs);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner refsyssel = (Spinner) findViewById(R.id.refsysSelectionCB);
		refsyssel.setAdapter(adapter2);
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
				m_dlg = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_SPINNER);
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

				m_dlg.dismiss();
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
					m_dlg.setTitle(MessageFormat.format(m_connectFormatString
 + "{0}"/*
																					 * %
																					 * complete
																					 * "
																					 */, _progress));

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #drivePowerChanged(boolean)
	 */
	public void drivePowerChanged(final boolean _hasPower) {
		// power state
		runOnUiThread(new Runnable() {

			public void run() {
				Switch sw = (Switch) findViewById(R.id.switch1);
				sw.setChecked(_hasPower);

				ImageView iv = (ImageView) findViewById(R.id.powerStateIcon);
				if (iv != null) {
					Resources res = getResources();
					iv.setImageDrawable(_hasPower ? res.getDrawable(R.drawable.power_on) : res.getDrawable(R.drawable.power_off));
					iv.invalidate();
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReadyChanged(java.lang.Boolean)
	 */
	public void driveIsReadyChanged(Boolean _isReady) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReferencedChanged(java.lang.Boolean)
	 */
	public void driveIsReferencedChanged(Boolean _isRef) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #cartesianPositionChanged(java.lang.String, java.lang.Number)
	 */
	public void cartesianPositionChanged(String _compName, Number _value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #axisPositionChanged(int, java.lang.Number, java.lang.String)
	 */
	public void axisPositionChanged(int _axisNo, Number _value, String _axisName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #overrideChanged(java.lang.Number)
	 */
	public void overrideChanged(final Number _override) {
		runOnUiThread(new Runnable() {

			public void run() {
				// set override label'S text
				TextView t = (TextView) findViewById(R.id.overrideLabel);
				t.setText("Override " + _override + "%");
				((SeekBar) findViewById(R.id.overrideBar)).setProgress(_override.intValue());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #pathVelocityChanged(float)
	 */
	public void pathVelocityChanged(float _velocityMms) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #chosenRefSysChanged(java.lang.String)
	 */
	public void chosenRefSysChanged(String _refsysName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #chosenToolChanged(java.lang.String)
	 */
	public void chosenToolChanged(String _toolName) {
	}
}
