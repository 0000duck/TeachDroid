package com.keba.kemro.kvs.teach.controller;

/**
 * This interface specifies the application message filter
 *
 */
public interface KvtMessageFilter {
	
	/**
	 * This method will be called for application messages. A application message is a message
	 * with a componentNr lesser then 1000.
	 * 
	 * @param kinematicNr current selected kinematic
	 * @param messageClass message class
	 * @param componentNr message component number
	 * @param messageNr message number
	 * @param instanceNr message instance number
	 * @return true if the message is valid and should be shown
	 */
	public boolean isMessageValid (int kinematicNr, int messageClass, int componentNr, int messageNr, int instanceNr);
}
