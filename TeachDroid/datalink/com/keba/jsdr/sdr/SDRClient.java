package com.keba.jsdr.sdr;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author gas
 *
 */
public class SDRClient {
   IConnection m_connection;
   private SdrConnection m_connectionData;
   public SDRClient(SdrConnection connection)  {
      m_connectionData = connection;
      
   }
   /**
    * @param funcNr
    * @param extCall
    * @param argValue
    * @param retValue
    * @throws SDRException
    * @throws IOException
    */
   public void call(int funcNr, boolean extCall, SDR argValue, SDR retValue) throws SDRException, IOException{
      try {
      if(m_connection == null) {
         throw new SDRException(SDRException.NOT_CONNECTED);
      }
      int id = -1;
      do {
         if(argValue != null) {
            argValue.reset();
         }
         retValue.reset();
         id = m_connection.send(m_connectionData.station, funcNr, argValue, retValue, extCall);
      }while(!m_connection.waitFor(id));
      
      }catch(IOException ex) {
         //close connection because of error
         close();
         throw ex;
      }
   }
   
   /**
    * Closes this clients connection
    */
   public void close() {
      if(m_connection != null) {
         ConnectionFactory.releaseConnection(m_connectionData);
         m_connection = null;
      }
   }
   
   
   /**
    * Trys to connect the client to the plc
    * @throws UnknownHostException
    * @throws IOException
    * @throws SDRException
    */
   public void connect() throws UnknownHostException, IOException, SDRException {
      m_connection = ConnectionFactory.getConnection(m_connectionData);
   }
   
   public void setTimeout(int timeMillis) {
      if(m_connection != null) {
         m_connection.setTimeOut(timeMillis);
      }
   }
   
   /**
    * Returns the socket where this client is connected to
    * @return host
    */
   public Socket getSocket() {
   	if(m_connection != null) {
   		return m_connection.getSocket();
   	}
   	return null;
   }
   
//   /**
//    * @param sconnection
//    * @return
//    * @throws IOException
//    * @throws SDRException
//    */
//   public static int getStationID(SdrConnection sconnection) throws IOException, SDRException {
//      IConnection connection = ConnectionFactory.getConnection(sconnection);
//      SDRByte stationValue = new SDRByte();
//      connection.send(0, 0,null, stationValue, false);
//      return stationValue.m_value;
//   }
   
//   public static void main(String[] args) {
//      Connection con = new Connection();
//      try {
////         SdrConnection connecton = new SdrConnection();
////         connecton.ip = "172.26.12.102";
////         connecton.timeOut = 6000;
////         con.connect(connecton);
////         SDRByte stationValue = new SDRByte();
////         int id = con.send(0, 0,null, stationValue, false);
////         con.waitFor(id);
////         System.out.println(stationValue.m_value);
//         SdrSystemNetworkClient client = new SdrSystemNetworkClient();
//         client.connect("172.26.12.102", "");
//         client.getDirInfo("/masterdisk");
//      } catch (UnknownHostException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      } catch (IOException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
////      } catch (SDRException e) {
////         // TODO Auto-generated catch block
////         e.printStackTrace();
//      } catch (NetworkException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }
//   }
   
}
