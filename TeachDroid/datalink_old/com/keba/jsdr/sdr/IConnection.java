
package com.keba.jsdr.sdr;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Interface that describes the behaviour of a communication connection
 *
 */
public interface IConnection {
   public int send(int station, int serviceNr, SDR arg, SDR ret, boolean ext)
         throws IOException, SDRException;

   /**
    * Waits for the message with the given message id to complete.
    * This method blocks until the response of a previous send call
    * has arived. If an error in the SDR protocol occurse the method
    * returns false.
    * @param msgNr message id
    * @return true if the response of the message has been successfuly received
    * @throws SDRException
    * @throws IOException 
    */
   public boolean waitFor(int msgNr) throws SDRException, IOException;

   /**
    * Returns the inputstream of this connection to read the response of
    * the plc
    * @return inputstream
    */
   public SDRInputStream getInStream();

   /**
    * Returns the outputstream of this connection to send the message data
    * to the plc
    * @return outpustream
    */
   public SDROutputStream getOutStream();

   /**
    * Aquires the lock for the outputstream
    */
   public void getOutStreamLock();

   /**
    * Releases the lock for the ouputstream
    */
   public void releaseOutStreamLock();

   
   /**
    * Connects to the plc with the given connection information
    * @param conData connection information
    * @throws UnknownHostException
    * @throws IOException
    */
   public void connect(SdrConnection conData)
         throws UnknownHostException, IOException;

   /**
    * Increments the connection reference counter
    */
   public void incRefCount();

   
   /**
    * Decrements the connection reference counter
    */
   public void decRefCount();

   /**
    * Returns the reference counter
    * @return reference counter
    */
   public int getRefCount();

   /**
    * Disconnects from the plc. Closes the socket, streams,...
    * @throws IOException
    */
   public void disConnect() throws IOException;
   
   /**
    * Sets the connection timeout in milliseconds
    * @param timeMillis timeout in ms
    */
   public void setTimeOut(int timeMillis);

   /**
    * Sets the default station id for this connection 
    * @param id station id 
    */
   public void setDefaultStation(int id);

   /**
    * Returns the default station id for this connection
    * @return default station id
    */
   public int getDefaultStation();
   
   /**
    * Returns the socket where this client is connected to
    * @return host
    */
   public Socket getSocket();
}
