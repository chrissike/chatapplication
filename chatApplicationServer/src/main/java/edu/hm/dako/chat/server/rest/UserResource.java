package edu.hm.dako.chat.server.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PduType;
import edu.hm.dako.chat.server.service.ProcessPDU;

@Path("user")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class UserResource {

	private static final Log log = LogFactory.getLog(UserResource.class);

	@Inject
	private ProcessPDU process;

	@GET
	@Path("login/{name}")
	public Response login(@PathParam("name") String username) {
		log.debug("User login gestartet mit dem Nutzernamen: " + username);

		long startTime = System.nanoTime();
		Validate.notNull(username);

		PduType requestType = PduType.LOGIN;
		ChatPDU pdu = process.createPDU(username, requestType);

		if (!process.processClientListChange(pdu, startTime)) {
			return Response.status(Status.CONFLICT).entity(Status.CONFLICT.getReasonPhrase()).build();
		}

		return Response.status(Status.OK).entity(Status.OK.getReasonPhrase()).build();
	}

	@GET
	@Path("logout/{name}")
	public Response logout(@PathParam("name") String username) {
		log.debug("User logout gestartet mit dem Nutzernamen: " + username);

		long startTime = System.nanoTime();
		Validate.notNull(username);

		PduType requestType = PduType.LOGOUT;
		ChatPDU pdu = process.createPDU(username, requestType);

		process.processClientListChange(pdu, startTime);

		return Response.status(Status.NO_CONTENT).entity(username).build();
	}
	
	@GET
	@Path("all/logout")
	public Response logoutAll() {
		log.debug("Logout aller User gestartet");

		process.clearSharedClientList();

		return Response.status(Status.OK).build();
	}
}
