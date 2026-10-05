Feature: Get Pet
  As an API client
  I want to retrieve pets
  So that I can verify pet information

  @smoke @regression @positive
  Scenario: PET-GET-001 Get an existing pet
    Given a pet with a known ID exists
    When I send a GET request to "/pet/{petId}" using the pet ID
    Then the response status code should be 200
    And the response should contain the requested pet

  @positive
  Scenario: PET-GET-002 Get a pet using a valid positive ID
    Given a pet with a known positive ID exists
    When I retrieve the pet using its ID
    Then the response status code should be 200
    And the returned pet ID should match the requested ID

  @negative
  Scenario: PET-GET-003 Get a pet that does not exist
    When I send a GET request to "/pet/{petId}" using a nonexistent ID
    Then the response status code should be 404

  @negative @validation
  Scenario: PET-GET-004 Get a pet using a non-integer ID
    When I send a GET request to "/pet/{petId}" using a non-integer ID
    Then the response status code should be 404

  @boundary
  Scenario Outline: PET-GET-005 Get a pet using integer boundary values
    When I send a GET request to "/pet/{petId}" using pet ID "<petId>"
    Then the API should return a valid API response
    And the API should not return a 5xx server error

    Examples:
      | petId                |
      | 0                    |
      | -1                   |
      | 9223372036854775807  |

  @security
  Scenario: PET-GET-006 Get a pet using injection-like path input
    When I send a GET request to "/pet/{petId}" using an injection-like value
    Then the API should not return a 5xx server error
