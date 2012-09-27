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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * A server device that receives a connection from a remote device. 
 * Generally this device is the TV device.
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 */
public class ServerDevice extends DeviceAbsImpl {
	
	public ServerDevice() {
		super();
		
	}
	
	
	public void connect() throws UnknownHostException, IOException {
		
		if (ip_address != null) {
			new Thread() {
				public void run() {
					try {
						socket = new Socket(ip_address, port);
						socket.setKeepAlive(true);
						socket.setTcpNoDelay(true);
						socket.setReuseAddress(true);
						output = new DataOutputStream(socket.getOutputStream());
						input = new DataInputStream(socket.getInputStream());
						
						if (socket.isConnected()) {
							if (listener != null) listener.connected(ServerDevice.this);
						} else {
							if (listener != null) listener.disconnected(ServerDevice.this);
						}
						
						monitorMessages();
						startHeartBeatPing();
						
					} catch (IOException e) {
						if (listener != null) listener.disconnected(ServerDevice.this);
					}
				}
			}.start();
		} else {
			throw new IllegalStateException("No IP Address provided");
		}
		
	}
	
}
