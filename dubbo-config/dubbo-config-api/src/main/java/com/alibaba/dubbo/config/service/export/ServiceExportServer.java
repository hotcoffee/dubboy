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

import java.util.Map;

import com.alibaba.dubbo.common.service.export.support.MethodUtils.MethodInfo;

/**
 * @author BruceZCQ
 */
public abstract class ServiceExportServer extends ServiceServer {

	public ServiceExportServer(Integer id) {
		super(id);
		// provider
		ServiceExporter exporter = new ServiceExporter(id);
		this.exportServices(exporter);
		this.exportServicePort(exporter.getPort());
		this.exportServicesApis(exporter.getServicesApi());
		this.exportServicesMethodsInfo(exporter.getMethodsInfo());
	}
	
	/**
	 * 暴露服务
	 * @param exporter
	 */
	public abstract void exportServices(ServiceExporter exporter);
	
	/**
	 * 导出服务的端口 <br/>
	 * 使用RedisHash{ key:自定义, field:servername , value:port} 存储;
	 * @param port
	 */
	public abstract void exportServicePort(Integer port);
	
	/**
	 * 暴露服务的Api <br/>
	 * @param servicesApi [Map] <br/>
	 * key-> @Path.value <br/>
	 * value-> apiInterfaceClas <br/>
	 * 可使用Redis.hmset全部写入,RedisHash的key自定义 <br/>
	 */
	public abstract void exportServicesApis(Map<Object, Object> servicesApi);
	
	/**
	 * 暴露服务的Api的Method信息 <br/>
	 * @param serviceMethodsInfo [Map]<br/>
	 * key-> {@linkplain MethodInfo#getPlainPath()} <br/>
	 * value-> {@linkplain MethodInfo} <br/>
	 * 可使用Redis.hmset全部写入,RedisHash的key自定义 <br/>
	 */
	public abstract void exportServicesMethodsInfo(Map<Object, Object> serviceMethodsInfo);
	
}
