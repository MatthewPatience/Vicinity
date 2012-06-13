package com.bnotions.vicinity.util;

import android.graphics.Bitmap;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeGenerator {
	
	private static final int CODE_WIDTH = 300;
	private static final int CODE_HEIGHT = 300;
	
	public static Bitmap encode(String data) {
		
		//get a byte matrix for the data
		BitMatrix matrix;
		com.google.zxing.Writer writer = new QRCodeWriter();
		try {
			matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT);
		} catch (com.google.zxing.WriterException e) {
			//exit the method
			return null;
		}

		//generate an image from the byte matrix
		int width = matrix.getWidth(); 
		int height = matrix.getHeight(); 

		//create bitmap to draw to
		Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		//iterate through the matrix and draw the pixels to the image
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
