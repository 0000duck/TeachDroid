package com.keba.jrpc.rpc;

import java.io.*;

public interface XDR {
   /**
    * Writes the data to the OutputStream out.
    * @param     out is a OutputStream to write data.
    */
   public void write (RPCOutputStream out) throws RPCException, IOException;
   /**
    * Reads the data from the InputStream in.
    * @param     in is a InputStream to read data.
    */
   public void read (RPCInputStream in) throws RPCException, IOException;

}
