package edu.hm.dako.chat.server.rest;

import edu.hm.dako.chat.server.datasink.DataSink;
import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.user.SharedChatClientList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

@Path("admin")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class AdminResource {

	private static final Log LOG = LogFactory.getLog(AdminResource.class);

	@Inject
	private static DataSink datasink;
	
	@Context
	private UriInfo uriInfo;

	@GET
	@Path("clients")
	public Response getClients() {
		LOG.info("AdminService.getClients aufgerufen!");
		
		return Response.status(Status.OK).entity(SharedChatClientList.getInstance().getRegisteredClientNameList()).build();

		// TODO: Remove Mock
		// JsonBuilderFactory factory = Json.createBuilderFactory(null);
		// JsonArray value = factory.createArrayBuilder()
		// .add(factory.createObjectBuilder()
		// .add("name", "Test01"))
		// .add(factory.createObjectBuilder()
		// .add("name", "Test02"))
		// .build();
		// return value.toString();

		// TODO: select on clientdb
	}

	@GET
	@Path("count/{clientId}")
	public Response getClientCount(@PathParam("clientId") String clientId) {
		
		//TODO Daten und Zugriffe auf Repos am besten über die DataSink-Klasse abfragen.
		//TODO ..dafür einfach neue Methoden in Count schreiben 
		//(habe hier mal nur beispielhaft eine mit getAll geschrieben)
		List<CountEntity> list = datasink.getAllCountData();
		
		if(list != null) {
			return Response.ok().entity(list).build();
		}
		
		return Response.status(Status.NO_CONTENT).build();
//		return Response.ok(200).entity(countRepository.getCountByClientname(clientId)).build();
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
