package edu.hm.dako.chat.server.rest;

import edu.hm.dako.chat.server.datasink.DataSink;
import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.rest.model.StatisticsDTO;
import edu.hm.dako.chat.server.user.SharedChatClientList;

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
		List<CountEntity> entityList = dataSink.getCountByClientname(clientId);
		return Response.ok(200).entity(entityList).build();
	}

	@GET
	@Path("statistics")
	public Response getStatistics() {
		return Response.ok(200).entity(new StatisticsDTO(dataSink.getAllTraceData())).build();
	}

	@DELETE
	@Path("deleteAllData")
	public Response deleteAllData() {
		dataSink.deleteAllData();
		return Response.ok(200).build();
	}
}
