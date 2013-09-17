package com.keba.teachdroid.app.fragments;

import java.io.Serializable;
import java.util.Locale;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor.KvtExecutionListener;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.KvtMotionModeListener;
import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

public class ProgramCodeFragment extends Fragment implements Serializable, KvtExecutionListener, KvtProjectAdministratorListener,
		KvtMotionModeListener {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7012715919241719807L;
	private transient View		mRootView;
	ProjectActivity				callback;
	transient TextView			t, pcTextfield;
	private KvtProgram			mProgram;
	private ImageButton			mStart;
	private ImageButton			mStop;
	private ImageButton			mExecModeBtn;
	private final int			mLineOffset			= 2;						// line
																				// nr.
																				// 2
																				// is
																				// actually
																				// the
																				// first
																				// code
																				// line!
	private SpannableString		mCodeLines;
	private String				mRawCode;

	public ProgramCodeFragment() {
		KvtExecutionMonitor.addListener(this);
		KvtProjectAdministrator.addProjectListener(this);
		KvtMotionModeAdministrator.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_program_code, container, false);

		mStart = (ImageButton) mRootView.findViewById(R.id.startProgramBtn);
		if (mStart != null) {
			mStart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View _v) {
					if (mProgram != null) {
						KvtExecutionMonitor.startProgram(mProgram);
						mStop.setEnabled(true);
						mStart.setEnabled(false);
					}
				}
			});
		}

		mStop = (ImageButton) mRootView.findViewById(R.id.stopProgramBtn);
		if (mStop != null) {
			mStop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View _v) {
					if (mProgram != null) {
						KvtExecutionMonitor.stopProgram(mProgram);
						mStop.setEnabled(false);
						mStart.setEnabled(true);
					}
				}
			});
		}

		mExecModeBtn = (ImageButton) mRootView.findViewById(R.id.changeExecModeBtn);
		if (mExecModeBtn != null) {
			mExecModeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View _v) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							KvtMotionModeAdministrator.toggleMotionMode();
						}
					}).start();

				}
			});
		}
		callback = (ProjectActivity) getArguments().getSerializable("connector");
		t = (TextView) mRootView.findViewById(R.id.programCode);
		t.setText(callback.getProgramCode());

		pcTextfield = (TextView) mRootView.findViewById(R.id.pcTextfield);
		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		t.setText(callback.getProgramCode());
	}

	public void setProgramCode() {
		mRawCode = callback.getProgramCode();

		mCodeLines = convertSourceCode(mRawCode);
		t.setText(mCodeLines, BufferType.SPANNABLE);

	}

	private SpannableString convertSourceCode(String _rawCode) {
		if (_rawCode != null) {
			StringBuffer bf = new StringBuffer();
			String[] s = _rawCode.split("\n");

			for (String line : s) {
				if (!line.toLowerCase(Locale.getDefault()).contains("kairoversion") && !line.isEmpty() && !line.contains(">>>EOF<<<")) {
					bf.append(line);
					bf.append("\n");
				}
			}
			String clearedCode = bf.toString();
			return new SpannableString(clearedCode);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtExecutionMonitor.KvtExecutionListener
	 * #programCounterChanged(int)
	 */
	@Override
	public void programCounterChanged(final int _line) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				if (pcTextfield != null && _line >= mLineOffset) {
					pcTextfield.setText("PC: " + (_line - mLineOffset));
					pcTextfield.invalidate();
				}
				if (t != null && mCodeLines != null) {
					mCodeLines = convertSourceCode(mRawCode);
					int startIx, endIx;
					startIx = findStartIndex(mCodeLines.toString(), _line - mLineOffset);
					endIx = mCodeLines.toString().indexOf("\n", startIx);

					if (endIx >= startIx && getActivity() != null) {

						mCodeLines.setSpan(new BackgroundColorSpan(getActivity().getResources().getColor(android.R.color.holo_blue_light)),
								startIx, endIx, 0);
					}
					t.setText(mCodeLines, BufferType.SPANNABLE);
				}
			}
		});

	}

	/**
	 * @param _string
	 * @param _line
	 * @return
	 */
	protected int findStartIndex(String _string, int _line) {
		int ix = 0;
		int lineCount = 0;

		for (; lineCount < _line; lineCount++) {
			int si = _string.indexOf("\n", ix);
			ix = si + 1;
		}
		return ix;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtExecutionMonitor.KvtExecutionListener
	 * #programCodeChanged(java.lang.String)
	 */
	@Override
	public void programCodeChanged(String _source) {
	}

	/**
	 * @param _prog
	 */
	public void setProgram(KvtProgram _prog) {
		mProgram = _prog;
		mStop.setEnabled(_prog.getProgramState() == KvtProgram.RUNNING);
		mStart.setEnabled(_prog.getProgramState() != KvtProgram.RUNNING);
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
		final int state = _prg.getProgramState();

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				mStart.setEnabled(state != KvtProgram.RUNNING);
				mStop.setEnabled(state == KvtProgram.RUNNING);
				mStart.invalidate();
				mStop.invalidate();
			}
		});

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.
	 * KvtMotionModeListener#motionModeChanged(int)
	 */
	@Override
	public void motionModeChanged(int _progMode) {
		Resources res = getActivity().getResources();
		Drawable drw = res.getDrawable(R.drawable.execution_mode_cont);
		switch (_progMode) {
		case KvtMotionModeAdministrator.MODE_CONT:
			drw = res.getDrawable(R.drawable.execution_mode_cont);
			break;
		case KvtMotionModeAdministrator.MODE_MOTION_STEP:
			drw = res.getDrawable(R.drawable.execution_mode_mstep);
			break;
		case KvtMotionModeAdministrator.MODE_STEP:
			drw = res.getDrawable(R.drawable.execution_mode_step);
			break;
		}
		final Drawable fDrw = drw;

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				mExecModeBtn.setImageDrawable(fDrw);
				mExecModeBtn.invalidate();
			}
		});
	}
}
