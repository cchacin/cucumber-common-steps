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
    When I make a GET call to "/successful/get" endpoint with header "Authorization" with value "OAuth qwerqweqrqwerqwer"
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
