package com.bnotions.vicinity;

/**
 * A callback interface that defines device state changes and 
 * received messages.
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 */
public interface DeviceListener {
	
	/**
	 * Called when the device successfully connects
	 */
	public void connected();
	
	/**
	 * Called when the device is disconnected.
	 */
	public void disconnected();
	
	/**
	 * Called when a message is received from the connected device.
	 * 
	 * @param message The received message
	 */
	public void messageReceived(String message);
	
}
