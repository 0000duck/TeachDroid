package com.keba.teachdroid.app.fragments.projectview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
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
	public static final String	ARG_ITEM_ID	= "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private KvtProject			mItem;

	private View				mRootView;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProjectDetailFragment() {
	}

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	//
	//
	//
	// if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
	// // Load the dummy content specified by the fragment
	// // arguments. In a real-world scenario, use a Loader
	// // to load content from a content provider.
	// // mItem =
	// // DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
	// String name = getArguments().getString(ARG_ITEM_ID);
	// for (KvtProject proj : RobotControlProxy.getProjects()) {
	// if (proj.getName().equals(name)) {
	// mItem = proj;
	// break;
	// }
	//
	// }
	// }
	//
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_project_detail, container, false);

		if (getArguments() != null && getArguments().containsKey("project")) {
			KvtProject p = (KvtProject) getArguments().getSerializable("project");
			if (p != null)
				mItem = p;
		}
		updateUI();

		return mRootView;
	}

	/**
	 * @param rootView
	 */
	private void updateUI() {
		// Show the dummy content as text in a TextView.
		if (mItem != null && mRootView != null) {
			// ((TextView)
			// rootView.findViewById(R.id.project_detail)).setText(mItem.content);
			// ((TextView)
			// rootView.findViewById(R.id.project_detail_list)).setText(mItem.getName());
			ListView lv = ((ListView) mRootView.findViewById(R.id.project_detail_list));
			lv.setAdapter(new ArrayAdapter<KvtProgram>(getActivity(), android.R.layout.simple_list_item_activated_1, android.R.id.text1, mItem
					.getPrograms()));

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
