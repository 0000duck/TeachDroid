package com.keba.teachdroid.app.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;


public class InnerListFragment extends ListFragment {
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private SelectionCallback			mCallbacks		= sDummyCallbacks;

	// private ProjectArrayAdapter mAdapter;
	private transient LayoutInflater	mInflater;

	/**
	 * A dummy implementation of the {@link SelectionCallback} interface that
	 * does nothing. Used only when this fragment is not attached to an
	 * activity.
	 */
	private static SelectionCallback	sDummyCallbacks	= new SelectionCallback() {

															@Override
															public void onProjectSelected(int id) {
															}
														};

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface SelectionCallback {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onProjectSelected(int id);
	}

	public InnerListFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mAdapter = new ArrayAdapter<String>(getActivity(),
		// R.layout.default_list_item);
		// for (KvtProject p : ((ProjectActivity) getActivity()).getProjects())
		// {
		// mAdapter.add(p.toString());
		// }
		// setListAdapter(mAdapter);


	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// mInflater = inflater;
	// List<KvtProject> projs = ((ProjectActivity) getActivity()).getProjects();
	// setListAdapter(new ProjectArrayAdapter(getActivity(),
	// R.layout.simple_text_list_item, projs));
	// return super.onCreateView(inflater, container, savedInstanceState);
	// }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Activities containing this fragment must implement its Callbacks.
		if (!(activity instanceof SelectionCallback)) {
			Log.w("ProjectListFragment", "Activity must implement fragment's callbacks.");
			mCallbacks = sDummyCallbacks;
		} else
			mCallbacks = (SelectionCallback) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active Callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onProjectSelected(position);
	}
}
