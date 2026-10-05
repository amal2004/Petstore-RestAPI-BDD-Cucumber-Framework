Feature: Delete User
  As an API client
  I want to delete users
  So that obsolete users can be removed

  @smoke @regression
  Scenario: USER-DEL-001 Delete an existing user
    Given a user exists
    When I send a DELETE request to "/user/{username}" using the username
    Then the response status code should be 200

  @positive
  Scenario: USER-DEL-002 Delete a known user
    Given a known user exists
    When I delete the user
    Then the response status code should be 200

  @negative
  Scenario: USER-DEL-003 Delete a nonexistent user
    When I delete a nonexistent username
    Then the response status code should be 404

 @negative
  Scenario: USER-DEL-004 Delete a user using invalid username input
    When I delete a user using invalid username input
    Then the response status code should be 404

  @edge
  Scenario: USER-DEL-005 Delete a user using an empty username
    When I delete a user using an empty username
    Then the response status code should be 405

  @security
  Scenario: USER-DEL-006 Delete a user using injection-like username input
    When I delete a user using an injection-like username
    Then the API should not return a 5xx server error

  @regression
  Scenario: USER-DEL-007 Delete a user and verify retrieval
    Given a user exists
    When I delete the user
    Then the response status code should be 200
    When I retrieve the deleted user
    Then the response status code should be 404
