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

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author BruceZCQ
 */
public class Log4jApplication {
	
	public Log4jApplication(Integer id) {
		PropertiesData srvProperties = DubboApplication.getServerPropertiesData();
		String logDir = srvProperties.get("application.logdir");
		if (null == logDir || "".equals(logDir)) {
			throw new IllegalArgumentException("Please set `application.logdir` in server.properties.");
		}
		
		Properties property = DubboApplication.getLog4jProperties();
		if (DubboApplication.DEBUG_MODE) {
			property.put("log4j.rootLogger", "DEBUG, stdout, file");
		}
		property.put("log4j.appender.file.File", logDir+DubboApplication.getApplicationName()+id+".log");
        PropertyConfigurator.configure(property);
	}
	
	public static void init(Integer id) {
		new Log4jApplication(id);
	}
}
