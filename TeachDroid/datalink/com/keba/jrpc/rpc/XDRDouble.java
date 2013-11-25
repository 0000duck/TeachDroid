package com.keba.jrpc.rpc;

import java.io.IOException;

public class XDRDouble implements XDR {
   public double value;

   public XDRDouble () {
   }
   public XDRDouble (double v) {
      value = v;
   }
   
   public void write (RPCOutputStream out) throws RPCException, IOException {
      out.writeDouble(value);
   }

   public void read (RPCInputStream in) throws RPCException, IOException {
      value = in.readDouble();
   }
}
