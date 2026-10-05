Feature: Delete Store Order
  As an API client
  I want to delete orders
  So that obsolete orders can be removed

  @smoke @regression
  Scenario: ORDER-DEL-001 Delete an order with an ID below 1000
    When I send a POST request to "/store/order" with a valid order
    Then the response status code should be 200
    When I delete the order
    Then the response status code should be 200

  @boundary @negative
  Scenario: ORDER-DEL-002 Delete an order that does not exist
    When I send a DELETE request to "/store/order/{orderId}" using a nonexistent order ID
    Then the response status code should be 404

  @negative @validation
  Scenario: ORDER-DEL-003 Delete an order using a non-integer ID
    When I send a DELETE request to "/store/order/{orderId}" using a non-integer ID
    Then the response status code should be 404

  @regression
  Scenario: ORDER-DEL-004 Delete an order and verify retrieval
    When I send a POST request to "/store/order" with a valid order
    Then the response status code should be 200
    When I delete the order
    Then the response status code should be 200
    When I retrieve the order using GET "/store/order/{orderId}"
    Then the response should follow the documented post-deletion behavior
