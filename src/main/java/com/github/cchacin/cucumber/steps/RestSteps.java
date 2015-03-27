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
package com.github.cchacin.cucumber.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class RestSteps {

    private Response response;

    private WebClient webClient;

    final String fileContent(String filePath) throws URISyntaxException, IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(RestSteps.class.getResource(
                filePath).toURI()));

        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded))
                .toString();
    }

    final String responseValue() throws IOException {
        return IOUtils
                .toString((InputStream) this.response.getEntity());
    }

    final void setHeader(final String headerName, final String headerValue) {
        this.webClient.header(headerName, headerValue);
    }

    final void setParam(final String paramName, final String paramValue) {
        this.webClient.query(paramName, paramValue);
    }

    final WebClient createWebClient(final String endpointUrl) {
        String url = endpointUrl;
        if (!endpointUrl.startsWith("http")) {
            url = "http://localhost:8080" + endpointUrl;
        }

        return WebClient.create(url);
    }

    @When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint$")
    public final void I_make_a_GET_HEAD_call_to_endpoint(final String method, final String endpointUrl) throws Throwable {
        this.webClient = createWebClient(endpointUrl);
        execute(method);
    }

    private void execute(final String method) {
        this.response = ("GET".equals(method)) ? this.webClient.get() : this.webClient.head();
        assertThat(this.response.getStatus()).isBetween(200, 299);
    }

    @When("^I make a (POST|PUT) call to \"([^\"]*)\" endpoint with post body:$")
    public final void I_make_a_POST_PUT_call_to_endpoint_with_post_body(
            String method, String endpointUrl, final String postBody)
            throws Throwable {
        this.webClient = createWebClient(endpointUrl);
        this.response = (method.equals("POST")) ? this.webClient.post(postBody) : this.webClient
                .put(postBody);
        assertThat(this.response.getStatus()).isBetween(200, 299);
    }

    @When("^I make a (POST|PUT) call to \"([^\"]*)\" endpoint with post body in file \"([^\"]*)\"$")
    public final void I_make_a_POST_PUT_call_to_endpoint_with_post_body_in_file(
            final String method, final String endpointUrl, final String postBodyFilePath)
            throws Throwable {
        I_make_a_POST_PUT_call_to_endpoint_with_post_body(method, endpointUrl,
                fileContent(postBodyFilePath));
    }

    @When("^I make a DELETE call to \"([^\"]*)\" endpoint$")
    public final void I_make_a_DELETE_call_to_endpoint(final String endpointUrl)
            throws Throwable {
        this.response = createWebClient(endpointUrl).delete();
        assertThat(this.response.getStatus()).isBetween(200, 299);
    }

    @When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint with headers:$")
    public void I_make_a_GET_HEAD_call_to_endpoint_with_headers(final String method, final String endpointUrl, final DataTable headers) throws Throwable {
        final Map<String, String> headersMap = headers.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            this.setHeader(entry.getKey(), entry.getValue());
        }
        I_make_a_GET_HEAD_call_to_endpoint(method, endpointUrl);
    }

    @When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint with query params:$")
    public void i_make_a_GET_HEAD_call_to_endpoint_with_query_params(final String method, final String endpointUrl, final DataTable params) throws Throwable {
        this.webClient = createWebClient(endpointUrl);
        final Map<String, String> paramsMap = params.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            this.setParam(entry.getKey(), entry.getValue());
        }
        execute(method);
    }

    @Then("^response status code should be \"([^\"]*)\"$")
    public final void response_status_code_should_be(final String statusCode)
            throws Throwable {
        assertThat(this.response.getStatus()).isEqualTo(
                Integer.valueOf(statusCode));
    }

    @Then("^response content type should be \"([^\"]*)\"$")
    public final void response_content_type_should_be(final String contentType)
            throws Throwable {
        assertThat(contentType).isEqualTo(
                this.response.getMetadata().getFirst("content-type"));
    }

    @Then("^response should be json in file \"([^\"]*)\"$")
    public final void response_should_be_json_responseBody(
            final String contentFilePath) throws Throwable {

        final String content = fileContent(contentFilePath);
        this.response_should_be_json(content);
    }

    @Then("^response should be json:$")
    public final void response_should_be_json(final String jsonResponseString)
            throws Throwable {
        assertThatJson(responseValue()).ignoring("${json-unit.ignore}").isEqualTo(
                jsonResponseString);
    }

    @Then("^response should be empty$")
    public final void response_should_be_empty() throws Throwable {
        assertThat(responseValue()).isEmpty();
    }

    @Then("^response should be file \"([^\"]*)\"$")
    public final void response_should_be_file(final String contentFilePath)
            throws Throwable {
        assertThat(responseValue()).isEqualTo(fileContent(contentFilePath));
    }

    @Then("^response header \"([^\"]*)\" should be \"([^\"]*)\";$")
    public final void response_header_should_be_(final String responseHeaderName,
                                                 final String headerValue) throws Throwable {
        assertThat(headerValue).isEqualTo(
                this.response.getMetadata().getFirst(responseHeaderName));
    }
}
