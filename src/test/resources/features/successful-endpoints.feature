Feature: Successful rest calls

  # DATABASE SQL SCRIPT
  Scenario: Retrieve users list preparing db with script
    Given I have the following sql script "sample-data.sql"
    When I make a GET call to "/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
    [
      {
          "id": 101,
          "created": "${json-unit.ignore}",
          "modified": "${json-unit.ignore}",
          "email": "cchacin@superbiz.org",
          "fullname": "Carlos",
          "password": "passWorD"
      }
    ]
    """

  # DATABASE GET
  Scenario: Retrieve users list
    Given I have only the following rows in the "models" table:
      | id | created             | modified            | email                | fullname | password |
      | 1  | 2014-07-16 00:00:00 | 2014-07-16 00:00:00 | cchacin@superbiz.org | Carlos   | passw0rd |
    When I make a GET call to "/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
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
    """

  # DATABASE GET
  Scenario: Retrieve users list cleaning db
    Given I have the following rows in the "models" table:
      | id | created             | modified            | email                 | fullname | password |
      | 2  | 2015-02-11 00:00:00 | 2015-02-11 00:00:00 | cchacin2@superbiz.org | Carlos2  | passw0rd |
    When I make a GET call to "/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
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
    """

  # EXTERNAL SERVICE
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

  #######
  # GET
  #######
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

  #######
  # HEAD
  #######
  Scenario:
    When I make a HEAD call to "/successful/head" endpoint
    Then response status code should be "204"
    And response should be empty

  #######
  # PUT
  #######
  Scenario:
    When I make a PUT call to "/successful/put" endpoint with post body:
    """
    {
    }
    """
    Then response status code should be "204"
    And response should be empty

  #######
  # POST
  #######
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

  #######
  # DELETE
  #######
  Scenario:
    When I make a DELETE call to "/successful/delete" endpoint
    Then response status code should be "204"
    And response should be empty
