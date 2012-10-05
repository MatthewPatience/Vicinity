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

import android.app.Application;

import com.bnotions.vicinity.manager.RemoteCommManager;
import com.bnotions.vicinity.manager.ServerCommManager;

/**
 * Extend this class and set it as your Application in your manifest. 
 * No other work needs to be done with this class unless you have some 
 * of you own to do.
 * 
 * @author Matthew Patience
 * @since 2012-09-27
 */
public class VicinityApplication extends Application {
	
	private ServerCommManager server_manager;
	private RemoteCommManager remote_manager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		server_manager = new ServerCommManager();
		remote_manager = new RemoteCommManager();
		
	}
	
	/**
	 * Returns the global instance of the ServerCommManager.
	 * 
	 * @return The global instance of the ServerCommManager
	 */
	public ServerCommManager getServerManager() {
		
		return server_manager;
	}
	
	/**
	 * Returns the global instance of the RemoteCommManager.
	 * 
	 * @return The global instance of the RemoteCommManager
	 */
	public RemoteCommManager getRemoteManager() {
		
		return remote_manager;
	}

}
