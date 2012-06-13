package com.bnotions.vicinity;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * This interface defines the common functionality of both Remote Devices 
 * and Server Devices.
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 */
public interface Device {
	
	/**
	 * This method will either make a connection to a socket or listen on 
	 * a socket, depending on the device type. The method will throw an IllegalStateException 
	 * if the device IP address is not set before calling this method.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect() throws UnknownHostException, IOException;
	
	/**
	 * This should be called when you are finished with a 
	 * connection. Be sure to call this in an Activity's onDestroy().
	 */
	public void disconnect();
	
	/**
	 * Sets a listener to listen for state changes and received messages.
	 * 
	 * @param listener
	 */
	public void setDeviceListener(DeviceListener listener);
	
	/**
	 * Sends a message to the connected device. This method will throw an 
	 * IllegalArgumentException if the device is not connected. This method 
	 * is asynchronous.
	 * 
	 * @param data The data to be sent
	 */
	public void sendMessage(String data);

}
