Feature: Successful rest calls

  # DATABASE SQL SCRIPT
  Scenario: Retrieve users list preparing db with script
    Given I have the following sql script "sample-data.sql"
    When I make a GET call to "/test-app/users" endpoint
    Then response status code should be 200
    And response content type should be "application/json"
    And response json path list "$.*" should be of length 2
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
    Then response status code should be 200
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
    Then response status code should be 200
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
    And response json path list "$..fullname" should be:
      | Carlos  |
      | Carlos2 |
      | Carlos3 |
      | Carlos2 |
    And response json path element "$[0].id" should be "1"

  # EXTERNAL SERVICE
  Scenario: Mock external API
    Given The call to external service should be:
      | method | url                | statusCode | filename      |
      | GET    | /user/71e7cb11?a=1 | 200        | 71e7cb11.json |
      | GET    | /user/71e7cb11?a=2 | 200        | 71e7cb11.json |
      | GET    | /user/71e7cb11?a=3 | 200        | 71e7cb11.json |
      | GET    | /user/71e7cb11?a=4 | 200        | 71e7cb11.json |
      | GET    | /user/71e7cb11?a=5 | 200        | 71e7cb11.json |
      | POST   | /user?b=b          | 201        |               |
      | PUT    | /user/71e7cb11     | 204        |               |
      | DELETE | /user/71e7cb11     | 204        |               |
    When I make a GET call to "/test-app/external/call/user/71e7cb11" endpoint
    Then response status code should be 200
    And response json path list "$.responses" should be of length 8
    And response should be json:
    """
    {
      "responses":[
        {
          "status":201
        },
        {
          "status":204
        },
        {
          "status":204
        },
        {
          "status":200
        },
        {
          "status":200
        },
        {
          "status":200
        },
        {
          "status":200
        },
        {
          "status":200
        }
      ]
    }
    """

  # EXTERNAL SERVICE PROXY
  Scenario: Mock external API as Proxy
    Given The call to external service should be:
      | method | url                | statusCode | filename      |
      | GET    | /user/71e7cb11?a=a | 200        | 71e7cb11.json |
    When I make a GET call to "/test-app/external/proxy/user/71e7cb11" endpoint
    Then response status code should be 200
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
    Then response status code should be 200
    And response content type should be "text/plain;charset=utf-8"

  Scenario:
    When I make a GET call to "/test-app/successful/get" endpoint
    Then response status code should be 200
    And response content type should be "application/json"
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/test-app/successful/get/csv" endpoint
    Then response status code should be 200
    And response content type should be "text/csv"
    And response should be file "/responses/sample.csv"

  Scenario:
    When I make a GET call to "/test-app/successful/get" endpoint with headers:
      | Authorization | OAuth qwerqweqrqwerqwer |
      | X-Request-Id  | test-request-id         |
    Then response status code should be 200
    And response content type should be "application/json"
    And response header "Authorization" should be "OAuth qwerqweqrqwerqwer"
    And response header "X-Request-Id" should be "test-request-id"
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/test-app/successful/get/params" endpoint with query params:
      | param1 | passwordParam |
      | param2 | nameParam     |
    Then response status code should be 200
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
    Then response status code should be 200
    And response content type should be "application/json"
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
    Then response status code should be 204
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
    Then response status code should be 204
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
    Then response status code should be 201
    And response should be empty

  Scenario:
    When I make a POST call to "/test-app/successful/post" endpoint with post body in file "/requests/post_request.json"
    Then response status code should be 201
    And response should be empty

  #######
  # DELETE
  #######
  Scenario:
    When I make a DELETE call to "/test-app/successful/delete" endpoint
    Then response status code should be 204
    And response should be empty

  #######
  # REDIS
  #######
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
