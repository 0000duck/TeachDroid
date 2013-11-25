package com.keba.kemro.teach.network.base;

public class TcStructuralCache {
   private static final int MAX_COLLISION = 20;
   private static final int DIRENTRY_CACHE_SIZE = 601;
   private static final int DIRENTRY_SECOND_HASH = 599;
   private static final int CACHE_SIZE = 4003;
   private static final int SECOND_HASH = 4001;
   private final TcStructuralAbstractNode[] dirEntryCache = new TcStructuralAbstractNode[DIRENTRY_CACHE_SIZE];
   private final TcStructuralAbstractNode[] cache = new TcStructuralAbstractNode[CACHE_SIZE];
   
   static int dirEntryCollisions;
   static int collision;
   
   protected TcStructuralCache() {	
   }
   
   
   /**
    * Entfernt alle Cache - Einträge.
    */
   public void removeAll () {
      for (int i = 0; i < DIRENTRY_CACHE_SIZE; i++) {
         dirEntryCache[i] = null;
      }
      for (int i = 0; i < CACHE_SIZE; i++) {
         cache[i] = null;
      }
   }
   
   /**
    * Entfernt das angebenen Projekt und alle Subknoten aus dem Cache.
    *
    * @param project Projekt
    */
   public void removeProject (TcStructuralAbstractNode project) {
      removeDirEntries(project);
      checkCache();
   }

   /**
    * Entfernt alle Programm und Bausteinknoten, die zum Projekt gehören, aus dem Cache. 
    *
    * @param project Projekt
    */
   protected void removeDirEntries (TcStructuralAbstractNode project) {
      for (int i = 0; i < DIRENTRY_CACHE_SIZE; i++) {
         if (dirEntryCache[i] != null) {
            if ((dirEntryCache[i].equals(project) || (dirEntryCache[i].parent == null) ||
                  dirEntryCache[i].parent.equals(project))) {
               dirEntryCache[i] = null;
            } else if (dirEntryCache[i].parent != null) {
            	TcStructuralAbstractNode parent = dirEntryCache[i].parent.parent;
               while ((parent != null) && parent.equals(project)) {
                  parent = parent.parent;
               }
               if (parent != null) {
                  dirEntryCache[i] = null;
               }
            }
         }
      }
      rehashDirEntries();
   }

   /**
    * Rehashed die Einträge im Programm- bzw. Baustein - Cache
    */
   protected void rehashDirEntries () {
      /*
         // for verifying
         TcStructuralNode[] verifyCache = new TcStructuralNode[DIRENTRY_CACHE_SIZE];
         int counter = 0;
         for (int i = 0; i < DIRENTRY_CACHE_SIZE; i++) {
            if (dirEntryCache[i] != null) {
               // for varify
               verifyCache[counter] = dirEntryCache[i];
               counter++;
            }
         }
       */
      for (int i = 0; i < DIRENTRY_CACHE_SIZE; i++) {
         if (dirEntryCache[i] != null) {
            dirEntryCache[i].setMarked(true);
         }
      }
      for (int i = 0; i < DIRENTRY_CACHE_SIZE; i++) {
         if ((dirEntryCache[i] != null) && dirEntryCache[i].isMarked()) {
            if ((dirEntryCache[i].handle % DIRENTRY_CACHE_SIZE) != i) {
            	TcStructuralAbstractNode osn = dirEntryCache[i];
               dirEntryCache[i] = null;
               rehashDirEntry(osn);
            } else {
               dirEntryCache[i].setMarked(false);
            }
         }
      }
      /*
         // verify cache
         for (int i = 0; i < counter; i++) {
            if (getFromDirEntryCache(verifyCache[i].handle) == null) {
               System.out.println("TcStructuralModel - rehashDirEntries: error");
            }
         }
       */
   }

   /**
    * Rehashed den angegeben Programm- bzw. Bausteinknoten.
    *
    * @param n Programm- bzw. Bausteinknoten
    */
   protected void rehashDirEntry (TcStructuralAbstractNode n) {
   	TcStructuralAbstractNode help = null;
      int pos = (n.handle) % DIRENTRY_CACHE_SIZE;
      if (dirEntryCache[pos] != null) {
         if (dirEntryCache[pos].isMarked()) {
            help = dirEntryCache[pos];
            n.setMarked(false);
            dirEntryCache[pos] = n;
            rehashDirEntry(help);
         } else {
            // collision
            int i = 0;
            int c = (n.handle % DIRENTRY_SECOND_HASH);
            pos = (pos + c) % DIRENTRY_CACHE_SIZE;
            if (
                (dirEntryCache[pos] != null) &&
                ((dirEntryCache[pos].handle % DIRENTRY_CACHE_SIZE) != pos)) {
               help = dirEntryCache[pos];
               dirEntryCache[pos] = null;
               rehashDirEntry(help);
            }
            while (
                   (i < MAX_COLLISION) && (dirEntryCache[pos] != null) &&
                   !dirEntryCache[pos].isMarked()) {
               pos = (pos + c) % DIRENTRY_CACHE_SIZE;
               i++;
            }
            if ((dirEntryCache[pos] != null) && dirEntryCache[pos].isMarked()) {
               help = dirEntryCache[pos];
               n.setMarked(false);
               dirEntryCache[pos] = n;
               rehashDirEntry(help);
            } else {
               n.setMarked(false);
               dirEntryCache[pos] = n;
            }
         }
      } else {
         n.setMarked(false);
         dirEntryCache[pos] = n;
      }
   }

   /**
    * Fügt eine Programm- oder Bausteinknoten zum Programm- bzw. Baustein - Cache hinzu. 
    *
    * @param n Programm- oder Bausteinknoten
    */
   public void putToDirEntryCache (TcStructuralAbstractNode n) {
      int pos = (n.handle) % DIRENTRY_CACHE_SIZE;
      if ((dirEntryCache[pos] != null) && (dirEntryCache[pos].handle != n.handle)) {
         int i = 0;
         int c = (n.handle % DIRENTRY_SECOND_HASH);
         pos = (pos + c) % DIRENTRY_CACHE_SIZE;
         while (
                (i < MAX_COLLISION) && (dirEntryCache[pos] != null) &&
                (dirEntryCache[pos].handle != n.handle)) {
            pos = (pos + c) % DIRENTRY_CACHE_SIZE;
            i++;
         }
         if ((dirEntryCache[pos] != null) && (dirEntryCache[pos].handle != n.handle)) {
            dirEntryCollisions++;
            //System.out.println("dirEntry collision at " + pos + ", counter: " + dirEntryCollisions + ", name: " + n.name);
            return;
         }
      }
      dirEntryCache[pos] = n;
   }

   /**
    * Liefert den Programm- oder Bausteinknoten für den angegebenen Handle zurück.
    *
    * @param handle Handle
    *
    * @return Programm- oder Bausteinknoten
    */
   public TcStructuralAbstractNode getFromDirEntryCache (int handle) {
      int pos = (handle) % DIRENTRY_CACHE_SIZE;
      if (dirEntryCache[pos] == null) {
         return null;
      } else if (dirEntryCache[pos].handle == handle) {
         return dirEntryCache[pos];
      }
      int i = 0;
      int c = (handle % DIRENTRY_SECOND_HASH);
      pos = (pos + c) % DIRENTRY_CACHE_SIZE;
      while (
             (i < MAX_COLLISION) && (dirEntryCache[pos] != null) &&
             (dirEntryCache[pos].handle != handle)) {
         pos = (pos + c) % DIRENTRY_CACHE_SIZE;
         i++;
      }
      return ((dirEntryCache[pos] != null) && (dirEntryCache[pos].handle == handle))
             ? dirEntryCache[pos] : null;
   }

   /**
    * Überprüft den Cache auf verwaiste Einträge.
    */
   protected void checkCache () {
      for (int i = 0; i < CACHE_SIZE; i++) {
         if (
             (cache[i] != null) &&
             ((cache[i].dirEntryNode == null) ||
             (getFromDirEntryCache(cache[i].dirEntryNode.getHandle()) == null))) {
            cache[i] = null;
         }
      }
      rehashCache();
   }

   /**
    * Rehashed den Cache.
    */
   protected void rehashCache () {
      /*
         //for verifying
         TcStructuralNode[] verifyCache = new TcStructuralNode[CACHE_SIZE];
         int counter = 0;
         for (int i = 0; i < CACHE_SIZE; i++) {
            if (cache[i] != null) {
               // for varify
               verifyCache[counter] = cache[i];
               counter++;
            }
         }
       */
      for (int i = 0; i < CACHE_SIZE; i++) {
         if (cache[i] != null) {
            cache[i].setMarked(true);
         }
      }
      for (int i = 0; i < CACHE_SIZE; i++) {
         if ((cache[i] != null) && cache[i].isMarked()) {
            if ((cache[i].handle % DIRENTRY_CACHE_SIZE) != i) {
            	TcStructuralAbstractNode osn = cache[i];
               cache[i] = null;
               rehashCache(osn);
            } else {
               cache[i].setMarked(false);
            }
         }
      }
      /*
         // verify cache
         for (int i = 0; i < counter; i++) {
            if (getFromCache(verifyCache[i].handle) == null) {
               System.out.println("TcStructuralModel - rehashCache: error");
            }
         }
       */
   }

   /**
    * Rehashed den angegebenen Knoten.
    *
    * @param n Strukturbaumknoten
    */
   protected void rehashCache (TcStructuralAbstractNode n) {
   	TcStructuralAbstractNode help = null;
      int pos = (n.handle) % CACHE_SIZE;
      if (cache[pos] != null) {
         if (cache[pos].isMarked()) {
            help = cache[pos];
            n.setMarked(false);
            cache[pos] = n;
            rehashCache(help);
         } else {
            // collision
            int i = 0;
            int c = (n.handle % SECOND_HASH);
            pos = (pos + c) % CACHE_SIZE;
            if ((cache[pos] != null) && ((cache[pos].handle % CACHE_SIZE) != pos)) {
               help = cache[pos];
               cache[pos] = null;
               rehashCache(help);
            }
            while ((i < MAX_COLLISION) && (cache[pos] != null) && !cache[pos].isMarked()) {
               pos = (pos + c) % CACHE_SIZE;
               i++;
            }
            if ((cache[pos] != null) && cache[pos].isMarked()) {
               help = cache[pos];
               n.setMarked(false);
               cache[pos] = n;
               rehashCache(help);
            } else {
               n.setMarked(false);
               cache[pos] = n;
            }
         }
      } else {
         n.setMarked(false);
         cache[pos] = n;
      }
   }

   /**
    * Liefert für den angegebenen Handle den zugehörigen Strukturbaumknoten.
    *
    * @param handle Handle
    *
    * @return Strukturbaumknoten
    */
   public TcStructuralAbstractNode getFromCache (int handle) {
      int pos = (handle) % CACHE_SIZE;
      if (cache[pos] == null) {
         return null;
      } else if (cache[pos].handle == handle) {
         return cache[pos];
      }
      int i = 0;
      int c = (handle % SECOND_HASH);
      pos = (pos + c) % CACHE_SIZE;
      while ((i < MAX_COLLISION) && (cache[pos] != null) && (cache[pos].handle != handle)) {
         pos = (pos + c) % CACHE_SIZE;
         i++;
      }
      return ((cache[pos] != null) && (cache[pos].handle == handle)) ? cache[pos] : null;
   }

   /**
    * Fügt einen Strukturbaumknoten zum Cache hinzu.
    *
    * @param n Strukturbaumknoten
    */
   public void putToCache (TcStructuralAbstractNode n) {
      int pos = (n.handle) % CACHE_SIZE;
      if ((cache[pos] != null) && (cache[pos].handle != n.handle)) {
         int i = 0;
         int c = (n.handle % SECOND_HASH);
         pos = (pos + c) % CACHE_SIZE;
         while ((i < MAX_COLLISION) && (cache[pos] != null) && (cache[pos].handle != n.handle)) {
            pos = (pos + c) % CACHE_SIZE;
            i++;
         }
         if ((cache[pos] != null) && (cache[pos].handle != n.handle)) {
            collision++;
            //System.out.println("cache collision at " + pos + ", counter: " + collision + ", name: " + n.name);
            return;
         }
      }
      cache[pos] = n;
   }

}
