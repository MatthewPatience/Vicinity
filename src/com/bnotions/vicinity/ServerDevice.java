package com.bnotions.vicinity;

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
						output = new DataOutputStream(socket.getOutputStream());
						input = new DataInputStream(socket.getInputStream());
						
						if (listener != null) listener.connected();
						
						monitorMessages();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		} else {
			throw new IllegalStateException("No IP Address provided");
		}
		
	}
	
}
