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

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author BruceZCQ
 */
public class RestExceptionMapper <E extends WebApplicationException> implements ExceptionMapper<E> {

	@Override
	public Response toResponse(E e) {
		Status status = Response.Status.INTERNAL_SERVER_ERROR;//500
		String message = "Oops! Internal Server Error!";
		if (e instanceof NotFoundException) {//404
			status = Response.Status.NOT_FOUND;
			message = "Oops! the requested resource is not found!";
		} else if (e instanceof NotAuthorizedException) {//403
			status = Response.Status.FORBIDDEN;
			message = "Oops! the requested resource is Forbidden!";
		} else if (e instanceof ServerErrorException) {//5xx
			;
		} else if (e instanceof ServiceUnavailableException) {//503
			status = Response.Status.SERVICE_UNAVAILABLE;
			message = "Oops! the Service Unavailable!";
		} else if (e instanceof NotAcceptableException) {//406
			status = Response.Status.NOT_ACCEPTABLE;
			message = "Oops! Not Acceptable!";
		} else if (e instanceof NotAllowedException) {//405
			status = Response.Status.METHOD_NOT_ALLOWED;
			message = "Oops! Method Not Allowed!";
		} else if (e instanceof NotSupportedException) {//415
			status = Response.Status.UNSUPPORTED_MEDIA_TYPE;
			message = "Oops! Unsupported Media Type!";
		} else if (e instanceof RedirectionException) {//3xx
			status = Response.Status.MOVED_PERMANENTLY;
			message = "Oops! redirection Error!";
		}
		return Response.status(status).entity(message).type("text/plain").build();
	}
}
