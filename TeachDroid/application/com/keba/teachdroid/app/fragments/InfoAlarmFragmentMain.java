package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.keba.teachdroid.app.R;

public class InfoAlarmFragmentMain extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_load_info_main, container, false);

		ListView listview = (ListView) rootView.findViewById(R.id.infoListView);
		String[] values = new String[] { "some information", "more info", "extraordinary long infos", "some information", "more info", "extraordinary long infos", "some information", "more info", "extraordinary long infos" };
		ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), R.layout.default_list_item, values);
		listview.setAdapter(adp);

		return rootView;
	}
}