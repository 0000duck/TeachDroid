package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.R;

public class RobotCartesianFragment extends Fragment implements KvtPositionMonitorListener {
	private transient View mRootView;

	public RobotCartesianFragment() {
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_cartesian, container, false);
		return mRootView;
	}

	public void cartesianPositionChanged(String _compName, Number _value) {

	}

	public void pathVelocityChanged(float _velocityMms) {
		final float vel = _velocityMms;
		final TextView t1 = (TextView) mRootView.findViewById(R.id.textView1);
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					t1.setText("actual path velocity: " + vel + "mm/s");
				}
			});
		}
	}

	public void axisPositionChanged(int axisNo, Number _value, String _axisName) {

	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #jogToolChanged(java.lang.String)
	 */
	public void jogToolChanged(String _jogTool) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #jogRefsysChanged(java.lang.String)
	 */
	public void jogRefsysChanged(String _jogRefsys) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #selectedRefSysChanged(java.lang.String)
	 */
	public void selectedRefSysChanged(String _refsysName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #selectedToolChanged(java.lang.String)
	 */
	public void selectedToolChanged(String _toolName) {
	}
}
