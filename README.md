[![Build Status](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)
[![Coverage Status](https://img.shields.io/coveralls/cchacin/cucumber-common-steps.svg)](https://coveralls.io/r/cchacin/cucumber-common-steps?branch=master)
[![Stories in Ready](https://badge.waffle.io/cchacin/cucumber-common-steps.svg?label=ready&title=Ready)](http://waffle.io/cchacin/cucumber-common-steps)
[![Dependency Status](https://www.versioneye.com/user/projects/5407630bccc023c90d000098/badge.svg)](https://www.versioneye.com/user/projects/5407630bccc023c90d000098)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.cchacin/cucumber-common-steps/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.cchacin/cucumber-common-steps)

# Cucumber Common Steps

This project is an intent to provide common steps definitions to make functional testing for REST API, Database setup, external service mocks, redis setup/assertions and HTML pages

This is based on [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) project.

You need to specify your features in the gherkin language and run it with [cucumber-java](https://github.com/cucumber/cucumber-java-skeleton) or [cukespace](https://github.com/cukespace/cukespace) + [arquillian](http://arquillian.org/)

* [Cucumber Common Steps] (#cucumber-common-steps)
  * [Dependency](#dependency)
  * [Call Restful APIs and assert Responses](#call-restful-apis-and-assert-responses)
  * [Mock externals API calls](#mock-externals-api-calls)
  * [Redis assertions and setup] (#redis-assertions-and-setup)
  * [Run the cucumber tests](#run-the-cucumber-tests)

## Dependency

**Add as dependency in your pom.xml:**

```xml
<dependency>
    <groupId>com.github.cchacin</groupId>
    <artifactId>cucumber-common-steps</artifactId>
    <version>${version}</version>
    <scope>test</scope>
</dependency>
```

## Database Setup
**Steps for Database Setup and verification of Endpoints Results**

Put your database connection properties in ```src/test/resources/test-db.properties```

```properties
database.url=jdbc:hsqldb:mem:test
database.driver=org.hsqldb.jdbcDriver
database.user=SA
database.password=
```

Write your feature in gherkin language in ```src/test/resources/features/example.feature```

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

## Call Restful APIs and assert Responses
**Steps to verify Restful API reponses**

```gherkin
  Scenario:
    When I make a GET call to "https://api.github.com/zen?z=1" endpoint
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
  
  Scenario: PUT call with headers
    When I make a PUT call to "/test-app/successful/headers/put" endpoint with post body in file "/requests/post_request.json" and headers:
      | Content-Type | application/json |
    Then response status code should be 204
    And response should be empty

  Scenario:
    When I make a POST call to "/successful/post" endpoint with post body:
    """
    {
    }
    """
    Then response status code should be "201"
    And response should be empty
  
  Scenario: POST call with headers
    When I make a POST call to "/test-app/successful/headers/post" endpoint with post body in file "/requests/post_request.json" and headers:
      | Content-Type | application/json |
    Then response status code should be 201
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


## Mock externals API calls

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

## Redis assertions and setup

```gherkin
Scenario: Redis Steps for Key/Value
  Given I have the redis key "key1" with value "value1"
  Given I have the redis key "key2" with value in file "responses/value2.text"
  Given I have the redis key "key3" with value:
  """
  value3

  """
  Given I have the redis key "key4" with value "value4" with ttl 5 seconds
  Then the redis key "key4" should be "value4"
  Then the redis key "key3" should exists
  Then the redis keys "key1,key2,key3" should exists
  Then the redis keys should exists:
    | key1 |
    | key2 |
    | key3 |
  Then the redis key "key4" should not exists after 6 seconds
  Then the redis key "key4" should not exists
  Then the redis keys "key100,key200,key300" should not exists
  Then the redis key "key1" should be "value1"
  Then the redis key "key2" should be:
  """
  value2

  """
  Then the redis key "key3" should be file "responses/value3.text"

Scenario: Redis Steps for Lists
  Given I have the redis list "list1" with values "value1"
  Given I have the redis list "list2" with values in file "responses/list2.text"
  Given I have the redis list "list3" with values:
    | value3   |
    | value33  |
    | value333 |
  Given I have the redis list "list4" with values "value4" with ttl 5 seconds
  Given I have the redis list "list5" with values "value5,value55,value555" with ttl 5 seconds
  Then the redis list "list4" should be "value4"
  Then the redis list "list5" should be "value5,value55,value555"
  Given I have the redis list "list6" with values "value6,value66,value666"
  Then the redis list "list6" should be:
    | value6   |
    | value66  |
    | value666 |
  Then the redis list "list3" should exists
  Then the redis lists "list1,list2,list3" should exists
  Then the redis lists should exists:
    | list1 |
    | list2 |
    | list3 |
  Then the redis list "list5" should not exists after 6 seconds
  Then the redis list "list4" should not exists
  Then the redis lists "list100,list200,list300" should not exists
  Given I have the redis list "list7" with values "value7,value77,value777"
  Then the redis list "list7" should be file "responses/list7.text"

Scenario: Redis Steps for Cleaning Database
  Given I have the redis key "key" in the db 8 with value "value"
  Then I have the redis key "key" in the db 8 with value "value"
  Given I have cleaned redis db 8
  Then the redis key "key" in the db 8 should not exists

Scenario: Redis Steps for Cleaning All Databases
  Given I have the redis key "key" in the db 0 with value "value"
  And I have the redis key "key" in the db 3 with value "value"
  And I have the redis key "key" in the db 7 with value "value"
  Then I have the redis key "key" in the db 0 with value "value"
  And I have the redis key "key" in the db 3 with value "value"
  And I have the redis key "key" in the db 7 with value "value"
  Given I have cleaned redis
  Then the redis key "key" in the db 0 should not exists
  And the redis key "key" in the db 3 should not exists
  And the redis key "key" in the db 7 should not exists

```


## Database assertions
You need to put your database connection properties in ```src/test/resources/test-db.properties```

```properties
database.url=jdbc:mysql://localhost/myapp_test
database.driver=com.mysql.jdbc.Driver
database.user=root
database.password=
```
Write your feature in gherkin language in ```src/test/resources/features/example.feature```

*it should be used in ```Then``` steps to check the database*

```gherkin
Scenario: Database check exists
    Given I have only the following rows in the "models" table:
      | id | created             | modified            | email                 | fullname | password |
      | 4  | 2015-02-11 00:00:00 | 2015-02-11 00:00:00 | cchacin2@superbiz.org | Carlos2  | passw0rd |
      | 5  | 2015-02-11 00:00:00 | 2015-02-11 00:00:00 | cchacin3@superbiz.org | Carlos3  | passw0rd |
    Then I should have the following rows in the "models" table:
      | id | created             | modified            | email                 | fullname | password |
      | 4  | 2015-02-11 00:00:00 | 2015-02-11 00:00:00 | cchacin2@superbiz.org | Carlos2  | passw0rd |
      | 5  | 2015-02-11 00:00:00 | 2015-02-11 00:00:00 | cchacin3@superbiz.org | Carlos3  | passw0rd |

```

## Run the cucumber tests

**Write a cucumber integration-test in ```src/test/java```:**

```java
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "casspath:features"
        },
        glue = {
                "com.github.cchacin.cucumber.steps"
        },
        format = {
                "pretty"
        }
)
public class MyCucumberStory {
}
```
