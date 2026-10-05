Feature: Update Pet
  As an API client
  I want to update an existing pet
  So that its information remains current

  @smoke @regression @positive
  Scenario: PET-UPD-001 Update an existing pet with valid data
    Given a pet exists in the Petstore
    When I send a PUT request to "/pet" with the updated pet
    Then the response status code should be 200
    And the response should contain the updated pet

  @positive
  Scenario: PET-UPD-002 Update a pet using JSON
    Given a pet exists in the Petstore
    When I update the pet using a valid JSON request
    Then the response status code should be 200

  @positive
  Scenario: PET-UPD-003 Update a pet using XML
    Given a pet exists in the Petstore
    When I update the pet using a valid XML request
    Then the response status code should be 200

  @positive
  Scenario: PET-UPD-004 Update a pet using form encoded data
    Given a pet exists in the Petstore
    When I update the pet using valid form encoded data
    Then the response status code should be 200

  @edge
  Scenario: PET-UPD-005 Update a pet without name
    When I send a PUT request to "/pet" without "name"
    Then the response status code should be 200

  @edge
  Scenario: PET-UPD-006 Update a pet without photoUrls
    When I send a PUT request to "/pet" without "photoUrls"
    Then the response status code should be 200

  @negative
  Scenario: PET-UPD-007 Update a pet with malformed JSON
    When I send a PUT request to "/pet" with malformed JSON
    Then the response status code should be in the range 400 to 499

  @edge
  Scenario: PET-UPD-008 Update a pet with an empty photoUrls array
    When I send a PUT request to "/pet" with an empty "photoUrls" array
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: PET-UPD-009 Update a pet with an empty name
    When I send a PUT request to "/pet" with an empty "name"
    Then the API should return a documented or implementation-defined response

  @regression
  Scenario Outline: PET-UPD-010 Update a pet to each supported status
    Given a pet exists in the Petstore
    When I update the pet status to "<status>"
    Then the response status code should be 200
    And the response pet status should be "<status>"

    Examples:
      | status    |
      | available |
      | pending   |
      | sold      |
