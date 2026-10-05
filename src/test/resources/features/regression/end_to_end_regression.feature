Feature: Petstore End-to-End Regression
  As an API QA engineer
  I want to validate complete Petstore workflows
  So that changes to one API operation do not break related operations

  @smoke @regression
  Scenario: REG-001 Add a pet and retrieve it
    Given the Petstore API is available
    And I have a unique valid pet payload
    When I send a POST request to "/pet" with a valid pet
    Then the response status code should be 200
    When I send a GET request to "/pet/{petId}" using the pet ID
    Then the response status code should be 200
    And the returned pet ID should match the requested ID

  @regression
  Scenario: REG-002 Add and update a pet
    Given the Petstore API is available
    And I have a valid pet
    When I send a POST request to "/pet" with a valid pet
    Then the response status code should be 200
    When I send a PUT request to "/pet" with the updated pet
    Then the response status code should be 200
    When I send a GET request to "/pet/{petId}" using the pet ID
    Then the response status code should be 200
    And the response should contain the updated pet

  @regression
  Scenario: REG-003 Add, update status and find a pet by status
    Given the Petstore API is available
    And I have a valid pet
    When I create the pet
    Then the response status code should be 200
    When I update the pet status to "pending"
    Then the response status code should be 200
    When I send a GET request to "/pet/findByStatus" with status "pending"
    Then the response status code should be 200
    And the response should be an array of pets

  @regression
  Scenario: REG-004 Add a tagged pet and find it by tag
    Given the Petstore API is available
    When I create a pet with a unique tag
    Then the response status code should be 200
    When I search for pets using the unique tag
    Then the response status code should be 200
    And the response should be an array of pets

  @regression
  Scenario: REG-005 Add a pet and upload an image
    Given the Petstore API is available
    And I have a valid pet
    And a valid image file is available
    When I create the pet
    Then the response status code should be 200
    When I upload the image for the pet
    Then the response status code should be 200


  @regression
  Scenario: REG-006 Add and delete a pet
    Given the Petstore API is available
    And I have a valid pet
    When I create the pet
    Then the response status code should be 200
    When I delete the pet
    Then the response status code should be 200
    When I retrieve the deleted pet
    Then the response status code should be 404

  @regression @security
  Scenario: REG-007 Retrieve inventory using an API key
    Given a valid API key is available
    When I retrieve the store inventory
    Then the response status code should be 200
    And the inventory response should contain integer quantities

  @regression
  Scenario: REG-008 Create and retrieve an order
    When I send a POST request to "/store/order" with a valid order
    Then the response status code should be 200
    When I retrieve the order using GET "/store/order/{orderId}"
    Then the response should follow implementation-defined behavior

  @regression
  Scenario: REG-009 Validate order GET behavior for existing and nonexistent orders
    When I send a POST request to "/store/order" with a valid order
    Then the response status code should be 200
    When I retrieve the order using GET "/store/order/{orderId}"
    Then the response status code should be 200
    When I send a GET request to "/store/order/{orderId}" using a nonexistent order ID
    Then the response status code should be 404

@regression
Scenario: REG-010 Validate order DELETE behavior for existing and nonexistent orders
  When I send a POST request to "/store/order" with a valid order
  Then the response status code should be 200
  When I delete the order
  Then the response status code should be 200
  When I send a DELETE request to "/store/order/{orderId}" using a nonexistent order ID
  Then the response status code should be 404

  @smoke @regression @security
  Scenario: REG-011 Login and logout
    Given valid user credentials are available
    When I login using the credentials
    Then the response status code should be 200
    And the response should contain the login result
    When I logout
    Then the response status code should be 200

  @regression
  Scenario: REG-012 Create and retrieve a user
    Given I have a unique valid user
    When I send a POST request to "/user" with a valid user
    Then the response status code should be 200
    When I retrieve the user by username
    Then the response status code should be 200
    And the returned username should match the requested username

  @regression
  Scenario: REG-013 Create and update a user
    Given I have a unique valid user
    When I create the user
    Then the response status code should be 200
    When I update one property of the user
    Then the response status code should be 200
    When I retrieve the user
    Then the response status code should be 200

  @regression
  Scenario: REG-014 Create and delete a user
    Given I have a unique valid user
    When I create the user
    Then the response status code should be 200
    When I delete the user
    Then the response status code should be 200
    When I retrieve the deleted user
    Then the response status code should be 404

  @regression
  Scenario: REG-015 Create multiple users
    Given I have multiple valid users
    When I send a POST request to "/user/createWithList" containing multiple valid users
    Then the response status code should be 200

  @regression @validation
  Scenario: REG-016 Validate Pet status enumeration
    Given the Petstore API is available
    When I send a POST request to "/pet" with status "available"
    Then the response status code should be 200
    When I send a POST request to "/pet" with status "pending"
    Then the response status code should be 200
    When I send a POST request to "/pet" with status "sold"
    Then the response status code should be 200
    When I send a POST request to "/pet" with status "invalid-status"
    Then the response status code should be 200

  @regression @validation
  Scenario: REG-017 Validate Order status enumeration
    When I create an order with status "placed"
    Then the response status code should be 200
    When I create an order with status "approved"
    Then the response status code should be 200
    When I create an order with status "delivered"
    Then the response status code should be 200
    When I create an order with status "invalid-status"
    Then the response status code should be 200

  @regression @security
  Scenario: REG-018 Verify protected inventory access without credentials
    When I send a GET request to "/store/inventory" without an API key
    Then the inventory request should return the implementation-defined security response

  @regression @security
  Scenario: REG-019 Verify protected inventory access with invalid credentials
    When I send a GET request to "/store/inventory" with an invalid API key
    Then the inventory request should return the implementation-defined security response

  @regression @negative
  Scenario: REG-020 Verify malformed Pet request does not cause a server error
    When I send a POST request to "/pet" with malformed JSON
    Then the response should be a client-side error
    And the response should not be 5xx

  @regression @negative
  Scenario: REG-021 Verify invalid User path input does not cause a server error
    When I retrieve a user using an injection-like username
    Then the API should not return a 5xx server error

  @regression @negative
  Scenario: REG-022 Verify invalid Pet ID does not cause a server error
    When I send a GET request to "/pet/{petId}" using a non-integer ID
    Then the response status code should be 404

  @regression
  Scenario: REG-023 Verify login response headers
    Given valid user credentials are available
    When I login
    Then the response status code should be 200
    And the response should contain the "X-Rate-Limit" header
    And the response should contain the "X-Expires-After" header
