/*
 * Copyright Copyright (c) 2015, BruceZCQ 朱丛启 (zcq@zhucongqi.cn).
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.dubbo.common.service.export.support;

/**
 * @author BruceZCQ
 *
 */
public class Protocol {

	private String name = null;
	private int startPort = 0;
	
	/**
	 * @param protocol format:name:port
	 */
	public Protocol(String protocol) {
		String[] info = protocol.split(":");
		if (null == info || info.length != 2) {
			throw (new IllegalArgumentException("protocol format error"));
		}
		
		this.setName(info[0]);
		this.setStartPort(Integer.parseInt(info[1]));
	}
	
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}
	
	public int getStartPort() {
		return startPort;
	}
	
	private void setStartPort(int startPort) {
		this.startPort = startPort;
	}

	@Override
	public String toString() {
		return "Protocol [name=" + name + ", startPort=" + startPort + "]";
	}
}
