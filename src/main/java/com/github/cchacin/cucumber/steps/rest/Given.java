package com.github.cchacin.cucumber.steps.rest;

import static java.lang.Thread.sleep;

public abstract class Given extends Base {

    @cucumber.api.java.en.Given("^header \"([^\"]*)\" with value \"([^\"]*)\"$")
    public final void header_with_value(final String headerName, final String headerValue)
            throws Throwable {
        this.setHeader(headerName, headerValue);
    }

    @cucumber.api.java.en.Given("^header \"([^\"]*)\" has not been set$")
    public final void header_has_not_been_set(final String headerName) throws Throwable {

    }

    @cucumber.api.java.en.Given("^wait for \"([^\"]*)\" seconds for the background processes to finish$")
    public final void wait_for_seconds_for_the_background_processes_to_finish(
            final Integer timeToWaitInSeconds) throws Throwable {
        sleep(timeToWaitInSeconds * 1_000);
    }
}
