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
package com.keba.kemro.teach.dfl.structural;

/**
 * Vektor, der Strukturknoten verwaltet und spezielle Zugriffsmethoden zur 
 * Verfügung stellt. Der Vektor hat eine Referenz auf seinen übergeordneten
 * Knoten.
 */
public class KStructNodeVector {
   // if below zero the initSize is stored
   private static int m_initSize = 10;
   /** Description of the Field */
   protected KStructNode[] children;
   /** Description of the Field */
   protected int counter;
   /** Description of the Field */
   protected KStructNode parent;

   /**
    * Erzeugt einen Strukturknotenvektor
    *
    * @param parent übergeordneter Knoten
    */
   public KStructNodeVector (KStructNode parent) {
      this.parent = parent;
      this.counter = -m_initSize;
   }

   /**
    * Erzeugt einen Strukturknotenvektor
    *
    * @param parent übergeordneter Knoten
    * @param initSize Initialisierungsgröße für den Zähler
    */
   public KStructNodeVector (KStructNode parent, int initSize) {
      this.parent = parent;
      counter = -initSize;
   }

   /**
    * Liefert ein Element des Vektors.
    *
    * @param childIndex Zugriffswert für das Element
    *
    * @return Strukturknoten
    */
   public KStructNode getChild (int childIndex) {
   	synchronized (parent.dfl.getLockObject()) {
	      if ((!parent.isLoaded()) && (parent.getAllowsChildren())) {
	         parent.loadChildren();
	      }
   	}
      if ((0 <= childIndex) && (childIndex < counter)) {
         return children[childIndex];
      }
      return null;
   }
   
   /**
    * Returns the child with the given key.
    * @param key key of the child
    * @param ignoreCase if false the key comparison is case sensitive
    * @return child node
    */
   public KStructNode getChild (String key, boolean ignoreCase) {
   	synchronized (parent.dfl.getLockObject()) {
	      if ((!parent.isLoaded()) && (parent.getAllowsChildren())) {
	         parent.loadChildren();
	      }
   	}
		for (int i = 0; i < counter; i++) {
			if (ignoreCase && children[i].getKey().equalsIgnoreCase(key)) {
			   return children[i];
			} else if (!ignoreCase && children[i].getKey().equals(key)) {
				return children[i];
			}
		}
      return null;
   }


   /**
    * Anzahl der Elemente des Vektors
    *
    * @return Elementanzahl
    */
   public int getChildCount () {
   	synchronized (parent.dfl.getLockObject()) {
	      if ((!parent.isLoaded()) && (parent.getAllowsChildren())) {
	         parent.loadChildren();
	      }
   	}
      if (children != null) {
         return counter;
      }
      return 0;
   }

   /**
    * Liefert den Zugriffswert für einen angegebenen Strukturknoten.
    *
    * @param child Strukturknoten
    *
    * @return Zugriffswert
    */
   public int getIndexOfChild (KStructNode child) {
      if (child == null) {
         return -1;
      }
   	synchronized (parent.dfl.getLockObject()) {
	      if ((!parent.isLoaded()) && (parent.getAllowsChildren())) {
	         parent.loadChildren();
	      }
   	}
      if (children != null) {
         int i = 0;
         while (i < counter) {
            if (child.equals(children[i])) {
               return i;
            }
            i++;
         }
      }
      return -1;
   }

   /**
    * Fügt dem Vektor ein Feld von Strukurknoten hinzu und setzt den einzelnen
    * Elementen den übergeordneten Knoten.
    *
    * @param childarray Feld mit Strukturknoten
    * @param size Anzahl der Strukturknoten
    */
   public void addChildren (KStructNode[] childarray, int size) {
      for (int i = 0; i < size; i++) {
         childarray[i].setParent(parent);
      }
      if (children == null) {
         children = new KStructNode[size];
         System.arraycopy(childarray, 0, children, 0, size);
         this.counter = size;
      } else {
         if ((children.length) > (counter + size)) {
            System.arraycopy(childarray, 0, children, counter, size);
            counter = counter + size;
         } else {
            int newSize;
            KStructNode[] help;

            newSize = size + counter;
            help = new KStructNode[newSize];
            System.arraycopy(children, 0, help, 0, counter);
            System.arraycopy(childarray, 0, help, counter, size);
            counter = newSize;
            children = help;
         }
      }
   }

   /**
    * Fügt einen einzelnen Strukturknoten hinzu.
    *
    * @param child Strukturknoten
    */
   public void addChild (KStructNode child) {
	   if (child != null){
	      if (children == null) {
	         children = new KStructNode[-counter];
	         counter = 0;
	      } else if (counter == children.length) {
	         if (children.length == 0) {
	            children = new KStructNode[5];
	         } else {
	            KStructNode[] help;
	
	            help = new KStructNode[children.length * 2];
	            System.arraycopy(children, 0, help, 0, children.length);
	            children = help;
	         }
	      }
	      try {
	         children[counter] = child;
	      } catch (ArrayIndexOutOfBoundsException e) {
	         e.printStackTrace();
	      }
	      counter++;
	      child.setParent(parent);
	   }
   }

   /**
    * Fügt einen Strukturknoten entsprechend einem angegebenen Zugriffswert ein.
    *
    * @param index Zugriffswert für das eingefügte Element
    * @param child Strukturknoten
    */
   public void insertChild (int index, KStructNode child) {
      if (children == null) {
         addChild(child);
         return;
      }
      if ((index < 0) || (index > counter)) {
         index = counter;
      }

      //    if ((0 <= index) && (index <= counter)) {
      if (counter == children.length) {
         KStructNode[] help;

         help = new KStructNode[children.length * 2];
         System.arraycopy(children, 0, help, 0, index);
         System.arraycopy(children, index, help, index + 1, counter - index);
         children = help;
         children[index] = child;
      } else if (index < counter) {
         System.arraycopy(children, index, children, index + 1, counter - index);
         children[index] = child;
      } else {
         children[counter] = child;
      }
      counter++;
      child.setParent(parent);
      //  }
   }

   /**
    * Entfernt einen Strukturknoten
    *
    * @param child Strukturknoten
    */
   public void removeChild (KStructNode child) {
      int i;

      i = getIndexOfChild(child);
      removeChild(i);
   }
   
   /**
    * Removes the child node of the position index.
    * @param index child position
    */
   public void removeChild (int index) {
      if ((children != null) && (0 <= index) && (index < counter)) {
         System.arraycopy(children, index + 1, children, index, counter - index - 1);
         counter--;
         children[counter] = null;
      }  	
   }

   /**
    * Entfernt alle Strukturknoten
    */
   public void removeChildren () {
   	synchronized (parent.dfl.getLockObject()) {
   		parent.setLoaded(true);
         if (children != null) {
            for (int i = 0; i < counter; i++) {
               children[i] = null;
            }
            counter = 0;
         }
   	}
   }
}
