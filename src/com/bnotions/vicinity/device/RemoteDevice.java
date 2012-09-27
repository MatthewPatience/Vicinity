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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;


/**
 * A remote device that connects to a server device. Generally this device is 
 * the non-TV device.
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 */
public class RemoteDevice extends DeviceAbsImpl {
	
	public RemoteDevice() {
		super();
		
		
	}
	
	public void connect() throws UnknownHostException, IOException {
		
		new Thread() {
			public void run() {
				try {
					ServerSocket ss = new ServerSocket();
					ss.setReuseAddress(true);
					ss.bind(new InetSocketAddress(port));
					socket = ss.accept();
					socket.setKeepAlive(true);
					socket.setTcpNoDelay(true);
					output = new DataOutputStream(socket.getOutputStream());
					input = new DataInputStream(socket.getInputStream());
					
					if (listener != null) listener.connected(RemoteDevice.this);
					
					monitorMessages();
					startHeartBeatPing();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}

}
