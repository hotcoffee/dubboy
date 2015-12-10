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
package com.alibaba.dubbo.rpc.protocol.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author BruceZCQ
 */
public class RestExceptionResponse {
	
	public static Response getResponse(Status status, String message) {
        return Response.status(status).entity(message).type("text/plain").build();
	}
	
	public static String getRestExtension() {
		StringBuilder extension = new StringBuilder();
		//forbidden
		extension.append(RestForbiddenExceptionMapper.class.getName()).append(",");
		//notacceptable
		extension.append(RestNotAcceptableExceptionMapper.class.getName()).append(",");
		//notallowed
		extension.append(RestNotAllowedExceptionMapper.class.getName()).append(",");
		//notauthrized
		extension.append(RestNotAuthorizedExceptionMapper.class.getName()).append(",");
		//notfound
		extension.append(RestNotFoundExceptionMapper.class.getName()).append(",");
		//notsupported
		extension.append(RestNotSupportedExceptionMapper.class.getName()).append(",");
		//redirection
		extension.append(RestRedirectionExceptionMapper.class.getName()).append(",");
		//servererror
		extension.append(RestServerErrorExceptionMapper.class.getName()).append(",");
		//serverunavailable
		extension.append(RestServiceUnavailableExceptionMapper.class.getName()).append(",");
		//validation
		extension.append(ValidationExceptionMapper.class.getName());
		return extension.toString();
	}
}
