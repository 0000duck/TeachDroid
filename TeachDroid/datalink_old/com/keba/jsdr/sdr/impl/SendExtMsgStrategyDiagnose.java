package com.keba.jsdr.sdr.impl;

import java.io.IOException;

import com.keba.jsdr.sdr.IConnection;
import com.keba.jsdr.sdr.IMessage;
import com.keba.jsdr.sdr.SDRException;

public class SendExtMsgStrategyDiagnose extends SendExtMsgStrategy {

   protected void receiveMessageFragment(IMessage msg, IConnection connection, int length) throws IOException, SDRException {
      logMessage("receiveMessageFragment: "+((Message)msg).m_msgNr+" length: "+length);
      super.receiveMessageFragment(msg, connection, length);
      logMessage("messageFragment received: "+((Message)msg).m_msgNr+" length: "+length+" done: "+((Message)msg).m_msgContext.done);
   }

   protected void sendRemainingArgData(IMessage msg, IConnection connection) throws IOException, SDRException {
      logMessage("sendRemainingArgData: "+((Message)msg).m_msgNr);
      super.sendRemainingArgData(msg, connection);
      logMessage("sendRemainingArgData returned: "+((Message)msg).m_msgNr+" done: "+((Message)msg).m_msgContext.done);
   }

   private void logMessage(String msg) {
      System.out.println("[SendExtMsgStrategy] "+Thread.currentThread()+": "+msg);
   }
   
   
}
