package com.keba.jrpc.rpc;

import java.io.IOException;

public class XDRString implements XDR {
   public String value;

   public XDRString () {
   }

   public XDRString (String v) {
      value = v;
   }

   public void write (RPCOutputStream out) throws RPCException, IOException {
      out.writeString(value);
   }

   public void read (RPCInputStream in) throws RPCException, IOException {
      value = in.readString();
   }
}
