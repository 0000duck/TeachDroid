/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Project : KEMRO.teachview.4
 *------------------------------------------------------------------------*/
package com.keba.kemro.kvs.teach.data.rc;

/**
 * This class provides the names for RC Enumerations, number of axes, etc.
 */
public class KvtRcAdministrator {
	public static final String RCDATA_PREFIX = "_system.";
	public static final int cRcMaxMainModes = 8;
	public static final int cRcMaxRobots = 256; 
	public static final int cRcMaxTools = 32; 
	public static final int cRcMaxRefSystems = 32;
	public static final int cRcMaxAxes = 16;
	public static final int cRcMaxJoints = 16;
	public static final int cRcMaxCartComps = 6;
	public static final int cRcMaxJogOverrideValues = 10;
	public static final int MIXED_CART_MAX = 3;
	public static final String KEY_AXES_NAME = "axes.name";
	public static final String KEY_AXES_STATE = "axes.state";
	public static final String KEY_JOINTS_NAME = "joints.name";
	public static final String KEY_CART_NAME = "cart.name";
	public static final String KEY_KINEMATICS_NAME = "kinematics.name";
	public static final String KEY_REF_SYSTEMS_NAME = "refSystems.name";
	public static final int DISPLAY_COORD_AXES = 0;
	public static final int DISPLAY_COORD_JOINTS = 1;
	public static final int DISPLAY_COORD_WORLD = 2;
	public static final int DISPLAY_COORD_WORLD_MIXED = 3;
	public static final int DISPLAY_COORD_OBJECT = 4;
	public static final int DISPLAY_COORD_OBJECT_MIXED = 5;
	public static final int JOG_COORD_JOINTS = 0;
	public static final int JOG_COORD_WORLD_MIXED = 1;
	public static final int JOG_COORD_WORLD = 2;
	public static final int JOG_COORD_TCP = 3;
	public static final int JOG_COORD_TCP_MIXED = 4;
	public static final int JOG_COORD_OBJECT = 5;
	public static final int JOG_COORD_OBJECT_MIXED = 6;
	
	public static final int JOINTS_STATE_BIT_SIMULATED = 1;
	public static final int JOINTS_STATE_BIT_REFERENCED = 2;
	public static final int JOINTS_STATE_BIT_ENDSWITCHPLUS = 4;
	public static final int JOINTS_STATE_BIT_ENDSWITCHMINUS = 8;
}