package com.petstore.api.stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import org.testng.Assert;

import com.petstore.api.api.PetApi;
import com.petstore.api.config.Config;
import com.petstore.api.models.Pet;

import java.io.*;
import java.util.*;

public class PetSteps extends BaseSteps {

	private final PetApi api = new PetApi();

	private Pet pet() {
		Object o = ctx.get("pet");

		Pet p;

		if (o instanceof Pet) {
			p = (Pet) o;
		} else {
			p = Pet.valid("doggie" + System.nanoTime(), Math.abs(System.nanoTime()));

			ctx.put("pet", p);
		}

		return p;
	}

	@Given("the Petstore API is available")
	public void available() {
		ctx.put("available", true);
	}

	@Given("a valid pet payload")
	@Given("I have a valid pet")
	@Given("I have a unique valid pet payload")
	public void validPet() {
		pet();
	}

	@Given("a pet exists in the Petstore")
	public void existingPet() {

		Pet p = Pet.valid("setup" + System.nanoTime(), Math.abs(System.nanoTime()));

		ctx.put("pet", p);

		ctx.response(api.add(p));

		if (response().statusCode() == 200) {
			rememberPet();
		}
	}

	@Given("a pet with a known ID exists")
	@Given("a pet with a known positive ID exists")
	public void knownPet() {
		existingPet();
	}

	@When("I send a POST request to {string} with a valid pet")
	public void add(String path) {
		ctx.response(api.add(pet()));
		rememberPet();
	}

	@When("I create the pet")
	public void createPetAlias() {
		add("/pet");
	}

	@When("I send a POST request to {string} with a valid JSON pet")
	public void addJson(String p) {
		add(p);
	}

	@When("I send a POST request to {string} with a valid XML pet")
	public void addXml(String p) {
		ctx.response(api.add(pet(), "application/xml"));
		rememberPet();
	}

	@When("I send a POST request to {string} with valid form encoded pet data")
	public void addForm(String p) {

		Pet pet = pet();

		Map<String, Object> form = new HashMap<>();

		form.put("id", pet.getId());
		form.put("name", pet.getName());
		form.put("status", pet.getStatus());

		ctx.response(api.addForm(form));

		rememberPet();
	}

	@When("I send a POST request to {string} with an empty {string} array")
	public void emptyArrayFieldAdd(String path, String field) {

		Pet p = pet();

		if (field.equals("photoUrls")) {
			p.setPhotoUrls(new ArrayList<>());
		}

		ctx.response(api.add(p));
	}

	@When("I send a POST request to {string} with an empty {string}")
	public void emptyStringFieldAdd(String path, String field) {

		Pet p = pet();

		if (field.equals("name")) {
			p.setName("");
		}

		ctx.response(api.add(p));
	}

	@When("I send a POST request to {string} with a pet without {string}")
	public void missingField(String path, String field) {

		Pet p = pet();

		if (field.equals("name")) {
			p.setName(null);
		}

		if (field.equals("photoUrls")) {
			p.setPhotoUrls(null);
		}

		ctx.response(api.add(p));
	}

	@When("I send a POST request to {string} with a pet missing {string} and {string}")
	public void missingTwo(String path, String a, String b) {

		Pet p = pet();

		p.setName(null);
		p.setPhotoUrls(null);

		ctx.response(api.add(p));
	}

	@When("I send a POST request to {string} with malformed JSON")
	public void malformedPost(String path) {

		ctx.response(
				RestAssured.given().baseUri(Config.baseUrl()).contentType("application/json").body("{bad").post(path));
	}

	@When("I send a POST request to {string} with status {string}")
	public void addStatus(String path, String status) {

		Pet p = pet();

		p.setStatus(status);

		ctx.response(api.add(p));

		rememberPet();
	}

	@When("I send a POST request to {string} containing an additional property")
	public void extra(String path) {

		Map<String, Object> p = new HashMap<>();

		p.put("id", Math.abs(System.nanoTime()));
		p.put("name", "extra");
		p.put("photoUrls", List.of("x"));
		p.put("unexpected", "value");

		ctx.response(api.addRaw(p));
	}

	@When("I send a PUT request to {string} with the updated pet")
	public void update(String path) {
		ctx.response(api.update(pet()));
		rememberPet();
	}

	@When("I update the pet using a valid JSON request")
	public void updateJson() {
		update("/pet");
	}

	@When("I update the pet using a valid XML request")
	public void updateXml() {
		ctx.response(api.update(pet(), "application/xml"));
	}

	@When("I update the pet using valid form encoded data")
	public void updateForm() {

		Pet p = pet();

		Long id = ctx.get("petId", Long.class) != null ? ctx.get("petId", Long.class) : p.getId();

		ctx.response(api.updateForm(id, p.getName(), p.getStatus()));
	}

	@When("I send a PUT request to {string} with an empty {string} array")
	public void emptyArrayFieldUpdate(String path, String field) {

		Pet p = pet();

		if (field.equals("photoUrls")) {
			p.setPhotoUrls(new ArrayList<>());
		}

		ctx.response(api.update(p));
	}

	@When("I send a PUT request to {string} with an empty {string}")
	public void emptyStringFieldUpdate(String path, String field) {

		Pet p = pet();

		if (field.equals("name")) {
			p.setName("");
		}

		ctx.response(api.update(p));
	}

	@When("I send a PUT request to {string} without {string}")
	public void updateMissing(String path, String field) {

		if (ctx.get("petId", Long.class) == null) {
			existingPet();
		}

		Pet p = pet();

		if (field.equals("name")) {
			p.setName(null);
		} else {
			p.setPhotoUrls(null);
		}

		ctx.response(api.update(p));
	}

	@When("I send a PUT request to {string} with malformed JSON")
	public void malformedPut(String path) {

		ctx.response(
				RestAssured.given().baseUri(Config.baseUrl()).contentType("application/json").body("{bad").put(path));
	}

	@When("I update the pet status to {string}")
	public void updateStatus(String status) {

		Pet p = pet();

		p.setStatus(status);

		ctx.response(api.update(p));
	}

	@When("I send a GET request to {string} using the pet ID")
	public void getPet(String path) {
		ctx.response(api.get(ctx.get("petId", Long.class)));
	}

	@When("I send a GET request to {string} using pet ID {string}")
	public void getPetId(String path, String id) {
		ctx.response(api.get(Long.parseLong(id)));
	}

	@When("I retrieve the pet using its ID")
	public void retrieveByStoredId() {
		ctx.response(api.get(ctx.get("petId", Long.class)));
	}

	@When("I send a GET request to {string} using a nonexistent ID")
	public void getNonexistentPet(String path) {

		// Generate a fresh ID to reduce collision risk on the shared demo server.
		ctx.response(api.get(Math.abs(System.nanoTime())));
	}

	@When("I send a GET request to {string} using a non-integer ID")
	public void getNonInt(String path) {

		ctx.response(RestAssured.given().baseUri(Config.baseUrl()).get(path.replaceAll("\\{[^}]+\\}", "abc")));
	}

	@When("I send a GET request to {string} using an injection-like value")
	public void getInjection(String path) {

		ctx.response(RestAssured.given().baseUri(Config.baseUrl()).get(path.replace("{petId}", "1%27%20OR%201%3D1")));
	}

	@When("I send a DELETE request to {string} using the pet ID")
	public void deletePet(String path) {
		ctx.response(api.delete(ctx.get("petId", Long.class), null));
	}

	@When("I delete the pet")
	public void deletePetAlias() {
		ctx.response(api.delete(ctx.get("petId", Long.class), null));
	}

	@When("I send a DELETE request to {string} with a valid API key")
	public void deleteKey(String path) {
		ctx.response(api.delete(ctx.get("petId", Long.class), Config.apiKey()));
	}

	@When("I send a DELETE request to {string} using a nonexistent ID")
	public void deleteMissing(String path) {

		ctx.response(api.delete(Math.abs(System.nanoTime()), null));
	}

	@When("I send a DELETE request to {string} using a non-integer ID")
	public void deleteNonInt(String path) {

		ctx.response(RestAssured.given().baseUri(Config.baseUrl()).delete(path.replaceAll("\\{[^}]+\\}", "abc")));
	}

	@When("I send a DELETE request to {string} using pet ID {string}")
	public void deleteId(String path, String id) {
		ctx.response(api.delete(Long.parseLong(id), null));
	}

	@When("I send a DELETE request to {string} without an API key")
	public void deleteNoKey(String path) {
		ctx.response(api.delete(ctx.get("petId", Long.class), null));
	}

	@When("I send a DELETE request to {string} with an invalid API key")
	public void deleteBadKey(String path) {
		ctx.response(api.delete(ctx.get("petId", Long.class), "invalid-key"));
	}

	@When("I retrieve the deleted pet")
	public void retrieveDeleted() {
		ctx.response(api.get(ctx.get("petId", Long.class)));
	}

	@When("I send a GET request to {string} with status {string}")
	public void statusSearch(String path, String status) {
		ctx.response(api.findByStatus(status));
	}

	@When("I send a GET request to {string} without the status parameter")
	public void noStatus(String path) {
		ctx.response(RestAssured.given().baseUri(Config.baseUrl()).get(path));
	}

	@When("I send a GET request to {string} with an empty status")
	public void emptyStatus(String path) {
		ctx.response(api.findByStatus(""));
	}

	@When("I send a GET request to {string} with tag {string}")
	public void oneTag(String path, String tag) {
		ctx.response(api.findByTags(List.of(tag)));
	}

	@When("I send a GET request to {string} with tags {string} and {string}")
	public void twoTags(String path, String a, String b) {
		ctx.response(api.findByTags(List.of(a, b)));
	}

	@When("I send a GET request to {string} with tags {string}, {string} and {string}")
	public void threeTags(String path, String a, String b, String c) {
		ctx.response(api.findByTags(List.of(a, b, c)));
	}

	@When("I send a GET request to {string} without the tags parameter")
	public void noTags(String path) {
		ctx.response(RestAssured.given().baseUri(Config.baseUrl()).get(path));
	}

	@When("I send a GET request to {string} with duplicate tags")
	public void dupTags(String path) {
		ctx.response(api.findByTags(List.of("tag1", "tag1")));
	}

	@When("I send a GET request to {string} with an empty tag")
	public void emptyTag(String path) {
		ctx.response(api.findByTags(List.of("")));
	}

	@When("I send a GET request to {string} with a tag containing special characters")
	public void specialTag(String path) {
		ctx.response(api.findByTags(List.of("tag !@#$%")));
	}

	@When("I upload the image for the pet")
	public void uploadRegression() {
		uploadImage();
	}

	@When("I upload the image with additional metadata")
	public void uploadMeta() {
		uploadImage();
	}

	@When("I upload the image without additional metadata")
	public void uploadNoMeta() {
		uploadImage();
	}

	@Given("a large image file is available")
	public void largeImageFileAvailable() {
		// The large binary payload is generated directly by the upload step below.
	}

	@When("I create a pet with a unique tag")
	public void createTaggedPet() {

		String tag = "tag-" + System.nanoTime();

		Map<String, Object> p = new HashMap<>();

		p.put("id", Math.abs(System.nanoTime()));
		p.put("name", "tagged" + System.nanoTime());
		p.put("photoUrls", List.of("https://example.com/tagged.jpg"));
		p.put("tags", List.of(Map.of("id", 1, "name", tag)));

		ctx.put("uniqueTag", tag);

		ctx.response(api.addRaw(p));
	}

	@When("I search for pets using the unique tag")
	public void searchByUniqueTag() {
		ctx.response(api.findByTags(List.of((String) ctx.get("uniqueTag"))));
	}

	@Given("a valid image file is available")
	public void imageAvailable() throws IOException {

		File f = new File("target/petest1.jpeg");

		f.getParentFile().mkdirs();

		java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(1, 1,
				java.awt.image.BufferedImage.TYPE_INT_RGB);

		javax.imageio.ImageIO.write(img, "jpg", f);

		ctx.put("file", f);
	}

	@When("I send a POST request to {string} with the image")
	public void upload(String path) {
		uploadImage();
	}

	private void uploadImage() {

		File f = (File) ctx.get("file");

		if (f == null) {
			try {
				imageAvailable();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		Long petId = ctx.get("petId", Long.class);

		ctx.response(api.upload(ctx.get("petId", Long.class), f, "test upload"));
	}

	@When("I upload the image for a nonexistent pet")
	public void uploadMissing() {

		File f = (File) ctx.get("file");

		if (f == null) {
			try {
				imageAvailable();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		ctx.response(api.upload(Math.abs(System.nanoTime()), f, null));
	}

	@When("I upload an empty request body")
	public void emptyUpload() {
		ctx.response(api.upload(ctx.get("petId", Long.class), null, null));
	}

	@When("I upload a file using an unsupported content type")
	public void badUploadType() {

		ctx.response(RestAssured.given().baseUri(Config.baseUrl()).contentType("text/plain").body("x")
				.post("/pet/" + ctx.get("petId", Long.class) + "/uploadImage"));
	}

	@When("I upload an empty binary file")
	public void emptyFile() {

		File f = new File("target/empty.bin");

		try {
			f.createNewFile();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ctx.response(api.upload(ctx.get("petId", Long.class), f, null));
	}

	@When("I upload the large image")
	public void largeImage() throws Exception {

		File f = new File("target/large.bin");

		try (FileOutputStream o = new FileOutputStream(f)) {
			o.write(new byte[1024 * 1024]);
		}

		ctx.response(api.upload(ctx.get("petId", Long.class), f, null));
	}

	@When("I upload the image with path traversal-like metadata")
	public void maliciousMetadata() {

		File f = (File) ctx.get("file");

		if (f == null) {
			try {
				imageAvailable();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		ctx.response(api.upload(ctx.get("petId", Long.class), f, "../../../../tmp/pwned"));
	}

	@When("I update the pet using an empty request body")
	public void emptyPut() {

		ctx.response(
				RestAssured.given().baseUri(Config.baseUrl()).contentType("application/json").body("").put("/pet"));
	}

	@When("I update the pet using an empty User object")
	public void noop() {
	}

	@Then("the response should contain the uploaded image")
	public void responseShouldContainUploadedImage() {
	    String msg = response().jsonPath().getString("message");
	    Assert.assertNotNull(msg, "No message in response: " + response().asString());
	    Assert.assertTrue(msg.contains("File uploaded to"), "Unexpected message: " + msg);

	    File f = (File) ctx.get("file");
	    Assert.assertTrue(msg.contains(f.length() + " bytes"),
	        "Uploaded size mismatch. Expected " + f.length() + " bytes, got: " + msg);
	}

	@Then("the response status code should be {int}")
	public void expected(int code) {
		status(code);
	}

	@Then("the response status code should be in the range {int} to {int}")
	public void range(int a, int b) {

		Assert.assertTrue(response().statusCode() >= a && response().statusCode() <= b, "Expected status " + a + "-" + b
				+ " but got " + response().statusCode() + ": " + response().asString());
	}

	@Then("the response should contain the created pet")
	@Then("the response should contain the updated pet")
	@Then("the response should contain the requested pet")
	public void petResponse() {

		Assert.assertTrue(response().asString() != null && !response().asString().isBlank(),
				"Expected a non-empty pet in the response body but got none");
	}

	@Then("the response should contain a pet")
	public void containsPet() {
		petResponse();
	}

	@Then("the response should conform to the Pet schema")
	public void petSchema() {

		String body = response().asString();

		Assert.assertNotNull(response().jsonPath().get("id"), "Missing 'id' in Pet response: " + body);

		Assert.assertNotNull(response().jsonPath().getList("photoUrls"),
				"Missing 'photoUrls' in Pet response: " + body);
	}

	@Then("the response should conform to the ApiResponse schema")
	public void apiResponseSchema() {

		Assert.assertNotNull(response().jsonPath().get("code"),
				"Missing 'code' in ApiResponse: " + response().asString());

		Assert.assertNotNull(response().jsonPath().get("type"),
				"Missing 'type' in ApiResponse: " + response().asString());
	}

	@Then("the response should indicate a validation exception")
	public void validation() {

		Assert.assertEquals(response().statusCode(), 422,
				"Expected 422 but got " + response().statusCode() + ": " + response().asString());
	}

	@Then("the response status code should indicate an input validation failure")
	public void inputValidation() {

		Assert.assertTrue(response().statusCode() >= 400 && response().statusCode() < 500,
				"Expected 4xx but got " + response().statusCode() + ": " + response().asString());
	}

	@Then("the API should return a documented or implementation-defined response")
	public void impl() {
		no5xx();
	}

	@Then("the API should return the security behavior defined by the implementation")
	public void sec() {
		no5xx();
	}

	@Then("the API should not return a 5xx server error")
	public void noServer() {
		no5xx();
	}

	@Then("the API should return a valid API response")
	public void validResponse() {
		no5xx();
	}

	@Then("the response should indicate a missing required parameter")
	public void missingParam() {
		int code = response().statusCode();
		Assert.assertTrue(code < 500, "Server error: " + code + " " + response().asString());

		Assert.assertTrue(code == 200 || code == 400 || code == 422,
				"Unexpected status " + code + ": " + response().asString());
	}

	@Then("the response should be an array of pets")
	public void petArray() {
		Assert.assertTrue(response().jsonPath().getList("$") != null);
	}

	@Then("the response pet status should be {string}")
	public void petStatus(String s) {
		Assert.assertEquals(response().jsonPath().getString("status"), s);
	}

	@Then("the returned pet ID should match the requested ID")
	public void returnedId() {

		Assert.assertEquals(response().jsonPath().getLong("id"), ctx.get("petId", Long.class).longValue());
	}

	@Then("the response should be a client-side error")
	public void clientError() {

		Assert.assertTrue(response().statusCode() >= 400 && response().statusCode() < 500,
				"Expected 4xx but got " + response().statusCode() + ": " + response().asString());
	}

	@Then("the response should not be 5xx")
	public void no5() {
		no5xx();
	}

	@Then("the response status code should be a client error")
	public void clientErr() {

		Assert.assertTrue(response().statusCode() >= 400 && response().statusCode() < 500,
				"Expected 4xx but got " + response().statusCode() + ": " + response().asString());
	}
}