package com.petstore.api.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petstore.api.hooks.Hooks;
import com.petstore.api.utils.ScenarioContext;

import io.restassured.response.Response;
import org.testng.Assert;

public abstract class BaseSteps {
	protected final ScenarioContext ctx = Hooks.ctx();
	protected final ObjectMapper mapper = new ObjectMapper();

	protected Response response() {
		return ctx.response();
	}

	protected void status(int code) {
		Assert.assertEquals(response().statusCode(), code, "Unexpected response: " + response().asString());
	}

	protected void no5xx() {
		Assert.assertTrue(response().statusCode() < 500, "Unexpected 5xx: " + response().statusCode());
	}

	protected long idFromResponse() {
		Long id = response().jsonPath().getLong("id");
		Assert.assertNotNull(id, "Response did not contain id: " + response().asString());
		return id;
	}


	protected boolean isJsonSuccess() {
		if (response() == null || response().statusCode() < 200 || response().statusCode() >= 300)
			return false;
		String body = response().asString();
		if (body == null)
			return false;
		body = body.trim();
		return body.startsWith("{") || body.startsWith("[");
	}

	protected void rememberPet() {
		if (!isJsonSuccess())
			return; // let the following "status code" step report the real status
		Long id = response().jsonPath().getLong("id");
		if (id != null)
			ctx.put("petId", id);

		ctx.put("petJson", response().asString());
	}

	protected void rememberOrder() {
		if (!isJsonSuccess())
			return;
		Long id = response().jsonPath().getLong("id");
		if (id != null)
			ctx.put("orderId", id);
	}

	protected void rememberUser() {
		if (!isJsonSuccess())
			return;
		String u = response().jsonPath().getString("username");
		if (u != null)
			ctx.put("username", u);
	}
}
