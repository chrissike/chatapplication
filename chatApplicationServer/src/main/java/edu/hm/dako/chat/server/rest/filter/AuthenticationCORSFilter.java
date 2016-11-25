package edu.hm.dako.chat.server.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Filter für Anfragen von Javascript. (siehe CORS)
 */
@Provider
public class AuthenticationCORSFilter implements ContainerResponseFilter {

	private static final Log log = LogFactory.getLog(AuthenticationCORSFilter.class);
		
		@Override
	    public void filter(ContainerRequestContext request,
	            ContainerResponseContext response) throws IOException {
			log.info("CORS-filter aufgerufen");
	        response.getHeaders().add("Access-Control-Allow-Origin", "*");
	        response.getHeaders().add("Access-Control-Allow-Headers",
	                "origin, content-type, accept, authorization");
	        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
	        response.getHeaders().add("Access-Control-Allow-Methods",
	                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
	    }

}