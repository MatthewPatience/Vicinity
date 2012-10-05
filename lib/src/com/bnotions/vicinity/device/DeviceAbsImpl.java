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

import android.os.Handler;
import android.util.Log;

import com.bnotions.vicinity.util.Constants;


/**
 * An abstract device can be of either the type Remote or Server. 
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 *
 */
public abstract class DeviceAbsImpl implements Device {

	public static final int DEFAULT_PORT = 6288;
	public static final int PING_INTERVAL = 2000;
	public static final int AUTO_DISCONNECT_INTERVAL = 3500;
	
	protected Socket socket;
	protected DataOutputStream output;
	protected DataInputStream input;
	private Handler handler;
	
	protected String ip_address;
	protected int port;
	protected int id = 0;
	
	protected boolean monitor;
	protected boolean connected;
	
	protected DeviceListener listener;
	
	public DeviceAbsImpl() {
		
		ip_address = null;
		port = DEFAULT_PORT;
		monitor = false;
		connected = false;
		handler = new Handler();
		
	}
	
	/**
	 * Will disconnect the current connection if it exists and close the 
	 * incoming and outgoing data streams.
	 */
	public void disconnect() {
		
		monitor = false;
		
		if (output != null) {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (input != null) {
			try {
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public void setDeviceListener(DeviceListener listener) {
		
		this.listener = listener;
		
	}

	/**
	 * Will write a String message to the output stream and append an EOM Marker 
	 * to it. 
	 */
	public void sendMessage(final String data) {
		
		if (output != null) {
			new Thread() {
				public void run() {
					if (output != null) {
						try {
							String message = data + Constants.EOM_MARKER;
							output.writeUTF(message);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						monitor = false;
						disconnect();
						if (listener != null) listener.disconnected(DeviceAbsImpl.this);
					}
				}
			}.start();
		} else {
			throw new IllegalStateException("Output stream is null, make sure connect() has been called");
		}
		
	}
	
	protected void monitorMessages() {
		
		monitor = true;
		
		new Thread() {
			public void run() {
				
				StringBuilder buffer = new StringBuilder("");
				
				while(monitor) {
					try {
						String msg = input.readUTF();
						if (msg == null || msg.equalsIgnoreCase("")) {
							continue;
						}
						buffer.append(msg);
						String buffer_string = buffer.toString();
						if (buffer_string.contains(Constants.EOM_MARKER)) {
							int msg_end = buffer_string.indexOf(Constants.EOM_MARKER);
							String message = buffer_string.substring(0, msg_end);
							if (message.equalsIgnoreCase(Constants.PING_REQUEST)) { 
								receivePing();
							} else { 
								if (listener != null) listener.messageReceived(DeviceAbsImpl.this, message);
							}
							buffer.delete(0, msg_end + Constants.EOM_MARKER.length());
						}
					} catch (Exception e) {
						e.printStackTrace();
						monitor = false;
						disconnect();
						if (listener != null) listener.disconnected(DeviceAbsImpl.this);
						break;
					} 
				}
			}
		}.start();
		
	}
	
	/**
	 * !! This is an internal method and should NEVER be called !!
	 */
	public void startHeartBeatPing() {
		
		new Thread() {
			public void run() {
				
				while (monitor) {
					
					if (Constants.DEBUG) Log.d("Vicinity", "SENDING PING...");
					sendMessage(Constants.PING_REQUEST);
					
					try { Thread.sleep(PING_INTERVAL); } catch (InterruptedException e) { e.printStackTrace(); }
					
				}
				
			}
		}.start();
		
		handler.postDelayed(ping_thread, AUTO_DISCONNECT_INTERVAL);
		
	}
	
	private void receivePing() {
		
		if (Constants.DEBUG) Log.d("Vicinity", "PING RECEIVED");
		
		connected = true;
		handler.removeCallbacks(ping_thread);
		handler.postDelayed(ping_thread, AUTO_DISCONNECT_INTERVAL);
		
	}
	
	private Runnable ping_thread = new Runnable() {
		public void run() {
			
			if (Constants.DEBUG) Log.d("Vicinity", "AUTO-DISCONNECTING - NO PING");
			connected = false;
			monitor = false;
			if (listener != null) {
				listener.disconnected(DeviceAbsImpl.this);
			}
			
		}
	};
	
	/**
	 * !! This is an internal method and should NEVER be called !!
	 */
	public void startMonitoring() {
		
		if (monitor != true) {
			monitor = true;
			monitorMessages();
		}
		
	}
	
	/**
	 * !! This is an internal method and should NEVER be called !!
	 */
	public void stopMonitoring() {
		
		monitor = false;
		
	}
	
	/**
	 * Whether or not the socket is currently connected 
	 * and is successfully receiving a ping.
	 * 
	 * @return Connected status
	 */
	public boolean isConnected() {
		
		if (socket == null) {
			return false;
		}
		
		return connected;
	}
	
	public String getIpAddress() {
		return ip_address;
	}

	public void setIpAddress(String ip_address) {
		this.ip_address = ip_address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
