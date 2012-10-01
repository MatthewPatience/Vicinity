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

import com.bnotions.vicinity.device.Device;
import com.bnotions.vicinity.device.DeviceAbsImpl;
import com.bnotions.vicinity.device.DeviceListener;
import com.bnotions.vicinity.device.RemoteDevice;
import com.bnotions.vicinity.util.Constants;

public class ServerCommManager implements DeviceListener {
	
	private ArrayList<RemoteDevice> list_connected;
	private RemoteDevice port_device;
	private int current_id = 1;
	
	private ArrayList<DeviceListener> list_listeners;
	
	public ServerCommManager() {
		
		list_connected = new ArrayList<RemoteDevice>();
		
		list_listeners = new ArrayList<DeviceListener>();
		
	}
	
	/**
	 * Will return an ArrayList of currently connected devices.
	 * 
	 * @return
	 */
	public ArrayList<RemoteDevice> getConnectedDevices() {
		
		return list_connected;
	}
	
	/**
	 * Will start the server listening on it's internal dedicated 
	 * ports, be sure to stop listening when all devices are connected.
	 * 
	 * @throws Exception
	 */
	public void startListening() throws Exception {
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - STARTED LISTENING...");
		
		port_device = new RemoteDevice();
		port_device.setPort(RemoteDevice.DEFAULT_PORT);
		port_device.setDeviceListener(this);
		try {
			port_device.connect();
		} catch (Exception e) {
			throw new Exception("Error occured while attempting to listen on port " + RemoteDevice.DEFAULT_PORT);
		}
		
	}
	
	/**
	 * Will stop listening on all dedicated ports.
	 */
	public void stopListening() {
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - STOP LISTENING REQUESTED");
		
		port_device.setListening(false);
		port_device.disconnect();
		
	}
	
	public boolean isListening() {
		
		if (port_device == null) {
			return false;
		}
		
		return port_device.isListening();
	}
	
	/**
	 * Disconnects all currently connected RemoteDevices.
	 */
	public void disconnectAll() {
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - DISCONNECT ALL DEVICES REQUESTED");
		
		port_device.disconnect();
		port_device = null;
		
	}
	
	/**
	 * Will send a message to all currently connected RemoteDevices.
	 * 
	 * @param message
	 */
	public void sendBroadcastMessage(String message) {
		
		int num_connected = list_connected.size();
		for (int i = 0; i < num_connected; i++) {
			RemoteDevice device = list_connected.get(i);
			device.sendMessage(message);
		}
		
	}
	
	/**
	 * Will send a direct message to a single Remote Device.
	 * 
	 * @param device_id The ID of the device to send a message to
	 * @param message 
	 */
	public void sendDirectMessage(int device_id, String message) {
		
		int num_connected = list_connected.size();
		for (int i = 0; i < num_connected; i++) {
			RemoteDevice device = list_connected.get(i);
			if (device.getId() == device_id) {
				device.sendMessage(message);
			}
		}
		
	}
	
	/**
	 * Will send a direct message to a single Remote Device.
	 * 
	 * @param device The RemoteDevice to send a message to
	 * @param message
	 */
	public void sendDirectMessage(Device device, String message) {
		
		device.sendMessage(message);
		
	}
	
	/**
	 * Registers an object for updates on Device status.
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
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - DEVICE CONNECTED ON PORT " + device.getPort());
		
		device.setId(current_id);
		list_connected.add((RemoteDevice) device);
		current_id++;
		
		int num_listeners = list_listeners.size();
		for (int i = 0; i < num_listeners; i++) {
			DeviceListener listener = list_listeners.get(i);
			listener.connected(device);
		}
		
	}

	public void disconnected(DeviceAbsImpl device) {
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - DEVICE DISCONNECTED FROM PORT " + device.getPort());
		
		int num_listeners = list_listeners.size();
		for (int i = 0; i < num_listeners; i++) {
			DeviceListener listener = list_listeners.get(i);
			listener.disconnected(device);
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
