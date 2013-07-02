
package com.keba.jsdr.sdr;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;

/**
 * Factory for creating the comminaction objects.
 * A connection to one plc will be shared between
 * the clients. With the environment property SdrConnection
 * differen connection objects can be assigned.
 * @author gas
 *
 */
public class ConnectionFactory {

   static Hashtable connectionTable = new Hashtable(5);
   static Class     connectionClass;

   static {
      String sdrClass = System.getProperty("SdrConnection",
            "com.keba.jsdr.sdr.impl.Connection");
      try {
         connectionClass = Class.forName(sdrClass);
      } catch (ClassNotFoundException e) {
         try {
            connectionClass = Class
                  .forName("com.keba.jsdr.sdr.impl.Connection");
         } catch (ClassNotFoundException e1) {
            // ignore
         }
      }
   }

   /**
    * Returns the connection for the given connection information
    * @param connectionData connection information
    * @return connection instance
    * @throws UnknownHostException
    * @throws IOException
    * @throws SDRException
    */
   public static synchronized IConnection getConnection(
         SdrConnection connectionData) throws UnknownHostException,
         IOException, SDRException {
      IConnection connection = (IConnection) connectionTable
            .get(connectionData.ip);
      if (connection == null) {
         try {
            connection = (IConnection) connectionClass.newInstance();
         } catch (InstantiationException e) {
            e.printStackTrace();
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         }

         connection.connect(connectionData);
         if(connectionData.station == -1) {
            SDRByte stationNr = new SDRByte();
            int mid = connection.send(0, 0, null, stationNr, false);
            if (connection.waitFor(mid)) {
               connectionData.station = stationNr.m_value;
               connection.setDefaultStation(connectionData.station);
            } else {
               connectionData.station = -1;
               connection.disConnect();
               throw new SDRException(SDRException.PROTOCOL_ERROR);
            }
         }
         connectionTable.put(connectionData.ip, connection);
         connection.incRefCount();
      } else {
         if(connectionData.station == -1) {
            connectionData.station = connection.getDefaultStation();
         }
         connection.incRefCount();
      }
      return connection;
   }

   /**
    * Releases the connection. If a connection has no more references
    * it will be closed.
    * @param connectionData connection information
    */
   public static synchronized void releaseConnection(
         SdrConnection connectionData) {
      IConnection connection = (IConnection) connectionTable
            .get(connectionData.ip);
      if (connection != null) {
         connection.decRefCount();
         if (connection.getRefCount() == 0) {
            try {
               connection.disConnect();
            } catch (IOException e) {
               // ignore
            }
            connectionTable.remove(connectionData.ip);
         }
      }

   }

}
