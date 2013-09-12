package com.keba.teachdroid.app.fragments;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.teachdroid.app.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class RobotConfigFragment extends Fragment {
	private List<String> mTools;
	private List<String> mRefSys;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_robot_config, container, false);
		Spinner refSysSpinner = (Spinner) mRootView.findViewById(R.id.spinner1);
		mRefSys = getRefSys();
		SpinnerAdapter adpRefSys = new ArrayAdapter<String>(getActivity(), R.layout.default_list_item, mRefSys);
		refSysSpinner.setAdapter(adpRefSys);
		
		Spinner toolSpinner = (Spinner) mRootView.findViewById(R.id.spinner2);
		mTools = getTools();
		SpinnerAdapter adpTools = new ArrayAdapter<String>(getActivity(), R.layout.default_list_item, mTools);
		toolSpinner.setAdapter(adpTools);
		return mRootView;
	}

	private List<String> getRefSys() {
		try {
			return new AsyncTask<Void, Void, List<String>>() {

				@Override
				protected List<String> doInBackground(Void... params) {
					return KvtPositionMonitor.getRefSysList();
				}

			}.execute((Void) null).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getTools() {
		try {
			return new AsyncTask<Void, Void, List<String>>() {

				@Override
				protected List<String> doInBackground(Void... params) {
					return KvtPositionMonitor.getTools();
				}

			}.execute((Void) null).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
