//package com.keba.teachdroid.app.fragments;
//
//import com.keba.teachdroid.app.R;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//public class InnerDetailFragment extends Fragment {
//
//	private ArrayAdapter<String> mAdapter;
//	private ListView l;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View mRootView = inflater.inflate(R.layout.fragment_inner_detail, container, false);
//		l = (ListView) mRootView.findViewById(R.id.listView1);
//		l.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, getArguments().getStringArray("programs")));
//		return mRootView;
//	}
//
//}

package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InnerDetailFragment extends ListFragment {
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private SelectionCallback mCallbacks = sDummyCallbacks;
	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private ArrayAdapter<String> mAdapter;

	/**
	 * A dummy implementation of the {@link SelectionCallback} interface that
	 * does nothing. Used only when this fragment is not attached to an
	 * activity.
	 */
	private static SelectionCallback sDummyCallbacks = new SelectionCallback() {

		public void onProgramSelected(int id) {
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
		public void onProgramSelected(int id);
	}

	public InnerDetailFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.default_list_item, ((ProjectActivity) getActivity()).getPrograms());
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof SelectionCallback)) {
			Log.w("ProjectListFragment", "Activity must implement fragment's callbacks.");
			mCallbacks = sDummyCallbacks;
		} else
			mCallbacks = (SelectionCallback) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onProgramSelected(position);
		getListView().setItemChecked(position, true);
	}
}

