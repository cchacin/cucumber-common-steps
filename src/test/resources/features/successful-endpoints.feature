Feature: Successful rest calls

  # DATABASE SQL SCRIPT
  Scenario: Retrieve users list preparing db with script
    Given I have the following sql script "sample-data.sql"
    When I make a GET call to "/test-app/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
    [
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin@superbiz.org",
        "fullname":"Carlos",
        "id":101,
        "modified":"${json-unit.ignore}",
        "password":"passWorD"
      },
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin2@superbiz.org",
        "fullname":"Carlos2",
        "id":102,
        "modified":"${json-unit.ignore}",
        "password":"passWorD2"
      }
    ]
    """

  # DATABASE GET
  Scenario: Retrieve users list
    Given I have only the following rows in the "models" table:
      | id | created             | modified            | email                 | fullname | password  |
      | 1  | 2014-07-16 00:00:00 | 2014-07-16 00:00:00 | cchacin@superbiz.org  | Carlos   | passw0rd  |
      | 2  | 2014-07-16 00:00:00 | 2014-07-16 00:00:00 | cchacin2@superbiz.org | Carlos2  | passw0rd2 |
      | 3  | 2014-07-16 00:00:00 | 2014-07-16 00:00:00 | cchacin3@superbiz.org | Carlos3  | passw0rd3 |
    When I make a GET call to "/test-app/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
    [
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin@superbiz.org",
        "fullname":"Carlos",
        "id":1,
        "modified":"${json-unit.ignore}",
        "password":"passw0rd"
      },
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin2@superbiz.org",
        "fullname":"Carlos2",
        "id":2,
        "modified":"${json-unit.ignore}",
        "password":"passw0rd2"
      },
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin3@superbiz.org",
        "fullname":"Carlos3",
        "id":3,
        "modified":"${json-unit.ignore}",
        "password":"passw0rd3"
      }
    ]
    """

  # DATABASE GET
  Scenario: Retrieve users list cleaning db
    Given I have the following rows in the "models" table:
      | id | created             | modified            | email                 | fullname | password |
      | 4  | 2015-02-11 00:00:00 | 2015-02-11 00:00:00 | cchacin2@superbiz.org | Carlos2  | passw0rd |
    When I make a GET call to "/test-app/users" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
    [
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin@superbiz.org",
        "fullname":"Carlos",
        "id":1,
        "modified":"${json-unit.ignore}",
        "password":"passw0rd"
      },
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin2@superbiz.org",
        "fullname":"Carlos2",
        "id":2,
        "modified":"${json-unit.ignore}",
        "password":"passw0rd2"
      },
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin3@superbiz.org",
        "fullname":"Carlos3",
        "id":3,
        "modified":"${json-unit.ignore}",
        "password":"passw0rd3"
      },
      {
        "created":"${json-unit.ignore}",
        "email":"cchacin2@superbiz.org",
        "fullname":"Carlos2",
        "id":4,
        "modified":"${json-unit.ignore}",
        "password":"passw0rd"
      }
    ]
    """

  # EXTERNAL SERVICE
  Scenario: Mock external API
    Given The call to external service should be:
      | method | url                | statusCode | filename      |
      | GET    | /user/71e7cb11?a=a | 200        | 71e7cb11.json |
      | GET    | /user/71e7cb12?a=b | 200        | 71e7cb11.json |
      | POST   | /user              | 201        |               |
      | PUT    | /user/71e7cb11     | 204        |               |
      | DELETE | /user/71e7cb11     | 204        |               |
    When I make a GET call to "/test-app/external/call/user/71e7cb11" endpoint
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

  # EXTERNAL SERVICE PROXY
  Scenario: Mock external API
    Given The call to external service should be:
      | method | url                | statusCode | filename      |
      | GET    | /user/71e7cb11?a=a | 200        | 71e7cb11.json |
    When I make a GET call to "/test-app/external/proxy/user/71e7cb11" endpoint
    Then response status code should be "200"
    And response should be json:
    """
    {
      "user": "sample"
    }
    """

  #######
  # GET
  #######
  Scenario:
    When I make a GET call to "https://api.github.com/zen?z=1" endpoint
    Then response status code should be "200"
    And response content type should be "text/plain;charset=utf-8"

  Scenario:
    When I make a GET call to "/test-app/successful/get" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/test-app/successful/get/csv" endpoint
    Then response status code should be "200"
    And response content type should be "text/csv"
    And response should be file "/responses/sample.csv"

  Scenario:
    When I make a GET call to "/test-app/successful/get" endpoint with headers:
      | Authorization | OAuth qwerqweqrqwerqwer |
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/test-app/successful/get/params" endpoint with query params:
      | param1 | passwordParam |
      | param2 | nameParam     |
    Then response status code should be "200"
    And response content type should be "application/json"
    And response should be json:
    """
    {
      "id": "${json-unit.ignore}",
      "created": "${json-unit.ignore}",
      "modified": "${json-unit.ignore}",
      "password": "passwordParam",
      "fullname": "nameParam"
    }
    """

  Scenario:
    When I make a GET call to "/test-app/successful/get" endpoint
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
    When I make a HEAD call to "/test-app/successful/head" endpoint
    Then response status code should be "204"
    And response should be empty

  #######
  # PUT
  #######
  Scenario:
    When I make a PUT call to "/test-app/successful/put" endpoint with post body:
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
    When I make a POST call to "/test-app/successful/post" endpoint with post body:
    """
    {
    }
    """
    Then response status code should be "201"
    And response should be empty

  Scenario:
    When I make a POST call to "/test-app/successful/post" endpoint with post body in file "/requests/post_request.json"
    Then response status code should be "201"
    And response should be empty

  #######
  # DELETE
  #######
  Scenario:
    When I make a DELETE call to "/test-app/successful/delete" endpoint
    Then response status code should be "204"
    And response should be empty
