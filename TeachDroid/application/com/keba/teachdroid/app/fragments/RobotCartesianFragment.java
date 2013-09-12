package com.keba.teachdroid.app.fragments;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.CustomPosition;
import com.keba.teachdroid.app.R;

public class RobotCartesianFragment extends Fragment implements KvtPositionMonitorListener {

	private class CartesianHolder {
		TextView mName;
		TextView mValue;
		TextView mUnit;
	}

	protected class CartesianArrayAdapter extends ArrayAdapter<CustomPosition> {

		private int mLayoutResourceId;
		private Context mContext;
		private List<CustomPosition> mData;

		/**
		 * @param _activity
		 * @param _layoutId
		 */
		public CartesianArrayAdapter(Context _context, int _layoutId, List<CustomPosition> _objects) {
			super(_context, _layoutId, _objects);
			mLayoutResourceId = _layoutId;
			mContext = _context;
			mData = _objects;
		}

		@Override
		public View getView(int _position, View _convertView, ViewGroup _parent) {
			View row = _convertView;
			CartesianHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
				row = inflater.inflate(mLayoutResourceId, _parent, false);

				holder = new CartesianHolder();
				holder.mName = (TextView) row.findViewById(R.id.nameText);
				holder.mValue = (TextView) row.findViewById(R.id.valueText);
				holder.mUnit = (TextView) row.findViewById(R.id.unitText);
				row.setTag(holder);
			} else {
				holder = (CartesianHolder) row.getTag();
			}

			if (_position >= 0 && _position < mData.size()) {
				CustomPosition line = mData.get(_position);

				holder.mName.setText(line.getName() + ": ");
				DecimalFormat df = new DecimalFormat("##0.00");
				String formatted = df.format(line.getValue());
				holder.mValue.setText(formatted);// String.format("%04.2f",
													// line.getValue()));
				holder.mUnit.setText(line.getUnit());
			}
			return row;
		}
	}

	private transient View mRootView;
	private transient ListView list;
	private transient CartesianArrayAdapter mAdapter;
	private transient List<CustomPosition> cartList;
	private final String[] axisNames = { "X", "Y", "Z", "A", "B", "C" };
	private Object mLck = new Object();

	public RobotCartesianFragment() {
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_cartesian, container, false);
		list = (ListView) mRootView.findViewById(R.id.cartList);
		
		getPositions();

		mAdapter = new CartesianArrayAdapter(getActivity(), R.layout.position_row_layout, cartList);

		list.setAdapter(mAdapter);
		return mRootView;
	}

	private void getPositions() {
		cartList = new Vector<CustomPosition>();
		List<Float> positions = null;
		try {
			positions = new AsyncTask<Void, Void, List<Float>>() {

				@Override
				protected List<Float> doInBackground(Void... params) {
					return KvtPositionMonitor.getCartesianPositions();
				}
			}.execute((Void) null).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		if (positions != null) {

			for (int i = 0; i < positions.size(); i++) {
				if (i < 3) {
					cartList.add(new CustomPosition("mm", axisNames[i], positions.get(i)));
				} else {
					cartList.add(new CustomPosition("deg", axisNames[i], positions.get(i)));
				}
			}
		}
	}

	public void cartesianPositionChanged(int _compNo, String _compName, Number _value) {
		getPositions();
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mAdapter.clear();
					mAdapter.addAll(cartList);
					mAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	public void pathVelocityChanged(float _velocityMms) {
	}

	public void axisPositionChanged(int axisNo, Number _value, String _axisName) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #jogToolChanged(java.lang.String)
	 */
	public void jogToolChanged(String _jogTool) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #jogRefsysChanged(java.lang.String)
	 */
	public void jogRefsysChanged(String _jogRefsys) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #selectedRefSysChanged(java.lang.String)
	 */
	public void selectedRefSysChanged(String _refsysName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #selectedToolChanged(java.lang.String)
	 */
	public void selectedToolChanged(String _toolName) {
	}
}
