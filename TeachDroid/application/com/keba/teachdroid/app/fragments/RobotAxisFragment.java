package com.keba.teachdroid.app.fragments;

import java.util.List;
import java.util.Vector;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RobotAxisFragment extends Fragment implements KvtPositionMonitorListener {

	private transient View mRootView;
	private transient ListView list;
	private transient ArrayAdapter<String> mAdapter;
	private transient List<String> axisList;

	public RobotAxisFragment() {
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_axis, container, false);
		list = (ListView) mRootView.findViewById(R.id.axisList);
		mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.default_list_item);
		axisList = new Vector<String>();
		axisList.add("A1: 0.0 deg");
		axisList.add("A2: 0.0 deg");
		axisList.add("A3: 0.0 deg");
		axisList.add("A4: 0.0 deg");
		axisList.add("A5: 0.0 deg");
		axisList.add("A6: 0.0 deg");
		mAdapter.addAll(axisList);
		list.setAdapter(mAdapter);
		return mRootView;
	}

	@Override
	public void cartesianPositionChanged(int _compNo, String _compName, Number _value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogToolChanged(String _jogTool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogRefsysChanged(String _jogRefsys) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pathVelocityChanged(float _velocityMms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void axisPositionChanged(int axisNo, Number _value, String _axisName) {
		axisList.remove(axisNo);
		axisList.add(axisNo, "A" + (axisNo + 1) + ": " + _value + " deg");

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mAdapter.clear();
					mAdapter.addAll(axisList);
					mAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	public void selectedRefSysChanged(String _refsysName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectedToolChanged(String _toolName) {
		// TODO Auto-generated method stub

	}
}
