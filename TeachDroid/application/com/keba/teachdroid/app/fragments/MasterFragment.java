package com.keba.teachdroid.app.fragments;

import java.io.Serializable;

import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MasterFragment extends Fragment implements Serializable {
	private transient View mRootView;
	private transient LayoutInflater inflater;
	private String[] programs;
	private int[] iconIds={R.drawable.program_state_pause,R.drawable.program_state_stop,R.drawable.program_state_run};
	private ProjectActivity mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		mRootView = inflater.inflate(R.layout.fragment_master, container, false);
		mCallback = ((ProjectActivity) getArguments().getSerializable("connector"));
		ListFragment listFragment = (ListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_inner_list);
		String[] projects = mCallback.getProjects();
		listFragment.setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.default_list_item, projects));

		return mRootView;
	}

	public void detailSelected(int id) {
		InnerDetailFragment fragment = new InnerDetailFragment();
		programs = ((ProjectActivity) getArguments().getSerializable("connector")).getPrograms();
		fragment.setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.program_list_item, programs) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = inflater.inflate(R.layout.program_list_item, null);

				TextView text = (TextView) row.findViewById(R.id.text);
				text.setText(programs[position]);
				text.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(iconIds[position]), null);

				return row;
			}
		});
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inner_detail, fragment).commit();
	}
}
