Feature: Create Store Order
  As an API client
  I want to create store orders
  So that purchases can be recorded

  @smoke @regression
  Scenario: ORDER-ADD-001 Create an order with valid data
    When I send a POST request to "/store/order" with a valid order
    Then the response status code should be 200
    And the response should contain an order

  @positive
  Scenario: ORDER-ADD-002 Create an order using JSON
    When I create an order using valid JSON
    Then the response status code should be 200

  @positive
  Scenario: ORDER-ADD-003 Create an order using XML
    When I create an order using valid XML
    Then the response status code should be 200

  @positive
  Scenario: ORDER-ADD-004 Create an order using form encoded data
    When I create an order using valid form encoded data
    Then the response status code should be 200

  @edge
  Scenario: ORDER-ADD-005 Create an order with an empty body
    When I send a POST request to "/store/order" with an empty body
    Then the API should return a documented or implementation-defined response

  @validation
  Scenario: ORDER-ADD-006 Create an order with each supported status
    When I create an order with status "placed"
    Then the response status code should be 200
    When I create an order with status "approved"
    Then the response status code should be 200
    When I create an order with status "delivered"
    Then the response status code should be 200

  @edge
  Scenario: ORDER-ADD-007 Create an order with a non-enumerated status value
    When I create an order with status "invalid-status"
    Then the response status code should be 200
    And the response should contain an order

  @negative @validation
  Scenario: ORDER-ADD-008 Create an order with an invalid date-time
    When I create an order with an invalid "shipDate"
	Then the response status code should be 500

  @boundary
  Scenario Outline: ORDER-ADD-009 Create an order using quantity boundary values
    When I create an order with quantity "<quantity>"
    Then the API should return a documented or implementation-defined response

    Examples:
      | quantity    |
      | -1          |
      | 0           |
      | 1           |
      | 2147483647  |
