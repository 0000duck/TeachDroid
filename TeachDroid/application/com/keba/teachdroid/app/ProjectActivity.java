package com.keba.teachdroid.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.teachdroid.app.fragments.ProgramListFragment;
import com.keba.teachdroid.app.fragments.InnerListFragment;
import com.keba.teachdroid.app.fragments.ProgramCodeFragment;
import com.keba.teachdroid.app.fragments.ProgramInfoFragment;
import com.keba.teachdroid.app.fragments.ProjectListFragment;
import com.keba.teachdroid.data.RobotControlProxy;

public class ProjectActivity extends BaseActivity implements InnerListFragment.SelectionCallback, ProgramListFragment.SelectionCallback {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= -1859827857696981188L;
	private transient ViewPager					mViewPager;
	private transient SectionsPagerAdapter		mSectionsPagerAdapter;
	private transient ProgressDialog			m_dlg;

	// dummy contents for the Pages!
	// String[] projects = { "Project1", "Project2", "Project3" };
	// Vector<String[]> programs = new Vector<String[]>();
	private Map<Integer, String>				programCodes		= new HashMap<Integer, String>();
	private Map<Integer, String>				programInfos		= new HashMap<Integer, String>();

	private transient List<KvtProject>			projects			= KvtProjectAdministrator.getAllProjectsList();
	private transient List<List<KvtProgram>>	programs			= new Vector<List<KvtProgram>>();

	private int									selectedProject		= 0;
	private int									selectedProgram		= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_project_swipe);

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mNavigationStrings = getResources().getStringArray(R.array.navigation_array);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNavigationStrings));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new MyActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mSectionsPagerAdapter);


		// late-load the project list
		// KvtSystemCommunicator.getTcDfl().directory.refreshProjects();

		for (KvtProject proj : projects) {
			programs.add(Arrays.asList(proj.getPrograms()));
		}
		// dummy content building
		// String[] programs1 = { "Prog1", "Prog2", "Prog3" };
		// String[] programs2 = { "Prog44", "Prog45", "Prog46" };
		// String[] programs3 = { "Prog7", "Prog8", "Prog9" };
		// programs.add(programs1);
		// programs.add(programs2);
		// programs.add(programs3);
	}

	public List<KvtProject> getProjects() {
		return projects;
	}

	public List<KvtProgram> getPrograms() {
		return programs.get(selectedProject);
	}

	public String getProgramCode() {
		if (programCodes != null) {
			return programCodes.get(Integer.valueOf(selectedProgram));
		} else {
			return "No Program selected";
		}
	}

	public String getProgramInfo() {
		if (programInfos != null) {
			return programInfos.get(Integer.valueOf(selectedProgram));
		} else {
			return "No Program selected";
		}
	}

	public int getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(int selectedProject) {
		this.selectedProject = selectedProject;
		programCodes.clear();
		programInfos.clear();
		mViewPager.invalidate();
	}

	public int getSelectedProgram() {
		return selectedProgram;
	}

	public void setSelectedProgram(int selectedProgram) {
		this.selectedProgram = selectedProgram;
		// programCodes.clear();
		final KvtProgram prog = getPrograms().get(selectedProgram);

		// int i = 0;
		// for (final KvtProgram prog : getPrograms()) {
		final StringBuffer programCode = new StringBuffer();
		programCode.append("Program: " + prog.getName() + "\n");

		// String code = null;
		String progCode = programCodes.get(Integer.valueOf(selectedProgram));
		if (progCode == null) {

			// IMPORTANT: we need to spawn an asynctask here, because
			// getTextForProgram() will internally
			// use a network connection, which causes a
			// NetworkOnMainthread-Exception if invoked on UI-Thread!



			try {
				progCode = new AsyncTask<Void, Integer, String>() {

					// @Override
					// protected void onPreExecute() {
					// super.onPreExecute();
					// m_dlg = new ProgressDialog(ProjectActivity.this,
					// ProgressDialog.STYLE_SPINNER);
					// m_dlg.setTitle("Loading...");
					// m_dlg.setCancelable(true);
					// m_dlg.setCanceledOnTouchOutside(false);
					// m_dlg.setMessage("Loading Program: " +
					// getPrograms().get(ProjectActivity.this.selectedProgram).toString());
					// m_dlg.setIndeterminate(true);
					// m_dlg.show();
					// while (!m_dlg.isShowing())
					// try {
					// Thread.sleep(10);
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// }
					// }

					@Override
					protected String doInBackground(Void... _params) {


						KvtProject parent = prog.getParent();
						boolean isBuilt = true;
						if (parent != null) {
							if (parent.getProjectState() <= KvtProject.NOT_BUILDED) {
								isBuilt = KvtProjectAdministrator.build(parent);
								Log.d("ProjectActivity", "building project \"" + parent.getName() + "\" was "
										+ (isBuilt ? "successful" : "not successful"));
							}
						} else
							throw new IllegalStateException("Parent project of program \"" + prog.getName() + "\" could not be obtained!");
						if (isBuilt)
							return KvtExecutionMonitor.getTextForProgram(prog);
						return null;
					}

					// @Override
					// protected void onPostExecute(String result) {
					// super.onPostExecute(result);
					// m_dlg.dismiss();
					// }
				}.execute((Void) null).get();// .get();

			} catch (InterruptedException e) {
				e.printStackTrace();
				programCode.append(e.getStackTrace());
			} catch (ExecutionException e) {
				e.printStackTrace();
				programCode.append(e.getStackTrace());
			}

			programCode.append(progCode);
			programCodes.put(Integer.valueOf(selectedProgram), programCode.toString());

			((ProgramCodeFragment) mSectionsPagerAdapter.getItem(1)).setProgramCode();

		}

		StringBuffer programInfo = new StringBuffer();
		programInfo.append("Project: " + prog.getParent().toString() + "\n");
		programInfo.append("Creation Date: " + prog.getProgramCreationDate() + "\n");
		programInfo.append("Modification Date: " + prog.getProgramModificationDate() + "\n");
		programInfo.append("Program Size: " + prog.getProgramSize() + "kB\n");
		programInfo.append("Program State: " + prog.getProgramStateString() + "\n");
		programInfos.put(Integer.valueOf(selectedProgram), programInfo.toString());

		((ProgramInfoFragment) mSectionsPagerAdapter.getItem(2)).setProgramInfo();

		mViewPager.setCurrentItem(1);

	}

	@Override
	public void onProjectSelected(int id) {
		setSelectedProject(id);
		((ProjectListFragment) mSectionsPagerAdapter.getItem(0)).detailSelected(id);
	}

	@Override
	public void onProgramSelected(int id) {

		setSelectedProgram(id);
		// ((MasterFragment)
		// mSectionsPagerAdapter.getItem(0)).detailSelected(id);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter implements Serializable {

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 4669970919454414915L;
		private Fragment[]			mFragments;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {

			if (position > getCount() - 1)
				return null;

			if (mFragments == null) {
				Bundle args = new Bundle();
				mFragments = new Fragment[] { new ProjectListFragment(), new ProgramCodeFragment(), new ProgramInfoFragment() };
				args.putSerializable("connector", ProjectActivity.this);
				mFragments[0].setArguments(args);
				mFragments[1].setArguments(args);
				mFragments[2].setArguments(args);
			}

			return mFragments[position];
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_project_list);
			case 1:
				return getString(R.string.title_section_program_code);
			case 2:
				return getString(R.string.title_program_info);
			default:
				return "NOT_DEFINED_" + position;
			}
		}
	}

}