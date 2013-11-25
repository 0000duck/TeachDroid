package com.keba.jrpc.rpc;

import java.io.IOException;

public class XDRHyper implements XDR {
   public long value;

   public XDRHyper () {
   }
   
   public XDRHyper (long v) {
      value = v;
   }
   
   public void write (RPCOutputStream out) throws RPCException, IOException {
      out.writeHyper(value);
   }

   public void read (RPCInputStream in) throws RPCException, IOException {
      value = in.readHyper();
   }
}
