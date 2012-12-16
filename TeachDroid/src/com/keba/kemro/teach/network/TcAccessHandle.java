package com.keba.kemro.teach.network;

public abstract class TcAccessHandle {
	protected int typeKind = -1;
	protected boolean hasValidAttributes;
	protected boolean isReadOnly;
	protected boolean isConstant;
	protected boolean isUser;
	protected boolean isPrivate;
   
   /**
    * Returns the type kind of the variable.
    * @return type kind
    */
   public int getTypeKind () {
   	return typeKind;
   }

   public boolean hasValidAttributes () {
   	return hasValidAttributes;
   }
   
	public boolean isReadOnly () {
		return isReadOnly;
	}
	
	public boolean isConstant () {
		return isConstant;
	}

	public boolean isUser () {
		return isUser;
	}
	
	public boolean isPrivate () {
		return isPrivate;
	}

}
