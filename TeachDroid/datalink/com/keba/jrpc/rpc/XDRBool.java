package com.keba.jrpc.rpc;

import java.io.IOException;

public class XDRBool implements XDR {
   public boolean value;

   public XDRBool () {
   }
   
   public XDRBool (boolean v) {
      value = v;
   }

   public void write (RPCOutputStream out) throws RPCException, IOException {
      out.writeBool(value);
   }

   public void read (RPCInputStream in) throws RPCException, IOException {
      value = in.readBool();
   }
}
