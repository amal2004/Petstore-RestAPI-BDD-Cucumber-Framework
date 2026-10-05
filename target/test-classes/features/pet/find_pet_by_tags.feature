Feature: Find Pets By Tags
  As an API client
  I want to find pets by tags
  So that I can retrieve pets associated with specific tags

  @smoke @regression
  Scenario: PET-TAGS-001 Find pets using one tag
    When I send a GET request to "/pet/findByTags" with tag "tag1"
    Then the response status code should be 200
    And the response should be an array of pets

  @positive
  Scenario: PET-TAGS-002 Find pets using multiple tags
    When I send a GET request to "/pet/findByTags" with tags "tag1" and "tag2"
    Then the response status code should be 200
    And the response should be an array of pets

  @positive
  Scenario: PET-TAGS-003 Find pets using three tags
    When I send a GET request to "/pet/findByTags" with tags "tag1", "tag2" and "tag3"
    Then the response status code should be 200

  @negative
  Scenario: PET-TAGS-004 Find pets without tags
    When I send a GET request to "/pet/findByTags" without the tags parameter
    Then the response should indicate a missing required parameter

  @edge
  Scenario: PET-TAGS-005 Find pets using duplicate tags
    When I send a GET request to "/pet/findByTags" with duplicate tags
    Then the API should return a valid API response

  @edge
  Scenario: PET-TAGS-006 Find pets using an empty tag
    When I send a GET request to "/pet/findByTags" with an empty tag
    Then the API should return a documented or implementation-defined response

  @edge
  Scenario: PET-TAGS-007 Find pets using a tag containing special characters
    When I send a GET request to "/pet/findByTags" with a tag containing special characters
    Then the API should return a valid API response
    And the API should not return a 5xx server error
