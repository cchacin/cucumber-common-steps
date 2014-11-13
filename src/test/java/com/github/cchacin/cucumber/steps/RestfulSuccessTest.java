package com.github.cchacin.cucumber.steps;

import cucumber.runtime.arquillian.ArquillianCucumber;
import cucumber.runtime.arquillian.api.Features;
import cucumber.runtime.arquillian.api.Glues;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@Glues({RestfulSteps.class})
@Features({"features/successful-endpoints.feature"})
@RunWith(ArquillianCucumber.class)
public class RestfulSuccessTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test-app.war")
                .addPackage(JaxRSActivator.class.getPackage());
    }
}
