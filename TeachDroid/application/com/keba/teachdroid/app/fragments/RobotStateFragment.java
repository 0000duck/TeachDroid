package com.keba.teachdroid.app.fragments;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtOverrideChangedListener;
import com.keba.teachdroid.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RobotStateFragment extends Fragment implements KvtOverrideChangedListener {
	private transient View mRootView;
	private int i = 0;

	public RobotStateFragment() {
		KvtPositionMonitor.addListener((KvtOverrideChangedListener) this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_state, container, false);
		return mRootView;
	}

	public void overrideChanged(Number _override) {
		final Number ovr = _override;
		getActivity().runOnUiThread(new Runnable() {

			public void run() {
				((TextView) mRootView.findViewById(R.id.textView2)).setText("Override: " + ovr + "%");
			}
		});
	}
}
