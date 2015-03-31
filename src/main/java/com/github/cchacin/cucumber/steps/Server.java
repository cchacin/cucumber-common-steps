package com.github.cchacin.cucumber.steps;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public enum Server {
    INSTANCE;

    private WireMockServer server;

    private Server() {
        server = new WireMockServer(wireMockConfig().port(9090));
    }

    public WireMockServer get() {
        return server;
    }

    public void start() {
        if (!server.isRunning()) {
            server.start();
        }
    }

    public void stop() {
        if (server.isRunning()) {
            server.stop();
        }
    }
}
