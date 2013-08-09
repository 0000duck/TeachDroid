package com.keba.teachdroid.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.keba.teachdroid.app.R;

public class ProgrammCodeFragmentMain extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView= inflater.inflate(R.layout.fragment_prog_code_main, container, false);
		
		TextView title= (TextView)rootView.findViewById(R.id.textView3);
		title.setText("hali halo");
		
		ImageView imgv = (ImageView) rootView.findViewById(R.id.imageView3);
		if (imgv != null) {
			imgv.setOnClickListener(new OnClickListener() {

				public void onClick(View _v) {
					Toast.makeText(getActivity(), "play button clicked", Toast.LENGTH_SHORT).show();
				}
			});
		}

		return rootView;
	}



}
