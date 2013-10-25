package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keba.teachdroid.app.R;

public class RobotRefsysToolFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_robot_refsys_tool, container, false);
		
		Fragment cartesianFragment = new RobotCartesianFragment();
		FragmentTransaction transaction1 = getChildFragmentManager().beginTransaction();
		transaction1.add(R.id.fragment_robot_refsystool_cartesian, cartesianFragment).commit();

		RobotConfigFragment configFragment = new RobotConfigFragment();
		FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
		transaction2.add(R.id.fragment_robot_refsystool_config, configFragment).commit();
		
		return mRootView;
	}
}
