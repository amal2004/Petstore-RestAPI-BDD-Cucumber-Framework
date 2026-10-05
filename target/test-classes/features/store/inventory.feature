Feature: Store Inventory
  As an API client
  I want to retrieve store inventory
  So that I can verify inventory quantities

  @smoke @regression @security
  Scenario: STORE-INV-001 Get inventory using a valid API key
    Given a valid API key is available
    When I send a GET request to "/store/inventory" with the API key
    Then the response status code should be 200
    And the response should contain inventory data

  @positive
  Scenario: STORE-INV-002 Validate inventory response values
    Given a valid API key is available
    When I retrieve the store inventory
    Then the response status code should be 200
    And the inventory response should contain integer quantities

  @security
  Scenario: STORE-INV-003 Get inventory without an API key
    When I send a GET request to "/store/inventory" without an API key
    Then the API should return the security behavior defined by the implementation

  @security
  Scenario: STORE-INV-004 Get inventory using an invalid API key
    When I send a GET request to "/store/inventory" with an invalid API key
    Then the API should return the security behavior defined by the implementation

  @edge
  Scenario: STORE-INV-005 Handle an empty inventory response
    Given a valid API key is available
    When I retrieve the store inventory
    Then the response should contain inventory data
