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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author BruceZCQ
 */
public class DubboApplication {
	
	public static boolean DEBUG_MODE = true;
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	private static final String CFG_DIR = "cfg/";
	private static final String FILE_EXT = ".properties";
	public static final String LOG_CFG_FILE = CFG_DIR + "log4j"+FILE_EXT;
	public static final String SERVICE_CFG_FILE = CFG_DIR + "server"+FILE_EXT;
	private static PropertiesData log4jProperties = null;
	private static PropertiesData serverProperties = null;
	private static String serverName = null;
	private static List<Protocol> protocols = null;
	private static String restServer = null;
	private static String appVersion = null;
	private static String serialization = null;
	private static Integer threads = null;
	
	/**
	 * 生产pid文件
	 */
	public static void createPidFile(Integer id) {
		// get name representing the running Java virtual machine.  
		// get pid  
		String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];  
		try {
			File file = new File(DubboApplication.CFG_DIR+DubboApplication.getApplicationName()+id+".pid");
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(pid.getBytes("utf-8"));
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 获取Log4j的配置属性
	 * @return
	 */
	public static PropertiesData getLog4jPropertiesData() {
		if (null == DubboApplication.log4jProperties) {
			DubboApplication.log4jProperties = PropertiesDataUtils.use(DubboApplication.LOG_CFG_FILE);
		}
		return DubboApplication.log4jProperties;
	}
	
	/**
	 * 获取Server的配置属性
	 * @return
	 */
	public static PropertiesData getServerPropertiesData() {
		if (null == DubboApplication.serverProperties) {
			DubboApplication.serverProperties = PropertiesDataUtils.use(DubboApplication.SERVICE_CFG_FILE);
		}
		return DubboApplication.serverProperties;
	}
	
	/**
	 * 获取Log4j的配置属性
	 * @return
	 */
	public static Properties getLog4jProperties() {
		return DubboApplication.getLog4jPropertiesData().getProperties();
	}
	
	/**
	 * 获取Server的配置属性
	 * @return
	 */
	public static Properties getServerProperties() {
		return DubboApplication.getServerPropertiesData().getProperties();
	}
	
	/**
	 * 校验数据
	 * @param propertyName
	 * @return
	 */
	private static final String validateServerProperty(String propertyName) {
		String property = DubboApplication.getServerPropertiesData().get(propertyName);
		if (null == property || "".equals(property)) {
			throw new IllegalArgumentException("Please set `"+propertyName+"` in "+DubboApplication.SERVICE_CFG_FILE+".");
		}
		return property;
	}
	
	/**
	 * 获取ServerName
	 * @return
	 */
	public static final String getApplicationName() {
		if (null == DubboApplication.serverName || "".equals(DubboApplication.serverName)) {
			DubboApplication.serverName = DubboApplication.validateServerProperty("application.name").toLowerCase();
		}
		return DubboApplication.serverName;
	}
	
	/**
	 * 获取app version
	 * @return
	 */
	public static final String getApplicationVersion() {
		if (null == DubboApplication.appVersion) {
			DubboApplication.appVersion = DubboApplication.validateServerProperty("application.version");
		}
		return DubboApplication.appVersion;
	}
	
	/**
	 * 获取rest server
	 * @return
	 */
	public static final String getRestServer() {
		if (null == DubboApplication.restServer || "".equals(DubboApplication.restServer)) {
			DubboApplication.restServer = DubboApplication.validateServerProperty("dubbo.rest.server");
		}
		return DubboApplication.restServer;
	}
	
	/**
	 * 获取协议
	 * @return
	 */
	public static List<Protocol> getProtocols() {
		if (null == DubboApplication.protocols) {
			DubboApplication.protocols = new ArrayList<Protocol>();
			String _protocols = DubboApplication.validateServerProperty("dubbo.protocols");
			if (_protocols.contains(",")) {
				String[] _protocols_ = _protocols.split(",");
				
				if (null != _protocols_) {
					for (String protocolStr : _protocols_) {
						DubboApplication.protocols.add(new Protocol(protocolStr));
					}
				}
			}
		}
		return DubboApplication.protocols;
	}
	
	/**
	 * 获取序列化
	 * @return
	 */
	public static final String getSerialization() {
		if (null == DubboApplication.serialization) {
			DubboApplication.serialization = DubboApplication.validateServerProperty("dubbo.serialization");
		}
		return DubboApplication.serialization;
	}
	
	/**
	 * 获取线程数
	 * @return
	 */
	public static final Integer getThreads() {
		if (null == DubboApplication.threads) {
			DubboApplication.threads = Integer.valueOf(DubboApplication.validateServerProperty("dubbo.threads"));
		}
		return DubboApplication.threads;
	}
}
