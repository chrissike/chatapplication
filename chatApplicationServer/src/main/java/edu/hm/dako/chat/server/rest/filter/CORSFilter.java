/**
 * Organisation: University of Applied Sciences Munich, FK07
 * Project: Software-Architektur Praktikum
 * Sub-Project: chatApplication
 * Author: David Sautter (04358212)
 * Created On: 25.11.16
 * Operating-System: Arch-Linux
 * Java-Version: 1.8.0_40
 * CPU: Intel(R) Core(TM)2 Duo CPU T5870 @ 2.00GHz
 */

package edu.hm.dako.chat.server.rest.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext,
                       final ContainerResponseContext cres) throws IOException {
        cres.getHeaders().add("Access-Control-Allow-Origin", "*");
        cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        cres.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}