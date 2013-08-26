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

public class HistoryInfoFragment extends Fragment implements Serializable{

	
	private static final long serialVersionUID = -5398636788493413524L;

	private InfoActivity  callback;
	private ListView list;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_history_info, container, false);

		callback = (InfoActivity) getArguments().getSerializable("connector");
		list = (ListView) mRootView.findViewById(R.id.historyList);
		List<String> history = callback.getMessageHistory();
		setAdapter(history);
		return mRootView;
	}

	public void setAdapter(List<String> history) {
		if (list != null)
			list.setAdapter(new ArrayAdapter<String>(callback, R.layout.default_list_item, history));
	}
}
