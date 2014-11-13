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
