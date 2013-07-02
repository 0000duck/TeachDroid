package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.keba.teachdroid.app.R;
import com.keba.teachdroid.app.data.RobotControlProxy;

public class OverviewFragment extends Fragment {

	public OverviewFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.fragment_layout_overview, container, false);

		// initialize robot select spinner
		String[] robots = RobotControlProxy.getRobotNames();

		Spinner sp = (Spinner) myFragmentView.findViewById(R.id.kinematicSelectSpinner);
		ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, robots);
		adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sp.setAdapter(adp);

		return myFragmentView;
	}
}
