package com.keba.teachdroid;

import keba.controlinterface.core.ControlInterface;
import keba.controlinterface.interfaces.ControlInterfaceException;
import keba.controlinterface.interfaces.ControlInterfaceTypes.ConnectResult;
import keba.controlinterface.interfaces.Result;
import android.app.ActionBar;
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

import com.keba.teachdroid.fragments.OverviewFragment;
import com.keba.teachdroid.fragments.ProgramsFragment;

public class MainTeachView extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static String[] m_viewNames;

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
			fragment= new ProgramsFragment();
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
