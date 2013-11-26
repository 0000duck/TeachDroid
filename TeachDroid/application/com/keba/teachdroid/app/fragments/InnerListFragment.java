package com.keba.teachdroid.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;

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
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> _arg0, View _arg1, int _pos, long _id) {
				KvtProject proj = (KvtProject) getListAdapter().getItem(_pos);
				displayCloseDialog(proj, _pos);
				return true;
			}
		});
	}

	protected void displayCloseDialog(final KvtProject _proj, final int _index) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

		final boolean canClose = _proj.getProjectState() >= KvtProject.SUCCESSFULLY_LOADED;

		if (!canClose)
			return;

		// String action = canClose ? "close" : "open";
		String action = "close";

		// set title
		alertDialogBuilder.setTitle(action + " project?");

		// set dialog message
		alertDialogBuilder.setMessage("Do you really want to " + action + " \"" + _proj.getName() + "\"?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (canClose) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									// KvtExecutionMonitor.closeProgram(_proj);
									KvtProjectAdministrator.unloadProject(_proj);
									KvtProjectAdministrator.destroy(_proj);
								}
							}).start();
						}

						else {
							InnerListFragment.this.mCallbacks.onProjectSelected(_index);
						}
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

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
