package com.keba.teachdroid.app.fragments;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.keba.teachdroid.app.AlarmUpdaterThread;
import com.keba.teachdroid.app.InfoActivity;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.MessageAdapter;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener;
import com.keba.teachdroid.data.RobotControlProxy;

public class InfoAlarmFragmentMain extends Fragment implements AlarmUpdaterListener {
	CheckBox cb;
	final private List<Message> lastFour = new Vector<Message>();
	private MessageAdapter<Message> adp;

	public InfoAlarmFragmentMain() {
		AlarmUpdaterThread.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_load_info_main, container, false);

		ListView listview = (ListView) rootView.findViewById(R.id.actualMessages);

		adp = new MessageAdapter<Message>(getActivity(), R.layout.default_list_item, lastFour);

		// RobotControlProxy.addObserver(new Observer() {
		//
		// public void update(Observable _observable, Object _data) {
		// loadThreeMessages();
		// }
		// });

		listview.setAdapter(adp);
		try {
			loadFourMessages();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listview.setClickable(false);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (getActivity() != null) {
					Intent infoActivity = new Intent(getActivity(), InfoActivity.class);
					startActivity(infoActivity);
				}
			}
		});
		return rootView;
	}

	private void loadFourMessages() throws InterruptedException, ExecutionException {
		List<Message> tmp = new AsyncTask<Void, Void, List<Message>>() {

			@Override
			protected List<Message> doInBackground(Void... params) {
				return AlarmUpdaterThread.getMessageQueue();
			}
		}.execute((Void) null).get();

		
		final List<Message> tmp2 = tmp;
		if (getActivity() != null) {

			new Handler(getActivity().getMainLooper()).post(new Runnable() {

				public void run() {
					lastFour.clear();
					if (tmp2.size() > 4){
						lastFour.addAll(tmp2.subList(0, 4));
					}else{
						lastFour.addAll(tmp2);
					}
					
					adp.notifyDataSetChanged();
				}
			});

		}
	}

	@Override
	public void queueChanged() {
		try {
			loadFourMessages();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void historyChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void traceChanged(String _line) {
		// TODO Auto-generated method stub

	}

}