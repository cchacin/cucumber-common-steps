package com.github.cchacin.cucumber.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import lombok.Value;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class CallsSteps {

    final WireMockServer server = new WireMockServer(wireMockConfig().port(9090));

    @Before
    public void setUp() {
        server.start();
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Given("^The call to external service should be:$")
    public void the_call_to_external_service_should_be(final DataTable data) throws Throwable {

        final List<Call> calls = data.asList(Call.class);

        for (Call call : calls) {
            server.stubFor(call.getHttpMethod().willReturn(aResponse().withStatus(call.getStatusCode()).withBodyFile(call.getFilename())));
        }
    }

    @Value
    private static class Call {
        private String method;
        private String url;
        private int statusCode;
        private String filename;
        private MappingBuilder builder;

        MappingBuilder getHttpMethod() {
            switch (getMethod().toUpperCase()) {
                case "POST":
                    return post(urlPathEqualTo(getUrl()));
                case "PUT":
                    return put(urlPathEqualTo(getUrl()));
                case "DELETE":
                    return delete(urlPathEqualTo(getUrl()));
                case "GET":
                    return get(urlPathEqualTo(getUrl()));
                default:
                    return get(urlPathEqualTo(getUrl()));
            }
        }
    }
}
