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

package com.bnotions.vicinity.object;

public class DedicatedPorts {
	
	private static final int FIRST_PORT = 2500;
	
	public static int[] getPorts() {
		
		int[] ports = new int[10];
		int current_port = FIRST_PORT;
		for (int i = 0; i < 10; i++) {
			ports[i] = current_port;
			current_port++;
		}
		
		return ports;
	}

}
