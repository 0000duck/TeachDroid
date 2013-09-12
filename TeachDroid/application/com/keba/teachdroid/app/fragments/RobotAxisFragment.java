package com.keba.teachdroid.app.fragments;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.CustomPosition;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.app.fragments.RobotCartesianFragment.CartesianArrayAdapter;

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

public class RobotAxisFragment extends Fragment implements KvtPositionMonitorListener {

	private class AxisHolder {
		TextView mName;
		TextView mValue;
		TextView mUnit;
	}

	protected class AxisArrayAdapter extends ArrayAdapter<CustomPosition> {

		private int mLayoutResourceId;
		private Context mContext;
		private List<CustomPosition> mData;

		/**
		 * @param _activity
		 * @param _layoutId
		 */
		public AxisArrayAdapter(Context _context, int _layoutId, List<CustomPosition> _objects) {
			super(_context, _layoutId, _objects);
			mLayoutResourceId = _layoutId;
			mContext = _context;
			mData = _objects;
		}

		@Override
		public View getView(int _position, View _convertView, ViewGroup _parent) {
			View row = _convertView;
			AxisHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
				row = inflater.inflate(mLayoutResourceId, _parent, false);

				holder = new AxisHolder();
				holder.mName = (TextView) row.findViewById(R.id.nameText);
				holder.mValue = (TextView) row.findViewById(R.id.valueText);
				holder.mUnit = (TextView) row.findViewById(R.id.unitText);
				row.setTag(holder);
			} else {
				holder = (AxisHolder) row.getTag();
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
	private transient AxisArrayAdapter mAdapter;
	private transient List<CustomPosition> axisList;

	public RobotAxisFragment() {
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_robot_axis, container, false);
		list = (ListView) mRootView.findViewById(R.id.axisList);
		
		getPositions();
		mAdapter = new AxisArrayAdapter(getActivity(), R.layout.position_row_layout, axisList);

		list.setAdapter(mAdapter);
		return mRootView;
	}

	private void getPositions() {
		axisList = new Vector<CustomPosition>();
		List<Float> positions = null;
		try {
			positions = new AsyncTask<Void, Void, List<Float>>() {

				@Override
				protected List<Float> doInBackground(Void... params) {
					return KvtPositionMonitor.getAxisPositions();
				}
			}.execute((Void) null).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		if (positions != null) {
			axisList = new Vector<CustomPosition>();
			for (int i = 0; i < positions.size(); i++) {
				axisList.add(new CustomPosition("deg", "A" + (i + 1), positions.get(i)));
			}
		}
	}

	@Override
	public void cartesianPositionChanged(int _compNo, String _compName, Number _value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogToolChanged(String _jogTool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogRefsysChanged(String _jogRefsys) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pathVelocityChanged(float _velocityMms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void axisPositionChanged(int axisNo, Number _value, String _axisName) {
		getPositions();
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mAdapter.clear();
					mAdapter.addAll(axisList);
					mAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	public void selectedRefSysChanged(String _refsysName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectedToolChanged(String _toolName) {
		// TODO Auto-generated method stub

	}
}
