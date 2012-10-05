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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bnotions.vicinity.device.DeviceListener;
import com.bnotions.vicinity.manager.RemoteCommManager;
import com.bnotions.vicinity.object.VicinityScanResult;

/**
 * This class should be extended by any activity within your application that 
 * handles remote interactions. For server interactions please extend ServerActivity.
 * 
 * @author Matthew Patience
 * @since 2012-09-27
 */
public abstract class RemoteActivity extends Activity implements DeviceListener {
	
	private RemoteCommManager remote_manager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initCommManagers();
		
	}
	
	private void initCommManagers() {
		
		remote_manager = ((VicinityApplication) getApplication()).getRemoteManager();
		remote_manager.addDeviceListener(this);
		
	}
	
	/**
	 * Use this method to retrieve the global instance of the RemoteCommManager. 
	 * The RemoteCommManager can then be used to send messages to the server.
	 * 
	 * @return The global instance of RemoteCommManager
	 */
	public RemoteCommManager getRemoteManager() {
		
		return remote_manager;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		try {
			connecting();
			VicinityScanResult result = new VicinityScanResult(requestCode, resultCode, intent);
			remote_manager.connect(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method will get called upon return from a successful scan. 
	 * This is for purpose of letting you know when a connection is being made. 
	 * The connected() method will be called when a connection is complete.
	 */
	public abstract void connecting();
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		remote_manager.removeDeviceListener(this);
		
	}

}
