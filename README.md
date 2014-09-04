[![Build Status](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/5407630bccc023c90d000098/badge.svg)](https://www.versioneye.com/user/projects/5407630bccc023c90d000098)

cucumber-common-steps
=====================

Cucumber common steps definitions


Getting Started
===============

Add as dependency in your pom.xml:

```xml
<dependency>
    <artifactId>cucumber-common-steps</artifactId>
    <packaging>jar</packaging>
    <version>@version@</version>
</dependency>
```

Write your feature in gherkin language with a tag(s) in ```src/test/resources```

```gherkin
@users_endpoint
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
@RunWith(Cucumber.class)
@CucumberOptions(format = {"pretty", "html:target/cucumber",
		"json:target/cucumber.json"}, tags = {"@users_endpoint"}, glue = {"com.github.cchacin","org.superbiz.javaee"})
public class UsersEndpointITest {
}
```

And then run: ```mvn test``` -> output:

