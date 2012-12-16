package com.keba.kemro.teach.network;

import java.util.*;

public abstract class TcClient {
   protected int TIMEOUT = 45000;
   protected final Vector listeners = new Vector();
   
   protected boolean isConnected;
   protected boolean hasWriteAccess;
   protected boolean userMode;

   protected int handle;
   protected String version;
   protected int major = 0;
   protected int minor = 0;
   protected int branchMajor = 0;
   protected int branchMinor = 0;
   protected String clientID;
   protected String localPath;
   protected int m_clientID;
   
	public TcDirectoryModel directory;
	public TcStructuralModel structure;
	public TcExecutionModel execution;
   

   protected TcClient () {
   	
   }
   
   public abstract void disconnect ();
   public abstract void close ();
   
   protected void setClientData (int handle, String clientID, String version, String localPath) {
      this.handle = handle;
      this.version = version;
      this.clientID = clientID;
      this.localPath = localPath;
      NumberEnumeration e = new NumberEnumeration(version);
      major = e.hasMoreElements() ? ((Integer) e.nextElement()).intValue(): 0;
      minor = e.hasMoreElements() ? ((Integer) e.nextElement()).intValue(): 0;
      branchMajor = e.hasMoreElements() ? ((Integer) e.nextElement()).intValue(): 0;
      branchMinor = e.hasMoreElements() ? ((Integer) e.nextElement()).intValue(): 0;
   }

   /**
    * @return true if the connection to TeachControl is up.
    */
   public boolean isConnected () {
   	return isConnected;
   }

   /**
    * Checks if the current TeachControl client is registered as Controller or as Observer.
    * Only Controllers may start programs, etc.
    * check {@link setWriteAccess} to obtain Controller rights.
    * @return true if Controller, false if Observer
    */
   public abstract boolean isTcController();
   
   /**
    * @return true if the client has write access
    */
   public boolean hasWriteAccess () {
      return hasWriteAccess;
   }

   /**
    * Returns the connection timeout.
    * @return timeout in ms
    */
   public int getTimeout () {
      return TIMEOUT;
   }
   /**
    * Sets the connection timeout.
    *
    * @param ms timeout
    */
   public void setTimeout (int ms) {
   	TIMEOUT = ms;
   }

   /**
    * Returns the end user mode.
    * @return true if the end user mode is set
    */
   public boolean getUserMode () {
   	return userMode;
   }
   
   /**
    * Sets the end user mode. If the end user mode is set then only end user objects
    * will be iterated.
    * @param on ture for the end user mode
    */
   public void setUserMode (boolean on) {
   	userMode = on;
   }
   
   public abstract boolean setWriteAccess (boolean writeAccessAllowed);


   /**
    * Liefert den TeachControl - Handle zurück.
    *
    * @return TeachControl - Handle
    */
   protected int getHandle () {
      return handle;
   }

   /**
    * Returns the client ID. The client ID contains the application name and
    * the client IP address.
    * 
    * @return Client ID
    */
   public String getClientID () {
      return clientID;
   }

   /**
    * Returns the local path of the teachtalk directory.
    * @return local path
    */
   public String getLocalPath () {
      return localPath;
   }
   /**
    * Liefert die Version des TeachControls zurück.
    *
    * @return Version
    */
   public String getVersion () {
      return version;
   }
   
   public int getMajorVersion () {
   	return major;
   }
   public int getMinorVersion () {
   	return minor;
   }
   public int getBranchMajorVersion () {
   	return branchMajor;
   }
   public int getBranchMinorVersion () {
   	return branchMinor;
   }
   
   protected synchronized void sendDisconnectEvent () {
   	if (isConnected) {
   		isConnected = false;
   		Thread notifier = new Thread () {
	         public void run () {
               fireConnectionEvent(false);
            }
         };
         notifier.start();
      }
   }

   /**
    * Adds a TcConnectionListener listener.
    *
    * @param l TcConnectionListener
    */
   public void addConnectionListener (TcConnectionListener l) {
      listeners.addElement(l);
   }

   /**
    * Removes the TcConnectionListener listener.
    *
    * @param l TcConnectionListener
    */
   public void removeConnectionListener (TcConnectionListener l) {
      listeners.removeElement(l);
   }

   public void removeAllConnectionListener () {
   	listeners.setSize(0);
   }
   private void fireConnectionEvent (boolean isConnected) {
   	java.util.Enumeration e = listeners.elements();
      while (e.hasMoreElements()) {
         TcConnectionListener l = (TcConnectionListener) e.nextElement();
         try {
            l.connectionStateChanged(isConnected);
         } catch (Exception ex) {
            System.out.println("TcConnectionManager - fireConnectionEvent: " + isConnected);
            ex.printStackTrace();
         }
      }
   }

   private static class NumberEnumeration implements Enumeration {
   	private String version;
   	private int index;
   	private static final int zero = '0';
   	private static final int nine = '9';
   	
   	private NumberEnumeration (String version) {
   		index = 0;
   		this.version = version;
   	}
   	
   	public boolean hasMoreElements() {
			if (index < version.length()) {
				int c = version.charAt(index);
	   		while ((index < version.length()) && ((c < zero) || (nine < c))) {
	   			index++;
	   			if (index < version.length()) {
	   				c = version.charAt(index);
	   			}     			
	   		}
			}
   		return index < version.length();			
   	}
   	
   	public Object nextElement() {
			if (index < version.length()) {
				int value = 0;
				int c = version.charAt(index);
				while ((index < version.length()) && (zero <= c) && (c <= nine)) {
					value = value*10;
					value += c - zero ;
					index++;
					if (index < version.length()) {
						c = version.charAt(index);
					}
				}
				return new Integer(value);
			}
			return null;
   	}
   }

}
