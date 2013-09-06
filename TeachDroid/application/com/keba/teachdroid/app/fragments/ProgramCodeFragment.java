package com.keba.teachdroid.app.fragments;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor.KvtExecutionListener;
import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

public class ProgramCodeFragment extends Fragment implements Serializable, KvtExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7012715919241719807L;
	private transient View mRootView;
	ProjectActivity callback;
	transient TextView t;

	public ProgramCodeFragment() {
		KvtExecutionMonitor.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mRootView = inflater.inflate(R.layout.fragment_program_code, container, false);
		callback = (ProjectActivity) getArguments().getSerializable("connector");
		t = (TextView) mRootView.findViewById(R.id.programCode);
		t.setText(callback.getProgramCode());
		return mRootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		t.setText(callback.getProgramCode());
	}

	public void setProgramCode() {
		t.setText(callback.getProgramCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtExecutionMonitor.KvtExecutionListener
	 * #programCounterChanged(int)
	 */
	public void programCounterChanged(int _line) {
		Toast.makeText(getActivity(), "PC: " + _line, Toast.LENGTH_SHORT).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtExecutionMonitor.KvtExecutionListener
	 * #programCodeChanged(java.lang.String)
	 */
	public void programCodeChanged(String _source) {
	}
}
