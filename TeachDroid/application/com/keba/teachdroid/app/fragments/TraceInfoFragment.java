package com.keba.teachdroid.app.fragments;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.controller.KvtTraceUpdater;
import com.keba.kemro.kvs.teach.controller.KvtTraceUpdater.KvtTraceUpdateListener;
import com.keba.teachdroid.app.AlarmUpdaterThread;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener;

public class TraceInfoFragment extends Fragment implements AlarmUpdaterListener/* KvtTraceUpdateListener */{

	public TraceInfoFragment() {
		AlarmUpdaterThread.addListener(this);
	}

	/**
	 * @author ltz
	 * 
	 */
	public class TraceRowHolder {

		public TextView txtTitle;

	}

	/**
	 * @author ltz
	 * 
	 */
	public class TraceLogRowAdapter extends ArrayAdapter<String> {

		private int mLayoutResourceId;
		private Context mContext;
		private List<String> mData;

		/**
		 * @param _context
		 * @param _resource
		 * @param _textViewResourceId
		 * @param _objects
		 */
		public TraceLogRowAdapter(Context _context, int _layoutResId, List<String> _objects) {
			super(_context, _layoutResId, _objects);
			mLayoutResourceId = _layoutResId;
			mContext = _context;
			mData = _objects;
		}

		@Override
		public View getView(int _position, View _convertView, ViewGroup _parent) {
			View row = _convertView;
			TraceRowHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
				row = inflater.inflate(mLayoutResourceId, _parent, false);

				holder = new TraceRowHolder();
				// holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
				holder.txtTitle = (TextView) row.findViewById(R.id.trace_row_layout);

				row.setTag(holder);
			} else {
				holder = (TraceRowHolder) row.getTag();
			}

			String line = mData.get(_position);
			holder.txtTitle.setText(line);

			return row;
		}

	}

	private ListView list;
	// private InfoActivity callback;
	private TraceLogRowAdapter mAdapter;
	private List<String> mTraceLines = new LinkedList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_trace_info, container, false);

		// callback = (InfoActivity)
		// getArguments().getSerializable("connector");
		list = (ListView) mRootView.findViewById(R.id.traceList);

		// List<String> traces = callback.getTraceList();
		// setAdapter(traces);

		// KvtTraceUpdater.setLoadMode(KvtTraceUpdater.MODE_HISTORY);
		// KvtTraceUpdater.addListener(this);

		// mAdapter = new ArrayAdapter<String>(getActivity(),
		// R.layout.fragment_trace_row_layout, mTraceLines);
		mTraceLines = AlarmUpdaterThread.getTracedLines();
		List<String> tmplist = null;
		try {
			tmplist = new AsyncTask<Void, Void, List<String>>() {

				@Override
				protected List<String> doInBackground(Void... params) {
					return AlarmUpdaterThread.getTracedLines();
				}

			}.execute((Void) null).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (tmplist != null) {
			mTraceLines.addAll(tmplist);
		}

		mAdapter = new TraceLogRowAdapter(getActivity(), R.layout.fragment_trace_row_layout, mTraceLines);

		list.setAdapter(mAdapter);

		return mRootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.controller.KvtTraceUpdater.KvtTraceUpdateListener
	 * #lineReceived(java.lang.String)
	 */
	// public void lineReceived(final String _line) {
	//
	// if (getActivity() == null)
	// return;
	//
	// // need to run on the main looper thread, otherwise an exception will be
	// // hurled in your face
	// Handler refresh = new Handler(Looper.getMainLooper());
	// refresh.post(new Runnable() {
	// public void run() {
	// mTraceLines.offerLast(_line);
	// mAdapter.notifyDataSetChanged();
	// list.invalidate();
	// }
	// });
	//
	// }

	@Override
	public void queueChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void historyChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void traceChanged(String _line) {
//		final String line = _line;
//		if (getActivity() != null) {
//
//			getActivity().runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
//					mTraceLines.add(line);
//					mAdapter.clear();
//					mAdapter.addAll(mTraceLines);
//					list.invalidate();
//				}
//			});
//		}
	}
}
