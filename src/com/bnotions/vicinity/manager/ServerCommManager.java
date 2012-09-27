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
import com.bnotions.vicinity.object.DedicatedPorts;
import com.bnotions.vicinity.util.Constants;

public class ServerCommManager implements DeviceListener {
	
	private int[] ports;
	private ArrayList<RemoteDevice> list_connected;
	private ArrayList<RemoteDevice> list_listening;
	private int current_id = 1;
	private boolean is_listening = false;
	
	private ArrayList<DeviceListener> list_listeners;
	
	public ServerCommManager() {
		
		list_connected = new ArrayList<RemoteDevice>();
		list_listening = new ArrayList<RemoteDevice>();
		
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
		
		is_listening = true;
		ports = DedicatedPorts.getPorts();
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - LISTENING ON PORTS " + ports[0] + " TO " + ports[ports.length-1]);
		
		list_listening.clear();
		
		for (int i = 0; i < ports.length; i++) {
			RemoteDevice device = new RemoteDevice();
			device.setPort(ports[i]);
			device.setDeviceListener(this);
			try {
				device.connect();
				list_listening.add(device);
			} catch (Exception e) {
				throw new Exception("Error occured while attempting to listen on port " + ports[i]);
			}
		}
		
	}
	
	/**
	 * Will stop listening on all dedicated ports.
	 */
	public void stopListening() {
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - STOP LISTENING REQUESTED");
		
		is_listening = false;
		int num_listening = list_listening.size();
		for (int i = 0; i < num_listening; i++) {
			RemoteDevice device = list_listening.get(i);
			device.disconnect();
		}
		
		list_listening.clear();
		
	}
	
	/**
	 * Disconnects all currently connected RemoteDevices.
	 */
	public void disconnectAll() {
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - DISCONNECT ALL DEVICES REQUESTED");
		
		int num_connected = list_connected.size();
		for (int i = 0; i < num_connected; i++) {
			list_connected.get(i).disconnect();
		}
		
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
		list_listening.remove((RemoteDevice) device);
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
		
		if (is_listening) {
			device.disconnect();
			RemoteDevice new_device = (RemoteDevice) device;
			try {
				new_device.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (Constants.DEBUG) Log.d("Vicinity", "SERVERMANAGER - LISTENING PORTS COUNT " + list_listening.size());
		
	}

	public void messageReceived(DeviceAbsImpl device, String message) {
		
		int num_listeners = list_listeners.size();
		for (int i = 0; i < num_listeners; i++) {
			DeviceListener listener = list_listeners.get(i);
			listener.messageReceived(device, message);
		}
		
	}

}
