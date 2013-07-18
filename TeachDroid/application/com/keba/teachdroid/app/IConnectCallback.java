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

	public void connect();

	public void disconnect();
}
