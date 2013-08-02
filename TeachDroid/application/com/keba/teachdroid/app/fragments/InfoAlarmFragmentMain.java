package com.keba.teachdroid.app.fragments;


import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class InfoAlarmFragmentMain extends Fragment {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			 
		 
			 View rootView= inflater.inflate(R.layout.fragment_load_info_main, container, false);

//		     ListView listview = (ListView) findViewById(R.id.fragment_load_info_main);
//		    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//		        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//		        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//		        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//		        "Android", "iPhone", "WindowsMobile" };

	
			 return rootView;
}
}