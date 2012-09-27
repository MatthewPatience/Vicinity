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
	public void connected(DeviceAbsImpl device);
	
	/**
	 * Called when the device is disconnected.
	 */
	public void disconnected(DeviceAbsImpl device);
	
	/**
	 * Called when a message is received from the connected device.
	 * 
	 * @param device The sender
	 * @param message The message
	 */
	public void messageReceived(DeviceAbsImpl device, String message);
	
}
