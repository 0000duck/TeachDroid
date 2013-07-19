package com.keba.teachdroid.app.fragments.projectview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.teachdroid.app.R;

/**
 * A fragment representing a single Project detail screen. This fragment is
 * either contained in a {@link ProjectListActivity} in two-pane mode (on
 * tablets) or a {@link ProjectDetailActivity} on handsets.
 */
public class ProjectDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String		ARG_ITEM_ID	= "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private KvtProject				mItem;

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
	private void updateUI() {
		if (mItem != null && mRootView != null) {

			ListView lv = ((ListView) mRootView.findViewById(R.id.project_detail_list));

			if (mAdapter == null) {
				mAdapter = new ProgramListRowAdapter(getActivity(), mItem);
				lv.setAdapter(mAdapter);
			} else {
				mAdapter.setParent(mItem);
				mAdapter.notifyDataSetChanged();
			}
			TextView tv = (TextView) mRootView.findViewById(R.id.projectNameTV);
			tv.setText(mItem.getName());

		}
	}

	/**
	 * @param _p
	 */
	public void setProject(KvtProject _p) {

		mItem = _p;
		updateUI();
	}
}
