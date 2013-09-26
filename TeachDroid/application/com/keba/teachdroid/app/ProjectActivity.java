package com.keba.teachdroid.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener;
import com.keba.teachdroid.app.fragments.InnerListFragment;
import com.keba.teachdroid.app.fragments.ProgramCodeFragment;
import com.keba.teachdroid.app.fragments.ProgramInfoFragment;
import com.keba.teachdroid.app.fragments.ProgramListFragment;
import com.keba.teachdroid.app.fragments.ProjectListFragment;

public class ProjectActivity extends BaseActivity implements InnerListFragment.SelectionCallback, ProgramListFragment.SelectionCallback, AlarmUpdaterListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1859827857696981188L;
	private transient ViewPager mViewPager;
	private transient SectionsPagerAdapter mSectionsPagerAdapter;
	private transient ProgressDialog m_dlg;

	// dummy contents for the Pages!
	// String[] projects = { "Project1", "Project2", "Project3" };
	// Vector<String[]> programs = new Vector<String[]>();
	private Map<Integer, String> programCodes = new HashMap<Integer, String>();
	private Map<Integer, String> programInfos = new HashMap<Integer, String>();

	private transient List<KvtProject> projects = KvtProjectAdministrator.getAllProjectsList();
	private transient List<List<KvtProgram>> programs = new Vector<List<KvtProgram>>();

	private int selectedProject = 0;
	private int selectedProgram = 0;

	public ProjectActivity() {
		AlarmUpdaterThread.addListener(this);
	}

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
			// if (!proj.isSystemProject())
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
		return programs.get(selectedProgram);
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
		KvtProject sel = getProjects().get(selectedProject);
		// final KvtProgram prog = getPrograms().get(selectedProgram);
		final KvtProgram prog = sel.getProgram(selectedProgram);

		// int i = 0;
		// for (final KvtProgram prog : getPrograms()) {
		final StringBuffer programCode = new StringBuffer();
		// programCode.append("Program: " + prog.getName() + "\n");

		// String code = null;
		String progCode = null;// programCodes.get(Integer.valueOf(selectedProgram));
		if (progCode == null /* || prog.getProgramState() <= KvtProgram.OPEN */) {

			// IMPORTANT: we need to spawn an asynctask here, because
			// getTextForProgram() will internally
			// use a network connection, which causes a
			// NetworkOnMainthread-Exception if invoked on UI-Thread!

			final ProgressDialog pd = new ProgressDialog(this);
			pd.setTitle("Please wait");
			pd.setMessage("Loading...");
			try {
				progCode = new AsyncTask<Void, Integer, String>() {

					@Override
					protected void onPostExecute(String _result) {
						if (pd != null) {
							pd.dismiss();
						}
					}

					@Override
					protected void onPreExecute() {
						pd.setCancelable(false);
						pd.setIndeterminate(true);
						pd.show();
					}

					@Override
					protected String doInBackground(Void... _params) {

						KvtProject parent = prog.getParent();
						boolean isBuilt = true;
						boolean startSuccessful = false;
						if (parent != null) {
							// first try to build
							if (parent.getProjectState() <= KvtProject.NOT_BUILDED) {
								isBuilt = KvtProjectAdministrator.build(parent);
								Log.d("ProjectActivity", "building project \"" + parent.getName() + "\" was " + (isBuilt ? "successful" : "not successful"));
							}

							// try to load project
							while (parent.getProjectState() < KvtProject.SUCCESSFULLY_LOADED) {
								KvtProjectAdministrator.loadProject(parent);
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									Log.v("ProjectActivity", e.toString());
								}
							}

							// then try to load program
							startSuccessful = KvtProjectAdministrator.startProgram(prog);
							while (prog.getProgramState() < KvtProgram.STOPPED) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									Log.v("ProjectActivity", e.toString());
								}
							}
						} else
							throw new IllegalStateException("Parent project of program \"" + prog.getName() + "\" could not be obtained!");
						if (isBuilt /* && startSuccessful */)
							return KvtExecutionMonitor.getProgramSourceCode(prog);
						else {
							Log.e(getClass().toString(), "Starting " + prog + " not successful, is it loaded?");
							return null;
						}
					}

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
			((ProgramCodeFragment) mSectionsPagerAdapter.getItem(1)).setProgram(prog);
		}

		StringBuffer programInfo = new StringBuffer();
		programInfo.append("Project: " + prog.getParent().toString() + "\n");
		programInfo.append("Creation Date: " + prog.getProgramCreationDate() + "\n");
		programInfo.append("Modification Date: " + prog.getProgramModificationDate() + "\n");
		programInfo.append("Program Size: " + prog.getProgramSize() + "kB\n");
		programInfo.append("Program State: " + prog.getProgramStateString() + "\n");
		programInfos.put(Integer.valueOf(selectedProgram), programInfo.toString());

		((ProgramInfoFragment) mSectionsPagerAdapter.getItem(2)).setProgramInfo();

		mViewPager.setCurrentItem(1, true);

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
		private static final long serialVersionUID = 4669970919454414915L;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener#queueChanged
	 * ()
	 */
	@Override
	public void queueChanged() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (getResources() != null) {
					List<Message> list = null;
					try {
						list = new AsyncTask<Void, Void, List<Message>>() {

							@Override
							protected List<Message> doInBackground(Void... params) {
								return AlarmUpdaterThread.getMessageQueue();
							}
						}.execute((Void) null).get();
					} catch (InterruptedException e) {
						Log.v(ProjectActivity.class.toString(), e.getMessage());
					} catch (ExecutionException e) {
						Log.i(ProjectActivity.class.toString(), e.getMessage());
					}

					int c = getResources().getColor(R.color.program_main_blue);
					if (list != null && !list.isEmpty()) {
						Message m = list.get(list.size() - 1);
						if (m.isAlarm()) {
							c = Color.RED;
						} else if (m.isWarning())
							c = Color.YELLOW;
					}
					PagerTitleStrip strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
					if (strip != null) {
						strip.setBackgroundColor(c);
						strip.invalidate();
					}
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener#
	 * historyChanged()
	 */
	@Override
	public void historyChanged() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.AlarmUpdaterThread.AlarmUpdaterListener#traceChanged
	 * (java.lang.String)
	 */
	@Override
	public void traceChanged(String _line) {
	}

}