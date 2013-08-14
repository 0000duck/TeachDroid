package com.keba.teachdroid.app.fragments;

import java.io.Serializable;
import java.util.List;

import com.keba.teachdroid.app.InfoActivity;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlarmInfoFragment extends Fragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1436151971908046236L;

	private ListView list;
	private InfoActivity callback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_alarm_info, container, false);

		callback = (InfoActivity) getArguments().getSerializable("connector");
		list = (ListView) mRootView.findViewById(R.id.alarmList);
		List<String> alarms = callback.getMessageQueue();
		setAdapter(alarms);
		return mRootView;
	}

	public void setAdapter(List<String> alarms) {
		if (list != null)
			list.setAdapter(new ArrayAdapter<String>(callback, R.layout.default_list_item, alarms));
	}
}
