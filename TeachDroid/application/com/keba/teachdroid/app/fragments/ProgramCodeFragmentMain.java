package com.keba.teachdroid.app.fragments;

import java.util.concurrent.ExecutionException;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtOverrideChangedListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramMode;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramState;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.teachdroid.app.MainActivity;
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;

public class ProgramCodeFragmentMain extends Fragment implements KvtProgramStateListener, KvtOverrideChangedListener, Touchable {

	private TextView		mProgName, mProgMode;
	private ImageView		mProgStateIcon;
	private ProgressBar		mOverride;
	private LinearLayout	mProgStateLayout;

	public ProgramCodeFragmentMain() {

		KvtProgramStateMonitor.addListener(this);
		KvtPositionMonitor.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_prog_code_main, container, false);

		// label for the loaded program's name
		mProgName = (TextView) rootView.findViewById(R.id.programNameLabel);
		String program = !KvtProgramStateMonitor.getLoadedProgram().equals("-") ? KvtProgramStateMonitor.getLoadedProgram()
				: "No program loaded";
		mProgName.setText(program);
		// KvtProgram p = RobotControlProxy.getLoadedProgram();
		// if (p != null)
		// loadedProgramNameChanged(p.getName());

		// label for the project name
		mProgMode = (TextView) rootView.findViewById(R.id.programModeLabel);
		ProgramMode m = KvtProgramStateMonitor.getProgramMode();
		if (m != null)
			loadedProgramModeChanged(m);

		// mProgName.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View _v) {
		// showProjectsView(_v);
		// }
		// });
		
		// system override
		mOverride = (ProgressBar) rootView.findViewById(R.id.overrideBarMain);
		int ovr = RobotControlProxy.getOverride();
		overrideChanged(ovr);

		mProgStateLayout = (LinearLayout) rootView.findViewById(R.id.programStateLayout);

		mProgStateIcon = (ImageView) rootView.findViewById(R.id.programStateIcon);
		ProgramState s = KvtProgramStateMonitor.getProgramState();
		if (s != null)
			loadedProgramStateChanged(s);

		// mProgStateLayout.setOnClickListener(new OnClickListener() {
		// mProgStateIcon.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View _v) {
		// startStopProgram(_v);
		// }
		// });
		

		boolean b = KvtProgramStateMonitor.getNoProgramRunning();
		isAnyProgramRunning(b);

		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #loadedProgramNameChanged(java.lang.String)
	 */
	@Override
	public void loadedProgramNameChanged(final String _programName) {
		if (getActivity() == null)
			return;
		new Handler(getActivity().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				String program = _programName.equals("-") ? "No program loaded" : _programName;
				mProgName.setText(program);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #
	 * loadedProgramModeChanged(com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor
	 * .ProgramMode)
	 */
	@Override
	public void loadedProgramModeChanged(final ProgramMode _newMode) {
		if (getActivity() == null)
			return;
		new Handler(getActivity().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				mProgMode.setText(_newMode.toString());
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #loadedProgramStateChanged(com.keba.kemro.kvs.teach.util.
	 * KvtProgramStateMonitor.ProgramState)
	 */
	@Override
	public void loadedProgramStateChanged(ProgramState _newState) {
		switch (_newState) {

		case eProgStateStopped:
			setStateIcon(R.drawable.program_state_run);
			break;
		case eProgStateInterrupted:
			setStateIcon(R.drawable.program_state_run);
			break;

		case eProgStateRunning:
			setStateIcon(R.drawable.program_state_pause);
			break;

		default:
			setStateIcon(R.drawable.program_state_stop);
			break;
		}
	}

	private void setStateIcon(final int _resouceId) {
		if (getActivity() == null)
			return;
		Handler h = new Handler(getActivity().getMainLooper());
		h.post(new Runnable() {

			@Override
			public void run() {
				mProgStateIcon.setImageDrawable(getActivity().getResources().getDrawable(_resouceId));
				mProgStateIcon.invalidate();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #isAnyProgramRunning(boolean)
	 */
	@Override
	public void isAnyProgramRunning(final boolean _isAnyRunning) {
		if (getActivity() == null)
			return;
		Handler h = new Handler(getActivity().getMainLooper());
		h.post(new Runnable() {

			@Override
			public void run() {
				mProgMode.setEnabled(_isAnyRunning);
				mProgStateIcon.setEnabled(_isAnyRunning);
				mProgName.setEnabled(_isAnyRunning);
				mOverride.setEnabled(_isAnyRunning);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtOverrideChangedListener
	 * #overrideChanged(java.lang.Number)
	 */
	@Override
	public void overrideChanged(Number _override) {
		if (getActivity() == null)
			return;

		int f = _override.intValue();
		if (f > 100)
			f = 100;
		if (f < 0)
			f = 0;
		final int val = f;
		new Handler(getActivity().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				mOverride.setProgress((int) val);
			}
		});
	}

	/**
	 * 
	 */
	public boolean startStopProgram() {
		String loadedProgram = KvtProgramStateMonitor.getLoadedProgram();
		if (!loadedProgram.equals("-")) {
			String proj = loadedProgram.substring(0, KvtProgramStateMonitor.getLoadedProgram().indexOf('.'));
			KvtProject project = KvtProjectAdministrator.getProject(proj);
			String prog = loadedProgram.substring(KvtProgramStateMonitor.getLoadedProgram().indexOf('.') + 1);
			if (project == null) {
				return false;
			}
			KvtProgram program = project.getProgram(prog);

			switch (KvtProgramStateMonitor.getProgramState()) {
			case eProgStateStopped:
			case eProgStateInterrupted:
				// KvtExecutionMonitor.startProgram(program);
				try {
					boolean success = new AsyncTask<KvtProgram, Integer, Boolean>() {

						@Override
						protected Boolean doInBackground(KvtProgram... _params) {
							KvtExecutionMonitor.startProgram(_params[0]);
							// return
							// KvtProjectAdministrator.startProgram(_params[0]);
							return true;
						}

					}.execute(program).get();
					Log.i("ProgramCodeFragmentMain", "Starting program " + program + ": " + (success ? "successful" : "failed"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

				break;
			case eProgStateRepositioning:
			case eProgStateRunning:
				try {
					boolean success = new AsyncTask<KvtProgram, Integer, Boolean>() {

						@Override
						protected Boolean doInBackground(KvtProgram... _params) {
							KvtExecutionMonitor.stopProgram(_params[0]);
							return true;
						}
					}.execute(program).get();

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}


				break;
			default:
				Log.d("Program State", "unknown program state");
				return false;
			}
			return true;
		} else {
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getActivity(), "No program loaded", Toast.LENGTH_SHORT).show();
				}
			});
			return false;
		}
	}

	/**
	 * @param _v
	 */
	public void showProjectsView() {
		MainActivity act = ((MainActivity) getActivity());
		act.onShowProjects();
	}

	/**
	 * @param _event
	 * @return
	 */
	@Override
	public void handleTouch(MotionEvent _event) {
		float x = _event.getX();
		float y = _event.getY();
		Rect rect = new Rect();
		System.out.println("X: " + x + " Y: " + y);
		mProgStateIcon.getHitRect(rect);
		int a = 0;
		if (rect.contains((int) x, (int) y)) {
			a = 1;
			if(!startStopProgram()) {
				showProjectsView();
			}
		} else {
			a = 2;
			showProjectsView();
		}

	}

}
