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

import org.apache.cxf.jaxrs.client.WebClient;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Path("/")
@Stateless
public class Controller {

    @Context
    UriInfo uriInfo;

    @EJB
    ModelDao modelDao;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @GET
    @Path("/successful/get")
    @Produces("application/json")
    public Response successfulGET(@Context final HttpServletRequest request) {
        Response.ResponseBuilder builder = Response.ok(new Model(1L, new Date(), new Date(), null, "", ""));
        buildResponseHeaders(builder, request);
        return builder.build();
    }

    @GET
    @Path("/successful/get/params")
    @Produces("application/json")
    public Response successfulGETQueryParams(@QueryParam("param1") final String param1, @QueryParam("param2") final String param2) {
        return Response.ok(new Model(1L, new Date(), new Date(), null, param2, param1)).build();
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

    @PUT
    @Path("/successful/headers/put")
    @Consumes("application/json")
    public Response successfulHeadersPUT(@Context final HttpServletRequest request, final String body) {
        Response.ResponseBuilder builder = Response.noContent();
        buildResponseHeaders(builder, request);
        return builder.build();
    }

    @POST
    @Path("/successful/headers/post")
    @Consumes("application/json")
    public Response successfulHeadersPOST(@Context final HttpServletRequest request, final String body) {
        Response.ResponseBuilder builder = Response.created(uriInfo.getAbsolutePathBuilder().path("1").build());
        buildResponseHeaders(builder, request);
        return builder.build();
    }

    private void buildResponseHeaders(final Response.ResponseBuilder builder, final HttpServletRequest request) {
        Enumeration<String> enumerations = request.getHeaderNames();
        while(enumerations.hasMoreElements()) {
            final String headerName = enumerations.nextElement();
            builder.header(headerName, request.getHeader(headerName));
        }
    }

    @DELETE
    @Path("/successful/delete")
    @Produces("application/json")
    public Response successfulDELETE() {
        return Response.noContent().build();
    }

    @GET
    @Path("/external/call/user/71e7cb11")
    @Consumes("application/json")
    @Produces("application/json")
    public Response mockExternalCall() {
        final WebClient client = WebClient.create("http://localhost:9090");
        final ResponseWrapper wrapper;
        final List<ResponseItem> items = Lists.newArrayList();
        final List<ResponseItem> getItems = Lists.newArrayList();

        List<Future<Response>> calls = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            final WebClient getClient = WebClient.create("http://localhost:9090").accept(MediaType.APPLICATION_JSON).path("/user/71e7cb11").query("a", i + 1);
            final Future<Response> future = executor.submit(new GetResponse(getClient));
            calls.add(future);
        }

        for (final Future<Response> responseFuture : calls) {
            try {
                final Response r = responseFuture.get();
                getItems.add(new ResponseItem(r.getStatus()));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        final Response postResponse = client.accept(MediaType.APPLICATION_JSON).back(true).path("/user").query("b", "b").post("{\"a\":\"a\"}");
        final Response putResponse = client.accept(MediaType.APPLICATION_JSON).back(true).path("/user/71e7cb11").put(null);
        final Response deleteResponse = client.accept(MediaType.APPLICATION_JSON).back(true).path("/user/71e7cb11").delete();

        final List<Response> responseList = ImmutableList.of(postResponse, putResponse, deleteResponse);

        for (final Response response : responseList) {
            items.add(new ResponseItem(response.getStatus()));
        }

        items.addAll(getItems);

        wrapper = new ResponseWrapper(items);
        return Response.ok(wrapper).build();
    }

    @Value
    static class GetResponse implements Callable<Response> {
        private WebClient client;

        @Override
        public Response call() throws Exception {
            return client.get();
        }
    }

    @GET
    @Path("/external/proxy/user/71e7cb11")
    @Produces("application/json")
    public Response mockExternalProxy() {
        final WebClient client = WebClient.create("http://localhost:9090");
        final Response response = client.accept(MediaType.APPLICATION_JSON).path("/user/71e7cb11").query("a", "a").get();
        return Response.ok(response.getEntity()).build();
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
