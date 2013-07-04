/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Erstautor : mau
 *------------------------------------------------------------------------*/
package com.keba.kemro.kvs.teach.data.program;

import com.keba.kemro.kvs.teach.data.project.*;
import com.keba.kemro.teach.dfl.edit.*;
import com.keba.kemro.teach.dfl.structural.*;

/**
 * retrieves the type of a statement (GOTO, ...)
 */
public class KvtStatementType {
	public static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_COMMENT = 0;
	public static final int TYPE_IF = 1;
	public static final int TYPE_IF_GOTO = 2;
	public static final int TYPE_ELSIF = 3;
	public static final int TYPE_ELSE = 4;
	public static final int TYPE_END_IF = 5;
	public static final int TYPE_REPEAT = 6;
	public static final int TYPE_UNTIL = 7;
	public static final int TYPE_WHILE = 8;
	public static final int TYPE_END_WHILE = 9;
	public static final int TYPE_CASE = 10;
	public static final int TYPE_CASE_ELEMENT = 11;
	public static final int TYPE_END_CASE = 12;
	public static final int TYPE_LOOP = 13;
	public static final int TYPE_END_LOOP = 14;
	public static final int TYPE_LABEL = 15;
	public static final int TYPE_GOTO = 16;
	public static final int TYPE_CALL = 17;
	public static final int TYPE_RETURN = 18;
	public static final int TYPE_WAIT = 19;
	public static final int TYPE_ASSIGN = 20;
	public static final int TYPE_EMPTY = 21;
	public static final int TYPE_FOR = 22;
	public static final int TYPE_END_FOR = 23;
	public static final int TYPE_SELECT = 24;
	public static final int TYPE_DESELECT = 25;
	public static final int TYPE_START = 26;
	public static final int TYPE_STOP = 27;
	public static final int TYPE_RUN = 28;
	public static final int TYPE_KILL = 29;
	public static final int TYPE_ROUTINECALL = 30;


	private static boolean isString(String line, int pos) {
		boolean str = false;
		for (int i = 0; i<pos; i++) {
			if (line.charAt(i)=='"') {
				str = !str;
			}
		}
		return str;
	}
	
	private static boolean findKeyword(String line, String keyword) {
		int pos = line.indexOf(keyword);
		while (pos != -1) {
			if (!isString(line, pos)) {
				return true;
			}
			pos = line.indexOf(keyword, pos+1);
		}
		return false;
	}
	
	public static boolean isDeactivateComment(String line) {
		return line.trim().startsWith("##");
	}

	/**
	 * checks if the program line starts with a keyword
	 * @param line program line
	 * @param keyword keyword to proof
	 * @return line starts with keyword
	 */	
	private static boolean startsWithKeyword(String line, String keyword) {
		boolean isKeyword = false;
		if (line.startsWith(keyword)) {
			isKeyword = true;
			if (line.length() > keyword.length()) {
				char space = line.charAt(keyword.length());
				if ((Character.isDigit(space)) || (Character.isLetter(space)) || (space == '_')) {
					isKeyword = false;
				}
			}
		}
		return isKeyword;
	}
	
	/**
	 * gets statement type (TYPE_*)
	 * @param line statement
	 * @return TYPE_*
	 */
	public static int getType(String line, KStructNode scope) {
		
		line = line.trim();
		if (line.startsWith("//")) {
			return TYPE_COMMENT;
		}
		
		if (line.startsWith("##")) { // ignore this comment type here as we still need the statement type
			line = line.substring(2).trim();
		}
		
		int pos = line.indexOf("//");
		if (pos != -1) { // cut comment which doesnt start at beginning of line
			line = line.substring(0, pos);
		}
		
		if (line.equals("")) {
			return TYPE_EMPTY;
		}
		
		if (startsWithKeyword(line, KEditKW.KW_IF)) {
			if (findKeyword(line, KEditKW.KW_GOTO)) {
				return TYPE_IF_GOTO;
			}
			return TYPE_IF;
		} else if (startsWithKeyword(line, KEditKW.KW_ELSE_IF)) {
			return TYPE_ELSIF;
		} else if (startsWithKeyword(line, KEditKW.KW_ELSE)) {
			return TYPE_ELSE;
		} else if (startsWithKeyword(line, KEditKW.KW_END_IF)) {
			return TYPE_END_IF;
		} else if (startsWithKeyword(line, KEditKW.KW_REPEAT)) {
			return TYPE_REPEAT;
		} else if (startsWithKeyword(line, KEditKW.KW_UNTIL)) {
			return TYPE_UNTIL;
		} else if (startsWithKeyword(line, KEditKW.KW_WHILE)) {
			return TYPE_WHILE;
		} else if (startsWithKeyword(line, KEditKW.KW_END_WHILE)) {
			return TYPE_END_WHILE;
		} else if (startsWithKeyword(line, KEditKW.KW_LOOP)) {
			return TYPE_LOOP;
		} else if (startsWithKeyword(line, KEditKW.KW_END_LOOP)) {
			return TYPE_END_LOOP;
		} else if (startsWithKeyword(line, KEditKW.KW_LABEL)) {
			return TYPE_LABEL;
		} else if (startsWithKeyword(line, KEditKW.KW_GOTO)) {
			return TYPE_GOTO;
		} else if (startsWithKeyword(line, KEditKW.KW_CALL)) {
			return TYPE_CALL;
		} else if (startsWithKeyword(line, KEditKW.KW_RUN)) {
			return TYPE_RUN;
		} else if (startsWithKeyword(line, KEditKW.KW_KILL)) {
			return TYPE_KILL;
		} else if (startsWithKeyword(line, KEditKW.KW_RETURN)) {
			return TYPE_RETURN;
		} else if (startsWithKeyword(line, KEditKW.KW_WAIT)) {
			return TYPE_WAIT;
		} else if (startsWithKeyword(line, KEditKW.KW_FOR)) {
			return TYPE_FOR;
		} else if (startsWithKeyword(line, KEditKW.KW_END_FOR)) {
			return TYPE_END_FOR;
		} else if (startsWithKeyword(line, KEditKW.KW_SELECT)) {
			return TYPE_SELECT;
		} else if (startsWithKeyword(line, KEditKW.KW_DESELECT)) {
			return TYPE_DESELECT;
		} else if (startsWithKeyword(line, KEditKW.KW_START)) {
			return TYPE_START;
		} else if (startsWithKeyword(line, KEditKW.KW_STOP)) {
			return TYPE_STOP;
		}
		
		if (findKeyword(line, KEditKW.KW_COLON_EQUAL)) {
			return TYPE_ASSIGN;
		}
		
		if (findKeyword(line, KEditKW.KW_PARENTHESES_LEFT) && findKeyword(line, KEditKW.KW_PARENTHESES_RIGHT)) {
			if (scope != null && isProgram(line, scope)) {
				return TYPE_CALL;
			} 
			return TYPE_ROUTINECALL;
		}
		
		return TYPE_UNKNOWN;
	}
	
	private static boolean isProgram(String line, KStructNode scope) {
		int index = line.indexOf(KEditKW.KW_PARENTHESES_LEFT);
		line = line.substring(0, index);
		KvtProject proj = KvtProjectAdministrator.getProject(scope.getKStructProject().getKey());
		return (proj != null) && proj.getProgram(line)!=null;
	}
}
