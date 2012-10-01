package com.bnotions.vicinity.zxing;

import android.graphics.Bitmap;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * This class can generate a QR Code bitmap based on any String. It 
 * also has a convenience method for creating VCards.
 * 
 * @author Matthew Patience
 * @since 2012-06-13
 */
public class QrCodeGenerator {
	
	private static int CODE_WIDTH = 300;
	private static int CODE_HEIGHT = 300;
	
	public static Bitmap encode(String data, int image_width, int image_height) {
		
		CODE_WIDTH = image_width;
		CODE_HEIGHT = image_height;
		
		BitMatrix matrix;
		com.google.zxing.Writer writer = new QRCodeWriter();
		try {
			matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT);
		} catch (com.google.zxing.WriterException e) {
			return null;
		}

		int width = matrix.getWidth(); 
		int height = matrix.getHeight(); 

		Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		for (int y = 0; y < height; y++) { 
			for (int x = 0; x < width; x++) { 
				image.setPixel(x, y, (matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF));
			}
		}
		
		return image;

	}
	
	public static String createVCard(String first, String last, String phone, String email, String address, String url) {
		
		return "MECARD:" +
				"N:" + last + "," + first + ";" +
				"TEL:" + phone + ";" +
				"EMAIL:" + email + ";" +
				"ADR:" + address + ";" +
				"URL:" + url + ";" +
				";";
	}

}
