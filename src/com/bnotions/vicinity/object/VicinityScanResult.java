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

import android.content.Intent;

import com.bnotions.vicinity.util.Constants;
import com.bnotions.vicinity.zxing.IntentIntegrator;
import com.bnotions.vicinity.zxing.IntentResult;

public class VicinityScanResult {
	
	public static final int REQUEST_CODE = 0x0000c0de;
	
	private String ip_address;
	private int[] ports;
	
	public VicinityScanResult(int request_code, int result_code, Intent intent) {
		
		IntentResult scan_result = IntentIntegrator.parseActivityResult(request_code, result_code, intent);
		String contents = scan_result.getContents();
		//Parse array of form "ip_address|port|port|port|port|port|port|port|port|"
		//Example: "192.168.0.1|2401|2402|2403|2404|2505|2506|2507|2508"
		String[] arr_data = contents.split(Constants.DELIMITER);
		
		ip_address = arr_data[0];
		ports = new int[arr_data.length - 1];
		for (int i = 1; i < arr_data.length; i++) {
			ports[i-1] = Integer.parseInt(arr_data[i]);
		}
		
	}
	
	public String getIpAddress() {
		return ip_address;
	}
	
	public void setIpAddress(String ip_address) {
		this.ip_address = ip_address;
	}
	
	public int[] getPorts() {
		return ports;
	}
	
	public void setPorts(int[] ports) {
		this.ports = ports;
	}

}
