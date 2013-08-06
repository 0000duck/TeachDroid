package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectListFragment extends Fragment implements OnItemClickListener {

	private View mRootView;
	ProjectActivity callback;
	ArrayAdapter<String> adp;
	ListView l;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_project_list, container, false);

		callback = (ProjectActivity) getArguments().getSerializable("connector");

		l = (ListView) mRootView.findViewById(R.id.projectListView);

		adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1);

		adp.add(callback.getProjects()[0]);
		adp.add(callback.getProjects()[1]);
		adp.add(callback.getProjects()[2]);
		l.setAdapter(adp);

		l.setOnItemClickListener(this);

		return mRootView;
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		callback.setSelectedProject(arg2);
	}

}
