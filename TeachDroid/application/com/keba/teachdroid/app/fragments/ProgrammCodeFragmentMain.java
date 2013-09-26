package com.keba.teachdroid.app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.keba.teachdroid.app.R;
import com.keba.teachdroid.data.RobotControlProxy;

public class ProgrammCodeFragmentMain extends Fragment implements KvtProgramStateListener, KvtOverrideChangedListener {

	private TextView mProgName, mProgMode;
	private ImageView mProgStateIcon;
	private ProgressBar mOverride;
	private LinearLayout mProgStateLayout;

	public ProgrammCodeFragmentMain() {

		KvtProgramStateMonitor.addListener(this);
		KvtPositionMonitor.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_prog_code_main, container, false);

		// label for the loaded program's name
		mProgName = (TextView) rootView.findViewById(R.id.programNameLabel);
		mProgName.setText(KvtProgramStateMonitor.getLoadedProgram());
		// KvtProgram p = RobotControlProxy.getLoadedProgram();
		// if (p != null)
		// loadedProgramNameChanged(p.getName());

		// label for the project name
		mProgMode = (TextView) rootView.findViewById(R.id.programModeLabel);
		ProgramMode m = KvtProgramStateMonitor.getProgramMode();
		if (m != null)
			loadedProgramModeChanged(m);

		// system override
		mOverride = (ProgressBar) rootView.findViewById(R.id.overrideBarMain);
		int ovr = RobotControlProxy.getOverride();
		overrideChanged(ovr);

		mProgStateIcon = (ImageView) rootView.findViewById(R.id.programStateIcon);
		ProgramState s = KvtProgramStateMonitor.getProgramState();
		if (s != null)
			loadedProgramStateChanged(s);

		mProgStateLayout = (LinearLayout) rootView.findViewById(R.id.programStateLayout);
		mProgStateLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View _v) {
				String loadedProgram = KvtProgramStateMonitor.getLoadedProgram();
				if (!loadedProgram.equals("-")) {
					String proj = loadedProgram.substring(0, KvtProgramStateMonitor.getLoadedProgram().indexOf('.'));
					KvtProject project = KvtProjectAdministrator.getProject(proj);
					String prog = loadedProgram.substring(KvtProgramStateMonitor.getLoadedProgram().indexOf('.') + 1);
					KvtProgram program = project.getProgram(prog);

					switch (KvtProgramStateMonitor.getProgramState()) {
					case eProgStateStopped:
						KvtExecutionMonitor.startProgram(program);
						break;
					case eProgStateInterrupted:
						KvtExecutionMonitor.startProgram(program);
						break;
					default:
						Toast.makeText(getActivity(), "No program loaded", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
		});

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
	public void loadedProgramNameChanged(final String _programName) {
		if (getActivity() == null)
			return;
		new Handler(getActivity().getMainLooper()).post(new Runnable() {

			public void run() {
				mProgName.setText(_programName);
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
	public void loadedProgramModeChanged(final ProgramMode _newMode) {
		if (getActivity() == null)
			return;
		new Handler(getActivity().getMainLooper()).post(new Runnable() {

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
	public void loadedProgramStateChanged(ProgramState _newState) {
		switch (_newState) {

		case eProgStateStopped:
			setStateIcon(R.drawable.program_state_stop);
			break;
		case eProgStateInterrupted:
			setStateIcon(R.drawable.program_state_pause);
			break;

		case eProgStateRunning:
			setStateIcon(R.drawable.program_state_run);
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
	public void isAnyProgramRunning(final boolean _isAnyRunning) {
		if (getActivity() == null)
			return;
		Handler h = new Handler(getActivity().getMainLooper());
		h.post(new Runnable() {

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

			public void run() {
				mOverride.setProgress((int) val);
			}
		});
	}

}
