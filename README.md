[![Build Status](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)
[![Coverage Status](https://img.shields.io/coveralls/cchacin/cucumber-common-steps.svg)](https://coveralls.io/r/cchacin/cucumber-common-steps?branch=master)
[![Stories in Ready](https://badge.waffle.io/cchacin/cucumber-common-steps.svg?label=ready&title=Ready)](http://waffle.io/cchacin/cucumber-common-steps)
[![Dependency Status](https://www.versioneye.com/user/projects/5407630bccc023c90d000098/badge.svg)](https://www.versioneye.com/user/projects/5407630bccc023c90d000098)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.cchacin/cucumber-common-steps/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.cchacin/cucumber-common-steps)

Cucumber Common Steps
=====================

This project is an intent to provide common steps definitions to make functional testing for REST API's, Database setup's and HTML page's

This is based on [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) project.

You need to specify your features in the gherkin language and run it with [cucumber-java](https://github.com/cucumber/cucumber-java-skeleton) or [cukespace](https://github.com/cukespace/cukespace) + [arquillian](http://arquillian.org/)


Getting Started
===============

Add as dependency in your pom.xml:

```xml
<dependency>
    <groupId>com.github.cchacin</groupId>
    <artifactId>cucumber-common-steps</artifactId>
    <version>0.0.5</version>
    <scope>test</scope>
</dependency>
```

Write your feature in gherkin language in ```src/test/resources```

```gherkin
Feature: REST API to manage users

  Background:
    Given I have the only following rows in the "users" table:
      | id | created             | modified            | email                | fullname | password | version |
      | 1  | 2014-07-16 00:00:00 | 2014-07-16 00:00:00 | cchacin@superbiz.org | Carlos   | passw0rd | 0       |

  Scenario: Retrieve users list
    When I make a GET call to "/users" endpoint
    Then the response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
        [
          {
              "id": "${json-unit.ignore}",
              "created": "${json-unit.ignore}",
              "modified": "${json-unit.ignore}",
              "email": "cchacin@superbiz.org",
              "fullname": "Carlos",
              "password": "passw0rd",
              "version": 0
          }
        ]
    """
```

Write a cucumber integration-test in ```src/test/java```:

```java
@Glues({RestSteps.class, DatabaseSteps.class})
@Features({"features/successful-endpoints.feature"})
@RunWith(ArquillianCucumber.class)
public class UsersEndpointITest {
}
```

And then run: ```mvn test``` -> output:


