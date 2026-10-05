Feature: Add Pet
  As an API client
  I want to add pets to the Petstore
  So that pets can be created and retrieved

  @smoke @regression @positive
  Scenario: PET-ADD-001 Add a pet with valid data
    Given the Petstore API is available
    When I send a POST request to "/pet" with a valid pet
    Then the response status code should be 200
    And the response should contain the created pet
    And the response should conform to the Pet schema

  @positive @regression
  Scenario: PET-ADD-002 Add a pet using JSON
    Given the Petstore API is available
    When I send a POST request to "/pet" with a valid JSON pet
    Then the response status code should be 200
    And the response should contain a pet

  @positive
  Scenario: PET-ADD-003 Add a pet using XML
    Given the Petstore API is available
    When I send a POST request to "/pet" with a valid XML pet
    Then the response status code should be 200
    And the response should contain a pet

  @positive
  Scenario: PET-ADD-004 Add a pet using form encoded data
    Given the Petstore API is available
    When I send a POST request to "/pet" with valid form encoded pet data
    Then the response status code should be 415

  @edge
  Scenario: PET-ADD-005 Add a pet without name
    When I send a POST request to "/pet" with a pet without "name"
    Then the response status code should be 200

  @edge
  Scenario: PET-ADD-006 Add a pet without photoUrls
    When I send a POST request to "/pet" with a pet without "photoUrls"
    Then the response status code should be 200


  @edge
  Scenario: PET-ADD-007 Add a pet without name and photoUrls
    When I send a POST request to "/pet" with a pet missing "name" and "photoUrls"
    Then the response status code should be 200


  @negative
  Scenario: PET-ADD-008 Add a pet with malformed JSON
    When I send a POST request to "/pet" with malformed JSON
    Then the response status code should be in the range 400 to 499

  @edge
  Scenario: PET-ADD-009 Add a pet with a non-enumerated status value
    When I send a POST request to "/pet" with status "invalid-status"
    Then the response status code should be 200

  @edge
  Scenario: PET-ADD-010 Add a pet with an empty photoUrls array
    When I send a POST request to "/pet" with an empty "photoUrls" array
    Then the API should return a documented or implementation-defined response
    And the response should not be 5xx

  @edge
  Scenario: PET-ADD-011 Add a pet with an empty name
    When I send a POST request to "/pet" with an empty "name"
    Then the API should return a documented or implementation-defined response
    And the response should not be 5xx

  @edge
  Scenario: PET-ADD-012 Add a pet containing an additional JSON property
    When I send a POST request to "/pet" containing an additional property
    Then the API should return a documented or implementation-defined response
    And the response should not be 5xx

  @regression @validation
  Scenario Outline: PET-ADD-013 Add a pet using each supported status
    When I send a POST request to "/pet" with status "<status>"
    Then the response status code should be 200
    And the response pet status should be "<status>"

    Examples:
      | status    |
      | available |
      | pending   |
      | sold      |
