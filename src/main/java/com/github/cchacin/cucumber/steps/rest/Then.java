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

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class Then extends When {

    @cucumber.api.java.en.Then("^response status code should be \"([^\"]*)\"$")
    public final void response_status_code_should_be(final String statusCode)
            throws Throwable {
        assertThat(this.response.getStatus()).isEqualTo(
                Integer.valueOf(statusCode));
    }

    @cucumber.api.java.en.Then("^response content type should be \"([^\"]*)\"$")
    public final void response_content_type_should_be(final String contentType)
            throws Throwable {
        assertThat(contentType).isEqualTo(
                this.response.getMetadata().getFirst("content-type"));
    }

    @cucumber.api.java.en.Then("^response should be json in file \"([^\"]*)\"$")
    public final void response_should_be_json_responseBody(
            final String contentFilePath) throws Throwable {

        final String content = fileContent(contentFilePath);
        this.response_should_be_json(content);
    }

    @cucumber.api.java.en.Then("^response should be json:$")
    public final void response_should_be_json(final String jsonResponseString)
            throws Throwable {
        assertThatJson(responseValue()).ignoring("${json-unit.ignore}").isEqualTo(
                jsonResponseString);
    }

    @cucumber.api.java.en.Then("^response should be empty$")
    public final void response_should_be_empty() throws Throwable {
        assertThat(responseValue()).isEmpty();
    }

    @cucumber.api.java.en.Then("^response should be file \"([^\"]*)\"$")
    public final void response_should_be_file(final String contentFilePath)
            throws Throwable {
        assertThat(responseValue()).isEqualTo(fileContent(contentFilePath));
    }

    @cucumber.api.java.en.Then("^response header \"([^\"]*)\" should be \"([^\"]*)\";$")
    public final void response_header_should_be_(final String responseHeaderName,
                                           final String headerValue) throws Throwable {
        assertThat(headerValue).isEqualTo(
                this.response.getMetadata().getFirst(responseHeaderName));
    }
}
