package com.keba.jsdr.sdr;

import java.io.IOException;


public interface SDR {
   /**
    * Writes the data to the OutputStream out.
    * @param     out is a OutputStream to write data.
    */
   public void write (SDROutputStream out, SDRContext context) throws SDRException, IOException;
   /**
    * Reads the data from the InputStream in.
    * @param     in is a InputStream to read data.
    */
   public void read (SDRInputStream in, SDRContext context) throws SDRException, IOException;
   /**
    * Returns the raw data size in bytes
    * @return data size
    */
   public int size();
   /**
    * Called to Reset/reinitialize a object
    */
   public void reset();
}
