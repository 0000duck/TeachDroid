package com.keba.jrpc.rpc;

import java.io.IOException;

public class XDRFloat implements XDR {
   public float value;

   public XDRFloat () {
   }
   
   public XDRFloat (float v) {
      value = v;
   }
   
   public void write (RPCOutputStream out) throws RPCException, IOException {
      out.writeFloat(value);
   }

   public void read (RPCInputStream in) throws RPCException, IOException {
      value = in.readFloat();
   }
}
