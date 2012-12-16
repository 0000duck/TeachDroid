package com.keba.teachdroid;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.teachdroid.R;
import com.keba.teachdroid.fragments.OverviewFragment;
import com.keba.teachdroid.fragments.ProgramsFragment;

public class MainTeachView extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static String[] m_viewNames;
	private String m_host = "10.150.52.202";
	private static final String m_connectFormatString = "Connecting attempt {0}";
	protected ProgressDialog m_dlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_viewNames = new String[] { getString(R.string.title_robotOverview),
				getString(R.string.title_robotPrograms),
				getString(R.string.title_robotJogging), };

		setContentView(R.layout.activity_main_teach_view);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, m_viewNames), this);

		m_dlg = ProgressDialog.show(this, "Connecting...", "Connecting to "
				+ m_host, true, true);
		ConnectTask con = new ConnectTask();
		con.execute(m_host);
		// try {
		// boolean isConnected= con.get().booleanValue();
		//
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// }

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
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
		args.putString(DummySectionFragment.ARG_SECTION_NUMBER,
				m_viewNames[position]);

		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}

	protected class ConnectTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... _params) {
			final String host = _params[0];

			for (int i = 0; i < 100; ++i) {
				try {
					if (i % 10 == 0)
						publishProgress(i);
					boolean isConnected = KvtSystemCommunicator.connectOnce(
							host, 10000, "_global");
					if (isConnected)
						i = 100;

					
					Thread.sleep(100);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return KvtSystemCommunicator.isConnected();
		}

		@Override
		protected void onCancelled(Boolean result) {
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(final Boolean result) {

			super.onPostExecute(result);

			runOnUiThread(new Runnable() {
				public void run() {
					// m_dlg.dismiss();

					String msg = "Connection "
							+ (result.booleanValue() ? "established!!"
									: "failed!!")
							+ "\nPress back button to dismiss.";
					m_dlg.setMessage(msg);
				}
			});
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(final Integer... values) {
			super.onProgressUpdate(values);
			// runOnUiThread(new Runnable() {
			// public void run() {
			m_dlg.setMessage(MessageFormat.format(m_connectFormatString,
					values[0]));
			// }
			// });

		}

		public void progress(int _p) {
			publishProgress(_p);
		}

	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		public DummySectionFragment() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(args.getString(ARG_SECTION_NUMBER));
			return textView;
		}
	}
}
