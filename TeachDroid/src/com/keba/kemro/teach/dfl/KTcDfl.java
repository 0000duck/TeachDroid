/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : tur
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl;

import android.util.Log;

import com.keba.kemro.teach.dfl.codepoint.*;
import com.keba.kemro.teach.dfl.compilerError.*;
import com.keba.kemro.teach.dfl.dir.*;
import com.keba.kemro.teach.dfl.edit.*;
import com.keba.kemro.teach.dfl.execution.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.value.*;
import com.keba.kemro.teach.network.*;


/*
 * Dfl = Data and function layer (Daten- und Funktionsschicht)
 *
 * Die Klasse ist ein Reräsentator für die Daten- und Funktionsschicht. Sie 
 * speichert den Pfad der Applikation auf der Steuerungs/TeachControlseite 
 * (Teachtalk-Pfad) und liefert die aktuelle Versionsinformation.
 */
public class KTcDfl {
	/** Projekt Extension */
	public static String PROJECT_DIR_EXTENSION = ".TT";
	/** Programextension */
	public static String PROG_FILE_EXTENSION = ".TTP";
	/** Baustein Extension */
	public static String OBJECT_FILE_EXTENSION = ".TTS";
	/** Save Data Extension */
	public static String SAVE_FILE_EXTENSION = ".TTD";
	/** End user program file extention */
	public static String USER_PROG_FILE_EXTENSION = ".TIP";
	/** End user variable file extention */
	public static String USER_VAR_FILE_EXTENSION = ".TID";

	private static final Object m_locker = new Object();

	private String globalFilter;
	public TcClient client;
	
	public KDirectoryAdministrator directory;
	public KStructAdministrator structure;
	public KExecAdministrator execution;
	public KCompilerErrorAdministrator error;
	public KEditAdministrator editor;
	public KCodePointAdministrator codepoint;
	public KVariableAdministrator variable;

	protected KTcDfl () {
	}
	
	public KTcDfl (TcClient client) {
		this(client, null);
	}

	public KTcDfl(TcClient client, String globalFilter) {
		this.globalFilter = globalFilter;
		this.client = client;
		Log.i("TC connection","Create directory admin");
		directory = new DirectoryAdministrator(this);
		Log.i("TC connection","Create structure admin");
		structure = new StructAdministrator(this);
		Log.i("TC connection","Create execution admin");
		execution = new ExecAdministrator(this);
		Log.i("TC connection","Create error admin");
		error = new CompilerErrorAdministrator(this);
		Log.i("TC connection","Create editor admin");
		editor = new EditAdministrator(this);
		codepoint = new CodePointAdministrator(this);
		Log.i("TC connection","Create variable admin");
		variable = new VariableAdministrator(this);
		Log.i("TC connection","Init directory admin");
		((DirectoryAdministrator) directory).init();
		Log.i("TC connection","Init structure admin");
		((StructAdministrator) structure).init();
		Log.i("TC connection","Init exec admin");
		((ExecAdministrator) execution).init();
		Log.i("TC connection","Init error admin");
		((CompilerErrorAdministrator) error).init();
		Log.i("TC connection","Init editor admin");
		((EditAdministrator) editor).init();
		((CodePointAdministrator) codepoint).init();
		Log.i("TC connection","Init variable admin");
		((VariableAdministrator) variable).init();
		Log.i("TC connection","Load global filters: directory");
		//	1. reload dir entry list
		directory.setGlobalFilter(globalFilter);
		// 2. reload global structural project
		Log.i("TC connection","Load global filters: structure");
		structure.setGlobalFilter(globalFilter);
		// 3. reload global execution project
		Log.i("TC connection","Load global filters: execution");
		execution.setGlobalFilter(globalFilter);
	}
	
	public void disconnect () {
		((StructAdministrator) structure).stop();
		((ExecAdministrator) execution).stop();
		((VariableAdministrator) variable).stop();
		client.removeAllConnectionListener();
		client.disconnect();

	}
	/**
	 * Liefert das Lock-Objekt für den Strukturadministrator. Wird benötigt beim
	 * Laden u. Entladen von Projekten um einen bestimmten Code-Abschnitt für 
	 * andere Threads zu sperren.
	 *
	 * @return Lock-Objekt 
	 */
	public Object getLockObject() {
		return m_locker;
	}

   /**
    * Liefert die aktuelle Version des TeachControl-Systems
    *
    * @return Version als Zeichenkette
    */
   public String getTeachControlVersion () {
      return (client != null) ? client.getVersion() : "";
   }
   
   public String getGlobalFilter () {
   	return globalFilter;
   }
   
   public void setGlobalFilter (String filter) {
   	if (((globalFilter == null) && (filter != null)) || ((globalFilter != null) && !globalFilter.equalsIgnoreCase(filter))) {
			globalFilter = (filter != null) ? filter/* .toLowerCase() */: null;
			//	1. reload dir entry list
   		directory.setGlobalFilter(globalFilter);
			// 2. reload global structural project
   		structure.setGlobalFilter(globalFilter);
			// 3. reload global execution project
   		execution.setGlobalFilter(globalFilter);
   	}
   }
   
	private static class DirectoryAdministrator extends KDirectoryAdministrator {
		protected DirectoryAdministrator (KTcDfl dfl) {
			super(dfl);
		}
		protected void init() {
			super.init();
		}
	}
	private static class StructAdministrator extends KStructAdministrator {
		protected StructAdministrator (KTcDfl dfl) {
			super(dfl);
		}
		protected void init() {
			super.init();
		}
		
		protected void stop() {
			super.stop();
		}
	}
	private static class ExecAdministrator extends KExecAdministrator {
		protected ExecAdministrator (KTcDfl dfl) {
			super(dfl);
		}
		protected void init() {
			super.init();
		}
		protected void stop() {
			super.stop();
		}
	}
	private static class CompilerErrorAdministrator extends KCompilerErrorAdministrator {
		protected CompilerErrorAdministrator (KTcDfl dfl) {
			super(dfl);
		}
		protected void init() {
			super.init();
		}
	}
	private static class EditAdministrator extends KEditAdministrator {
		protected EditAdministrator (KTcDfl dfl) {
			super(dfl);
		}
		protected void init() {
			super.init();
		}
	}
	private static class CodePointAdministrator extends KCodePointAdministrator {
		protected CodePointAdministrator (KTcDfl dfl) {
			super(dfl);
		}
		protected void init() {
			super.init();
		}
	}
	private static class VariableAdministrator extends KVariableAdministrator {
		protected VariableAdministrator (KTcDfl dfl) {
			super(dfl);
		}
		protected void init() {
			super.init();
		}
		protected void stop() {
			super.stop();
		}

	}

}
