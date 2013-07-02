package com.keba.teachdroid.app;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.teachdroid.app.data.InitializationTask;
import com.keba.teachdroid.app.data.InitializationTask.InitializationListener;
import com.keba.teachdroid.app.data.RobotControlProxy;
import com.keba.teachdroid.app.fragments.OverviewFragment;
import com.keba.teachdroid.app.fragments.ProgramsFragment;

public class MainTeachView extends FragmentActivity implements ActionBar.OnNavigationListener, InitializationListener {

	private static final String	STATE_SELECTED_NAVIGATION_ITEM	= "selected_navigation_item";
	private static String[]		m_viewNames;
	private String				m_host							= "10.0.0.5";
	static final String	m_connectFormatString			= "Connecting attempt {0}";
	protected ProgressDialog	m_dlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_viewNames = new String[] { getString(R.string.title_robotOverview), getString(R.string.title_robotPrograms),
				getString(R.string.title_robotJogging), };

		setContentView(R.layout.activity_main_teach_view);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(
				// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, m_viewNames),
				this);


		// show the progress dialog
		m_dlg = ProgressDialog.show(this, "Connecting...", "Connecting to " + m_host, true, true);
		InitializationTask con = new InitializationTask(this);
		try {
			Boolean result = con.execute(m_host).get();

			RobotControlProxy.setConnected(result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_teach_view, menu);
		return true;
	}

	public boolean onNavigationItemSelected(int position, long id) {
		// When the given tab is selected, show the tab contents in the
		// container
		Fragment fragment;

		switch (position) {
		case 0:
			fragment = new OverviewFragment();
			break;
		case 1:
			fragment = new ProgramsFragment();
			break;
		default:
			fragment = new DummySectionFragment();
			break;
		}
		Bundle args = new Bundle();
		args.putString(DummySectionFragment.ARG_SECTION_NUMBER, m_viewNames[position]);

		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		public DummySectionFragment() {
		}

		public static final String	ARG_SECTION_NUMBER	= "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(args.getString(ARG_SECTION_NUMBER));
			return textView;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationBegin()
	 */
	public void initializationBegin() {

		runOnUiThread(new Runnable() {

			public void run() {
				m_dlg.setMessage("Begin initialization...");
				m_dlg.show();

			}

		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationComplete()
	 */
	public void initializationComplete(final boolean _success) {
		runOnUiThread(new Runnable() {
			public void run() {
				// m_dlg.dismiss();

				String msg = "Connection " + (_success ? "established" : "failed!!") + "\nPress back button to dismiss dialog.";

				m_dlg.setMessage(msg);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				m_dlg.cancel();

				KvtProject[] prjList = KvtProjectAdministrator.getAllProjects();

				for (KvtProject prj : prjList) {
					Log.i("Tc connection", "Project: " + prj.getName() + " has " + prj.getProgramCount() + " programs");
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #setInitializationProgress(int)
	 */
	public void setInitializationProgress(final int _progress) {
		runOnUiThread(new Runnable() {
			public void run() {
				m_dlg.setMessage(MessageFormat.format(MainTeachView.m_connectFormatString, _progress));
			}
		});
	}

}
