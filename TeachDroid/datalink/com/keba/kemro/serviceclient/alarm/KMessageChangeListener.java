/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : ede
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.serviceclient.alarm;

import java.util.*;

/**
 *  change notification of messages
 */
public interface KMessageChangeListener {
   /**
    * called whenever messages are changed, addded or removed 
    * 
    * @param addedMsgs vector of added messages
    *  @param bufferName name of buffer
    */
	public void messagesChanged (String bufferName,Vector addedMsgs, Vector removedMsgs, Vector changedMsgs);

   
	/**
	 * called on disconnect
    * @param bufferName name of buffer
    */
   public void allMessagesRemoved(String bufferName);
}
