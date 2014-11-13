package com.github.cchacin.cucumber.steps;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
@Stateless
public class RestfulSuccessController {

    @GET
    @Path("/successful/get")
    @Produces("application/json")
    public Response successfulGET() {
        return Response.ok("{\"id\":\"some-id\"}").header("a", "a").build();
    }

    @PUT
    @Path("/successful/put")
    public Response successfulPUT(final String body) {
        return Response.noContent().build();
    }
}
