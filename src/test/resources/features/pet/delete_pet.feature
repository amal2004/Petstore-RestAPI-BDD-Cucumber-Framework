Feature: Delete Pet
  As an API client
  I want to delete pets
  So that obsolete pets can be removed

  @smoke @regression @positive
  Scenario: PET-DEL-001 Delete an existing pet
    Given a pet exists in the Petstore
    When I send a DELETE request to "/pet/{petId}" using the pet ID
    Then the response status code should be 200

  @positive
  Scenario: PET-DEL-002 Delete an existing pet using an API key
    Given a pet exists in the Petstore
    When I send a DELETE request to "/pet/{petId}" with a valid API key
    Then the response status code should be 200

  @negative
  Scenario: PET-DEL-003 Delete a pet that does not exist
    When I send a DELETE request to "/pet/{petId}" using a nonexistent ID
    Then the response status code should be 404

  @negative @validation
  Scenario: PET-DEL-004 Delete a pet using a non-integer ID
    When I send a DELETE request to "/pet/{petId}" using a non-integer ID
    Then the response status code should be 404

  @boundary
  Scenario Outline: PET-DEL-005 Delete a pet using boundary IDs
    When I send a DELETE request to "/pet/{petId}" using pet ID "<petId>"
    Then the API should return a valid API response

    Examples:
      | petId |
      | 0     |
      | -1    |

  @security
  Scenario: PET-DEL-006 Delete a pet without an API key
    Given a pet exists in the Petstore
    When I send a DELETE request to "/pet/{petId}" without an API key
    Then the API should return the security behavior defined by the implementation

  @security
  Scenario: PET-DEL-007 Delete a pet using an invalid API key
    Given a pet exists in the Petstore
    When I send a DELETE request to "/pet/{petId}" with an invalid API key
    Then the API should return the security behavior defined by the implementation

  @regression
  Scenario: PET-DEL-008 Delete a pet and verify it cannot be retrieved
    Given a pet exists in the Petstore
    When I delete the pet
    Then the response status code should be 200
    When I retrieve the deleted pet
    Then the response status code should be 404
