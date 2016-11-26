package edu.hm.dako.chat.server.rest;

import edu.hm.dako.chat.server.datasink.DataSink;
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
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdminResource {

    private static final Log LOG = LogFactory.getLog(AdminResource.class);

    @Inject
    private static DataSink dataSink;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("clients")
    public Response getClients() {
        return Response.status(Status.OK).entity(SharedChatClientList.getInstance().getRegisteredClientNameList()).build();
    }

    @GET
    @Path("count/{clientId}")
    public Response getClientCount(@PathParam("clientId") String clientId) {
        return Response.ok(200).entity(dataSink.getCountByClientname(clientId)).build();
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
