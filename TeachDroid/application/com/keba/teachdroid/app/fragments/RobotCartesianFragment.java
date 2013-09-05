package com.keba.teachdroid.app.fragments;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RobotCartesianFragment extends Fragment implements KvtPositionMonitorListener {
	int i = 0;
	int j = 0;
	int k = 0;
	int l = 0;
	int m = 0;

	public RobotCartesianFragment() {
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_robot_cartesian, container, false);
		return mRootView;
	}

	public void cartesianPositionChanged(String _compName, Number _value) {
		
	}

	public void pathVelocityChanged(float _velocityMms) {
		System.out.println("pathvelocity changed: " + j);
		j++;
	}

	public void axisPositionChanged(int axisNo, Number _value, String _axisName) {
		
	}

	public void chosenRefSysChanged(String _refsysName) {
		System.out.println("Refsys changed: " + l);
		l++;
	}

	public void chosenToolChanged(String _toolName) {

	}
}
