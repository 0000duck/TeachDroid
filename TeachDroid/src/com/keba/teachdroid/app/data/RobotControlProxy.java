package com.keba.teachdroid.app.data;

import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;

public class RobotControlProxy {

	private static String clientID;

	static {
		KvtProjectAdministrator.init();
	}

	/**
	 * returns a list with the names of all available robots as strings
	 * 
	 * @return a String array with all robots' names
	 */
	public static String[] getRobotNames() {
		return new String[] { "ArtarmTX60L", "ArtarmTX40", "Scara RS80" };
	}

	/**
	 * Sets the mode of program execution, either CONT, MSTEP or STEP
	 * 
	 * @param _mmode
	 */
	public void setMotionMode(int _mmode) {

	}


}
