package com.bnotions.vicinity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
		
		if (ip_address != null) {
			new Thread() {
				public void run() {
					try {
						ServerSocket ss = new ServerSocket(port);
						socket = ss.accept();
						socket.setKeepAlive(true);
						socket.setTcpNoDelay(true);
						output = new DataOutputStream(socket.getOutputStream());
						input = new DataInputStream(socket.getInputStream());
						
						monitorMessages();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} else {
			throw new IllegalStateException("No IP Address provided");
		}
		
	}

}
