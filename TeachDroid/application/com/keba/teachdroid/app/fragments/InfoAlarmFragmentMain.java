package com.keba.teachdroid.app.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.keba.teachdroid.app.AlarmUpdaterThread;
import com.keba.teachdroid.app.InfoActivity;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.MessageAdapter;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener;
import com.keba.teachdroid.data.RobotControlProxy;

public class InfoAlarmFragmentMain extends Fragment implements AlarmUpdaterListener {
	
	final private List<Message> lastSix = new Vector<Message>();
	private MessageArrayAdapter adp;

	public InfoAlarmFragmentMain() {
		AlarmUpdaterThread.addListener(this);
	}

	/**
	 * @author ltz
	 * 
	 */
	protected class MessageArrayAdapter extends ArrayAdapter<Message> {

		private int mLayoutResourceId;
		private Context mContext;
		private List<Message> mData;

		/**
		 * @param _activity
		 * @param _layoutId
		 */
		public MessageArrayAdapter(Context _context, int _layoutId, List<Message> _objects) {
			super(_context, _layoutId, _objects);
			mLayoutResourceId = _layoutId;
			mContext = _context;
			mData = _objects;
		}
		
		/**
		 * @author ltz
		 * 
		 */
		private class MessageHolder {
			ImageView mIcon;
			TextView mMessageText;
		}

		@Override
		public View getView(int _position, View _convertView, ViewGroup _parent) {
			View row = _convertView;
			MessageHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
				row = inflater.inflate(mLayoutResourceId, _parent, false);

				holder = new MessageHolder();
				holder.mIcon = (ImageView) row.findViewById(R.id.alarmIcon);
				holder.mMessageText = (TextView) row.findViewById(R.id.alarmText);
				row.setTag(holder);
			} else {
				holder = (MessageHolder) row.getTag();
			}

			if (_position >= 0 && _position < mData.size()) {
				Message line = mData.get(_position);
				holder.mMessageText.setText(line.toString());
				holder.mIcon.setImageResource(line.getImageID());
			}
			return row;
		}
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_load_info_main, container, false);

		ListView listview = (ListView) rootView.findViewById(R.id.actualMessages);
		TextView textview = (TextView)rootView.findViewById(R.id.empty_list_item);
		listview.setEmptyView(textview);
		
		adp = new MessageArrayAdapter(getActivity(), R.layout.fragment_alarm_row_layout_overview, lastSix);

		// RobotControlProxy.addObserver(new Observer() {
		//
		// public void update(Observable _observable, Object _data) {
		// loadThreeMessages();
		// }
		// });

		listview.setAdapter(adp);
		try {
			loadSixMessages();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	private void loadSixMessages() throws InterruptedException, ExecutionException {
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
					lastSix.clear();
					if (tmp2.size() > 6){
						lastSix.addAll(tmp2.subList(0, 6));
					}else{
						lastSix.addAll(tmp2);
					}
					
					adp.notifyDataSetChanged();
				}
			});

		}
	}

	@Override
	public void queueChanged() {
		try {
			loadSixMessages();
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