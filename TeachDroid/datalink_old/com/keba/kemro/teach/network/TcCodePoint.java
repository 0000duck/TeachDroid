package com.keba.kemro.teach.network;

/**
 * Breakpoint oder Watchpoint
 */
public class TcCodePoint {
   /** Breakpoint */
   public static int BREAKPOINT;
   /** Watchpoint */
   public static int WATCHPOINT;
   /** Main flow breakpoint */
   public static int MAIN_FLOW_BREAKPOINT;
   
   /** Zeilennummer */
   public int lineNr;
   /** BREAKPOINT, WATCHPOINT, MAIN_FLOW_BREAKPOINT */
   public int kind;
   /** true für freigeschaltet  */
   public boolean isEnabled;
}