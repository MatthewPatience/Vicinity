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

package com.bnotions.vicinity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;
import android.graphics.Bitmap;

import com.bnotions.vicinity.util.Constants;
import com.bnotions.vicinity.zxing.IntentIntegrator;
import com.bnotions.vicinity.zxing.QrCodeGenerator;

/**
 * An all purpose class with important static methods.
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 */
public class Vicinity {
	
	/**
	 * Launches the Barcode Scanner application if it is installed, if not 
	 * the user will be asked to download it from the Android Market. Be sure to 
	 * override onActivityResult().
	 * 
	 * @param activity
	 */
	public static void scan(Activity activity) {
		
		IntentIntegrator integrator = new IntentIntegrator(activity);
		integrator.initiateScan();
		
	}
	
	/**
	 * This will return a bitmap representation of a QR Code that contains
	 * the local IP address.
	 * 
	 * @return A QR Code
	 */
	public static Bitmap getIpQrCode() {
		
		StringBuilder contents = new StringBuilder(getLocalIpAddress());
		int port = 2500;
		for (int i = 0; i < 10; i++) {
			contents.append(Constants.DELIMITER + String.valueOf(port));
			port++;
		}
		
		return QrCodeGenerator.encode(contents.toString());
	}
	
	private static String getLocalIpAddress() {
		
		try { 
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) { 
				NetworkInterface intf = en.nextElement(); 
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) { 

					InetAddress inetAddress = enumIpAddr.nextElement(); 
					if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) { 
						return inetAddress.getHostAddress();
					} 
				} 
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		
		return null; 
	} 

}
