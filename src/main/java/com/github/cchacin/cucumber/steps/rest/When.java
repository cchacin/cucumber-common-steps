/**
 * Copyright (C) 2014 Carlos Chacin (cchacin@gmail.com)
 *
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
package com.github.cchacin.cucumber.steps.rest;

import cucumber.api.DataTable;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class When extends Given {

    @cucumber.api.java.en.When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint$")
    public final void I_make_a_GET_HEAD_call_to_endpoint(final String method, final String endpointUrl) throws Throwable {
        createClientWithAuthHeader();
        this.response = (method.equals("GET")) ? this.webClient.path(
                endpointUrl).get() : this.webClient.path(endpointUrl).head();
        assertThat(this.response.getStatus()).isBetween(200, 299);
    }

    @cucumber.api.java.en.When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint in host \"([^\"]*)\"$")
    public final void I_make_a_GET_HEAD_call_to_endpoint_in_host(final String method, final String endpointUrl, final String host) throws Throwable {
        createClientWithAuthHeader(host);
        this.response = (method.equals("GET")) ? this.webClient.path(
                endpointUrl).get() : this.webClient.path(endpointUrl).head();
        assertThat(this.response.getStatus()).isBetween(200, 299);
    }

    @cucumber.api.java.en.When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint with header \"([^\"]*)\" with value \"([^\"]*)\"$")
    public final void I_make_a_GET_HEAD_call_to_endpoint_with_header_with_value(
            final String method, final String endpointUrl, final String headerName,
            final String headerValue) throws Throwable {
        this.setHeader(headerName, headerValue);
        I_make_a_GET_HEAD_call_to_endpoint(method, endpointUrl);
    }

    @cucumber.api.java.en.When("^I make a (POST|PUT) call to \"([^\"]*)\" endpoint with post body:$")
    public final void I_make_a_POST_PUT_call_to_endpoint_with_post_body(
            String method, String endpointUrl, final String postBody)
            throws Throwable {
        createClientWithAuthHeader();
        this.response = (method.equals("POST")) ? this.webClient.path(
                endpointUrl).post(postBody) : this.webClient.path(endpointUrl)
                .put(postBody);
        assertThat(this.response.getStatus()).isBetween(200, 299);
    }

    @cucumber.api.java.en.When("^I make a (POST|PUT) call to \"([^\"]*)\" endpoint with post body in file \"([^\"]*)\"$")
    public final void I_make_a_POST_PUT_call_to_endpoint_with_post_body_in_file(
            final String method, final String endpointUrl, final String postBodyFilePath)
            throws Throwable {
        I_make_a_POST_PUT_call_to_endpoint_with_post_body(method, endpointUrl,
                fileContent(postBodyFilePath));
    }

    @cucumber.api.java.en.When("^I make a DELETE call to \"([^\"]*)\" endpoint$")
    public final void I_make_a_DELETE_call_to_endpoint(final String endpointUrl)
            throws Throwable {
        this.createClientWithAuthHeader();
        this.response = this.webClient.path(endpointUrl).delete();
        assertThat(this.response.getStatus()).isBetween(200, 299);
    }


    @cucumber.api.java.en.When("^I make a (GET|HEAD) call to \"([^\"]*)\" endpoint with headers:$")
    public void I_make_a_GET_HEAD_call_to_endpoint_with_headers(final String method, final String endpointUrl, final DataTable headers) throws Throwable {
        Map<String, String> headersMap = headers.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            this.setHeader(entry.getKey(), entry.getValue());
        }
        I_make_a_GET_HEAD_call_to_endpoint(method, endpointUrl);
    }
}
