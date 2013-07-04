/**
 * 
 */
package com.keba.kemro.teach.dfl.structural.type;

import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.edit.KEditKW;

/**
 * @author ltz
 *
 */
public class KStructTypeLReal extends KStructTypeSimple {

	 KStructTypeLReal (String key, int visibility, KTcDfl dfl) {
	      super(key.length() == 0 ? KEditKW.KW_LREAL: key, visibility, dfl);
	   }

	   /**
	    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultLowerRange()
	    */
	   public Number getDefaultLowerRange () {
	      return LOWER_LREAL;
	   }

	   /**
	    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultUpperRange()
	    */
	   public Number getDefaultUpperRange () {
	      return UPPER_LREAL;
	   }
}
