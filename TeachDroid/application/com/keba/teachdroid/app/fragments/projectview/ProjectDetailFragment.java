package com.keba.teachdroid.app.fragments.projectview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.teachdroid.app.R;

/**
 * A fragment representing a single Project detail screen. This fragment is
 * either contained in a {@link ProjectListActivity} in two-pane mode (on
 * tablets) or a {@link ProjectDetailActivity} on handsets.
 */
public class ProjectDetailFragment extends Fragment implements KvtProjectAdministratorListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String		ARG_ITEM_ID	= "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private KvtProject				mProject;

	private View					mRootView;

	private ProgramListRowAdapter	mAdapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProjectDetailFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_project_detail, container, false);

		KvtProjectAdministrator.addProjectListener(this);

		// if (getArguments() != null && getArguments().containsKey("project"))
		// {
		// KvtProject p = (KvtProject)
		// getArguments().getSerializable("project");
		// if (p != null)
		// mItem = p;
		// }
		updateUI();

		return mRootView;
	}

	/**
	 * @param rootView
	 */
	private synchronized void updateUI() {
		if (mProject != null && mRootView != null) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					final ListView lv = ((ListView) mRootView.findViewById(R.id.projectDetailList));

					if (mAdapter == null) {

						mAdapter = new ProgramListRowAdapter(getActivity(), mProject);
						lv.setAdapter(mAdapter);

					} else {
						mAdapter.setParent(mProject);
					}
					TextView tv = (TextView) mRootView.findViewById(R.id.projectNameTV);
					tv.setText(mProject.getName());
				}
			});
		}
	}

	/**
	 * @param _p
	 */
	public void setProject(KvtProject _p) {

		mProject = _p;
		updateUI();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		KvtProjectAdministrator.removeProjectListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProject)
	 */
	public void projectStateChanged(KvtProject _prj) {
		if (_prj.getName().equals(mProject.getName())) {
			setProject(_prj);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #programStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProgram)
	 */
	public void programStateChanged(KvtProgram _prg) {

		KvtProgram prg = mProject.getProgram(_prg.getName());
		if (prg != null) {
			KvtProject prj = prg.getParent();
			if (prj != null) {
				mAdapter = null;
				setProject(prj);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectListChanged()
	 */
	public void projectListChanged() {

	}
}
