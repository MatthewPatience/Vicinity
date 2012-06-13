package com.bnotions.vicinity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * An abstract device can be of either the type Remote or Server. 
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 *
 */
public abstract class DeviceAbsImpl implements Device {

	public static final int DEFAULT_PORT = 6288;
	
	protected Socket socket;
	protected DataOutputStream output;
	protected DataInputStream input;
	
	protected String ip_address;
	protected int port;
	protected int id = 0;
	
	protected boolean monitor;
	
	protected DeviceListener listener;
	
	public DeviceAbsImpl() {
		
		ip_address = null;
		port = DEFAULT_PORT;
		monitor = false;
		
	}
	
	public void disconnect() {
		
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (input != null) {
			try {
				input.close();
				input = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (output != null) {
			try {
				output.close();
				output = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (listener != null) listener.disconnected();
		
	}

	public void setDeviceListener(DeviceListener listener) {
		
		this.listener = listener;
		
	}

	public void sendMessage(final String data) {
		
		if (output != null) {
			new Thread() {
				public void run() {
					try {
						output.writeUTF(data);
					} catch (IOException e) {
						e.printStackTrace();
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
				while(monitor) {
					try {
						if (socket.isConnected()) {
							String message = input.readUTF();
							if (listener != null) listener.messageReceived(message);
						} else {
							monitor = false;
							disconnect();
							if (listener != null) listener.disconnected();
						}
					} catch (IOException e) {} 
				}
			}
		}.start();
		
	}
	
	/**
	 * This method should not have to be called and only should be called 
	 * if a connection fails or resets.
	 */
	public void startMonitoring() {
		
		if (monitor != true) {
			monitor = true;
			monitorMessages();
		}
		
	}
	
	/**
	 * This will stop the monitoring of the connection for changes in state 
	 * and for received messages.
	 */
	public void stopMonitoring() {
		
		monitor = false;
		
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

	/**
	 * It is suggested that this method be used to override the default port 
	 * to avoid port conflicts.
	 * 
	 * @param port Port number to connect to or listen on
	 */
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
