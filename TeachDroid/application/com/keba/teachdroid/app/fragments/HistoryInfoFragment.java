package com.keba.teachdroid.app.fragments;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener;
import com.keba.teachdroid.app.AlarmUpdaterThread;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.app.fragments.AlarmInfoFragment.MessageArrayAdapter;

public class HistoryInfoFragment extends AlarmInfoFragment {

	private static final long serialVersionUID = -4263232762352449273L;
	private ListView mList;
	private List<Message> mMessages = new LinkedList<Message>();
	private MessageArrayAdapter mAdapter;

	public HistoryInfoFragment() {
		super();
		AlarmUpdaterThread.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_history_info, container, false);
		mList = (ListView) mRootView.findViewById(R.id.historyList);

		List<Message> list = null;
		try {
			list = new AsyncTask<Void, Void, List<Message>>() {

				@Override
				protected List<Message> doInBackground(Void... params) {
					return AlarmUpdaterThread.getMessageHistory();
				}

			}.execute((Void) null).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(list != null){
			mMessages.addAll(list);
		}

		mAdapter = new MessageArrayAdapter(getActivity(), R.layout.fragment_alarm_row_layout, mMessages);
		mList.setAdapter(mAdapter);
		
		return mRootView;
	}

	@Override
	public void historyChanged() {
		try {
			mMessages = new AsyncTask<Void, Void, List<Message>>() {

				@Override
				protected List<Message> doInBackground(Void... params) {
					return AlarmUpdaterThread.getMessageHistory();
				}

			}.execute((Void) null).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (getActivity() != null) {
			
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mAdapter.clear();
					mAdapter.addAll(mMessages);
					mAdapter.notifyDataSetChanged();
					mList.invalidate();
				}
			});
		}
	}

	// @Override
	// public void messageRemoved(String _bufferName, KMessage _msg) {
	// // do nothing - no removing from history
	// }

	// @Override
	// public boolean appliesFilter(String _input) {
	// return false;
	// }

}
