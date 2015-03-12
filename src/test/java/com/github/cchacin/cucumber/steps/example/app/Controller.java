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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
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
    @Path("/successful/get/params")
    @Produces("application/json")
    public Response successfulGETQueryParams(@QueryParam("param1") final String param1, @QueryParam("param2") final String param2) {
        return Response.ok(new Model("1", new Date(), new Date(), null, param2, param1)).build();
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

    @GET
    @Path("/external/call/user/71e7cb11")
    @Produces("application/json")
    public Response mockExternalCall() {
        final WebClient client = WebClient.create("http://localhost:9090");
        final ResponseWrapper wrapper;
        final List<ResponseItem> items = Lists.newArrayList();

        final Response getResponse = client.accept(MediaType.APPLICATION_JSON).path("/user/71e7cb11").query("a", "a").get();
        final Response postResponse = client.accept(MediaType.APPLICATION_JSON).back(true).path("/user").post(null);
        final Response putResponse = client.accept(MediaType.APPLICATION_JSON).back(true).path("/user/71e7cb11").put(null);
        final Response deleteResponse = client.accept(MediaType.APPLICATION_JSON).back(true).path("/user/71e7cb11").delete();

        final List<Response> responseList = ImmutableList.of(getResponse, postResponse, putResponse, deleteResponse);

        for (final Response response : responseList) {
            items.add(new ResponseItem(response.getStatus()));
        }

        wrapper = new ResponseWrapper(items);
        return Response.ok(wrapper).build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    private static class ResponseWrapper {
        private List<ResponseItem> responses;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResponseItem {
        private int status;
    }
}
