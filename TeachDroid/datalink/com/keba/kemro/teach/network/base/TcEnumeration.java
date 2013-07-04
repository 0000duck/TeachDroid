/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : sinn
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:  sinn
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.network.base;

/**
 * TcEnumeration ist die abstrakte Basisklasse für alle Enumeration von TeachControl - Objekten.
 */
abstract public class TcEnumeration implements java.util.Enumeration {
   /** Handle */
   protected Integer iterHandle = new Integer(0);
   private boolean isFirst = true;
   private boolean isValid = false;
   private Object nextElem = null;

   /**
    * @see java.util.Enumeration#hasMoreElements()
    */
   public boolean hasMoreElements () {
      if (!isValid) {
         if (!isFirst) {
            nextElem = getNext(iterHandle);
         } else {
            isFirst = false;
            nextElem = getFirst(iterHandle);
         }
         isValid = nextElem != null;
      }
      return isValid;
   }

   /**
    * @see java.util.Enumeration#nextElement()
    */
   public Object nextElement () {
      if (hasMoreElements()) {
         isValid = false;
         return nextElem;
      }
      return null;
   }

   abstract protected Object getFirst (Integer iterHandle);
   abstract protected Object getNext (Integer iterHandle);

   static class Integer {
      int value;
      Integer (int value) {
         this.value = value;
      }
   }
}
