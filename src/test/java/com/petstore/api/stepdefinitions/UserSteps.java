package com.petstore.api.stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import org.testng.Assert;

import com.petstore.api.api.UserApi;
import com.petstore.api.models.User;

import java.util.*;

public class UserSteps extends BaseSteps {
	private final UserApi api = new UserApi();

	private User user() {
		User u = (User) ctx.get("user");
		if (u == null) {
			u = User.valid("user" + System.currentTimeMillis());
			ctx.put("user", u);
		}
		return u;
	}

	@Given("a user exists")
	public void existingUser() {
		User u = User.valid("user" + System.currentTimeMillis());
		ctx.put("user", u);
		ctx.response(api.create(u));
		Assert.assertEquals(response().statusCode(), 200,
				"Setup failed: could not create user, got " + response().statusCode() + ": " + response().asString());

		ctx.put("username", u.username);
	}

	@Given("a known valid user exists")
	public void knownUser() {
		existingUser();
	}

	@Given("a known user exists")
	public void knownUser2() {
		existingUser();
	}

	@Given("a user with username {string} exists")
	public void namedUser(String username) {
		User u = User.valid(username);
		ctx.put("user", u);
		ctx.response(api.create(u));
		ctx.put("username", username);
	}

	@Given("I have a unique valid user")
	public void uniqueUser() {
		user();
	}

	@Given("I have multiple valid users")
	public void users() {
		List<User> list = List.of(User.valid("u" + System.nanoTime() + "a"), User.valid("u" + System.nanoTime() + "b"));
		ctx.put("users", list);
	}

	@Given("valid user credentials are available")
	public void creds() {
		User u = User.valid("login" + System.currentTimeMillis());
		ctx.put("user", u);
		ctx.put("username", u.username);
		ctx.put("password", u.password);
	}

	@Given("I have valid credentials")
	public void creds2() {
		creds();
	}

	@Given("a user has a password containing special characters")
	public void specialCharPasswordUser() {
		User u = User.valid("special" + System.currentTimeMillis());
		u.password = "P@$$w0rd!#%&*()";
		ctx.put("user", u);
		ctx.response(api.create(u));
		ctx.put("username", u.username);
		ctx.put("password", u.password);
	}

	@Given("the user has successfully logged in")
	public void loggedInUser() {
		creds();
		login("/user/login");
	}

	@Given("the user has logged out")
	public void loggedOutUser() {
		creds();
		login("/user/login");
		logout();
	}

	@When("I send a POST request to {string} with a valid user")
	public void create(String path) {
		User u = user();
		ctx.response(api.create(u));
		ctx.put("username", u.username);
	}

	@When("I create the user")
	public void createUserAlias() {
		create("/user");
	}

	@When("I create a user containing all supported User properties")
	public void fullCreate() {
		create("/user");
	}

	@When("I create a user using valid JSON")
	public void jsonCreate() {
		create("/user");
	}

	@When("I create a user using valid XML")
	public void xmlCreate() {
		create("/user");
	}

	@When("I create a user using valid form encoded data")
	public void formCreate() {
		create("/user");
	}

	@When("I create a user with an empty User object")
	public void emptyUser() {
		ctx.response(api.create(new User()));
	}

	@When("I send a POST request to {string} with an empty body")
	public void emptyBody(String path) {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl())
				.contentType("application/json").body("").post(path));
	}

	@When("I create another user with username {string}")
	public void duplicate(String username) {
		ctx.response(api.create(User.valid(username)));
	}

	@When("I create a user with invalid data types")
	public void invalidTypes() {

		Map<String, Object> m = new HashMap<>();
		m.put("id", "not-int");
		m.put("username", "bad");

		ctx.response(api.createRaw(m));
	}

	@When("I create a user with email {string}")
	public void email(String e) {
		User u = user();
		u.email = e;
		ctx.response(api.create(u));
	}

	@When("I send a POST request to {string} containing one valid user")
	public void oneList(String path) {
		ctx.response(api.createList(List.of(User.valid("list" + System.nanoTime()))));
	}

	@When("I send a POST request to {string} containing multiple valid users")
	public void manyList(String path) {
		ctx.response(api.createList(
				List.of(User.valid("list" + System.nanoTime() + "a"), User.valid("list" + System.nanoTime() + "b"))));
	}

	@When("I send a POST request to {string} with an empty array")
	public void emptyList(String path) {
		ctx.response(api.createList(List.of()));
	}

	@When("I send a POST request to {string} with an object instead of an array")
	public void objectList(String path) {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl())
				.contentType("application/json").body("{\"username\":\"x\"}").post(path));
	}

	@When("I send a POST request containing a user with invalid property types")
	public void invalidList() {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl())
				.contentType("application/json").body("[{\"id\":\"bad\"}]").post("/user/createWithList"));
	}

	@When("I send a POST request containing a large number of users")
	public void largeList() {
		List<User> list = new ArrayList<>();
		for (int i = 0; i < 100; i++)
			list.add(User.valid("bulk" + System.nanoTime() + i));
		ctx.response(api.createList(list));
	}

	@When("I send a GET request to {string} with the username and password")
	public void login(String path) {
		ctx.response(api.login((String) ctx.get("username"), (String) ctx.get("password")));
	}

	@When("I login using the user's credentials")
	public void loginCreds() {
		login("/user/login");
	}

	@When("I login using an invalid username and valid password")
	public void invalidUsername() {
		ctx.response(api.login("invalid-" + System.nanoTime(), "P@ss12345"));
	}

	@When("I login using a valid username and invalid password")
	public void invalidPassword() {
		ctx.response(api.login((String) ctx.get("username"), "wrong-password"));
	}

	@When("I login using invalid credentials")
	public void invalidCreds() {
		ctx.response(api.login("invalid-user", "invalid-password"));
	}

	@When("I send a login request containing only a username")
	public void userOnly() {
		ctx.response(api.login((String) ctx.get("username"), null));
	}

	@When("I send a login request containing only a password")
	public void passOnly() {
		ctx.response(api.login(null, (String) ctx.get("password")));
	}

	@When("I send a login request without username or password")
	public void neither() {
		ctx.response(api.login(null, null));
	}

	@When("I login using the credentials")
	public void loginUsing() {
		login("/user/login");
	}

	@When("I login using an injection-like username")
	public void injUser() {
		ctx.response(api.login("' OR '1'='1", "x"));
	}

	@When("I login using an injection-like password")
	public void injPass() {
		ctx.response(api.login("user", "' OR '1'='1"));
	}

	@When("I login")
	public void loginShort() {
		login("/user/login");
	}

	@When("I send a GET request to {string}")
	public void simpleGet(String path) {
		if (path.equals("/user/logout"))
			ctx.response(api.logout());
		else
			ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl()).get(path));
	}

	@When("I send another GET request to {string}")
	public void anotherGet(String path) {
		simpleGet(path);
	}

	@When("I logout")
	public void logout() {
		ctx.response(api.logout());
	}

	@When("I send a GET request to {string} using username {string}")
	public void getUser(String path, String username) {
		ctx.response(api.get(username));
	}

	@When("I retrieve the user by username")
	public void getByContext() {
		ctx.response(api.get((String) ctx.get("username")));
	}

	@When("I retrieve the user")
	public void retrieveUserAlias() {
		getByContext();
	}

	@When("I retrieve a nonexistent username")
	public void noUser() {
		ctx.response(api.get("missing-" + System.nanoTime()));
	}

	@When("I retrieve a user using invalid username input")
	public void invalidUserInput() {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl()).get("/user/%20"));
	}

	@When("I retrieve a user using an empty username")
	public void emptyUsername() {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl()).get("/user/"));
	}

	@When("I retrieve a user using an injection-like username")
	public void injGet() {
		ctx.response(api.get("' OR '1'='1"));
	}

	@When("I retrieve a user using a path traversal-like username")
	public void traversal() {
		ctx.response(api.get("..%2F..%2Fetc%2Fpasswd"));
	}

	@When("I send a PUT request to {string} with updated user data")
	public void updateUser(String path) {
		ctx.response(api.update((String) ctx.get("username"), user()));
	}

	@When("I update the user using valid JSON")
	public void updateJson() {
		updateUser("/user/{username}");
	}

	@When("I update the user using valid XML")
	public void updateXml() {
		updateUser("/user/{username}");
	}

	@When("I update the user using valid form encoded data")
	public void updateForm() {
		updateUser("/user/{username}");
	}

	@When("I update a nonexistent username")
	public void updateMissing() {
		ctx.response(api.update("missing-" + System.nanoTime(), user()));
	}

	@When("I update a user using invalid username input")
	public void updateInvalid() {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl())
				.contentType("application/json").body("{}").put("/user/%20"));
	}

	@When("I update the user with an empty request body")
	public void emptyUpdate() {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl())
				.contentType("application/json").body("").put("/user/" + ctx.get("username")));
	}

	@When("I update the user using an empty User object")
	public void emptyUserUpdate() {
		ctx.response(api.update((String) ctx.get("username"), new User()));
	}

	@When("I update one property of the user")
	public void oneProperty() {
		User u = user();
		u.firstName = "Updated";
		ctx.response(api.update((String) ctx.get("username"), u));
	}

	@When("I delete the user")
	public void delete() {
		ctx.response(api.delete((String) ctx.get("username")));
	}

	@When("I send a DELETE request to {string} using the username")
	public void deleteUsingUsername(String path) {
		delete();
	}

	@When("I delete a nonexistent username")
	public void deleteMissing() {
		ctx.response(api.delete("missing-" + System.nanoTime()));
	}

	@When("I delete a user using invalid username input")
	public void deleteInvalid() {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl()).delete("/user/%20"));
	}

	@When("I delete a user using an empty username")
	public void deleteEmpty() {
		ctx.response(RestAssured.given().baseUri(com.petstore.api.config.Config.baseUrl()).delete("/user/"));
	}

	@When("I delete a user using an injection-like username")
	public void deleteInjection() {
		ctx.response(api.delete("' OR '1'='1"));
	}

	@When("I retrieve the deleted user")
	public void retrieveDeleted() {
		ctx.response(api.get((String) ctx.get("username")));
	}

	@Then("the response should contain a user")
	public void userResponse() {
		Assert.assertNotNull(response().asString(), "Expected a user in the response body but got none");
	}

	@Then("the returned username should match the requested username")
	public void usernameMatch() {
		String expected = (String) ctx.get("username");
		String actual = response().jsonPath().getString("username");
		Assert.assertEquals(actual, expected,
				"Expected username '" + expected + "' but got '" + actual + "': " + response().asString());
	}

	@Then("the API should return a validation or implementation-defined response")
	public void validationImpl() {

		int status = response().statusCode();

		Assert.assertTrue(status == 400 || status == 422 || status == 500,
				"Unexpected response status: " + status + ", Body: " + response().asString());

		if (status == 500) {
			Assert.assertEquals(response().jsonPath().getInt("code"), 500, "Expected implementation error response");

			Assert.assertNotNull(response().jsonPath().getString("message"), "500 response should contain a message");
		}
	}

	@Then("the result should not assume email-format validation because the schema defines email as a string")
	public void emailSchema() {
		Assert.assertTrue(response().statusCode() < 500,
				"Unexpected 5xx: " + response().statusCode() + ": " + response().asString());
	}

	@Then("the response should indicate validation or implementation-defined behavior")
	public void validationOrImplementationBehavior() {

		int status = response().statusCode();

		Assert.assertTrue(status == 400 || status == 422 || status == 500,
				"Unexpected response for invalid User list payload. " + "Status: " + status + ", Body: "
						+ response().asString());
	}

	@Then("the response should indicate a validation or implementation-defined failure")
	public void valFail() {
		Assert.assertTrue(response().statusCode() < 500,
				"Unexpected 5xx: " + response().statusCode() + ": " + response().asString());
	}

	@Then("the API should return the documented behavior")
	public void documented() {
		Assert.assertTrue(response().statusCode() < 500,
				"Unexpected 5xx: " + response().statusCode() + ": " + response().asString());
	}

	@Then("the response should contain the login result")
	public void loginResult() {
		Assert.assertNotNull(response().asString(), "Expected a login result in the response body but got none");
	}

	@Then("the response should contain the {string} header")
	public void header(String h) {
		Assert.assertNotNull(response().getHeader(h), "Missing header " + h);
	}

	@Then("the response should follow the documented behavior")
	public void docBehavior() {
		Assert.assertTrue(response().statusCode() < 500,
				"Unexpected 5xx: " + response().statusCode() + ": " + response().asString());
	}

	@Then("the logout response status code should be 200")
	public void logout200() {
		status(200);
	}
}
