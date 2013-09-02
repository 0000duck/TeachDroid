package com.keba.teachdroid.app.fragments;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.MessageAdapter;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;

public class InfoAlarmFragmentMain extends Fragment {
	// ConfirmMessageDialog dialog;
	CheckBox						cb;
	final private List<Message>		lastThree	= new Vector<Message>();	;
	private MessageAdapter<Message>	adp;

	public InfoAlarmFragmentMain() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_load_info_main, container, false);

		ListView listview = (ListView) rootView.findViewById(R.id.actualMessages);

		adp = new MessageAdapter<Message>(getActivity(), R.layout.default_list_item, lastThree);

		RobotControlProxy.addObserver(new Observer() {

			public void update(Observable _observable, Object _data) {
				loadThreeMessages();
			}
		});

		listview.setAdapter(adp);
		loadThreeMessages();

		// listview.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View _v) {
		// rootView.get
		// }
		// });
		return rootView;
	}

	private void loadThreeMessages() {

		if (getActivity() != null) {

			new Handler(getActivity().getMainLooper()).post(new Runnable() {

				public void run() {
					List<Message> tmp = RobotControlProxy.getMessageBacklog();

					if (tmp.size() > 3)
						tmp = lastThree.subList(0, 3);

					lastThree.clear();
					lastThree.addAll(tmp);

					adp.notifyDataSetChanged();
				}
			});

		}
	}

}