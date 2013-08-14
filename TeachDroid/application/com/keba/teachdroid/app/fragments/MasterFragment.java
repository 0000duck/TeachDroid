package com.keba.teachdroid.app.fragments;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 94239349838371051L;
	private transient View mRootView;
	private transient LayoutInflater inflater;
	private transient List<KvtProgram> programs;
	private int[] iconIds = { R.drawable.program_state_pause, R.drawable.program_state_stop, R.drawable.program_state_run, R.drawable.program_state_pause, R.drawable.program_state_stop, R.drawable.program_state_run, R.drawable.program_state_pause,
			R.drawable.program_state_stop, R.drawable.program_state_run };
	private ProjectActivity mCallback;
	private transient ArrayAdapter<String> mProjectAdapter;
	private List<String> programStrings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		mRootView = inflater.inflate(R.layout.fragment_master, container, false);
		mCallback = ((ProjectActivity) getArguments().getSerializable("connector"));
		ListFragment listFragment = (ListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_inner_list);
		List<KvtProject> projects = mCallback.getProjects();
		mProjectAdapter = new ArrayAdapter<String>(getActivity(), R.layout.default_list_item);
		for (KvtProject p : projects) {
			mProjectAdapter.add(p.toString());
		}
		listFragment.setListAdapter(mProjectAdapter);

		return mRootView;
	}

	public void detailSelected(int id) {
		InnerDetailFragment fragment = new InnerDetailFragment();
		programs = ((ProjectActivity) getArguments().getSerializable("connector")).getPrograms();
		programStrings = new Vector<String>();
		for (KvtProgram p : programs) {
			programStrings.add(p.toString());
		}

		fragment.setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.program_list_item, programStrings) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = inflater.inflate(R.layout.program_list_item, null);

				TextView text = (TextView) row.findViewById(R.id.text);
				text.setText(programStrings.get(position));
				text.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(iconIds[position]), null);

				return row;
			}
		});
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inner_detail, fragment).commit();
	}
}
