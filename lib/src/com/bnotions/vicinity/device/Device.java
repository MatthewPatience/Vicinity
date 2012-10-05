/***********************************************************************************************************************
 *
 * Vicinity - Multi-Screen Android SDK
 * ==========================================
 *
 * Copyright (C) 2012 by Matthew Patience
 * http://www.github.com/MatthewPatience/Vicinity
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package com.bnotions.vicinity.device;

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
