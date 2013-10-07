package com.keba.teachdroid.app.fragments;

import java.io.Serializable;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.keba.kemro.kvs.teach.data.program.KvtVarManager;
import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor.KvtExecutionListener;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.KvtMotionModeListener;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;
import com.keba.teachdroid.app.ProjectActivity;
import com.keba.teachdroid.app.R;

public class ProgramCodeFragment extends Fragment implements Serializable, KvtExecutionListener, KvtProjectAdministratorListener,
		KvtMotionModeListener {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7012715919241719807L;
	private static final String	STRING_PTP			= "PTP";
	private static final String	STRING_LIN			= "LIN";
	private transient View		mRootView;
	ProjectActivity				callback;
	transient TextView			codeTextView, pcTextfield;
	private KvtProgram			mProgram;
	private ImageButton			mStart;
	private ImageButton			mStop;
	private ImageButton			mExecModeBtn;
	private ImageButton			mTeachBtn;
	private final int			mLineOffset			= 2;
	private SpannableString		mCodeLines;
	private String				mRawCode;
	private BackgroundColorSpan	mMainflowSpanObj;
	private BackgroundColorSpan	mSelectionSpanObj;
	private Context				mContext;
	protected String			mCurrentSelVarname;
	private boolean				mTeachResult		= false;

	public ProgramCodeFragment() {
		KvtExecutionMonitor.addListener(this);
		KvtProjectAdministrator.addProjectListener(this);

		new Thread(new Runnable() {

			@Override
			public void run() {
				KvtMotionModeAdministrator.addListener(ProgramCodeFragment.this);
			}
		}).start();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_program_code, container, false);

		mContext = getActivity();

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
					Log.d("Button", "MotionMode change!");
					new Thread(new Runnable() {

						@Override
						public void run() {
							KvtMotionModeAdministrator.toggleMotionMode();
						}
					}).start();

				}
			});
		}

		mTeachBtn = (ImageButton) mRootView.findViewById(R.id.teachBtn);
		if (mTeachBtn != null)
			mTeachBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View _v) {
					KStructVarWrapper wrp = browseForVarname(mCurrentSelVarname);
					teachVariable(wrp);
				}
			});

		callback = (ProjectActivity) getArguments().getSerializable("connector");
		codeTextView = (TextView) mRootView.findViewById(R.id.programCodeTextView);
		codeTextView.setText(callback.getProgramCode());
		codeTextView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View _v, MotionEvent _event) {
				float y = _event.getY();
				float lineHeight = codeTextView.getLineHeight();
				int numLines = codeTextView.getLineCount() - 1;

				if (y > numLines * lineHeight) { // outside textarea
					return false;
				}

				double lineIndex = Math.floor(y / lineHeight);

				Log.v("TouchEvent", "y: " + y + " lineheight: " + lineHeight + " line: " + lineIndex);

				highlight((int) lineIndex);

				updateTeachButton(((int) lineIndex));

				return false;
			}
		});

		mMainflowSpanObj = new BackgroundColorSpan(mContext.getResources().getColor(android.R.color.holo_blue_light));

		pcTextfield = (TextView) mRootView.findViewById(R.id.pcTextfield);
		return mRootView;
	}

	/**
	 * @param _wrp
	 */
	protected void teachVariable(final KStructVarWrapper _wrp) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String p1 = mProgram.getParent().toString();
				String p2 = mProgram.toString();
				mTeachResult = KvtVarManager.teach(_wrp, new Object[] { p1, p2 });
				if (mTeachResult) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getActivity(), "Position \"" + _wrp.getKey() + " \" successfully taught", Toast.LENGTH_SHORT).show();
						}
					});
					mTeachResult = false;
				}
			}
		}).start();

	}

	/**
	 * @param _i
	 */
	protected void updateTeachButton(int _line) {
		if (mCodeLines == null)
			return;

		String[] lines = mCodeLines.toString().split("\n");
		String line = lines[_line];

		if (line.toUpperCase().contains(STRING_PTP) || line.toUpperCase().contains(STRING_LIN)) {
			// do something
			int startix = line.indexOf("(");
			int endix = line.indexOf(")");

			String varName = line.substring(startix + 1, endix);
			mCurrentSelVarname = varName;
			Log.d("Teach Pos", "teachable variable " + varName + " found");
			mTeachBtn.setVisibility(Button.VISIBLE);
		} else {
			mCurrentSelVarname = null;
			mTeachBtn.setVisibility(Button.INVISIBLE);
		}
	}

	/**
	 * @param _lineIndex
	 */
	protected void highlight(int _lineIndex) {
		if (mCodeLines == null)
			return;

		String[] lines = mCodeLines.toString().split("\n");
		String criterion = lines[_lineIndex];

		int ix = mCodeLines.toString().indexOf(criterion);
		int len = criterion.length();

		if (mSelectionSpanObj == null) {
			mSelectionSpanObj = new BackgroundColorSpan(mContext.getResources().getColor(android.R.color.holo_orange_light));
		} else {
			mCodeLines.removeSpan(mSelectionSpanObj);
		}

		mCodeLines.setSpan(mSelectionSpanObj, ix, ix + len, 0);
		codeTextView.setText(mCodeLines, BufferType.SPANNABLE);
	}

	@Override
	public void onResume() {
		super.onResume();
		codeTextView.setText(callback.getProgramCode());
	}

	public void setProgramCode() {
		mRawCode = callback.getProgramCode();

		mCodeLines = convertSourceCode(mRawCode);
		codeTextView.setText(mCodeLines, BufferType.SPANNABLE);

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
				if (codeTextView != null && mCodeLines != null) {
					mCodeLines.removeSpan(mMainflowSpanObj);
					int startIx, endIx;
					startIx = findStartIndex(mCodeLines.toString(), _line - mLineOffset, "\n");
					endIx = mCodeLines.toString().indexOf("\n", startIx);

					if (endIx >= startIx && mContext != null) {

						mCodeLines.setSpan(mMainflowSpanObj, startIx, endIx, 0);
					}
					codeTextView.setText(mCodeLines, BufferType.SPANNABLE);
				}
			}
		});

	}

	/**
	 * @param _string
	 * @param _line
	 * @param _separator
	 *            TODO
	 * @return
	 */
	protected int findStartIndex(String _string, int _line, String _separator) {
		int ix = 0;
		int lineCount = 0;

		for (; lineCount < _line; lineCount++) {
			int si = _string.indexOf(_separator, ix);
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
		if (mContext == null)
			return;
		Resources res = mContext.getResources();
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

	private KStructVarWrapper browseScope(KStructNode _scope, String _varname) {
		try {
			if (_scope.getClass().getField("variables") != null) {
				KStructNodeVector progVars;

				progVars = (KStructNodeVector) _scope.getClass().getField("variables").get(_scope);
				int numVars = progVars.getChildCount();

				for (int i = 0; i < numVars; i++) {
					KStructNode n = progVars.getChild(i);
					if (n.getKey().equalsIgnoreCase(_varname)) {
						String path = n.getPath();
						KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
						if (dfl != null) {
							KStructVarWrapper wrp = dfl.variable.createKStructVarWrapper(path);
							return wrp;
						}
					}

				}

			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	private KStructVarWrapper browseForVarname(String _varname) {
		KStructVarWrapper res = null;

		try {
			res = new AsyncTask<String, Integer, KStructVarWrapper>() {

				@Override
				protected KStructVarWrapper doInBackground(String... _params) {
					// try program scope
					KStructVarWrapper wrp = browseScope(mProgram.getStructProgram(), _params[0]);

					// try project scope, if failed
					if (wrp == null) {
						wrp = browseScope(mProgram.getParent().getStructProject(), _params[0]);
					}

					// try global scope, if failed
					if (wrp == null) {
						wrp = browseScope(KvtProjectAdministrator.getGlobalProject().getStructProject(), _params[0]);
					}
					return wrp;

				}
			}.execute(_varname).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return res;
	}
}
