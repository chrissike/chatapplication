package edu.hm.dako.chat.server.rest;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.datasink.repo.CountRepository;
import edu.hm.dako.chat.server.user.ClientListEntry;
import edu.hm.dako.chat.common.PduType;
import edu.hm.dako.chat.server.user.SharedChatClientList;

@Path("user")
public class UserResource {

	private static final Log LOG = LogFactory.getLog(UserResource.class);
	
	@EJB
    private static CountRepository bean;

	@Context
	private UriInfo uriInfo;

	@GET
	@Path("login/{name}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response login(@PathParam("name") String username) {
		LOG.info("User login gestartet mit dem Nutzernamen: " + username);
		
		Validate.notNull(username);

		ChatPDU requestPdu = new ChatPDU();
		requestPdu.setPduType(PduType.LOGIN_REQUEST);
//		requestPdu.setClientStatus(sharedClientData.status);
		Thread.currentThread().setName("Client-" + username);
		requestPdu.setClientThreadName(Thread.currentThread().getName());
		requestPdu.setUserName(username);

//		AbstractWorkerThread awt = new RestChatWorkerThreadImpl(null, null, null, null);
//		awt.loginRequestAction(requestPdu);
		
		SharedChatClientList clientList = SharedChatClientList.getInstance();
		clientList.createClient(username, new ClientListEntry(username, null));
		final Boolean success = true; //new AbstractWorkerThread().loginRequestAction(requestPdu); //exampleService.getById(username);
		Validate.notNull(success);
		if (success) {
			return Response.status(Status.OK).build();
		} else {
			return Response.status(Status.NOT_FOUND).entity(Status.NOT_FOUND.getReasonPhrase()).build();
		}
	}
	
	@GET
	@Path("logout/{name}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response logout(@PathParam("name") String username) {
		LOG.info("User logout gestartet mit dem Nutzernamen: " + username);
		
		Validate.notNull(username);
		final Boolean success = true;
		
		if (success) {
			return Response.status(Status.OK).build();
		} else {
			return Response.status(Status.NOT_FOUND).entity(Status.NOT_FOUND.getReasonPhrase()).build();
		}
	}
}
