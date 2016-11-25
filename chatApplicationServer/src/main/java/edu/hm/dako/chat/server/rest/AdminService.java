package edu.hm.dako.chat.server.rest;

import edu.hm.dako.chat.server.datasink.repo.CountRepository;
import edu.hm.dako.chat.server.datasink.repo.TraceRepository;
import edu.hm.dako.chat.server.user.SharedChatClientList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

@Path("admin")
public class AdminService {

	private static final Log LOG = LogFactory.getLog(AdminService.class);

	@Inject
	private static CountRepository countRepository;
	private static TraceRepository traceRepository;
	private static SharedChatClientList chatClientList;

	@Context
	private UriInfo uriInfo;

	@GET
	@Path("clients")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClients() {
		LOG.info("AdminService.getClients aufgerufen!");
		return Response.status(Status.OK).entity(chatClientList.getRegisteredClientNameList()).build();

		// TODO: Remove Mock
		// JsonBuilderFactory factory = Json.createBuilderFactory(null);
		// JsonArray value = factory.createArrayBuilder()
		// .add(factory.createObjectBuilder()
		// .add("name", "Test01")
		// )
		// .add(factory.createObjectBuilder()
		// .add("name", "Test02")
		// )
		// .build();
		// return value.toString();

		// TODO: select on clientdb
	}

	@GET
	@Path("count/{clientId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClientCount(@PathParam("clientId") String clientId) {
		return Response.ok(200).entity(countRepository.getCountByClientname(clientId)).build();
	}

	/*
	 * @GET
	 * 
	 * @Path("trace") public String getTrace() { //TODO: Remove mock //
	 * JsonBuilderFactory factory = Json.createBuilderFactory(null); //
	 * JsonArray value = factory.createArrayBuilder() //
	 * .add(factory.createObjectBuilder() // .add("cThread", "David") //
	 * .add("sThread", "Server1") // .add("msg", "Hello there!") // ) //
	 * .build(); // return value.toString();
	 * 
	 * // TODO: Select on tracedb }
	 * 
	 * @DELETE
	 * 
	 * @Path("deleteAllData") public String deleteAllData() { //TODO: Implement
	 * DELETE on tracedb & countdb // JsonBuilderFactory factory =
	 * Json.createBuilderFactory(null); // JsonObject value =
	 * factory.createObjectBuilder() // .add("deleted", true) // .build(); //
	 * return value.toString(); }
	 */
}
