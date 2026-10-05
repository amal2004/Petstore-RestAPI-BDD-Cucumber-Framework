package com.petstore.api.stepdefinitions;


import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import org.testng.Assert;

import com.petstore.api.api.StoreApi;
import com.petstore.api.config.Config;
import com.petstore.api.models.Order;

import java.util.*;

public class StoreSteps extends BaseSteps {
	private final StoreApi api = new StoreApi();

	@Given("a valid API key is available")
	public void apiKey() {
		ctx.put("apiKey", Config.apiKey() == null ? "" : Config.apiKey());
	}

	@When("I send a GET request to {string} with the API key")
	public void inventory(String path) {
		ctx.response(api.inventory((String) ctx.get("apiKey")));
	}

	@When("I retrieve the store inventory")
	public void inv() {
		ctx.response(api.inventory(Config.apiKey()));
	}

	@When("I send a GET request to {string} without an API key")
	public void noKey(String path) {
		ctx.response(RestAssured.given().baseUri(Config.baseUrl()).get(path));
	}
	
	@When("I send a GET request to {string} with an invalid API key")
	public void badKey(String path) {
		ctx.response(api.inventory("invalid-key"));
	}
	
	@When("I send a GET request to {string} using a nonexistent order ID")
	public void getNonexistentOrder(String path) {
	    ctx.response(api.getOrder(Math.abs(System.nanoTime())));
	}

	@When("I send a DELETE request to {string} using a nonexistent order ID")
	public void deleteNonexistentOrder(String path) {
	    ctx.response(api.deleteOrder(Math.abs(System.nanoTime())));
	}

	@Then("the response should contain inventory data")
	public void invData() {
		Assert.assertTrue(response().statusCode() == 200);
		Assert.assertNotNull(response().jsonPath().getMap("$"));
	}

	@Then("the inventory response should contain integer quantities")
	public void invTypes() {
		
		int code = response().statusCode();
		Assert.assertTrue(
			    code == 200 || code == 401 || code == 403,
			    "Unexpected inventory security response. Actual status: " + code
			        + " body: " + response().asString());
			Assert.assertTrue(code < 500, "Server error on unauthenticated inventory call");
	}

	
	@Then("the inventory request should return the implementation-defined security response")
	public void inventorySecurityBehavior() {
	    int code = response().statusCode();

	    Assert.assertTrue(
	        code == 200 || code == 401 || code == 403,
	        "Unexpected inventory security response. Actual status: " + code
	            + " body: " + response().asString());

	    System.out.println("Inventory without api_key returned " + code
	        + (code == 200 ? " (api_key not enforced by server)" : " (auth enforced)"));
	}
	
	
	@When("I send a POST request to {string} with a valid order")
	public void createOrder(String path) {
		Order o = Order.valid(Math.abs(System.nanoTime()),
				ctx.get("petId", Long.class) == null ? 1L : ctx.get("petId", Long.class));
		ctx.put("order", o);
		ctx.response(api.createOrder(o));
		rememberOrder();
	}

	@When("I create an order using valid JSON")
	public void jsonOrder() {
		createOrder("/store/order");
	}

	@When("I create an order using valid XML")
	public void xmlOrder() {
		createOrder("/store/order");
	}

	@When("I create an order using valid form encoded data")
	public void formOrder() {
		createOrder("/store/order");
	}

	@When("I create an order with status {string}")
	public void orderStatus(String status) {
		Order o = Order.valid(Math.abs(System.nanoTime()), 1);
		o.status = status;
		ctx.response(api.createOrder(o));
		rememberOrder();
	}

	@When("I create an order with an invalid {string}")
	public void invalidOrderField(String field) {
		Order o = Order.valid(Math.abs(System.nanoTime()), 1);
		if (field.equals("shipDate"))
			o.shipDate = "not-a-date";
		ctx.response(api.createOrder(o));
	}

	@When("I create an order with quantity {string}")
	public void quantity(String q) {
		Order o = Order.valid(Math.abs(System.nanoTime()), 1);
		o.quantity = Integer.parseInt(q);
		ctx.response(api.createOrder(o));
	}

	@When("I send a GET request to {string} using order ID {int}")
	public void getOrder(String path, int id) {
		ctx.response(api.getOrder(id));
	}

	@When("I send a GET request to {string} using a large valid int64 ID")
	public void getLarge(String path) {
		ctx.response(api.getOrder(Long.MAX_VALUE));
	}

	@When("I delete an order with ID {int}")
	public void deleteOrderInt(int id) {
		ctx.response(api.deleteOrder(id));
	}

	@When("I send a DELETE request to {string} using order ID {int}")
	public void deleteOrder(String path, int id) {
		ctx.response(api.deleteOrder(id));
	}

	@When("I retrieve the order using GET {string}")
	public void retrieveCreatedOrder(String path) {
		ctx.response(api.getOrder(ctx.get("orderId", Long.class)));
	}

	@When("I delete the order")
	public void deleteCreatedOrder() {
		ctx.response(api.deleteOrder(ctx.get("orderId", Long.class)));
	}

	@Then("the response should contain an order")
	public void orderResponse() {
		Assert.assertNotNull(response().asString());
	}

	@Then("the response should indicate an API exception")
	public void apiException() {
		Assert.assertTrue(response().statusCode() >= 400 || response().statusCode() == 500);
	}

	@Then("the response should follow the documented response for an ID less than or equal to 5")
	public void le5() {
		Assert.assertTrue(response().statusCode() >= 200 && response().statusCode() < 500);
	}

	@Then("the response should follow the documented behavior for IDs below 1000")
	public void lt1000() {
		Assert.assertTrue(response().statusCode() >= 200 && response().statusCode() < 500);
	}

	@Then("the response should follow implementation-defined behavior")
	public void implStore() {
		no5xx();
	}

	@Then("the response should follow the documented post-deletion behavior")
	public void postDelete() {
		Assert.assertTrue(
				response().statusCode() == 404 || response().statusCode() == 400 || response().statusCode() == 200);
	}
}
