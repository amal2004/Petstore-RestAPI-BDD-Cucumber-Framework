package com.petstore.api.assertions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import io.restassured.response.Response;

public final class ApiAssertions {

	private ApiAssertions() {
	}

	public static void statusCode(Response response, int expected) {

		assertEquals(response.statusCode(), expected, "Unexpected HTTP status");
	}

	public static void successful(Response response) {

		assertTrue(response.statusCode() >= 200 && response.statusCode() < 300,

				"Expected successful response");
	}

	public static void clientError(Response response) {

		assertTrue(response.statusCode() >= 400 && response.statusCode() < 500,

				"Expected client error");
	}

	public static void fieldExists(Response response, String field) {

		assertNotNull(response.jsonPath().get(field),

				"Missing response field: " + field);
	}

	public static void responseTimeUnder(Response response, long milliseconds) {

		assertTrue(response.time() < milliseconds,

				"Response exceeded " + milliseconds + " ms");
	}
}
