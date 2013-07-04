package com.keba.jrpc.rpc;

public class RPCException extends Exception {
   // Accept State
   public static final int PROG_UNAVAIL    = 1;   /* remote hasn't exported program        */
   public static final int PROG_MISMATCH   = 2;   /* remote can't support version #        */
   public static final int PROC_UNAVAIL    = 3;   /* program can't support procedure       */
   public static final int GARBAGE_ARGS    = 4;   /* procedure can't decode params         */
   public static final int SYSTEM_ERR      = 5;   /* errors like memory allocation failure */
   // Denied State
   public static final int RPC_MISMATCH    = 6;   /* RPC version number != 2          */
   public static final int AUTH_ERROR      = 7;   /* remote can't authenticate caller */

   public static final int NO_REPLY       = 8;   /* replay message expected */
   public static final int TRANSACTION_ID = 9;   /* wrong transaction id */

   public static final int RECORD_ERROR    = 10; /* record marking standard error */

   private int kind;

   public RPCException (int kind, String s) {
      super(s);
      this.kind = kind;
   }

   public int getKind () {
      return kind;
   }

}