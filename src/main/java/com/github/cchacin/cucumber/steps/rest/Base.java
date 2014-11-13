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

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.test.api.ArquillianResource;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

abstract class Base {
    Response response;

    @ArquillianResource
    URL contextPath;

    WebClient webClient;

    String authorizationHeader;

    final String fileContent(String filePath) {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(getClass().getResource(
                    filePath).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

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

    final void createClientWithAuthHeader() throws Throwable {
        this.webClient = WebClient.create(this.contextPath.toString());
        if (this.authorizationHeader != null) {
            this.setHeader("Authorization", this.authorizationHeader);
        }
    }
}
