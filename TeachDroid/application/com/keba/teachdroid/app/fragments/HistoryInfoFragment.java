package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keba.kemro.serviceclient.alarm.KMessage;

public class HistoryInfoFragment extends AlarmInfoFragment {

	private static final long	serialVersionUID	= -4263232762352449273L;

	public HistoryInfoFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void messageRemoved(String _bufferName, KMessage _msg) {
		// do nothing - no removing from history
	}

	@Override
	public boolean appliesFilter(String _input) {
		return false;
	}


}
