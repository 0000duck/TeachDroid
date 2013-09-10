package com.keba.teachdroid.app.fragments;

import java.io.Serializable;
import java.util.List;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.teachdroid.app.R;

public class ProjectListFragment extends Fragment implements Serializable, KvtProjectAdministratorListener {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 94239349838371051L;
	private transient View						mRootView;
	private transient LayoutInflater			inflater;
	private transient List<KvtProgram>			programs;
	private int[]								iconIds				= { R.drawable.program_state_pause, R.drawable.program_state_stop,
			R.drawable.program_state_run, R.drawable.program_state_pause, R.drawable.program_state_stop, R.drawable.program_state_run,
			R.drawable.program_state_pause, R.drawable.program_state_stop, R.drawable.program_state_run };
	// private ProjectActivity mCallback;
	private transient ArrayAdapter<KvtProject>	mProjectAdapter;
	private List<String>						programStrings;
	private ProgramListFragment	mProgramFragment;

	public ProjectListFragment() {
		KvtProjectAdministrator.addProjectListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		mRootView = inflater.inflate(R.layout.fragment_projectlist, container, false);

		// mCallback = ((ProjectActivity)
		// getArguments().getSerializable("connector"));

		ListFragment listFragment = (ListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_inner_list);

		// List<KvtProject> projects = mCallback.getProjects();
		KvtProject[] projects = KvtProjectAdministrator.getAllProjects();

		mProjectAdapter = new ArrayAdapter<KvtProject>(getActivity(), R.layout.default_list_item);
		listFragment.setListAdapter(mProjectAdapter);

		if (projects != null)
			for (KvtProject p : projects) {
				// if (!p.isSystemProject())
				mProjectAdapter.add(p);
			}

		return mRootView;
	}

	public void detailSelected(int id) {
		mProgramFragment = new ProgramListFragment();
		final KvtProgram[] progs = mProjectAdapter.getItem(id).getPrograms();

		mProgramFragment.setListAdapter(new ArrayAdapter<KvtProgram>(getActivity(), R.layout.program_list_item, progs) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = inflater.inflate(R.layout.program_list_item, null);

				TextView text = (TextView) row.findViewById(R.id.text);
				text.setText(progs[position].getName());
				int progState = progs[position].getProgramState();
//				int drawableId = R.drawable.program_state_stop;
				
				Resources res = getContext().getResources();
				Drawable drw = null;
				
				
				switch (progState) {
				case KvtProgram.FINISHED:
					drw= res.getDrawable(R.drawable.program_state_stop);
					break;
				case KvtProgram.LOADED:
					drw=null;
					break;
				case KvtProgram.NOT_USED:
					drw= res.getDrawable(R.drawable.program_state_stop);
					break;
				case KvtProgram.OPEN:
					drw= res.getDrawable(R.drawable.program_state_pause);
					break;
				case KvtProgram.RUNNING:
					drw= res.getDrawable(R.drawable.program_state_run);
					break;
				case KvtProgram.STEPPING:
					drw= res.getDrawable(R.drawable.program_state_pause);
					break;
				case KvtProgram.STOPPED:
					drw= res.getDrawable(R.drawable.program_state_pause);
					break;
				case KvtProgram.WAITING:
					drw= res.getDrawable(R.drawable.program_state_pause);
					break;

				}
				
				
				text.setCompoundDrawablesWithIntrinsicBounds(null, null, drw, null);

				return row;
			}
		});
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inner_detail, mProgramFragment).commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProject)
	 */
	@Override
	public void projectStateChanged(KvtProject _prj) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #programStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProgram)
	 */
	@Override
	public void programStateChanged(KvtProgram _prg) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectListChanged()
	 */
	@Override
	public void projectListChanged() {
		KvtProject[] projs = KvtProjectAdministrator.getCurrentRunningProjects();
		KvtProject[] allprojs = KvtProjectAdministrator.getAllProjects();

		int a = 0;
	}
}
