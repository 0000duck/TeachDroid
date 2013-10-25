package com.keba.teachdroid.app.fragments;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.Log;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.teachdroid.app.AlarmUpdaterThread;
import com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener;
import com.keba.teachdroid.app.InfoActivity;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.R;

public class InfoAlarmFragmentMain extends Fragment implements AlarmUpdaterListener {
	
	final private List<Message> lastSix = new Vector<Message>();
	private MessageArrayAdapter mMessageArrayAdapter;
	private ListView	mMessageListView;

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

		mMessageListView = (ListView) rootView.findViewById(R.id.actualMessages);
		TextView textview = (TextView)rootView.findViewById(R.id.empty_list_item);
		mMessageListView.setEmptyView(textview);
		
		mMessageListView.setLongClickable(true);
		mMessageListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> _parent, View _view, final int _pos, long __id) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

				// set title
				alertDialogBuilder.setTitle(getString(R.string.dialog_confirm_message_title));

				// set dialog message
				alertDialogBuilder.setMessage(getString(R.string.dialog_confirm_message_text)).setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Message m = mMessageArrayAdapter.getItem(_pos);

								boolean success = false;
								if (m != null) {
									try {
										success = new AsyncTask<Message, Integer, Boolean>() {

											@Override
											protected Boolean doInBackground(Message... _params) {
												KMessage toConfirm = findInQueue(_params[0]);

												return toConfirm != null ? toConfirm.quitMessage() : false;
											}
										}.execute(m).get();
									} catch (InterruptedException e) {
										e.printStackTrace();
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
								Log.i("AlarmInfoFragment", "confirming was " + (success ? "successful" : "unsuccessful"));
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				return false;
			}
		});
		mMessageArrayAdapter = new MessageArrayAdapter(getActivity(), R.layout.fragment_alarm_row_layout_overview, lastSix);

		// RobotControlProxy.addObserver(new Observer() {
		//
		// public void update(Observable _observable, Object _data) {
		// loadThreeMessages();
		// }
		// });

		mMessageListView.setAdapter(mMessageArrayAdapter);
		try {
			loadSixMessages();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mMessageListView.setOnItemClickListener(new OnItemClickListener() {

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

				@Override
				public void run() {
					lastSix.clear();
					if (tmp2.size() > 6){
						lastSix.addAll(tmp2.subList(0, 6));
					}else{
						lastSix.addAll(tmp2);
					}
					
					mMessageArrayAdapter.notifyDataSetChanged();
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

	private KMessage findInQueue(Message _m) {
		int hash = _m.getID();
		List<KMessage> curBuffer = AlarmUpdaterThread.getKMessageQueue();

		for (KMessage msg : curBuffer) {
			if (msg.hashCode() == hash)
				return msg;
		}

		return null;

	}
}