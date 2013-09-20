/*
 *  -------------------------------------------------------------------------
 *  (c) 2003 by KEBA AG
 *  Linz/AUSTRIA
 *  All rights reserved
 *  --------------------------------------------------------------------------
 *  Projekt   : S2000-HMI
 *  Auftragsnr: 591100
 *  Erstautor : len
 *  Datum     : 5.5.2003
 *  --------------------------------------------------------------------------
 *  $Revision: $
 *  $Author: $
 *  $Date: $
 *  ------------------------------------------------------------------------
 */

package com.keba.jrpc.rpc;

import java.util.*;

/*******************************************************************************
 * Erlaubt das Überwachen von HMI-Services auf RPC-Ebene
 * 
 * @author len
 * @created 07. Mai 2003
 ******************************************************************************/
public class NetworkMonitor {

   private Hashtable             services;

   /********************************************************************************
    *******************************************************************************/
   public final static int       NO_SERVICE      = 0, VAR_SERVICE = 2001,
         ILOG_SERVICE = 2002, SYS_SERVICE = 2003, PDP_SERVICE = 2004,
         CAT_SERVICE = 2005, TASK_SERVICE = 2006, MIC_SERVICE = 2007,
         RPC_MANAGER = 1000, ODS_SERVICE = 1483, UOS_SERVICE = 1484,
         SYS_OBJ_MAN = 3728;

   public static final int[]     SERVICE_NUMBERS = new int[] { VAR_SERVICE,
         ILOG_SERVICE, SYS_SERVICE, PDP_SERVICE, CAT_SERVICE, TASK_SERVICE,
         MIC_SERVICE, RPC_MANAGER, ODS_SERVICE, UOS_SERVICE, SYS_OBJ_MAN };

   /********************************************************************************
    *******************************************************************************/
   public final static int       ALL             = 0, SENT = 1, RECVD = 2;

   private static NetworkMonitor instance;

   private static Hashtable      names;

   private ResourceBundle        bundle;

   private boolean               m_enable        = false;

   /****************************************************************************
    * Konstruktor für NetworkMonitor
    ***************************************************************************/
   private NetworkMonitor() {
      services = new Hashtable();
      try{
         bundle = ResourceBundle.getBundle("method");
      }catch(Exception ex){}
      try {
         String value = ResourceBundle.getBundle("hmicfg").getString(
               "RPCMonitorStart");
         if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on"))
            m_enable = true;
      } catch (Throwable th) {
      }
   }

   /****************************************************************************
    * @return Liefert alle RPC-Clients
    ***************************************************************************/
   public Enumeration getClients() {
      return services.elements();
   }

   /****************************************************************************
    * Liefert den Durchsatz für einen Service
    * 
    * @param service
    *           Service-Typ
    * @param type
    *           Gesendete, Empfangene oder alle Daten
    * @return Datendurchsatz in Bytes
    ***************************************************************************/
   public synchronized int getThroughput(int service, int type) {
      Vector entries = (Vector) services.get(new Integer(service));
      Enumeration enumeration;
      int retVal = 0;
      RPCClient client;
      if (entries != null) {
         enumeration = entries.elements();
         while (enumeration.hasMoreElements()) {
            client = (RPCClient) enumeration.nextElement();
            if (type == ALL || type == SENT) {
               retVal += client.getSentByteCount();
            }
            if (type == ALL || type == RECVD) {
               retVal += client.getReceivedByteCount();
            }
         }
      }
      return retVal;
   }

   /****************************************************************************
    * Registriert einen Netzwerk-Client
    ***************************************************************************/
   public synchronized void registerClient(RPCClient client) {
      if (client.getProgramNumber() != NO_SERVICE) {
         Integer key = new Integer(getService(client.getProgramNumber()));
         Vector entries = (Vector) services.get(key);
         if (entries == null) {
            entries = new Vector(1);
            services.put(key, entries);
         }
         entries.addElement(client);
         if (m_enable) {
            client.setDebug(true);
         }
      }
   }

   /****************************************************************************
    * @param Deregistriert
    *           einen Netzwerk-Client
    ***************************************************************************/
   public synchronized void unregisterClient(RPCClient client) {
      Integer key = new Integer(getService(client.getProgramNumber()));
      Vector entries = (Vector) services.get(key);
      if (entries != null) {
         entries.removeElement(client);
         if (entries.size() == 0) {
            services.remove(key);
         }
      }
   }

   /****************************************************************************
    * Liefert den Service-Typ für eine bestimmte Programm-Nummer
    ***************************************************************************/
   private int getService(int programNumber) {
      if (programNumber == RPC_MANAGER) {
         return RPC_MANAGER;
      }
      if (programNumber == ODS_SERVICE) {
         return ODS_SERVICE;
      }
      if (programNumber == SYS_OBJ_MAN) {
         return SYS_OBJ_MAN;
      }
      if (programNumber == UOS_SERVICE) {
         return UOS_SERVICE;
      }
      if (programNumber > 2000 && programNumber < 2100) {
         programNumber -= 2001;
         return 2001 + programNumber % 7;
      }
      return NO_SERVICE;
   }

   /****************************************************************************
    * Singleton-Pattern: Falls der Monitor aktiv ist (System-Property
    * NETWORK_DEBUG gesetzt), so wird die Instanz des Monitors zurückgegeben.
    * Ansonsten ist der Rückgabewert null.
    ***************************************************************************/
   public static NetworkMonitor getInstance() {
      if (instance == null) {// && isLogNetworkTraffic()) {
         instance = new NetworkMonitor();
      }
      return instance;
   }

   /****************************************************************************
    * Gibt an, ob der Monitor aktiv ist
    ***************************************************************************/
   public static boolean isLogNetworkTraffic() {
      return System.getProperty("NETWORK_DEBUG") != null;
   }

   /****************************************************************************
    * Liefert die String-Repräsentation für einen bestimmten Service.
    ***************************************************************************/
   public static String getServiceName(int service) {
      return (String) names.get(new Integer(service));
   }

   static {
      names = new Hashtable();
      names.put(new Integer(VAR_SERVICE), "Variable-Service");
      names.put(new Integer(ILOG_SERVICE), "SaIlog-Service");
      names.put(new Integer(SYS_SERVICE), "System-Service");
      names.put(new Integer(PDP_SERVICE), "PDP-Service");
      names.put(new Integer(CAT_SERVICE), "Catalog-Service");
      names.put(new Integer(TASK_SERVICE), "Task-Service");
      names.put(new Integer(MIC_SERVICE), "Mic-Service");
      names.put(new Integer(RPC_MANAGER), "RPC-Manager");
      names.put(new Integer(ODS_SERVICE), "TC-Service");
      names.put(new Integer(UOS_SERVICE), "UOS-Service");
      names.put(new Integer(SYS_OBJ_MAN), "System-Object-Manager");
   }

   /**
    * Resets the debuginfo table
    */
   public void resetDebugInfos() {
      Enumeration enumeration = services.keys();
      while (enumeration.hasMoreElements()) {
         Object key = enumeration.nextElement();
         Vector clientVect = (Vector) services.get(key);
         if (clientVect != null) {
            int cnt = clientVect.size();
            for (int i = 0; i < cnt; i++) {
               ((RPCClient) clientVect.elementAt(i)).resetCallInfo();
            }
         }
      }
   }

   /**
    * Starts debunging for the given clientid
    * 
    * @param clientIds
    *           client id
    */
   public void startDebugCalls(int clientIds) {
      startStopDebugCalls(clientIds, true);
   }

   /**
    * Stops debunging for the given clientid
    * 
    * @param clientIds
    *           client id
    */
   public void stopDebugCalls(int clientIds) {
      startStopDebugCalls(clientIds, false);
   }

   private void startStopDebugCalls(int clientIds, boolean start) {
      Vector clientVect = (Vector) services.get(new Integer(clientIds));
      if (clientVect != null) {
         int clientCnt = clientVect.size();
         for (int j = 0; j < clientCnt; j++) {
            ((RPCClient) clientVect.elementAt(j)).setDebug(start);
         }
      }
   }

   /**
    * Returns the debuginfos for the given client id
    * 
    * @param clientId
    *           client id
    * @return debuginfos
    */
   public Hashtable[] getDebuCallInfo(int clientId) {
      Vector vect = (Vector) services.get(new Integer(clientId));
      if (vect == null)
         return null;
      int cnt = vect.size();
      Hashtable[] result = new Hashtable[cnt];
      for (int i = 0; i < cnt; i++) {
         result[i] = ((RPCClient) vect.elementAt(i)).getDebugCallInfo();
      }
      return result;
   }

   /**
    * Returns the methodname of the given method for the given service
    * 
    * @param serviceNr
    *           service number
    * @param methodNr
    *           method number
    * @return methodname
    */
   public String getMethodName(int serviceNr, int methodNr) {
      String serverName = getServiceName(serviceNr);
      try {
         return bundle.getString(serverName + "_" + methodNr);
      } catch (Exception ex) {
      }
      return serverName + "_" + methodNr;
   }
}