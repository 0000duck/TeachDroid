package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectDetailFragment extends Fragment implements OnItemClickListener {
	private View mRootView;
	ArrayAdapter<String> adp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_project_detail, container, false);

		ProjectActivity callback = (ProjectActivity) getArguments().getSerializable("connector");
		ListView l = (ListView) mRootView.findViewById(R.id.projectDetailList);
		l.setAdapter(createAdapter(callback.getPrograms()));
		l.setOnItemClickListener(this);

		return mRootView;
	}

	private ArrayAdapter<String> createAdapter(String[] _arr) {
		if (adp == null)
			adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1);
		else
			adp.clear();
		for (String s : _arr) {
			adp.add(s);
		}
		return adp;
	}

	@Override
	public void onResume() {
		super.onResume();
		adp.clear();
		// adp.add(callback.getPrograms()[0]);
		// adp.add(callback.getPrograms()[1]);
		// adp.add(callback.getPrograms()[2]);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// callback.setSelectedProgram(arg2);

	}

	public void setPrograms(String[] strings) {
		((ListView) mRootView.findViewById(R.id.projectDetailList)).setAdapter(createAdapter(strings));

	}
}
