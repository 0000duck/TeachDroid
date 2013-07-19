/**
 * 
 */
package com.keba.teachdroid.app.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;

/**
 * @author ltz
 * 
 */
public class OverviewFragment extends Fragment implements KvtDriveStateListener, KvtPositionMonitorListener {

	private View	mRootView;
	private boolean	mConnected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_overview, container, false);

		// add listeners
		KvtDriveStateMonitor.addListener(this);
		KvtPositionMonitor.addListener(this);
		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {

			public void teachviewDisconnected() {
				getActivity().runOnUiThread(new Runnable() {

					public void run() {
						ImageView v = (ImageView) mRootView.findViewById(R.id.connStateIcon);
						if (v != null) {
							Resources res = getResources();
							// v.setImageResource(R.drawable.conn);
							v.setImageDrawable(res.getDrawable(R.drawable.disconn));
							v.invalidate();
						}
						mConnected = false;
					}
				});

			}

			public void teachviewConnected() {
				getActivity().runOnUiThread(new Runnable() {

					public void run() {
						ImageView v = (ImageView) mRootView.findViewById(R.id.connStateIcon);
						if (v != null) {
							Resources res = getResources();
							v.setImageDrawable(res.getDrawable(R.drawable.conn));
							// v.setImageResource(R.drawable.disconn);
							v.invalidate();
						}
						mConnected = false;
					}
				});
			}
		});
		// setup override seekbar
		SeekBar s = (SeekBar) mRootView.findViewById(R.id.overrideBar);
		s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar _seekBar) {
				int progress = _seekBar.getProgress();
				if (progress >= 0 && progress <= 100 && RobotControlProxy.isConnected())
					RobotControlProxy.setOverride(progress);
			}

			public void onStartTrackingTouch(SeekBar _seekBar) {
			}

			public void onProgressChanged(SeekBar _seekBar, int _progress, boolean _fromUser) {
				TextView t = (TextView) mRootView.findViewById(R.id.overrideLabel);
				t.setText("Override " + _progress + "%");
			}
		});

		// drives power switch
		Switch sw = (Switch) mRootView.findViewById(R.id.switch1);
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
				// if (RobotControlProxy.isConnected()) {
				// RobotControlProxy.toggleDrivesPower();
				// }
			}
		});
		sw.setClickable(false);

		// power button
		Button b = (Button) mRootView.findViewById(R.id.button2);
		if (b != null)
			b.setOnClickListener(new OnClickListener() {

				public void onClick(View _v) {
					RobotControlProxy.toggleDrivesPower();
				}
			});

		ImageView v = (ImageView) mRootView.findViewById(R.id.connStateIcon);
		if (v != null) {
			Resources res = getResources();
			int id = mConnected ? R.drawable.conn : R.drawable.disconn;
			v.setImageDrawable(res.getDrawable(id));
			v.invalidate();
		}

		return mRootView;
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
		getActivity().runOnUiThread(new Runnable() {

			public void run() {
				Switch sw = (Switch) mRootView.findViewById(R.id.switch1);
				sw.setChecked(_hasPower);

				ImageView iv = (ImageView) mRootView.findViewById(R.id.powerStateIcon);
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
	 * #pathVelocityChanged(float)
	 */
	public void pathVelocityChanged(float _velocityMms) {
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
		getActivity().runOnUiThread(new Runnable() {

			public void run() {
				// set override label'S text
				TextView t = (TextView) mRootView.findViewById(R.id.overrideLabel);
				t.setText("Override " + _override + "%");
				((SeekBar) mRootView.findViewById(R.id.overrideBar)).setProgress(_override.intValue());
			}
		});
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
