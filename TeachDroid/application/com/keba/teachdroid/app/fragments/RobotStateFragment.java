package com.keba.teachdroid.app.fragments;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.SafetyState;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.KvtMainModeListener;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtOverrideChangedListener;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RobotStateFragment extends Fragment implements KvtMainModeListener, KvtOverrideChangedListener, KvtPositionMonitorListener, KvtDriveStateListener {
	private transient View mRootView;

	public RobotStateFragment() {
		KvtPositionMonitor.addListener((KvtOverrideChangedListener) this);
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
		KvtMainModeAdministrator.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_state, container, false);
		ImageView img = (ImageView) mRootView.findViewById(R.id.powerIndicator);
		if (img != null) {
			Resources res = getResources();
			int id = RobotControlProxy.drivesPower() ? R.drawable.power_on : R.drawable.power_off;
			img.setImageDrawable(res.getDrawable(id));
			img.invalidate();
		}
		return mRootView;
	}

	public void overrideChanged(Number _override) {
		final Number ovr = _override;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					((TextView) mRootView.findViewById(R.id.override)).setText("Override: " + ovr + "%");
					((ProgressBar) mRootView.findViewById(R.id.overrideProgress)).setProgress(ovr.intValue());
				}
			});
		}
	}

	public void mainModeChanged(int _newMainMode) {
		final int mainMode = _newMainMode;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					String mainModeString = null;
					switch (mainMode) {
					case 1:
						mainModeString = "A";
						break;
					case 2:
						mainModeString = "T1";
						break;
					case 4:
						mainModeString = "AE";
						break;
					default:
						mainModeString = "";
						break;
					}
					((TextView) mRootView.findViewById(R.id.mainMode)).setText("Main Mode: " + mainModeString);
				}
			});
		}
	}

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

				public void run() {
					((TextView) mRootView.findViewById(R.id.jogtool)).setText("JogTool:\n" + newTool);
				}
			});
		}
	}

	@Override
	public void jogRefsysChanged(String _jogRefsys) {
		final String newRefsys = _jogRefsys;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {

					((TextView) mRootView.findViewById(R.id.jogrefSys)).setText("JogRefsys:\n" + newRefsys);
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

				public void run() {

					((TextView) mRootView.findViewById(R.id.refSys)).setText("Refsys:\n" + newRefsys);
				}
			});
		}
	}

	@Override
	public void selectedToolChanged(String _toolName) {
		final String newTool = _toolName;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					((TextView) mRootView.findViewById(R.id.tool)).setText("Tool:\n" + newTool);
				}
			});
		}
	}

	@Override
	public void drivePowerChanged(boolean _hasPower) {
		final boolean power = _hasPower;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					ImageView img = (ImageView) mRootView.findViewById(R.id.powerIndicator);
					if (img != null) {
						Resources res = getResources();
						int id = power ? R.drawable.power_on : R.drawable.power_off;
						img.setImageDrawable(res.getDrawable(id));
						img.invalidate();
					}
				}
			});
		}
	}

	@Override
	public void driveIsReadyChanged(Boolean isReady) {
		// TODO Auto-generated method stub

	}

	@Override
	public void driveIsReferencedChanged(Boolean _isRef) {
		// TODO Auto-generated method stub

	}
}
