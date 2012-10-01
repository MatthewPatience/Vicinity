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

import com.bnotions.vicinity.zxing.IntentIntegrator;
import com.bnotions.vicinity.zxing.IntentResult;

public class VicinityScanResult {
	
	public static final int REQUEST_CODE = 0x0000c0de;
	
	private String ip_address;
	private int[] ports;
	
	public VicinityScanResult(int request_code, int result_code, Intent intent) {
		
		IntentResult scan_result = IntentIntegrator.parseActivityResult(request_code, result_code, intent);
		String contents = scan_result.getContents();
		
		ip_address = contents;
		
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
