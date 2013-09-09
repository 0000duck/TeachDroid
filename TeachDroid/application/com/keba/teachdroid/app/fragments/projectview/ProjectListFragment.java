package com.keba.teachdroid.app.fragments.projectview;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.teachdroid.data.RobotControlProxy;

/**
 * A list fragment representing a list of Projects. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ProjectDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the
 * {@link SelectionCallback} interface.
 */
public class ProjectListFragment extends ListFragment implements KvtProjectAdministratorListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String			STATE_ACTIVATED_POSITION	= "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private SelectionCallback			mCallbacks					= sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int							mActivatedPosition			= ListView.INVALID_POSITION;

	private ArrayAdapter<KvtProject>	mAdapter;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface SelectionCallback {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(KvtProject id);
	}

	/**
	 * A dummy implementation of the {@link SelectionCallback} interface that
	 * does nothing. Used only when this fragment is not attached to an
	 * activity.
	 */
	private static SelectionCallback	sDummyCallbacks	= new SelectionCallback() {

															public void onItemSelected(KvtProject id) {
															}
														};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProjectListFragment() {
		KvtProjectAdministrator.addProjectListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KvtProjectAdministrator.addProjectListener(this);
		mAdapter = new ArrayAdapter<KvtProject>(getActivity(), android.R.layout.simple_list_item_activated_1, android.R.id.text1,
				new Vector<KvtProject>());
		setListAdapter(mAdapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// if (RobotControlProxy.isConnected())
		// // TODO: replace with contents from a listener
		// setListAdapter(new ArrayAdapter<KvtProject>(getActivity(),
		// android.R.layout.simple_list_item_activated_1, android.R.id.text1,
		// RobotControlProxy.getProjects()));

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
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

		mCallbacks.onItemSelected(RobotControlProxy.getProjects().get(position));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProject)
	 */
	public void projectStateChanged(KvtProject _prj) {
		// int ix = getIndexOfProject(_prj);
		// if (ix >= 0) {
		// KvtProject p = (KvtProject) getListAdapter().getItem(ix);
		//
		// }
		projectListChanged();

	}

	/**
	 * @param _prj
	 */
	private int getIndexOfProject(KvtProject _prj) {
		for (int i = 0; i < getListAdapter().getCount(); i++) {
			KvtProject p = (KvtProject) getListAdapter().getItem(i);
			if (p.getName().equals(_prj.getName()))
				return i;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #programStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProgram)
	 */
	public void programStateChanged(KvtProgram _prg) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectListChanged()
	 */
	public void projectListChanged() {

		final List<KvtProject> projs = getProjects(false);
		if (projs != null && !projs.isEmpty()) {
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					mAdapter.clear();
					mAdapter.addAll(projs);
					mAdapter.notifyDataSetChanged();
				}
			});

		}

	}

	/**
	 * @param _includeSystemProj
	 * @return
	 */
	private List<KvtProject> getProjects(boolean _includeSystemProj) {
		KvtProject[] all = KvtProjectAdministrator.getAllProjects();
		ArrayList<KvtProject> list = new ArrayList<KvtProject>();
		for (KvtProject p : all) {
			if (!_includeSystemProj && p.isSystemProject()) {
				continue;
			}
			list.add(p);

		}
		return list;
	}
}
