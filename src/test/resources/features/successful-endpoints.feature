Feature: Successful rest calls

  #######
  # GET
  #######
  Scenario:
    When I make a GET call to "/rest/successful/get" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json in file "/responses/successful.json"

  Scenario:
    When I make a GET call to "/rest/successful/get" endpoint
    Then response status code should be "200"
    And response content type should be "application/json"
    And response header "a" should be "a";
    And response should be json:
    """
    {
      "id": "some-id"
    }
    """

  #######
  # PUT
  #######
  Scenario:
    When I make a PUT call to "/rest/successful/put" endpoint with post body:
    """
    {
    }
    """
    Then response status code should be "204"
    And response should be empty
