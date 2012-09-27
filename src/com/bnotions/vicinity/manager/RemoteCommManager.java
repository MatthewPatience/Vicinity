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

package com.bnotions.vicinity.manager;

import java.util.ArrayList;

import android.util.Log;

import com.bnotions.vicinity.device.DeviceAbsImpl;
import com.bnotions.vicinity.device.DeviceListener;
import com.bnotions.vicinity.device.ServerDevice;
import com.bnotions.vicinity.object.VicinityScanResult;
import com.bnotions.vicinity.util.Constants;


public class RemoteCommManager implements DeviceListener {
	
	private ServerDevice server;
	private ArrayList<DeviceListener> list_listeners;
	
	private VicinityScanResult scan_result;
	private int current_port_pos = 0;
	private boolean first_connection = true;
	
	public RemoteCommManager() {
		
		server = new ServerDevice();
		server.setDeviceListener(this);
		
		list_listeners = new ArrayList<DeviceListener>();
		
	}
	
	/**
	 * Returns a reference to the server; is not guaranteed to be connected.
	 * 
	 * @return The server device
	 */
	public ServerDevice getServerDevice() {
		
		return server;
	}
	
	public void connect(VicinityScanResult result) throws Exception {
		
		this.scan_result = result;
		
		server.setIpAddress(result.getIpAddress());
		
		int[] ports = result.getPorts();
		if (!server.isConnected()) {
			if (Constants.DEBUG) Log.d("Vicinity", "REMOTEMANAGER - CONNECT ON PORT + " + ports[current_port_pos]);
			server.setPort(ports[current_port_pos]);
			try {
				server.connect();
			} catch (Exception e) {
				throw new Exception("Error occured while attempting to connect on port " + ports[current_port_pos]);
			}
		}
		
	}
	
	/**
	 * Will send a String message to the server.
	 * 
	 * @param message The message to be sent
	 */
	public void sendMessage(String message) {
		
		server.sendMessage(message);
		
	}
	
	/**
	 * Will disconnect the server device and reset it to it's default state.
	 */
	public void disconnect() {
		
		if (Constants.DEBUG) Log.d("Vicinity", "REMOTEMANAGER - DISCONNECT REQUESTED");
		
		if (server.isConnected()) {
			server.disconnect();
		}
		
		server = new ServerDevice();
		server.setDeviceListener(this);
		
	}
	
	/**
	 * Will register an object for updates on device status.
	 * 
	 * @param listener
	 */
	public void addDeviceListener(DeviceListener listener) {
		
		list_listeners.add(listener);
		
	}
	
	public void removeDeviceListener(DeviceListener listener) {
		
		list_listeners.remove(listener);
		
	}

	public void connected(DeviceAbsImpl device) {
		
		if (Constants.DEBUG) Log.d("Vicinity", "REMOTEMANAGER - CONNECTED - " + device.getPort());
		
		first_connection = false;
		int num_listeners = list_listeners.size();
		for (int i = 0; i < num_listeners; i++) {
			DeviceListener listener = list_listeners.get(i);
			listener.connected(device);
		}
		
	}

	public void disconnected(DeviceAbsImpl device) {
		
		if (Constants.DEBUG) Log.d("Vicinity", "REMOTEMANAGER - DISCONNECTED - " + device.getPort());
		
		int num_listeners = list_listeners.size();
		for (int i = 0; i < num_listeners; i++) {
			DeviceListener listener = list_listeners.get(i);
			listener.disconnected(device);
		}

		//Attempt to reconnect
		try {
			if (first_connection && current_port_pos < scan_result.getPorts().length) {
				if (Constants.DEBUG) Log.d("Vicinity", "REMOTEMANAGER - RECONNECTING...");
				current_port_pos++;
				connect(scan_result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void messageReceived(DeviceAbsImpl device, String message) {
		
		int num_listeners = list_listeners.size();
		for (int i = 0; i < num_listeners; i++) {
			DeviceListener listener = list_listeners.get(i);
			listener.messageReceived(device, message);
		}
		
	}

}
