package com.github.cchacin.cucumber.steps.calls;

import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.Getter;
import lombok.Value;
import org.glassfish.grizzly.http.util.HttpStatus;

import java.util.List;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.delete;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.put;

public abstract class Given {
    private StubServer server;

    @Before
    public void start() {
        server = new StubServer(9090).run();
    }

    @After
    public void stop() {
        server.stop();
    }

    @cucumber.api.java.en.Given("^The call to external service should be:$")
    public void the_call_to_external_service_should_be(final DataTable data) throws Throwable {
        List<Call> calls = data.asList(Call.class);

        for (Call call : calls) {
            whenHttp(server)
                    .match(call.getConditional())
                    .then(status(HttpStatus.getHttpStatus(call.getStatusCode())));
        }
    }

    @Value
    @Getter
    private static class Call {
        private String method;
        private String url;
        private int statusCode;

        public Condition getConditional() {
            switch (getMethod().toUpperCase()) {
                case "GET":
                default:
                    return get(getUrl());
                case "POST":
                    return post(getUrl());
                case "PUT":
                    return put(getUrl());
                case "DELETE":
                    return delete(getUrl());
            }
        }
    }
}
