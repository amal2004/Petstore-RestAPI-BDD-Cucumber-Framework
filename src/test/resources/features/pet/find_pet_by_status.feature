Feature: Find Pets By Status
  As an API client
  I want to find pets by status
  So that I can retrieve pets matching a supported status

  @smoke @regression
  Scenario Outline: PET-STATUS-001 Find pets using a supported status
    When I send a GET request to "/pet/findByStatus" with status "<status>"
    Then the response status code should be 200
    And the response should be an array of pets

    Examples:
      | status    |
      | available |
      | pending   |
      | sold      |

  @negative @validation
  Scenario: PET-STATUS-002 Find pets using an invalid status
    When I send a GET request to "/pet/findByStatus" with status "invalid-status"
    Then the response status code should be 200

  @negative
  Scenario: PET-STATUS-003 Find pets without the required status parameter
    When I send a GET request to "/pet/findByStatus" without the status parameter
    Then the response should indicate a missing required parameter

  @edge
  Scenario: PET-STATUS-004 Find pets using an empty status
    When I send a GET request to "/pet/findByStatus" with an empty status
    Then the response should indicate a missing required parameter

  @edge
  Scenario: PET-STATUS-005 Find pets using an uppercase status
    When I send a GET request to "/pet/findByStatus" with status "AVAILABLE"
    Then the response status code should be 200
