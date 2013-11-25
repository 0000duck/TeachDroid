package com.keba.kemro.teach.network;

import java.util.*;

public interface TcDirectoryModel {
	public Enumeration getEntries(TcDirEntry parent, int kind);
	public TcDirEntry getRoot ();
	public String getLibPath ();
	public TcDirEntry add (String directory, String name, int kind);
	public TcDirEntry copy (TcDirEntry source, String destDirectory, String destName);
	public TcDirEntry rename (TcDirEntry dirEntry, String newName);
	public boolean delete (TcDirEntry dirEntry);
}