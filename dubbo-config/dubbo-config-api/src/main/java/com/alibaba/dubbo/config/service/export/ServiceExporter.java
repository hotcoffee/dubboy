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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.alibaba.dubbo.common.service.export.support.DubboApplication;
import com.alibaba.dubbo.common.service.export.support.MethodUtils;
import com.alibaba.dubbo.common.service.export.support.MethodUtils.MethodInfo;
import com.alibaba.dubbo.common.service.export.support.Protocol;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.protocol.rest.exception.RestExceptionResponse;
import com.alibaba.dubbo.rpc.protocol.rest.exception.RestNotFoundExceptionMapper;
import com.alibaba.dubbo.rpc.protocol.rest.exception.ValidationExceptionMapper;

/**
 * 
 * @author BruceZCQ
 */
public class ServiceExporter {

	private ApplicationConfig application = null;
	private RegistryConfig registry = null;
	private List<DubboProtocol> protocols = new ArrayList<DubboProtocol>();
	private static final String CONSUMER = "-Consumer";
	private int port = -1;
	private Map<String, ReferenceConfig<Object>> referenceMapping = new HashMap<String, ReferenceConfig<Object>>();
	private Map<Object, Object> servicesApi = new HashMap<Object, Object>();
	private Map<Object, Object> methodsInfo = new HashMap<Object, Object>();

	private static class DubboProtocol extends ProtocolConfig {

		private static final long serialVersionUID = -6210646334091868933L;

		public DubboProtocol(String name, int port) {
			this.setName(name);
			this.setPort(port);
			this.setSerialization(DubboApplication.getSerialization());
			this.setThreads(DubboApplication.getThreads());
			if (name.equals("rest")) {
				this.setServer(DubboApplication.getRestServer());
				this.setContextpath(DubboApplication.getApplicationVersion());
				this.setExtension(RestExceptionResponse.getRestExtension());
			}
		}
	}

	public ServiceExporter(Integer id) {
		this();
		// 服务提供者协议配置
		List<Protocol> protocols = DubboApplication.getProtocols();
		DubboProtocol protocolCfg = null;
		for (Protocol protocol : protocols) {
			protocolCfg = new DubboProtocol(protocol.getName(), protocol.getStartPort()+id);
			this.protocols.add(protocolCfg);
		}
	}

	public ServiceExporter() {
		this.application = new ApplicationConfig();
		this.application.setName(DubboApplication.getApplicationName());
		// 连接注册中心配置
		this.registry = new RegistryConfig();
		this.registry.setAddress(DubboApplication.getServerProperties().getProperty("registry.address"));
		this.registry.setUsername(DubboApplication.getServerProperties().getProperty("registry.username"));
		this.registry.setPassword(DubboApplication.getServerProperties().getProperty("registry.password"));
	}

	/**
	 * 获取端口
	 * 
	 * @return
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * 获取所有服务
	 * 
	 * @return
	 */
	public Map<Object, Object> getServicesApi() {
		return this.servicesApi;
	}

	/**
	 * 获取服务的Method信息
	 * 
	 * @return
	 */
	public Map<Object, Object> getMethodsInfo() {
		return this.methodsInfo;
	}

	/**
	 * 导出服务
	 * 
	 * @param serviceInstance
	 *            服务实现实例
	 */
	public void exportService(Object serviceInstance) {
		ServiceConfig<Object> service = new ServiceConfig<Object>();
		service.setApplication(this.application);
		service.setRegistry(this.registry);
		service.setProtocols(this.protocols);
		service.setRef(serviceInstance);
		Class<?> apiInstanceClass = serviceInstance.getClass();
		Class<?> apiClass = apiInstanceClass.getInterfaces()[0];
		service.setInterface(apiClass);
		service.setVersion(DubboApplication.getApplicationVersion());
		service.export();

		// parser service info
		Path path = apiClass.getAnnotation(Path.class);
		if (null != path) {
			this.servicesApi.put(path.value(), apiClass);

			Method[] methods = apiClass.getMethods();
			MethodInfo methodInfo = null;
			for (Method method : methods) {
				methodInfo = MethodUtils.getMethodInfo(method, apiInstanceClass);
				methodsInfo.put(methodInfo.getPlainPath(), methodInfo);
			}
		}
	}

	/**
	 * 获取服务
	 * 
	 * @param serviceInterfaceClazz
	 * @return
	 */
	public Object getService(Class<?> serviceInterfaceClazz) {
		String className = serviceInterfaceClazz.getName();
		Object service = null;
		ReferenceConfig<Object> reference = null;
		if (this.referenceMapping.containsKey(className)) {
			reference = this.referenceMapping.get(className);
			service = (null != reference) ? reference.get() : null;
		}
		if (null == service) {
			reference = new ReferenceConfig<Object>();
			String application = this.application.getName();
			if (!application.endsWith(ServiceExporter.CONSUMER)) {
				this.application.setName(application + ServiceExporter.CONSUMER);
			}
			reference.setApplication(this.application);
			reference.setRegistry(this.registry);
			reference.setInterface(serviceInterfaceClazz);
			reference.setCheck(false);
			reference.setVersion(DubboApplication.getApplicationVersion());
			service = reference.get();
			this.referenceMapping.put(className, reference);
		}
		return service;
	}

	/**
	 * 获取服务，使用dubbo协议
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            端口
	 * @param serviceInterfaceClazz
	 *            服务器接口名称
	 * @return
	 */
	public Object getService(String host, int port, Class<?> serviceInterfaceClazz) {
		return this.getService("dubbo", host, port, serviceInterfaceClazz);
	}

	/**
	 * 获取服务
	 * 
	 * @param protocol
	 *            协议
	 * @param host
	 *            服务器地址
	 * @param port
	 *            端口
	 * @param serviceInterfaceClazz
	 *            服务器接口名称
	 * @return
	 */
	public Object getService(String protocol, String host, int port, Class<?> serviceInterfaceClazz) {
		StringBuilder url = new StringBuilder();
		if (!protocol.endsWith("://")) {
			url.append(protocol).append("://");
		}
		url.append(host).append(":").append(port).append("/").append(serviceInterfaceClazz.getName());
		return this.getService(url.toString(), serviceInterfaceClazz);
	}

	private Object getService(String url, Class<?> serviceInterfaceClazz) {
		Object service = null;
		ReferenceConfig<Object> reference = null;
		if (this.referenceMapping.containsKey(url)) {
			reference = this.referenceMapping.get(url);
			service = (null != reference) ? reference.get() : null;
		}
		if (null == service) {
			reference = new ReferenceConfig<Object>();
			String application = this.application.getName();
			if (!application.endsWith(ServiceExporter.CONSUMER)) {
				this.application.setName(application + ServiceExporter.CONSUMER);
			}
			reference.setApplication(this.application);
			reference.setUrl(url);
			reference.setCheck(false);
			reference.setInterface(serviceInterfaceClazz);
			reference.setVersion(DubboApplication.getApplicationVersion());
			service = reference.get();
			this.referenceMapping.put(url, reference);
		}
		return service;
	}
}
