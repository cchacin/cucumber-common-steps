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

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.jayway.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class RestSteps {

    private Response response;

    private RequestSpecification spec = given();
    private String responseValue;
    private String basePath;

    final String fileContent(String filePath) throws URISyntaxException, IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(RestSteps.class.getResource(
                filePath).toURI()));

        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded))
                .toString();
    }

    final void setHeader(final String headerName, final String headerValue) {
        this.spec.header(headerName, headerValue);
    }

    final void setParam(final String paramName, final String paramValue) {
        this.spec.param(paramName, paramValue);
    }

    final RequestSpecification createWebClient(final String endpointUrl) {
        this.basePath = endpointUrl;
        if (!endpointUrl.startsWith("http")) {
            this.basePath = endpointUrl;
        }
        return given();
    }

    @When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint$")
    public final void I_make_a_GET_HEAD_call_to_endpoint(final String method, final String endpointUrl) throws Throwable {
        this.spec = createWebClient(endpointUrl);
        execute(method);
    }

    private void execute(final String method) throws Exception {
        this.response = ("GET".equals(method)) ? this.spec.get(this.basePath) : this.spec.head(this.basePath);
        this.responseValue = this.response.asString();
    }

    @When("^I make a (POST|PUT) call to \"([^\"]*)\" endpoint with post body:$")
    public final void I_make_a_POST_PUT_call_to_endpoint_with_post_body(
            String method, String endpointUrl, final String postBody)
            throws Throwable {
        this.spec = createWebClient(endpointUrl).body(postBody);
        this.response = (method.equals("POST")) ? this.spec.post(this.basePath) : this.spec
                .put(this.basePath);
        this.responseValue = this.response.asString();
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
        this.response = createWebClient(endpointUrl).delete(endpointUrl);
        this.responseValue = this.response.asString();
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
        this.spec = createWebClient(endpointUrl);
        final Map<String, String> paramsMap = params.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            this.setParam(entry.getKey(), entry.getValue());
        }
        execute(method);
    }

    @Then("^response status code should be \"([^\"]*)\"$")
    public final void response_status_code_should_be(final String statusCode)
            throws Throwable {
        assertThat(this.response.getStatusCode()).isEqualTo(
                Integer.valueOf(statusCode));
    }

    @Then("^response content type should be \"([^\"]*)\"$")
    public final void response_content_type_should_be(final String contentType)
            throws Throwable {
        assertThat(contentType).isEqualTo(
                this.response.contentType());
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
        assertThatJson(this.responseValue).ignoring("${json-unit.ignore}").isEqualTo(
                jsonResponseString);
    }

    @Then("^response should be empty$")
    public final void response_should_be_empty() throws Throwable {
        assertThat(this.responseValue).isEmpty();
    }

    @Then("^response should be file \"([^\"]*)\"$")
    public final void response_should_be_file(final String contentFilePath)
            throws Throwable {
        assertThat(this.responseValue).isEqualTo(fileContent(contentFilePath));
    }

    @Then("^response header \"([^\"]*)\" should be \"([^\"]*)\";$")
    public final void response_header_should_be_(final String responseHeaderName,
                                                 final String headerValue) throws Throwable {
        assertThat(headerValue).isEqualTo(
                this.response.headers().get(responseHeaderName).getValue());
    }

    @Then("^response json path list \"(.*?)\" should be:$")
    public void response_json_path_list_should_be(final String jsonPath, final DataTable list) throws Throwable {
        final List<String> responseList = JsonPath.read(this.responseValue, jsonPath);
        assertThat(responseList).isEqualTo(list.asList(String.class));
    }

    @Then("^response json path element \"(.*?)\" should be \"(.*?)\"$")
    public void response_json_path_element_should_be(final String jsonPath, final String value) throws Throwable {
        final Object responseValue = JsonPath.read(this.responseValue, jsonPath);
        assertThat(String.valueOf(responseValue)).isEqualTo(value);
    }

    @Then("^response json path list \"(.*?)\" should be of length (\\d)$")
    public void response_json_path_list_should_be_of_length(final String jsonPath, final int length) {
        final List<Object> responseList = JsonPath.read(this.responseValue, jsonPath);
        assertThat(responseList.size()).isEqualTo(length);
    }
}
