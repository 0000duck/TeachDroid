package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ProjectNestingFragment extends Fragment {

	private View mRootView;
	private ListView projectList, programList;
	private ProjectActivity callback;
	private ArrayAdapter<String> adpProject, adpProgram;
	private MyOnItemClickListener moicl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_project_nesting, container, false);
		callback = (ProjectActivity) getArguments().getSerializable("connector");
		projectList = (ListView) mRootView.findViewById(R.id.listViewProjects);
		programList = (ListView) mRootView.findViewById(R.id.listViewPrograms);

		moicl = new MyOnItemClickListener();
		projectList.setOnItemClickListener(moicl);
		programList.setOnItemClickListener(moicl);

		adpProject = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1);
		adpProgram = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1);

//		for (String s : callback.getProjects()) {
//			adpProject.add(s);
//		}

		setProgramListContent();
		projectList.setAdapter(adpProject);
		programList.setAdapter(adpProgram);

		return mRootView;
	}

	public void setProgramListContent() {
		adpProgram.clear();
		
//		for (String s : callback.getPrograms()) {
//			adpProgram.add(s);
//		}
	}

	private class MyOnItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> adv, View view, int position, long arg3) {
			if (((TextView)view).getText() == projectList.getItemAtPosition(position)) {
				callback.setSelectedProject(position);
			} else {
				callback.setSelectedProgram(position);
			}
		}

	}
}
