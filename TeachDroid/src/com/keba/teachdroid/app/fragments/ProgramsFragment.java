package com.keba.teachdroid.app.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.keba.teachdroid.app.R;

public class ProgramsFragment extends Fragment {

	public ProgramsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater _inf, ViewGroup _container,
			Bundle _savedInst) {

		View myProgView = _inf.inflate(R.layout.fragment_layout_programcontrol,
				_container, false);

		ExpandableListView li = (ExpandableListView) myProgView
				.findViewById(R.id.expandableListView1);

		SimpleExpandableListAdapter adp = new SimpleExpandableListAdapter(
				getActivity().getApplicationContext(), createGroupList(), // Creating
																			// group
																			// List.
				R.layout.exp_list_view_group_layout, // Group item layout XML.
				new String[] { "Project" }, // the key of group item.
				new int[] { R.id.row_name }, // ID of each group item.-Data
												// under the key goes into this
												// TextView.
				createChildList(), // childData describes second-level <span
									// id="IL_AD11"
									// class="IL_AD">entries</span>.
				R.layout.exp_list_view_childrow_layout, // Layout for sub-level
														// entries(second
														// level).
				new String[] { "Program" }, // Keys in childData maps to <span
											// id="IL_AD4"
											// class="IL_AD">display</span>.
				new int[] { R.id.grp_child } // Data under the keys above go
												// into these TextViews.
		);

		li.setAdapter(adp);

		return myProgView;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List createChildList() {

		ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String,String>>>();
		/*
		 * each group need each HashMap-Here for each group we have 3 subgroups
		 */
		ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
		// for (int n = 0; n < 2; n++) {
		// HashMap child = new HashMap();
		// child.put("Sub Item", "Sub Item " + n);
		// secList.add(child);
		// }
		HashMap<String, String> c = new HashMap<String, String>();
		c.put("Program", "sysProj1");
		secList.add(c);
		c = new HashMap<String, String>();
		c.put("Program", "sysProj2");
		secList.add(c);
		result.add(secList);

		
		secList = new ArrayList<HashMap<String, String>>();
		c = new HashMap<String, String>();
		c.put("Program", "global_test");
		secList.add(c);
		result.add(secList);

		secList = new ArrayList<HashMap<String, String>>();
		c = new HashMap<String, String>();
		c.put("Program", "Reference");
		secList.add(c);
		c = new HashMap<String, String>();
		c.put("Program", "Homing_main");
		secList.add(c);
		result.add(secList);

		secList = new ArrayList<HashMap<String, String>>();
		c = new HashMap<String, String>();
		c.put("Program", "MoveHomeZero");
		secList.add(c);
		c = new HashMap<String, String>();
		c.put("Program", "MoveStart");
		secList.add(c);

		result.add(secList);

		return result;
	}

	/* Creating the Hashmap for the row */

	private List<HashMap<String, String>> createGroupList() {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		// for (int i = 0; i < 15; ++i) { // 15 groups........
		// HashMap m = new HashMap();
		// m.put("Group Item", "Group Item " + i); // the key and it's value.
		// result.add(m);
		// }
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("Project", "System");
		result.add(m);

		m = new HashMap<String, String>();
		m.put("Project", "Global");
		result.add(m);

		m = new HashMap<String, String>();
		m.put("Project", "Homing");
		result.add(m);

		m = new HashMap<String, String>();
		m.put("Project", "Util");
		result.add(m);

		return (List<HashMap<String, String>>) result;
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		System.out
				.println("Inside onChildClick at groupPosition = "
						+ groupPosition + " Child clicked at position "
						+ childPosition);
		return true;
	}
}
