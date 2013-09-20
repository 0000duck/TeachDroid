
package com.keba.jsdr.sdr.impl;

import java.io.IOException;

import com.keba.jsdr.sdr.IConnection;
import com.keba.jsdr.sdr.IMessage;
import com.keba.jsdr.sdr.SDRException;
import com.keba.jsdr.sdr.ISendStrategy;

public class SendShortMsgStrategy implements ISendStrategy {

   public void receive(IMessage msg, IConnection connection, int length)
         throws IOException, SDRException {
      // messagenumber received outside

      connection.getInStream().ensureAvailableBytes(length);
      msg.getMsgContext().remainingStreamBytes = length;
      msg.getRetValue().read(connection.getInStream(), msg.getMsgContext());
      if (!msg.getMsgContext().done) {
         // error
         if (msg.getMsgContext().remainingStreamBytes > 0) {
            connection.getInStream().skip(
                  msg.getMsgContext().remainingStreamBytes);
         }
         throw new SDRException(SDRException.DATA_ERROR);
      }

      if (msg.getMsgContext().remainingStreamBytes > 0) {
         // error
         connection.getInStream()
               .skip(msg.getMsgContext().remainingStreamBytes);
         throw new SDRException(SDRException.DATA_ERROR);
      }

   }

   public void send(IMessage msg, IConnection connection) throws IOException,
         SDRException {
      // messagenumber send outside
      // sendMsgNr(outStream);
      int argSize = 0;

      if (msg.getArgValue() != null) {
         argSize = msg.getArgValue().size();
      }
      // TODO checkargsize
      msg.sendHeader(connection.getOutStream(), argSize);
      msg.getMsgContext().remainingStreamBytes = argSize;
      if (msg.getArgValue() != null) {
         msg.getArgValue()
               .write(connection.getOutStream(), msg.getMsgContext());
      }
      connection.getOutStream().flush();
   }
}
