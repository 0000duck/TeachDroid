package com.keba.jsdr.sdr;

import java.io.IOException;

public interface ISendStrategy {
   public  void send(IMessage msg, IConnection mCon) throws IOException, SDRException;
   public  void receive(IMessage msg, IConnection mCon, int length) throws IOException, SDRException;
}
