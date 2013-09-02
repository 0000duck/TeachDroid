package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtMultiKinematikAdministrator;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;

public class RobotFragmentMain extends Fragment implements KvtDriveStateListener {
	
	TextView	mRobotNameLabel;
	TextView	mRefSysLabel;
	CheckBox	cb;
	View mRootView;
	
	public RobotFragmentMain() {
		KvtDriveStateMonitor.addListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_robot_main, container, false);

		mRobotNameLabel = (TextView) mRootView.findViewById(R.id.robotNameLabel);

		cb = (CheckBox) mRootView.findViewById(R.id.robotPower);
		cb.setClickable(false);
		cb.setChecked(RobotControlProxy.drivesPower());

		// mRefSysLabel = (TextView) mRootView.findViewById(R.id.refsysLabel);
		updateUI();
		return mRootView;
	}

	/**
	 * 
	 */
	private void updateUI() {
		final String kin = KvtMultiKinematikAdministrator.getKinematicFilter();
		// final String refsys = RobotControlProxy.getRefsysName();

		if (getActivity() != null) {
			new Handler(getActivity().getMainLooper()).post(new Runnable() {

				public void run() {
					mRobotNameLabel.setText(kin);
					// mRefSysLabel.setText(refsys);
					mRootView.invalidate();
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #drivePowerChanged(boolean)
	 */
	public void drivePowerChanged(final boolean _hasPower) {
		// need a handler task to execute UI modifications
		if (getActivity() == null)
			return;
		Handler h = new Handler(getActivity().getMainLooper());
		h.post(new Runnable() {

			public void run() {
				if (cb != null) {
					cb.setChecked(_hasPower);
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
}
