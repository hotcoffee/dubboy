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

package com.alibaba.dubbo.config.service.export;

import com.alibaba.dubbo.common.service.export.support.DubboApplication;
import com.alibaba.dubbo.common.service.export.support.Log4jApplication;

/**
 * @author BruceZCQ
 */
public abstract class ServiceServer {

	public ServiceServer(Integer id) {
		//create pid
		DubboApplication.createPidFile(id);
		//init log4j
		Log4jApplication.init(id);
		//init db
		this.initDb();
		// init cache
		this.initCache();
	}
	
	/**
	 * 初始化数据库
	 */
	public abstract void initDb();
	
	/**
	 * 初始化缓存
	 */
	public abstract void initCache();
	
}
