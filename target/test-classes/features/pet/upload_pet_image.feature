Feature: Upload Pet Image
  As an API client
  I want to upload an image for a pet
  So that the pet can have an associated image

  @only @smoke @regression
  Scenario: PET-UPLOAD-001 Upload an image for an existing pet
  Given a pet exists in the Petstore
    And a valid image file is available
    When I send a POST request to "/pet/{petId}/uploadImage" with the image
    Then the response status code should be 200
    And the response should contain the uploaded image

  @positive
  Scenario: PET-UPLOAD-002 Upload an image with additional metadata
    Given a pet exists in the Petstore
    And a valid image file is available
    When I upload the image with additional metadata
    Then the response status code should be 200

  @positive
  Scenario: PET-UPLOAD-003 Upload an image without additional metadata
    Given a pet exists in the Petstore
    And a valid image file is available
    When I upload the image without additional metadata
    Then the response status code should be 200

  @only @negative
  Scenario: PET-UPLOAD-004 Upload an image for a nonexistent pet
    Given a valid image file is available
    When I upload the image for a nonexistent pet
    Then the response status code should be 404

  @only @negative
  Scenario: PET-UPLOAD-005 Upload without an image
    Given a pet exists in the Petstore
    When I upload an empty request body
    Then the response status code should be a client error

  @negative
  Scenario: PET-UPLOAD-006 Upload using an unsupported content type
    Given a pet exists in the Petstore
    When I upload a file using an unsupported content type
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: PET-UPLOAD-007 Upload an empty binary file
    Given a pet exists in the Petstore
    When I upload an empty binary file
    Then the API should return a valid API response
    And the API should not return a 5xx server error

  @edge
  Scenario: PET-UPLOAD-008 Upload a large image
    Given a pet exists in the Petstore
    And a large image file is available
    When I upload the large image
    Then the API should return a documented or implementation-defined response

  @security
  Scenario: PET-UPLOAD-009 Upload an image with malicious metadata
    Given a pet exists in the Petstore
    And a valid image file is available
    When I upload the image with path traversal-like metadata
    Then the API should not return a 5xx server error
