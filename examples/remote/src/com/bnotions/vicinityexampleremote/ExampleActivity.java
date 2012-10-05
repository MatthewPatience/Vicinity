package com.bnotions.vicinityexampleremote;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.bnotions.vicinity.RemoteActivity;
import com.bnotions.vicinity.Vicinity;
import com.bnotions.vicinity.device.DeviceAbsImpl;

public class ExampleActivity extends RemoteActivity implements OnClickListener {
	
	private Button btn_scan;
	private Button btn_send;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);
		
		init();
		
	}
	
	private void init() {
		
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_scan.setOnClickListener(this);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		
		if (getRemoteManager().getServerDevice().isConnected()) {
			btn_send.setEnabled(true);
			btn_scan.setEnabled(false);
		}
		
	}

	public void connected(DeviceAbsImpl device) {
		
		btn_send.post(new Runnable() {
			public void run() {
				btn_send.setEnabled(true);
				btn_scan.setEnabled(false);
			}
		});
		
	}

	public void disconnected(DeviceAbsImpl device) {
		
		btn_send.post(new Runnable() {
			public void run() {
				btn_send.setEnabled(false);
				btn_scan.setEnabled(true);
			}
		});
		
	}

	public void messageReceived(DeviceAbsImpl device, final String message) {
		
		btn_send.post(new Runnable() {
			public void run() {
				Toast.makeText(ExampleActivity.this, "MESSAGE: " + message, Toast.LENGTH_LONG).show();
			}
		});
		
	}

	@Override
	public void connecting() {
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		getRemoteManager().disconnect();
		
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case (R.id.btn_scan):
			Vicinity.scan(this);
			break;
		case (R.id.btn_send):
			getRemoteManager().sendMessage("THIS IS JUST A TEST!");
			break;
		}
		
	}

}
