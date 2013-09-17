package com.keba.teachdroid.app.fragments;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.teachdroid.app.R;

public class ProjectListFragment extends Fragment implements Serializable, KvtProjectAdministratorListener {
	/**
	 * @author ltz
	 * 
	 */
	private class ProgramListAdapter extends ArrayAdapter<KvtProgram> {
		/**
		 * 
		 */
		private final KvtProgram[]	mProgs;

		/**
		 * @param _context
		 * @param _textViewResourceId
		 * @param _objects
		 * @param _progs
		 */
		private ProgramListAdapter(Context _context, int _textViewResourceId, KvtProgram[] _objects, KvtProgram[] _progs) {
			super(_context, _textViewResourceId, _objects);
			mProgs = _progs;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View row = inflater.inflate(R.layout.simple_text_list_item, null);

			TextView text = (TextView) row.findViewById(R.id.text);
			text.setText(mProgs[position].getName());
			int progState = mProgs[position].getProgramState();
			// int drawableId = R.drawable.program_state_stop;

			Resources res = getContext().getResources();
			Drawable drw = null;

			switch (progState) {
			case KvtProgram.FINISHED:
				drw = res.getDrawable(R.drawable.program_state_stop);
				break;
			case KvtProgram.LOADED:
				// drw = null;
				drw = res.getDrawable(R.drawable.program_state_ready);
				break;
			case KvtProgram.NOT_USED:
				drw = res.getDrawable(R.drawable.program_state_unknown);
				break;
			case KvtProgram.OPEN:
				drw = res.getDrawable(R.drawable.program_state_ready);
				break;
			case KvtProgram.RUNNING:
				drw = res.getDrawable(R.drawable.program_state_run);
				break;
			case KvtProgram.STEPPING:
				drw = res.getDrawable(R.drawable.program_state_stop);
				break;
			case KvtProgram.STOPPED:
				drw = res.getDrawable(R.drawable.program_state_pause);
				break;
			case KvtProgram.WAITING:
				drw = res.getDrawable(R.drawable.program_state_pause);
				break;

			default:
				drw = res.getDrawable(R.drawable.program_state_unknown);
			}

			text.setCompoundDrawablesWithIntrinsicBounds(null, null, drw, null);
			// text.setOnTouchListener(new RightDrawableOnTouchListener(text) {
			//
			// @Override
			// public boolean onDrawableTouch(MotionEvent _event) {
			// return startStopProgram(mProgs[position]);
			// }
			//
			// });

			return row;
		}
	}

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 94239349838371051L;
	private transient View						mRootView;
	private transient LayoutInflater			inflater;
	// private ProjectActivity mCallback;
	private transient ArrayAdapter<KvtProject>	mProjectAdapter;
	private ProgramListFragment					mProgramFragment;
	private Context								mContext;

	public ProjectListFragment() {
		KvtProjectAdministrator.addProjectListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		mRootView = inflater.inflate(R.layout.fragment_projectlist, container, false);

		mContext = getActivity();

		ListFragment projectListFragment = (ListFragment) ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(
				R.id.fragment_inner_list);

		KvtProject[] projects = KvtProjectAdministrator.getAllProjects();

		Vector<KvtProject> plist = new Vector<KvtProject>();
		for (KvtProject p : projects)
			plist.add(p);

		mProjectAdapter = new ProjectArrayAdapter(mContext, R.layout.simple_text_list_item, plist);
		projectListFragment.setListAdapter(mProjectAdapter);

		return mRootView;
	}

	public void detailSelected(int id) {
		mProgramFragment = new ProgramListFragment();
		final KvtProgram[] progs = mProjectAdapter.getItem(id).getPrograms();

		mProgramFragment.setListAdapter(new ProgramListAdapter(mContext, R.layout.simple_text_list_item, progs, progs));
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
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				KvtProject[] projects = KvtProjectAdministrator.getAllProjects();

				// mProjectAdapter = new ArrayAdapter<KvtProject>(getActivity(),
				// R.layout.default_list_item);
				mProjectAdapter.clear();
				// projectListFragment.setListAdapter(mProjectAdapter);

				if (projects != null)
					for (KvtProject p : projects) {
						// if (!p.isSystemProject())
						mProjectAdapter.add(p);
					}

				mProjectAdapter.notifyDataSetChanged();
			}
		});

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
		final KvtProgram[] progs = _prg.getParent().getPrograms();
		try {
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					if (mProgramFragment != null) {
						mProgramFragment.setListAdapter(new ProgramListAdapter(mContext, R.layout.simple_text_list_item, progs, progs));
						((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
								.replace(R.id.fragment_inner_detail, mProgramFragment).commit();
					}
				}
			});
		} catch (IllegalStateException e) {
			Log.d("ProjectListFragment", e.toString());
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
		KvtProjectAdministrator.getCurrentRunningProjects();
		KvtProjectAdministrator.getAllProjects();

		final List<KvtProject> proj = KvtProjectAdministrator.getAllProjectsList();

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mProjectAdapter.clear();
					mProjectAdapter.addAll(proj);
					mProjectAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	private class ProjectArrayAdapter extends ArrayAdapter<KvtProject> {

		private List<KvtProject>	mProjects;

		/**
		 * @param _context
		 * @param _textViewResourceId
		 * @param _objects
		 */
		private ProjectArrayAdapter(Context _context, int _textViewResourceId, List<KvtProject> _objects) {
			super(_context, _textViewResourceId, _objects);
			mProjects = _objects;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View row = inflater.inflate(R.layout.simple_text_list_item, null);

			TextView text = (TextView) row.findViewById(R.id.text);
			text.setText(mProjects.get(position).getName());
			int projState = mProjects.get(position).getProjectState();

			Resources res = getContext().getResources();
			Drawable drw = null;

			if (projState >= KvtProject.SUCCESSFULLY_LOADED)
				drw = res.getDrawable(R.drawable.project_state_loaded);
			else if (projState >= KvtProject.BUILDED_WITHOUT_ERROR) {
				drw = res.getDrawable(R.drawable.project_state_open);
			} else
				drw = res.getDrawable(R.drawable.project_state_closed);

			text.setCompoundDrawablesWithIntrinsicBounds(drw, null, null, null);

			return row;
		}

	}
}
