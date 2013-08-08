package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.keba.teachdroid.app.ConfirmMessageDialog;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.MessageAdapter;
import com.keba.teachdroid.app.MessageTypes;
import com.keba.teachdroid.app.R;

public class InfoAlarmFragmentMain extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_load_info_main, container, false);

		ListView listview = (ListView) rootView.findViewById(R.id.actualMessages);
		// String[] values = new String[] { "some information", "more info",
		// "extraordinary long infos", "some information", "more info",
		// "extraordinary long infos", "some information", "more info",
		// "extraordinary long infos" };
		// ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
		// R.layout.default_list_item, values);
		Message[] values = new Message[] { new Message("DebugMessage", MessageTypes.DEBUG), new Message("AlarmMessage", MessageTypes.ALARM), new Message("WarningMessage", MessageTypes.WARNING), new Message() };
		MessageAdapter<Message> adp = new MessageAdapter<Message>(getActivity(), R.layout.default_list_item, values);
		listview.setAdapter(adp);
		
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				new ConfirmMessageDialog().show(getFragmentManager(), getTag());
				return false;
			}

		});

		// Spinner spin = (Spinner) rootView.findViewById(R.id.actualMessages);
		// String[] values = new String[] { "some information", "more info",
		// "extraordinary long infos", "some information", "more info",
		// "extraordinary long infos", "some information", "more info",
		// "extraordinary long infos" };
		// ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
		// R.layout.default_list_item, values);
		//
		// spin.setAdapter(adp);

		return rootView;
	}
}