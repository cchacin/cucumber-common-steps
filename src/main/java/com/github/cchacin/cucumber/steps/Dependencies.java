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
package com.github.cchacin.cucumber.steps;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Dependencies {

    private static final Future<File[]> DEPENDENCIES;

    static {
        System.out.println("GO");
        final ExecutorService es = Executors.newSingleThreadExecutor();
        DEPENDENCIES = es.submit(new Callable<File[]>() {
            @Override
            public File[] call() throws Exception {
                return Maven.resolver().loadPomFromFile("pom.xml")
                        .importCompileAndRuntimeDependencies().resolve()
                        .withTransitivity().asFile();
            }
        });
        es.shutdown();
    }

    public static File[] get() {
        System.out.println(new Date());
        try {
            return DEPENDENCIES.get(); // block if not done or return
            // immediately
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(new Date());
        }
    }
}
