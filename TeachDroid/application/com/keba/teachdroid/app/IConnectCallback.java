/**
 * 
 */
package com.keba.teachdroid.app;

import java.io.Serializable;

/**
 * @author ltz
 *
 */
public interface IConnectCallback extends Serializable {

	public void connect(String _ip);

	public void disconnect();
}
