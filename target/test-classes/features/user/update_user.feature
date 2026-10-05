Feature: Update User
  As an API client
  I want to update users
  So that user information remains current

  @smoke @regression
  Scenario: USER-UPD-001 Update an existing user
    Given a user exists
    When I send a PUT request to "/user/{username}" with updated user data
    Then the response status code should be 200

  @positive
  Scenario: USER-UPD-002 Update a user using JSON
    Given a user exists
    When I update the user using valid JSON
    Then the response status code should be 200

  @positive
  Scenario: USER-UPD-003 Update a user using XML
    Given a user exists
    When I update the user using valid XML
    Then the response status code should be 200

  @positive
  Scenario: USER-UPD-004 Update a user using form encoded data
    Given a user exists
    When I update the user using valid form encoded data
    Then the response status code should be 200

  @negative
  Scenario: USER-UPD-005 Update a nonexistent user
    When I update a nonexistent username
    Then the response status code should be 200

  @negative
  Scenario: USER-UPD-006 Update using an invalid username
    When I update a user using invalid username input
    Then the response status code should be 200

  @edge
  Scenario: USER-UPD-007 Update a user using an empty body
    Given a user exists
    When I update the user with an empty request body
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: USER-UPD-008 Update a user with an empty User object
    Given a user exists
    When I update the user using an empty User object
    Then the API should return a documented or implementation-defined response

  @regression
  Scenario: USER-UPD-009 Update one user property
    Given a user exists
    When I update one property of the user
    Then the response status code should be 200
