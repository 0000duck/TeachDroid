package com.keba.teachdroid.app.fragments;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.R;

public class RobotCartesianFragment extends Fragment implements KvtPositionMonitorListener {
	private transient View mRootView;
	private transient ListView list;
	private transient ArrayAdapter<String> mAdapter;
	private transient List<String> cartList;

	public RobotCartesianFragment() {
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_cartesian, container, false);
		list = (ListView) mRootView.findViewById(R.id.cartList);
		mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.default_list_item);
		cartList = new Vector<String>();
		cartList.add("X: 0.0 mm");
		cartList.add("Y: 0.0 mm");
		cartList.add("Z: 0.0 mm");
		cartList.add("A: 0.0 deg");
		cartList.add("B: 0.0 deg");
		cartList.add("C: 0.0 deg");
		mAdapter.addAll(cartList);
		list.setAdapter(mAdapter);
		return mRootView;
	}

	public void cartesianPositionChanged(int _compNo, String _compName, Number _value) {
		cartList.remove(_compNo);
		if (_compNo < 3) {
			cartList.add(_compNo, _compName + ": " + _value + " mm");
		} else {
			cartList.add(_compNo, _compName + ": " + _value + " deg");
		}
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mAdapter.clear();
					mAdapter.addAll(cartList);
					mAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	public void pathVelocityChanged(float _velocityMms) {
		// final float vel = _velocityMms;
		// final TextView t1 = (TextView)
		// mRootView.findViewById(R.id.textView1);
		// if (getActivity() != null) {
		// getActivity().runOnUiThread(new Runnable() {
		//
		// public void run() {
		// t1.setText("actual path velocity: " + vel + "mm/s");
		// }
		// });
		// }
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
