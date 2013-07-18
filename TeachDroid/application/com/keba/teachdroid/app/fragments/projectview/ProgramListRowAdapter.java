/**
 * 
 */
package com.keba.teachdroid.app.fragments.projectview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.teachdroid.app.R;

/**
 * @author ltz
 * 
 */
public class ProgramListRowAdapter extends BaseAdapter {
	private Activity				activity;
	private KvtProject				mParent;

	private static LayoutInflater	inflater	= null;

	public ProgramListRowAdapter(Activity a, KvtProject _p) {
		activity = a;
		mParent = _p;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mParent.getProgramCount();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.program_list_row, null);

		TextView name = (TextView) vi.findViewById(R.id.list_program_name); // title

		ImageView state_image = (ImageView) vi.findViewById(R.id.list_state_icon); // thumb
																					// image

		int id = R.drawable.program_state_stop;

		KvtProgram prog = mParent.getProgram(position);
		name.setText(prog.getName());

		switch (prog.getProgramState()) {
		case KvtProgram.RUNNING:
			id = R.drawable.program_state_run;
		case KvtProgram.LOADED:
		case KvtProgram.OPEN:
		case KvtProgram.STOPPED:
		case KvtProgram.FINISHED:
			id = R.drawable.program_state_stop;
		case KvtProgram.STEPPING:
		case KvtProgram.WAITING:
			id = R.drawable.program_state_pause;

		}

		Drawable drw = activity.getResources().getDrawable(id);
		state_image.setImageDrawable(drw);

		return vi;
	}
}
