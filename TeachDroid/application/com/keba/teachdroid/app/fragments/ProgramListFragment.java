package com.keba.teachdroid.app.fragments;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;

public class ProgramListFragment extends ListFragment implements KvtProjectAdministratorListener {
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private SelectionCallback mCallbacks = sDummyCallbacks;

	/**
	 * A dummy implementation of the {@link SelectionCallback} interface that
	 * does nothing. Used only when this fragment is not attached to an
	 * activity.
	 */
	private static SelectionCallback sDummyCallbacks = new SelectionCallback() {

		@Override
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

	public ProgramListFragment() {
		KvtProjectAdministrator.addProjectListener(this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setLongClickable(true);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> _view, View _source, int _pos, long _id) {
				KvtProgram prg = (KvtProgram) getListAdapter().getItem(_pos);
				displayCloseDialog(prg, _pos);
				return true;

			}
		});
	}

	/**
	 * @param _prg
	 * 
	 */
	protected void displayCloseDialog(final KvtProgram _prg, final int _index) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

		final boolean canClose = _prg.getProgramState() > KvtProgram.LOADED;

		String action = canClose ? "close" : "open";

		// set title
		alertDialogBuilder.setTitle(action + " program?");

		// set dialog message
		alertDialogBuilder.setMessage("Do you really want to " + action + " \"" + _prg.getName() + "\"?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if (canClose) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							KvtExecutionMonitor.closeProgram(_prg);
						}
					}).start();

				}

				else
					mCallbacks.onProgramSelected(_index);
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

		// workaround for problem with nested fragments!!! taken from here:
		// http://stackoverflow.com/a/15656428/2741812
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onProgramSelected(position);
	}

	/**
	 * @param _prg
	 */
	public void updateProgramState(KvtProgram _prg) {
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
		Context c = getActivity();
		if (c != null) {
			// getListAdapter().
		}
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
	}
}
