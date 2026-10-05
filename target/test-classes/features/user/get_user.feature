Feature: Get User
  As an API client
  I want to retrieve users
  So that user information can be verified

  @smoke @regression
  Scenario: USER-GET-001 Get user1
    Given a user exists
  	When I retrieve the user by username
  	Then the response status code should be 200
  	And the response should contain a user

  @positive
  Scenario: USER-GET-002 Get an existing user
    Given a user exists
    When I retrieve the user by username
    Then the response status code should be 200
    And the returned username should match the requested username

  @negative
  Scenario: USER-GET-003 Get a nonexistent user
    When I retrieve a nonexistent username
    Then the response status code should be 404

  @negative @validation
  Scenario: USER-GET-004 Get a user using invalid username input
    When I retrieve a user using invalid username input
    Then the response status code should be 404

  @negative
	Scenario: USER-GET-004 Get a user using invalid username input
  When I retrieve a user using invalid username input
  Then the response status code should be 404

  @security
  Scenario: USER-GET-006 Get a user using injection-like username input
    When I retrieve a user using an injection-like username
    Then the API should not return a 5xx server error

  @security
  Scenario: USER-GET-007 Get a user using path traversal-like username input
    When I retrieve a user using a path traversal-like username
    Then the API should not return a 5xx server error
