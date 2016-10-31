package edu.hm.dako.chat.server.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

@Path("admin")
public class AdminService {

    private static final Log LOG = LogFactory.getLog(AdminService.class);

//	@EJB
//    private static CountRepository countRepository;
//	private static TraceRepository traceRepository;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("clients")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String getClients() {
        // TODO: Remove Mock
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArray value = factory.createArrayBuilder()
                .add(factory.createObjectBuilder()
                        .add("name", "Test01")
                )
                .add(factory.createObjectBuilder()
                        .add("name", "Test02")
                )
                .build();
        return value.toString();

        //TODO: select on clientdb
    }

    @GET
    @Path("count/{clientId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public int getClientCount(@PathParam("clientId") String clientId) {
        // TODO: Remove Mock
        if (Objects.equals(clientId.toLowerCase(), "david")) {
            return 1000;
        }
        return 0;

        //TODO: select on clientdb
    }

    @GET
    @Path("trace")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String getTrace() {
        //TODO: Remove mock
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArray value = factory.createArrayBuilder()
                .add(factory.createObjectBuilder()
                        .add("cThread", "David")
                        .add("sThread", "Server1")
                        .add("msg", "Hello there!")
                )
                .build();
        return value.toString();

        // TODO: Select on tracedb
    }

    @POST
    @Path("deleteAllData")
    @Produces({MediaType.APPLICATION_JSON})
    public String deleteAllData() {
        //TODO: Implement DELETE on tracedb & countdb
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObject value = factory.createObjectBuilder()
                .add("deleted", true)
                .build();
        return value.toString();
    }

}
