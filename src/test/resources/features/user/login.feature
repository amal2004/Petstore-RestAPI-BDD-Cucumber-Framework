Feature: User Login
  As an API client
  I want to authenticate users
  So that authenticated operations can be performed

  @smoke @regression @security
  Scenario: LOGIN-001 Login with valid credentials
    Given valid user credentials are available
    When I send a GET request to "/user/login" with the username and password
    Then the response status code should be 200
    And the response should contain the login result

  @positive
  Scenario: LOGIN-002 Login with a known valid user
    Given a known valid user exists
    When I login using the user's credentials
    Then the response status code should be 200

  @negative @security
  Scenario: LOGIN-003 Login with an invalid username
    When I login using an invalid username and valid password
    Then the response status code should be 200

  @negative @security
  Scenario: LOGIN-004 Login with an invalid password
    When I login using a valid username and invalid password
    Then the response status code should be 200

  @negative @security
  Scenario: LOGIN-005 Login with invalid username and password
    When I login using invalid credentials
    Then the response status code should be 200

  @edge
  Scenario: LOGIN-006 Login with username only
    When I send a login request containing only a username
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: LOGIN-007 Login with password only
    When I send a login request containing only a password
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: LOGIN-008 Login without username or password
    When I send a login request without username or password
    Then the API should return a documented or implementation-defined response

  @security
  Scenario: LOGIN-009 Login using special characters in the password
    Given a user has a password containing special characters
    When I login using the credentials
    Then the API should return a documented or implementation-defined response

  @security
  Scenario: LOGIN-010 Login using injection-like username input
    When I login using an injection-like username
    Then the API should not return a 5xx server error

  @security
  Scenario: LOGIN-011 Login using injection-like password input
    When I login using an injection-like password
    Then the API should not return a 5xx server error

  @regression
  Scenario: LOGIN-012 Validate X-Rate-Limit response header
    Given valid user credentials are available
    When I login using the credentials
    Then the response status code should be 200
    And the response should contain the "X-Rate-Limit" header

  @regression
  Scenario: LOGIN-013 Validate X-Expires-After response header
    Given valid user credentials are available
    When I login using the credentials
    Then the response status code should be 200
    And the response should contain the "X-Expires-After" header
