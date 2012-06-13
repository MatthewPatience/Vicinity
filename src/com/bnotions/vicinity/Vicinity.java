package com.bnotions.vicinity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;
import android.graphics.Bitmap;

import com.bnotions.vicinity.util.IntentIntegrator;
import com.bnotions.vicinity.util.QrCodeGenerator;

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
		
		return QrCodeGenerator.encode(getLocalIpAddress());
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
