package edu.hm.dako.chat.server.rest;

import javax.ejb.EJB;
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

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.datasink.repo.CountRepository;
import edu.hm.dako.chat.server.process.ProcessChatPDU;
import edu.hm.dako.chat.server.user.ClientListEntry;
import edu.hm.dako.chat.common.PduType;
import edu.hm.dako.chat.server.user.SharedChatClientList;

@Path("user")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class UserResource {

	private static final Log LOG = LogFactory.getLog(UserResource.class);

	@EJB
	private static CountRepository bean;

	@Inject
	private ProcessChatPDU process;

	@GET
	@Path("login/{name}")
	public Response login(@PathParam("name") String username) {
		LOG.info("User login gestartet mit dem Nutzernamen: " + username);
		long startTime = System.nanoTime();
		Validate.notNull(username);

		ChatPDU requestPdu = new ChatPDU();
		requestPdu.setPduType(PduType.LOGIN_REQUEST);
		// requestPdu.setClientStatus(sharedClientData.status);
		Thread.currentThread().setName("Client-" + username);
		requestPdu.setClientThreadName(Thread.currentThread().getName());
		requestPdu.setUserName(username);

		SharedChatClientList clientList = SharedChatClientList.getInstance();

		if (clientList.existsClient(username)) {
			return Response.status(Status.CONFLICT).entity(Status.CONFLICT.getReasonPhrase()).build();
		}

		clientList.createClient(username, new ClientListEntry(username));
		process.processClientListChange(clientList, startTime);

		return Response.status(Status.OK).entity(Status.OK.getReasonPhrase()).build();
	}

	@GET
	@Path("logout/{name}")
	public Response logout(@PathParam("name") String username) {
		LOG.info("User logout gestartet mit dem Nutzernamen: " + username);
		long startTime = System.nanoTime();
		Validate.notNull(username);
		Boolean success = true;

		SharedChatClientList clientList = SharedChatClientList.getInstance();

		if (clientList.existsClient(username)) {
			clientList.deleteClientWithoutCondition(username);
//			if (success) {
				process.processClientListChange(clientList, startTime);
//			}
		}

		// Wenn der User gelöscht wurde oder gar nicht vorhanden ist, wird ein
		// 204 "No Content" gesendet.
//		if (success) {
			return Response.status(Status.NO_CONTENT).entity(username).build();
//		} else {
//			return Response.status(Status.FORBIDDEN).entity(Status.FORBIDDEN.getReasonPhrase()).build();
//		}
	}
}
