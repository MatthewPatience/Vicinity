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
import android.os.Bundle;

import com.bnotions.vicinity.device.DeviceListener;
import com.bnotions.vicinity.manager.ServerCommManager;

/**
 * This class should be extended by any activity within your application that 
 * handles server interactions. For remote interactions please extend RemoteActivity.
 * 
 * @author Matthew Patience
 * @since 2012-09-27
 */
public abstract class ServerActivity extends Activity implements DeviceListener {
	
	private ServerCommManager server_manager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initCommManagers();
		
	}
	
	private void initCommManagers() {
		
		server_manager = ((VicinityApplication) getApplication()).getServerManager();
		server_manager.addDeviceListener(this);
		
	}
	
	/**
	 * Use this method to retrieve the global instance of the ServerCommManager. 
	 * The ServerCommManager can then be used to send messages to devices.
	 * 
	 * @return The global instance of ServerCommManager
	 */
	public ServerCommManager getServerManager() {
		
		return server_manager;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		server_manager.removeDeviceListener(this);
		
	}

}
