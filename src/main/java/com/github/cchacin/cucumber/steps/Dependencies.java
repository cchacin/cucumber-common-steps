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
