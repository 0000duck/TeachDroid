
package com.keba.jrpc.rpc;

/**
 * Class containing debug information like invokation counter, invokation time
 */
public class DebugCallInfo implements Cloneable {

   private int  m_callCnt     = 0;

   private long m_minCallTime = Long.MAX_VALUE;

   private long m_maxCallTime = 0;

   private long m_sumCallTime = 0;

   /**
    * Constructor
    */
   public DebugCallInfo() {
   }

   /**
    * Updates the data; increments the callcount and updates the time
    * information
    * 
    * @param time
    *           invokationtime
    */
   public void incMethodCall(long time) {
      m_callCnt++;
      if (time < m_minCallTime) {
         m_minCallTime = time;
      }
      if (time > m_maxCallTime) {
         m_maxCallTime = time;
      }

      m_sumCallTime += time;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   public Object clone() {
      DebugCallInfo clone = new DebugCallInfo();
      clone.m_callCnt = m_callCnt;
      clone.m_maxCallTime = m_maxCallTime;
      clone.m_minCallTime = m_minCallTime;
      clone.m_sumCallTime = m_sumCallTime;
      return clone;
   }

   /**
    * Returns the invocation counter
    * 
    * @return Returns the callCnt.
    */
   public int getCallCnt() {
      return m_callCnt;
   }

   /**
    * Returns the maximum invocation time
    * 
    * @return maximum invocation time
    */
   public long getMaxCallTime() {
      return m_maxCallTime;
   }

   /**
    * Returns the minimum invocation time
    * 
    * @return minimum invocation time
    */
   public long getMinCallTime() {
      return m_minCallTime;
   }

   /**
    * Returns the average invocation time
    * 
    * @return average invocation time
    */
   public long getAvgCallTime() {
      return m_sumCallTime / m_callCnt;
   }
}