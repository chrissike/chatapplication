package edu.hm.dako.chat.server.rest;

import edu.hm.dako.chat.server.datasink.DataSink;
import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.user.SharedChatClientList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("admin")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class AdminResource {

	private static final Log log = LogFactory.getLog(AdminResource.class);

	@Inject
	private DataSink dataSink;

	@GET
	@Path("clients")
	public Response getClients() {
		return Response.status(Status.OK).entity(SharedChatClientList.getInstance().getClientNameList())
				.build();
	}

	@GET
	@Path("count/{clientId}")
	public Response getClientCount(@PathParam("clientId") String clientId) {
		log.info("getClientCount() mit id " + clientId + " für die Datasink: " + dataSink);
		
		List<CountEntity> entityList = dataSink.getCountByClientname(clientId);
		return Response.ok(200).entity(entityList).build();
	}

	@GET
	@Path("trace")
	public Response getTrace() {
		return Response.ok(200).entity(dataSink.getAllTraceData()).build();
	}

	@DELETE
	@Path("deleteAllData")
	public Response deleteAllData() {
		dataSink.deleteAllData();
		return Response.ok(200).build();
	}
}
