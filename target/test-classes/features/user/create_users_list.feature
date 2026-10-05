Feature: Create Users With List
  As an API client
  I want to create users in a list
  So that multiple users can be processed

  @smoke @regression
  Scenario: USER-LIST-001 Create users using a single-user list
    When I send a POST request to "/user/createWithList" containing one valid user
    Then the response status code should be 200

  @positive
  Scenario: USER-LIST-002 Create multiple users
    When I send a POST request to "/user/createWithList" containing multiple valid users
    Then the response status code should be 200

  @edge
  Scenario: USER-LIST-003 Create users using an empty list
    When I send a POST request to "/user/createWithList" with an empty array
    Then the API should return a documented or implementation-defined response

  @negative @validation
  Scenario: USER-LIST-004 Send an object instead of a user array
    When I send a POST request to "/user/createWithList" with an object instead of an array
    Then the response should indicate validation or implementation-defined behavior

  @negative @validation
  Scenario: USER-LIST-005 Send a list containing invalid user data
    When I send a POST request containing a user with invalid property types
    Then the response should indicate validation or implementation-defined behavior

  @edge
  Scenario: USER-LIST-006 Send a very large user list
    When I send a POST request containing a large number of users
    Then the API should return a documented or implementation-defined response
    And the API should not return a 5xx server error
