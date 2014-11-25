/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cchacin.cucumber.steps.example.app;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;

@Path("/")
@Stateless
public class Controller {

    @Context
    UriInfo uriInfo;

    @Context
    HttpHeaders headers;

    @EJB
    ModelDao modelDao;

    @GET
    @Path("/successful/get")
    @Produces("application/json")
    public Response successfulGET() {
        return Response.ok(new Model("1", new Date(), new Date(), null, "", "")).header("a", "a").build();
    }

    @GET
    @Path("/users")
    @Produces("application/json")
    public List<Model> successfulGETUsers() {
        return modelDao.get();
    }

    @GET
    @Path("/successful/get/csv")
    @Produces("text/csv")
    public Response successfulGETCSV() {
        return Response.ok("\"headerA\",\"headerB\"\n\"row1A\",\"row1B\"\n").build();
    }

    @HEAD
    @Path("/successful/head")
    @Produces("application/json")
    public Response successfulHEAD() {
        return Response.noContent().build();
    }

    @PUT
    @Path("/successful/put")
    public Response successfulPUT(final String body) {
        return Response.noContent().build();
    }

    @POST
    @Path("/successful/post")
    public Response successfulPOST(final String body) {
        return Response.created(uriInfo.getAbsolutePathBuilder().path("1").build()).build();
    }

    @DELETE
    @Path("/successful/delete")
    @Produces("application/json")
    public Response successfulDELETE() {
        return Response.noContent().build();
    }
}
