Feature: User Logout
  As an API client
  I want to log out
  So that the current login operation can be terminated

  @smoke @regression
  Scenario: LOGOUT-001 Logout
    When I send a GET request to "/user/logout"
    Then the response status code should be 200

  @positive
  Scenario: LOGOUT-002 Logout after login
    Given the user has successfully logged in
    When I send a GET request to "/user/logout"
    Then the response status code should be 200

  @edge
  Scenario: LOGOUT-003 Logout without a login
    When I send a GET request to "/user/logout"
    Then the response should follow the documented behavior

  @regression
  Scenario: LOGOUT-004 Login and logout
    Given I have valid credentials
    When I login
    And I logout
    Then the logout response status code should be 200

  @regression
  Scenario: LOGOUT-005 Logout twice
    Given the user has logged out
    When I send another GET request to "/user/logout"
    Then the response should follow the documented behavior
