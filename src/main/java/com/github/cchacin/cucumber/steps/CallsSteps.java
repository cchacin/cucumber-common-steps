
package com.github.cchacin.cucumber.steps;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.google.common.collect.Maps;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@Slf4j
public class CallsSteps {

    @Before
    public void setUp() {
        Server.INSTANCE.start();
    }

    @After
    public void tearDown() {
        Server.INSTANCE.stop();
    }

    @Given("^The call to external service should be:$")
    public void the_call_to_external_service_should_be(final DataTable data) throws Throwable {

        final List<Call> calls = data.asList(Call.class);

        for (Call call : calls) {

            final ResponseDefinitionBuilder response = aResponse()
                    .withStatus(call.getStatusCode())
                    .withBodyFile(call.getFilename());


            MappingBuilder mappingBuilder = call.getHttpMethod()
                    .willReturn(response);
            for (final Map.Entry<String, String> kv : call.buildQueryParams().entrySet()) {
                mappingBuilder = mappingBuilder.withQueryParam(kv.getKey(), matching(kv.getValue()));
            }
            Server.INSTANCE.get().stubFor(mappingBuilder);

        }
        log.info("Stub Mappings: \n{}", Server.INSTANCE.get().listAllStubMappings().getMappings());
    }

    @Value
    private static class Call {
        private String method;
        private String url;
        private int statusCode;
        private String filename;

        MappingBuilder getHttpMethod() {
            switch (getMethod().toUpperCase()) {
                case "POST":
                    return post(urlPathEqualTo(buildUrl())).withRequestBody(matching(".*"));
                case "PUT":
                    return put(urlPathEqualTo(buildUrl())).withRequestBody(matching(".*"));
                case "DELETE":
                    return delete(urlPathEqualTo(buildUrl()));
                case "GET":
                    return get(urlPathEqualTo(buildUrl()));
                default:
                    return get(urlPathEqualTo(buildUrl()));
            }
        }

        String buildUrl() {
            return (getUrl().contains("?")) ? getUrl().split("\\?")[0] : getUrl();
        }

        HashMap<String, String> buildQueryParams() {
            final String queryParamsStr = (getUrl().contains("?")) ? getUrl().split("\\?")[1] : null;

            final String[] kvs = queryParamsStr != null ? queryParamsStr.split("\\&") : new String[0];
            final HashMap<String, String> queryParams = Maps.newHashMapWithExpectedSize(kvs.length);
            for (final String kv : kvs) {
                final String[] sp = kv.split("\\=");
                queryParams.put(sp[0], sp[1]);
            }
            return queryParams;
        }
    }
}
