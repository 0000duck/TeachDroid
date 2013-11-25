
package com.keba.jsdr.sdr.impl;

import com.keba.jsdr.sdr.ISendStrategy;

public class StrategyFactory {
   static ISendStrategy       EXT_STRATEGY;                                // =
                                                                           // new
                                                                           // SendExtMsgStrategy();
                                                                           // static
                                                                           // final
                                                                           // ISendStrategy
                                                                           // EXT_STRATEGY_DIAG
                                                                           // =
                                                                           // new
                                                                           // SendExtMsgStrategyDiagnose();
   static final ISendStrategy SHORT_STRATEGY = new SendShortMsgStrategy();
   static boolean             DIAGNOSE       = false;

   static {
      String sdrClass = System.getProperty("SdrSendExtStrategy",
            "com.keba.jsdr.sdr.impl.SendExtMsgStrategy");
      try {
         try {
            EXT_STRATEGY = (ISendStrategy) Class.forName(sdrClass)
                  .newInstance();
         } catch (InstantiationException e) {
            // ignore
         } catch (IllegalAccessException e) {
            // ignore
         }
      } catch (ClassNotFoundException e) {
         try {
            try {
               EXT_STRATEGY = (ISendStrategy) Class.forName(
                     "com.keba.jsdr.sdr.impl.SendExtMsgStrategy").newInstance();
            } catch (InstantiationException e1) {
               // ignore
            } catch (IllegalAccessException e1) {
               // ignore
            }
         } catch (ClassNotFoundException e1) {
            // ignore
         }
      }
   }

   public static ISendStrategy getStrategy(boolean ext) {
      ISendStrategy strategy;
      if (ext) {
         strategy = EXT_STRATEGY;
      } else {
         strategy = SHORT_STRATEGY;
      }
      return strategy;
   }
}
