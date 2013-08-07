package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MasterFragment extends Fragment {
	private View mRootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_master, container, false);
		}

		ListFragment listFragment = (ListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_inner_list);
		String[] projects = ((ProjectActivity)getArguments().getSerializable("connector")).getProjects();
		listFragment.setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.default_list_item, projects));
		

		return mRootView;
	}
	
	public void detailSelected(int id){
		InnerDetailFragment fragment = new InnerDetailFragment();
		String[] programs = ((ProjectActivity)getArguments().getSerializable("connector")).getPrograms();
		fragment.setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.default_list_item, programs));
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inner_detail, fragment).commit();
	}
}
