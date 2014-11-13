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
