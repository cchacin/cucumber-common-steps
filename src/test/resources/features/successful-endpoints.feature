Feature: Successful rest calls

  #######
  # GET
  #######
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
