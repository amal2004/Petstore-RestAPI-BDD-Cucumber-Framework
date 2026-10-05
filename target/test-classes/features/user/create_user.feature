Feature: Create User
  As an API client
  I want to create users
  So that users can be registered in the system

  @smoke @regression
  Scenario: USER-CREATE-001 Create a user with valid data
    When I send a POST request to "/user" with a valid user
    Then the response status code should be 200
    And the response should contain a user

  @positive
  Scenario: USER-CREATE-002 Create a complete user
    When I create a user containing all supported User properties
    Then the response status code should be 200

  @positive
  Scenario: USER-CREATE-003 Create a user using JSON
    When I create a user using valid JSON
    Then the response status code should be 200

  @positive
  Scenario: USER-CREATE-004 Create a user using XML
    When I create a user using valid XML
    Then the response status code should be 200

  @positive
  Scenario: USER-CREATE-005 Create a user using form encoded data
    When I create a user using valid form encoded data
    Then the response status code should be 200

  @edge
  Scenario: USER-CREATE-006 Create a user with an empty User object
    When I create a user with an empty User object
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: USER-CREATE-007 Create a user with an empty request body
    When I send a POST request to "/user" with an empty body
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: USER-CREATE-008 Create a user with a duplicate username
    Given a user with username "testuser" exists
    When I create another user with username "testuser"
    Then the API should return a documented or implementation-defined response

  @validation
  Scenario: USER-CREATE-009 Validate User property types
    When I create a user with invalid data types
    Then the API should return a validation or implementation-defined response

  @edge
  Scenario: USER-CREATE-010 Create a user with a non-email-formatted email value
    When I create a user with email "not-an-email"
    Then the API should return a documented or implementation-defined response
    And the result should not assume email-format validation because the schema defines email as a string
