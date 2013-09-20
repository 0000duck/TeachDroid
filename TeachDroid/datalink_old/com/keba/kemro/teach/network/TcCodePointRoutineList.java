package com.keba.kemro.teach.network;

/**
 * CodePoint routine change list. It contains a list of routines which contains codepoints
 * if the change flag is set.
 *
 */
public class TcCodePointRoutineList {
	/**
	 * True if a code point was set or removed.
	 */
	public int changeCount = -1;
	/**
	 * Only valid if hasChanged is true otherwise the array contains the routines 
	 * which have a code point set.
	 */
	public TcStructuralRoutineNode[] routines;
}