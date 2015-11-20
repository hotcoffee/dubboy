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
import java.util.HashMap;
import java.util.Map;

/**
 * PropertiesDataUtils. PropertiesDataUtils can load PropertyUtilserties file from CLASSPATH or File object.
 */
public class PropertiesDataUtils {
	
	private static PropertiesData PropertyUtils = null;
	private static final Map<String, PropertiesData> map = new HashMap<String, PropertiesData>();
	
	private PropertiesDataUtils() {}
	
	/**
	 * Using the PropertiesDataUtils serties file. It will loading the PropertiesDataUtils serties file if not loading.
	 * @see #use(String, String)
	 */
	public static PropertiesData use(String fileName) {
		return use(fileName, DubboApplication.DEFAULT_ENCODING);
	}
	
	/**
	 * Using the PropertiesDataUtils serties file. It will loading the PropertiesDataUtils serties file if not loading.
	 * <p>
	 * Example:<br>
	 * PropertiesDataUtils.use("config.txt", "UTF-8");<br>
	 * PropertiesDataUtils.use("other_config.txt", "UTF-8");<br><br>
	 * String userName = PropertiesDataUtils.get("userName");<br>
	 * String password = PropertiesDataUtils.get("password");<br><br>
	 * 
	 * userName = PropertiesDataUtils.use("other_config.txt").get("userName");<br>
	 * password = PropertiesDataUtils.use("other_config.txt").get("password");<br><br>
	 * 
	 * PropertiesDataUtils.use("com/alibaba/dubbo/config_in_sub_directory_of_classpath.txt");
	 * 
	 * @param fileName the PropertiesDataUtils serties file's name in classpath or the sub directory of classpath
	 * @param encoding the encoding
	 */
	public static PropertiesData use(String fileName, String encoding) {
		PropertiesData result = map.get(fileName);
		if (result == null) {
			synchronized (map) {
				result = map.get(fileName);
				if (result == null) {
					result = new PropertiesData(fileName, encoding);
					map.put(fileName, result);
					if (PropertiesDataUtils.PropertyUtils == null)
						PropertiesDataUtils.PropertyUtils = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Using the PropertiesDataUtils serties file bye File object. It will loading the PropertiesDataUtils serties file if not loading.
	 * @see #use(File, String)
	 */
	public static PropertiesData use(File file) {
		return use(file, DubboApplication.DEFAULT_ENCODING);
	}
	
	/**
	 * Using the PropertiesDataUtils serties file bye File object. It will loading the PropertiesDataUtils serties file if not loading.
	 * <p>
	 * Example:<br>
	 * Properties.use(new File("/var/config/my_config.txt"), "UTF-8");<br>
	 * Strig userName = PropertiesDataUtils.use("my_config.txt").get("userName");
	 * 
	 * @param file the PropertiesDataUtils serties File object
	 * @param encoding the encoding
	 */
	public static PropertiesData use(File file, String encoding) {
		PropertiesData result = map.get(file.getName());
		if (result == null) {
			synchronized (map) {
				result = map.get(file.getName());
				if (result == null) {
					result = new PropertiesData(file, encoding);
					map.put(file.getName(), result);
					if (PropertiesDataUtils.PropertyUtils == null)
						PropertiesDataUtils.PropertyUtils = result;
				}
			}
		}
		return result;
	}
	
	public static PropertiesData useless(String fileName) {
		PropertiesData previous = map.remove(fileName);
		if (PropertiesDataUtils.PropertyUtils == previous)
			PropertiesDataUtils.PropertyUtils = null;
		return previous;
	}
	
	public static void clear() {
		PropertyUtils = null;
		map.clear();
	}
	
	public static PropertiesData getPropertyUtils() {
		if (PropertyUtils == null)
			throw new IllegalStateException("Load PropertyUtil sties file by invoking PropKit.use(String fileName) method first.");
		return PropertyUtils;
	}
	
	public static PropertiesData getPropertyUtils(String fileName) {
		return map.get(fileName);
	}
	
	public static String get(String key) {
		return getPropertyUtils().get(key);
	}
	
	public static String get(String key, String defaultValue) {
		return getPropertyUtils().get(key, defaultValue);
	}
	
	public static Integer getInt(String key) {
		return getPropertyUtils().getInt(key);
	}
	
	public static Integer getInt(String key, Integer defaultValue) {
		return getPropertyUtils().getInt(key, defaultValue);
	}
	
	public static Long getLong(String key) {
		return getPropertyUtils().getLong(key);
	}
	
	public static Long getLong(String key, Long defaultValue) {
		return getPropertyUtils().getLong(key, defaultValue);
	}
	
	public static Boolean getBoolean(String key) {
		return getPropertyUtils().getBoolean(key);
	}
	
	public static Boolean getBoolean(String key, Boolean defaultValue) {
		return getPropertyUtils().getBoolean(key, defaultValue);
	}
	
	public static boolean containsKey(String key) {
		return getPropertyUtils().containsKey(key);
	}
}


