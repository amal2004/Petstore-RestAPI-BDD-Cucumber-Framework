Feature: Get Store Order
  As an API client
  I want to retrieve orders
  So that I can verify order information

  @smoke @regression
  Scenario: ORDER-GET-001 Get a newly created order
    When I send a POST request to "/store/order" with a valid order
    Then the response status code should be 200
    When I retrieve the order using GET "/store/order/{orderId}"
    Then the response status code should be 200
    And the response should contain an order

  @boundary @negative
  Scenario: ORDER-GET-002 Get an order that does not exist
    When I send a GET request to "/store/order/{orderId}" using a nonexistent order ID
    Then the response status code should be 404

  @negative @validation
  Scenario: ORDER-GET-003 Get an order using a non-integer ID
    When I send a GET request to "/store/order/{orderId}" using a non-integer ID
    Then the response status code should be 404

  @boundary
  Scenario: ORDER-GET-004 Get an order using a negative ID
    When I send a GET request to "/store/order/{orderId}" using order ID -1
    Then the API should not return a 5xx server error

  @boundary
  Scenario: ORDER-GET-005 Get an order using a large int64 ID
    When I send a GET request to "/store/order/{orderId}" using a large valid int64 ID
    Then the API should return a valid API response
