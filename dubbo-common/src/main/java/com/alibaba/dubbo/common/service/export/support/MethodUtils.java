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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.ws.rs.Path;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

/**
 * 分析Method信息
 * @author BruceZCQ
 */
public class MethodUtils {
	
	public static class MethodInfo implements Serializable {
		
		private static final long serialVersionUID = -8638975169499192668L;
		private String name = null;
		private String path = null;
		private String plainPath = null;
		private Class<?>[] argsTypes = null;
		private String[] argsNames = null;
		
		public MethodInfo(Method method, Class<?> declaringClass) {
			this.name = method.getName();
			this.argsTypes = method.getParameterTypes();
			Path path = method.getAnnotation(Path.class);
			if (null != path) {
				this.path = path.value();
				this.parserPlainPath();
				this.parserArgsNames(method, declaringClass);
			}
		}
		
		private void parserPlainPath() {
			this.plainPath = this.path;
		}
		
		private void parserArgsNames(Method method, Class<?> declaringClass) {
			ClassPool pool = ClassPool.getDefault();
			try {
				CtClass cc = pool.get(declaringClass.getName());
				CtMethod cm = cc.getDeclaredMethod(method.getName());
				CodeAttribute codeAttribute = cm.getMethodInfo().getCodeAttribute();
				LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
						.getAttribute(LocalVariableAttribute.tag);
				if (attr != null) {
					int len = cm.getParameterTypes().length;
					this.argsNames = new String[len];
					int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
					for (int i = 0; i < len; i++) {
						this.argsNames[i] = attr.variableName(i + pos);
					}
				}
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getPath() {
			return this.path;
		}
		
		public String getPlainPath() {
			return this.plainPath;
		}
		
		public Class<?>[] getArgsTypes() {
			return this.argsTypes;
		}
		
		public String[] getArgsNames() {
			return this.argsNames;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(argsNames);
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MethodInfo other = (MethodInfo) obj;
			if (!Arrays.equals(argsNames, other.argsNames))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString() {
			final int maxLen = 10;
			return "MethodInfo [name=" + name + ", argsTypes="
					+ (argsTypes != null ? Arrays.asList(argsTypes).subList(0, Math.min(argsTypes.length, maxLen))
							: null)
					+ ", argsNames="
					+ (argsNames != null ? Arrays.asList(argsNames).subList(0, Math.min(argsNames.length, maxLen))
							: null) + "]";
		}
	}
	
	public static MethodInfo getMethodInfo(Method method, Class<?> declaringClass) {
		return (new MethodInfo(method, declaringClass));
	}
}
