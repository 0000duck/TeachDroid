package com.keba.jrpc.rpc;

import java.io.IOException;

public class XDRInt implements XDR {
   public int value;

   public XDRInt () {
   }

   public XDRInt (int v) {
      value = v;
   }

   public void write (RPCOutputStream out) throws RPCException, IOException {
      out.writeInt(value);
   }

   public void read (RPCInputStream in) throws RPCException, IOException {
      value = in.readInt();
   }
}
