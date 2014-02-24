package com.keba.teachdroid.app.fragments;

import java.util.concurrent.ExecutionException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.KvtMainModeListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.SafetyState;
import com.keba.kemro.kvs.teach.util.KvtMultiKinematikAdministrator;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.teachdroid.app.MainActivity;
import com.keba.teachdroid.app.R;

public class RobotFragmentMain extends Fragment implements KvtDriveStateListener, KvtPositionMonitorListener, KvtMainModeListener, Touchable {

	TextView			mRobotNameLabel;
	TextView			mRefSysLabel;
	TextView			mToolLabel;
	CheckBox			cb;
	View				mRootView;
	private ImageView	mRobotImage;

	public RobotFragmentMain() {
		KvtDriveStateMonitor.addListener(this);
		KvtPositionMonitor.addListener((KvtPositionMonitorListener) this);
		KvtMainModeAdministrator.addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_robot_main, container, false);

		mRobotNameLabel = (TextView) mRootView.findViewById(R.id.robotNameLabel);
		mRefSysLabel = (TextView) mRootView.findViewById(R.id.refsysLabel);
		mToolLabel = (TextView) mRootView.findViewById(R.id.toolLabel);
		cb = (CheckBox) mRootView.findViewById(R.id.robotPower);
		mRobotImage = (ImageView) mRootView.findViewById(R.id.robotImg);
		mainModeChanged(KvtMainModeAdministrator.getMainMode());

		try {
			updateUI();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mRootView;
	}

	/**
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * 
	 */
	private void updateUI() throws InterruptedException, ExecutionException {
		cb.setClickable(false);
		cb.setFocusable(false);
		cb.setChecked(KvtDriveStateMonitor.getDrivesPower());
		if (KvtDriveStateMonitor.getDrivesPower()) {
			mRobotImage.setImageResource(R.drawable.robot);
		} else {
			mRobotImage.setImageResource(R.drawable.robot_deact);
		}
		// readKinematicFilter() needs a network connection! -> AsyncTask!!!
		final String kin = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String k= KvtMultiKinematikAdministrator.readKinematicFilter();
				int cnt = 0;
				while (k == null || k.equalsIgnoreCase("_global") && cnt < 10) {
					k= KvtMultiKinematikAdministrator.readKinematicFilter();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					cnt++;
				}
				return k;
			}

		}.execute((Void) null).get();
		final String refsys = KvtPositionMonitor.getChosenRefSys();
		final String tool = KvtPositionMonitor.getChosenTool();

		if (getActivity() != null) {
			new Handler(getActivity().getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					mRobotNameLabel.setText(kin);
					mRefSysLabel.setText(refsys);
					mToolLabel.setText(tool);
					mRootView.invalidate();
				}
			});
		}
	}

	@Override
	public void mainModeChanged(int _newMainMode) {
		final int mainMode = _newMainMode;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String mainModeString = null;
					int drawableId = 0;
					switch (mainMode) {
					case 1:
						mainModeString = "A";
						drawableId = com.keba.teachdroid.app.R.drawable.ic_opmode_auto;
						break;
					case 2:
						mainModeString = "T1";
						drawableId = com.keba.teachdroid.app.R.drawable.ic_opmode_manual;
						break;
					case 4:
						mainModeString = "AE";
						drawableId = com.keba.teachdroid.app.R.drawable.ic_opmode_auto_extern;
						break;
					default:
						mainModeString = "";
						break;
					}
					if (drawableId != 0) {
						((TextView) mRootView.findViewById(R.id.mainMode)).setText(mainModeString);
						((TextView) mRootView.findViewById(R.id.mainMode)).setCompoundDrawablesWithIntrinsicBounds(
								getResources().getDrawable(drawableId), null, null, null);
					}
				}
			});
		}
	}

	@Override
	public void safetyStateChanged(SafetyState _state) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #drivePowerChanged(boolean)
	 */
	@Override
	public void drivePowerChanged(final boolean _hasPower) {
		// need a handler task to execute UI modifications
		if (getActivity() == null)
			return;
		Handler h = new Handler(getActivity().getMainLooper());
		h.post(new Runnable() {

			@Override
			public void run() {
				if (cb != null) {
					cb.setChecked(_hasPower);
				}

				if (_hasPower) {
					mRobotImage.setImageResource(R.drawable.robot);
				} else {
					mRobotImage.setImageResource(R.drawable.robot_deact);
				}
				mRobotImage.invalidate();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReadyChanged(java.lang.Boolean)
	 */
	@Override
	public void driveIsReadyChanged(Boolean _isReady) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReferencedChanged(java.lang.Boolean)
	 */
	@Override
	public void driveIsReferencedChanged(Boolean _isRef) {
	}

	@Override
	public void cartesianPositionChanged(int _compNo, String _compName, Number _value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogToolChanged(String _jogTool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogRefsysChanged(String _jogRefsys) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pathVelocityChanged(float _velocityMms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void axisPositionChanged(int axisNo, Number _value, String _axisName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectedRefSysChanged(String _refsysName) {
		final String refsys = _refsysName;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mRefSysLabel.setText(refsys);
				}
			});
		}
	}

	@Override
	public void selectedToolChanged(String _toolName) {
		final String tool = _toolName;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mToolLabel.setText(tool);
				}
			});
		}
	}

	private Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * @param _event
	 */
	@Override
	public void handleTouch(MotionEvent _event) {
		float x = _event.getX();
		float y = _event.getY();
		Rect rect = new Rect();
		System.out.println("X: " + x + " Y: " + y);
		mRobotImage.getHitRect(rect);
		int a = 0;
		if (rect.contains((int) x, (int) y)) {
			a = 1;
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... _params) {
					KvtDriveStateMonitor.toggleDrivesPower();
					return null;
				}

			}.execute((Void) null);

		} else {
			a = 2;
			((MainActivity) getActivity()).onShowPositions();
		}

	}
}
