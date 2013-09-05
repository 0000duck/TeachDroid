package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RobotDetailInfoFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_robot_detail_info, container, false);

		RobotCartesianFragment cartesianFragment = new RobotCartesianFragment();
		FragmentTransaction transaction1 = getChildFragmentManager().beginTransaction();
		transaction1.add(R.id.fragment_robot_cartesian, cartesianFragment).commit();

		RobotAxisFragment axisFragment = new RobotAxisFragment();
		FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
		transaction2.add(R.id.fragment_robot_axis, axisFragment).commit();

		return mRootView;
	}
}
