/**
 * 
 */
package com.keba.teachdroid.app.fragments;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.teachdroid.app.IConnectCallback;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;
import com.keba.teachdroid.util.PreferenceManager;

/**
 * @author ltz
 * 
 */
public class ConnectFragment extends Fragment {

	private IConnectCallback	mCallback;
	private View				mRootView;

	public ConnectFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_connect, container, false);

		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {

			public void teachviewDisconnected() {
				getActivity().runOnUiThread(new Runnable() {

					public void run() {
						Button connect = (Button) mRootView.findViewById(R.id.connectButton);
						connect.setText(getActivity().getString(R.string.action_connect));
						connect.invalidate();
					}
				});

			}

			public void teachviewConnected() {
				getActivity().runOnUiThread(new Runnable() {

					public void run() {
						Button connect = (Button) mRootView.findViewById(R.id.connectButton);
						connect.setText(getActivity().getString(R.string.action_disconnect));
						connect.invalidate();
					}
				});

			}
		});

		// extract the connector
		Serializable con = getArguments() == null ? null : getArguments().getSerializable("connector");
		if (con != null && con instanceof IConnectCallback)
			mCallback = (IConnectCallback) con;

		// set ip address
		EditText ip = (EditText) mRootView.findViewById(R.id.ipAddress);
		ip.setText(PreferenceManager.getInstance().getHostname());

		// add button callback
		Button connect = (Button) mRootView.findViewById(R.id.connectButton);
		connect.setOnClickListener(new OnClickListener() {

			public void onClick(View _v) {
				// connect
				if (mCallback != null) {
					if (RobotControlProxy.isConnected())
						mCallback.disconnect();
					else
						mCallback.connect();
				}

				// switch to next section
			}
		});

		return mRootView;
	}

}
