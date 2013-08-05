package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgramCodeFragment extends Fragment {

	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		mRootView = inflater.inflate(R.layout.fragment_program_code, container, false);
		
		return mRootView;
	}
}
