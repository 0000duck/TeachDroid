package com.keba.teachdroid.app.fragments;

import java.util.concurrent.ExecutionException;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.KvtMainModeListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.SafetyState;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtOverrideChangedListener;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.R;

public class RobotStateFragment extends Fragment implements KvtMainModeListener, KvtOverrideChangedListener, KvtPositionMonitorListener,
		KvtDriveStateListener {
	private transient View	mRootView;

	public RobotStateFragment() {
		KvtPositionMonitor.addListener((KvtOverrideChangedListener) this);
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
		KvtDriveStateMonitor.addListener(this);
		KvtMainModeAdministrator.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_state, container, false);

		try {
			new AsyncTask<Void, Void, Void>() {
				@Override
				public Void doInBackground(Void... _params) {
					mainModeChanged(KvtMainModeAdministrator.getMainMode());
					drivePowerChanged(KvtDriveStateMonitor.getDrivesPower());
					overrideChanged(KvtPositionMonitor.getOverride());
					selectedRefSysChanged(KvtPositionMonitor.getChosenRefSys());
					selectedToolChanged(KvtPositionMonitor.getChosenTool());
					jogRefsysChanged(KvtPositionMonitor.getJogRefSys());
					jogToolChanged(KvtPositionMonitor.getJogTool());
					return null;
				}
			}.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		mRootView.findViewById(R.id.powerIndicator).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View _v) {
				toggleDrivesPower();
			}
		});

		return mRootView;
	}

	@Override
	public void overrideChanged(Number _override) {
		final Number ovr = _override;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((TextView) mRootView.findViewById(R.id.override)).setText("Override: " + ovr + "%");
					((ProgressBar) mRootView.findViewById(R.id.overrideProgress)).setProgress(ovr.intValue());
				}
			});
		}
	}

	@Override
	public void mainModeChanged(int _newMainMode) {
		final int mainMode = _newMainMode;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String mainModeString = null;
					int drawableId = 0;
					switch (mainMode) {
					case 1:
						mainModeString = "A";
						drawableId = com.keba.teachdroid.app.R.drawable.ic_opmode_auto;
						break;
					case 2:
						mainModeString = "T1";
						drawableId = com.keba.teachdroid.app.R.drawable.ic_opmode_manual;
						break;
					case 4:
						mainModeString = "AE";
						drawableId = com.keba.teachdroid.app.R.drawable.ic_opmode_auto_extern;
						break;
					default:
						mainModeString = "";
						break;
					}
					if (drawableId != 0) {
						((TextView) mRootView.findViewById(R.id.mainMode)).setText(mainModeString);
						((TextView) mRootView.findViewById(R.id.mainMode)).setCompoundDrawablesWithIntrinsicBounds(
								getResources().getDrawable(drawableId), null, null, null);
					}
				}
			});
		}
	}

	@Override
	public void safetyStateChanged(SafetyState _state) {

	}

	@Override
	public void cartesianPositionChanged(int _compNo, String _compName, Number _value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogToolChanged(String _jogTool) {
		final String newTool = _jogTool;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((TextView) mRootView.findViewById(R.id.jogtool)).setText(newTool);
				}
			});
		}
	}

	@Override
	public void jogRefsysChanged(String _jogRefsys) {
		final String newRefsys = _jogRefsys;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {

					((TextView) mRootView.findViewById(R.id.jogrefSys)).setText(newRefsys);
				}
			});
		}
	}

	@Override
	public void pathVelocityChanged(float _velocityMms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void axisPositionChanged(int axisNo, Number _value, String _axisName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectedRefSysChanged(String _refsysName) {
		final String newRefsys = _refsysName;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {

					((TextView) mRootView.findViewById(R.id.refSys)).setText(newRefsys);
				}
			});
		}
	}

	@Override
	public void selectedToolChanged(String _toolName) {
		final String newTool = _toolName;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((TextView) mRootView.findViewById(R.id.tool)).setText(newTool);
				}
			});
		}
	}

	@Override
	public void drivePowerChanged(final boolean _hasPower) {
		final boolean power = _hasPower;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					TextView drv = (TextView) mRootView.findViewById(R.id.powerIndicator);
					if (drv != null) {
						Resources res = getResources();
						// int id = KvtDriveStateMonitor.getDrivesPower() ?
						// R.drawable.power_on : R.drawable.power_off;
						// String text = KvtDriveStateMonitor.getDrivesPower() ?
						// "ON" : "OFF";
						int id = _hasPower ? R.drawable.power_on : R.drawable.power_off;
						String text = _hasPower ? "ON" : "OFF";
						drv.setCompoundDrawablesWithIntrinsicBounds(res.getDrawable(id), null, null, null);
						drv.setText(text);
						drv.invalidate();
					}
				}
			});
		}
	}

	@Override
	public void driveIsReadyChanged(Boolean isReady) {
		// TODO Auto-generated method stub

	}

	public void toggleDrivesPower() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... _params) {
				KvtDriveStateMonitor.toggleDrivesPower();
				return null;
			}

		}.execute((Void) null);

	}

	@Override
	public void driveIsReferencedChanged(Boolean _isRef) {
		// TODO Auto-generated method stub

	}
}
