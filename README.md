[![Build Status](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)
[![Coverage Status](https://img.shields.io/coveralls/cchacin/cucumber-common-steps.svg)](https://coveralls.io/r/cchacin/cucumber-common-steps?branch=master)
[![Stories in Ready](https://badge.waffle.io/cchacin/cucumber-common-steps.svg?label=ready&title=Ready)](http://waffle.io/cchacin/cucumber-common-steps)
[![Dependency Status](https://www.versioneye.com/user/projects/5407630bccc023c90d000098/badge.svg)](https://www.versioneye.com/user/projects/5407630bccc023c90d000098)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.cchacin/cucumber-common-steps/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.cchacin/cucumber-common-steps)

Cucumber Common Steps
=====================

This project is an intent to provide common steps definitions to make functional testing for REST API, Database setup, external service mocks and HTML pages

This is based on [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) project.

You need to specify your features in the gherkin language and run it with [cucumber-java](https://github.com/cucumber/cucumber-java-skeleton) or [cukespace](https://github.com/cukespace/cukespace) + [arquillian](http://arquillian.org/)


Getting Started
===============

Add as dependency in your pom.xml:

```xml
<dependency>
    <groupId>com.github.cchacin</groupId>
    <artifactId>cucumber-common-steps</artifactId>
    <version>0.0.6</version>
    <scope>test</scope>
</dependency>
```

Write your feature in gherkin language in ```src/test/resources```


**Steps for Database Setup and verification of Endpoints Results**

Put your database connection properties in ```src/test/resources/test-db.properties```

```properties
database.url=jdbc:hsqldb:mem:test
database.driver=org.hsqldb.jdbcDriver
database.user=SA
database.password=
```

*it should be used in ```Given``` steps to prepare the database*

```gherkin
  Scenario: Retrieve users list
    Given I have only the following rows in the "models" table:
      | id | created             | modified            | email                | fullname | password |
      | 1  | 2014-07-16 00:00:00 | 2014-07-16 00:00:00 | cchacin@superbiz.org | Carlos   | passw0rd |
    When I make a GET call to "/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
    {
      "model":
        [
          {
              "id": 1,
              "created": "${json-unit.ignore}",
              "modified": "${json-unit.ignore}",
              "email": "cchacin@superbiz.org",
              "fullname": "Carlos",
              "password": "passw0rd"
          }
        ]
    }
    """

  Scenario: Retrieve users list
    Given I have the following rows in the "models" table:
      | id | created             | modified            | email                 | fullname | password |
      | 2  | 2015-02-11 00:00:00 | 2015-02-11 00:00:00 | cchacin2@superbiz.org | Carlos2  | passw0rd |
    When I make a GET call to "/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
    {
      "model":
        [
          {
              "id": 1,
              "created": "${json-unit.ignore}",
              "modified": "${json-unit.ignore}",
              "email": "cchacin@superbiz.org",
              "fullname": "Carlos",
              "password": "passw0rd"
          },
          {
              "id": 2,
              "created": "${json-unit.ignore}",
              "modified": "${json-unit.ignore}",
              "email": "cchacin2@superbiz.org",
              "fullname": "Carlos2",
              "password": "passw0rd"
          }
        ]
    }
    """
```

**Steps to verify Restful API reponses**

```gherkin
  Scenario:
    When I make a GET call to "/zen" endpoint in host "https://api.github.com"
    Then response status code should be "200"
    And response content type should be "text/plain;charset=utf-8"

  Scenario:
    When I make a GET call to "/successful/get" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/successful/get/csv" endpoint
    Then response status code should be "200"
    And response content type should be "text/csv"
    And response should be file "/responses/sample.csv"

  Scenario:
    When I make a GET call to "/successful/get" endpoint with header "Authorization" with value "OAuth qwerqweqrqwerqwer"
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/successful/get" endpoint with headers:
      | headerName    | headerValue             |
      | Authorization | OAuth qwerqweqrqwerqwer |
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/successful/get" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json:
    """
    {
      "id": "${json-unit.ignore}",
      "created": "${json-unit.ignore}",
      "modified": "${json-unit.ignore}",
      "password": "",
      "fullname": ""
    }
    """
    
  Scenario:
    When I make a HEAD call to "/successful/head" endpoint
    Then response status code should be "204"
    And response should be empty

  Scenario:
    When I make a PUT call to "/successful/put" endpoint with post body:
    """
    {
    }
    """
    Then response status code should be "204"
    And response should be empty

  Scenario:
    When I make a POST call to "/successful/post" endpoint with post body:
    """
    {
    }
    """
    Then response status code should be "201"
    And response should be empty

  Scenario:
    When I make a POST call to "/successful/post" endpoint with post body in file "/requests/post_request.json"
    Then response status code should be "201"
    And response should be empty

  Scenario:
    When I make a DELETE call to "/successful/delete" endpoint
    Then response status code should be "204"
    And response should be empty
```


*Mocking external API calls*

```gherkin
  Scenario: Mock external API
    Given The call to external service should be:
      | method | url            | statusCode |
      | GET    | /user/71e7cb11 | 200        |
      | POST   | /user          | 201        |
      | PUT    | /user/71e7cb11 | 204        |
      | DELETE | /user/71e7cb11 | 204        |
    When I make a GET call to "/external/call/user/71e7cb11" endpoint
    Then response status code should be "200"
    And response should be json:
    """
    {
      "responses": [
        {
          "status": 200
        },
        {
          "status": 201
        },
        {
          "status": 204
        },
        {
          "status": 204
        }
      ]
    }
    """
```

And then put the payloads (convention over configuration) in ```src/test/resources/restito```: i.e. ```get.user.71e7cb11.json```

```json
{
    "sample": 21
}
```

Write a cucumber integration-test in ```src/test/java```:

```java
@Glues({RestSteps.class, DatabaseSteps.class})
@Features({"features/successful-endpoints.feature"})
@RunWith(ArquillianCucumber.class)
public class UsersEndpointITest {
}
```
