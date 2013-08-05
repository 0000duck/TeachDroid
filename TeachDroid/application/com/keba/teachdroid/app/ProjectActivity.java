package com.keba.teachdroid.app;

import java.io.Serializable;

import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.teachdroid.app.fragments.ConnectFragment;
import com.keba.teachdroid.app.fragments.OverviewFragment;
import com.keba.teachdroid.app.fragments.ProgramCodeFragment;
import com.keba.teachdroid.app.fragments.ProgrammCodeFragmentMain;
import com.keba.teachdroid.app.fragments.projectview.ProjectDetailFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectListFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectListFragment.SelectionCallback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ProjectActivity extends FragmentActivity implements Serializable, SelectionCallback {

	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_project_swipe);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Fragment[] mFragments;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {
			
			if (position > getCount() - 1)
				return null;

			if (mFragments == null) {
				Bundle args = new Bundle();

//				mFragments = new Fragment[] { new ConnectFragment(),
//						new OverviewFragment(), new ProjectListFragment(),
//						new ProjectDetailFragment() };
				mFragments= new Fragment[]{new ProgramCodeFragment(), new OverviewFragment()};

				args.putSerializable("connector", ProjectActivity.this);
				mFragments[0].setArguments(args);
			}

			// if (position == 3 && mSelectedProject != null) {
			// KvtProject p =
			// KvtProjectAdministrator.getProject(mSelectedProject);
			// if (p != null) {
			//
			// Bundle args = new Bundle();
			// args.putSerializable("project", p);
			// mFragments[3].setArguments(args);
			// }
			// }

			return mFragments[position];
		}

		@Override
		public int getCount() {
			// if (RobotControlProxy.isConnected())
			return mFragments != null?mFragments.length:2; // if projects AND programs are available
			// return 3; // if not connected
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section_connect);
			case 1:
				return getString(R.string.title_section_overview);
			case 2:
				return getString(R.string.title_section_projects);
			case 3:
				return getString(R.string.title_section_programs);
			default:
				return "NOT_DEFINED_" + position;
			}
		}
	}

	public void onItemSelected(KvtProject _entry) {
//		mSelectedProject = _entry;
//
//		if (mSelectedProject != null) {
//			Fragment f = mSectionsPagerAdapter.getItem(3);
//			if (f instanceof ProjectDetailFragment) {
//				((ProjectDetailFragment) f).setProject(mSelectedProject);
//			}
//			mViewPager.setCurrentItem(3);
//		}
		
	}
}