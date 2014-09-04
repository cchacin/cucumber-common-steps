[![Build Status](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)](https://travis-ci.org/cchacin/cucumber-common-steps.svg?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/540762f7ccc02339e400016c/badge.svg)](https://www.versioneye.com/user/projects/540762f7ccc02339e400016c)

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
    <version>0.0.3-SNAPSHOT</version>
</dependency>
```

Create an integration test:

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
