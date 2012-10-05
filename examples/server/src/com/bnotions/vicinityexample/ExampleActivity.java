package com.bnotions.vicinityexample;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bnotions.vicinity.ServerActivity;
import com.bnotions.vicinity.Vicinity;
import com.bnotions.vicinity.device.DeviceAbsImpl;

public class ExampleActivity extends ServerActivity {
	
	private ImageView img_qrcode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);
		
		init();
		
	}
	
	private void init() {
		
		img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
		img_qrcode.setImageBitmap(Vicinity.getIpQrCode(300, 300));
		
		try {
			getServerManager().startListening();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void connected(DeviceAbsImpl device) {
		
		img_qrcode.post(new Runnable() {
			public void run() {
				Toast.makeText(ExampleActivity.this, "DEVICE CONNECTED!", Toast.LENGTH_LONG).show();
			}
		});
		
	}

	public void disconnected(DeviceAbsImpl device) {
		
		img_qrcode.post(new Runnable() {
			public void run() {
				Toast.makeText(ExampleActivity.this, "DEVICE DISCONNECTED!", Toast.LENGTH_LONG).show();
			}
		});
		
	}

	public void messageReceived(DeviceAbsImpl device, final String message) {
		
		img_qrcode.post(new Runnable() {
			public void run() {
				Toast.makeText(ExampleActivity.this, "MESSAGE: " + message, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		getServerManager().stopListening();
		getServerManager().disconnectAll();
		
	}

}
