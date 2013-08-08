package com.keba.teachdroid.app.fragments;

import java.io.Serializable;

import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgramInfoFragment extends Fragment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8292119305764249943L;
	transient TextView t;
	ProjectActivity callback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_program_info, container, false);
		t = (TextView) mRootView.findViewById(R.id.programInfo);
		callback = (ProjectActivity) getArguments().getSerializable("connector");
		t.setText(callback.getProgramInfo());
		return mRootView;
	}

	public void setProgramInfo() {
		if (t != null)
			t.setText(callback.getProgramInfo());
	}

}
