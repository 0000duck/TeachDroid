package com.keba.kemro.teach.network;

public class TcVariableGroup {
	protected String name;
	protected int handle;
	
	protected TcVariableGroup (String name, int handle) {
		this.name = name;
		this.handle = handle;
   }

   /**
	 * @return variable group name
    */
	public String getName () {
		return name;
   }

}
