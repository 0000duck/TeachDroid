package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.teachdroid.app.ConfirmMessageDialog;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.MessageAdapter;
import com.keba.teachdroid.app.MessageTypes;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;

public class InfoAlarmFragmentMain extends Fragment implements KvtDriveStateListener {
	ConfirmMessageDialog dialog;
	CheckBox cb; 

	public InfoAlarmFragmentMain() {
		KvtDriveStateMonitor.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_load_info_main, container, false);

		ListView listview = (ListView) rootView.findViewById(R.id.actualMessages);


		Message[] values = new Message[] { new Message("DebugMessage", MessageTypes.DEBUG), new Message("AlarmMessage", MessageTypes.ALARM), new Message("WarningMessage", MessageTypes.WARNING), new Message() };
		MessageAdapter<Message> adp = new MessageAdapter<Message>(getActivity(), R.layout.default_list_item);
		for (Message msg : values) {
			adp.add(msg);
		}

		cb = (CheckBox)rootView.findViewById(R.id.robotPower);
		cb.setClickable(false);
		cb.setChecked(RobotControlProxy.drivesPower());

		listview.setAdapter(adp);

		dialog = new ConfirmMessageDialog();
		Bundle args = new Bundle();
		args.putSerializable("msgadp", adp);
		dialog.setArguments(args);

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				dialog.show(getFragmentManager(), getTag());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #drivePowerChanged(boolean)
	 */
	public void drivePowerChanged(final boolean _hasPower) {
		// need a handler task to execute UI modifications
		if (getActivity() == null)
			return;
		Handler h = new Handler(getActivity().getMainLooper());
		h.post(new Runnable() {

			public void run() {
				if (cb != null) {
					cb.setChecked(_hasPower);
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReadyChanged(java.lang.Boolean)
	 */
	public void driveIsReadyChanged(Boolean _isReady) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReferencedChanged(java.lang.Boolean)
	 */
	public void driveIsReferencedChanged(Boolean _isRef) {
	}
}