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
package com.keba.kemro.teach.dfl.structural.type;

import com.keba.kemro.teach.dfl.*;

/**
 * Ist die Basisklasse der einfachen Datentypen wie String, Real, Int,...
 */
public class KStructTypeSimple extends KStructTypeAny {
   /** Untergrenze 8 Bit */
   public final static Byte LOWER_BYTE = new Byte(Byte.MIN_VALUE);
   /** Obergrenze 8 Bit */
   public final static Byte UPPER_BYTE = new Byte(Byte.MAX_VALUE);
   /** Untergrenze 16 Bit */
   public final static Short LOWER_SHORT = new Short(Short.MIN_VALUE);
   /** Obergrenze 16 Bit */
   public final static Short UPPER_SHORT = new Short(Short.MAX_VALUE);
   /** Untergrenze 32 Bit */
   public final static Integer LOWER_INT = new Integer(Integer.MIN_VALUE);
   /** Obergrenze 32 Bit */
   public final static Integer UPPER_INT = new Integer(Integer.MAX_VALUE);
   /** Untergrenze 64 Bit */
   public final static Long LOWER_LONG = new Long(Long.MIN_VALUE);
   /** Obergrenze 64 Bit */
   public final static Long UPPER_LONG = new Long(Long.MAX_VALUE);
   /** Untergrenze Flieﬂkomma */
   public final static Float LOWER_FLOAT = new Float(-Float.MAX_VALUE);
   /** Obergrenze Flieﬂkomma */
   public final static Float UPPER_FLOAT = new Float(Float.MAX_VALUE);
   
   public final static Double LOWER_LREAL = new Double(Double.MIN_VALUE);
   
   public final static Double UPPER_LREAL = new Double(Double.MAX_VALUE);

   KStructTypeSimple (String key, int visibility, KTcDfl dfl) {
      super(key, visibility, false, dfl);
   }

   
   /**
    * Liefert die Standartmaﬂig eingestellte Untergrenze
    * @return die Standartmaﬂig eingestellte Untergrenze
    */
   public Number getDefaultLowerRange () {
      return null;
   }

   /**
    * Liefert die Standartmaﬂig eingestellte Obergrenze
    * @return die Standartmaﬂig eingestellte Obergrenze
    */
   public Number getDefaultUpperRange () {
      return null;
   }
}
