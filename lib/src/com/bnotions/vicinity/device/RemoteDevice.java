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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Looper;
import android.util.Log;

import com.bnotions.vicinity.util.Constants;


/**
 * A remote device that connects to a server device. Generally this device is 
 * the non-TV device.
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 */
public class RemoteDevice extends DeviceAbsImpl {
	
	private boolean listening = false;
	
	public RemoteDevice() {
		super();
		
		
	}
	
	public RemoteDevice(Socket socket, DeviceListener listener) {
		super();
		
		try {
			this.socket = socket;
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());
			this.ip_address = socket.getInetAddress().getHostAddress();
			this.port = RemoteDevice.DEFAULT_PORT;
			this.listener = listener;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void connect() throws UnknownHostException, IOException {
		
		listening = true;
		
		new Thread() {
			public void run() {
				Looper.prepare();
				while (listening) {
					try {
						ServerSocket ss = new ServerSocket(port);
						socket = ss.accept();
						socket.setKeepAlive(true);
						socket.setTcpNoDelay(true);
						
						if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - NEW DEVICE CONNECTED - ADDRESS: " + socket.getInetAddress().getHostAddress());
						RemoteDevice new_device = new RemoteDevice(socket, listener);
						
						if (listener != null) listener.connected(new_device);
						
						new_device.monitorMessages();
						new_device.startHeartBeatPing();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}

	public boolean isListening() {
		return listening;
	}

	public void setListening(boolean listening) {
		this.listening = listening;
	}

}
