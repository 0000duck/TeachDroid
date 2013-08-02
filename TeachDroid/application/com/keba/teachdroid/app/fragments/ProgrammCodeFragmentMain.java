package com.keba.teachdroid.app.fragments;


import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgrammCodeFragmentMain extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView= inflater.inflate(R.layout.fragment_prog_code_main, container, false);
		
		TextView title= (TextView)rootView.findViewById(R.id.textView3);
		title.setText("hali halo");
		
		return rootView;
	}



}
