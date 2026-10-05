package com.petstore.api.clients;

import com.petstore.api.config.Config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {

	protected RequestSpecification requestSpecification() {

		return new RequestSpecBuilder().setBaseUri(Config.get("base.url")).setContentType("application/json")
				.setAccept("application/json").build();
	}
}
