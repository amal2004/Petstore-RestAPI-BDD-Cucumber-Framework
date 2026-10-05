package com.petstore.api.specifications;

import com.petstore.api.config.Config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class ApiSpecifications {

	private ApiSpecifications() {
	}

	public static RequestSpecification requestSpec() {

		return new RequestSpecBuilder()

				.setBaseUri(Config.get("base.url"))

				.setContentType("application/json")

				.setAccept("application/json")

				.build();
	}

	public static ResponseSpecification successResponse() {

		return new ResponseSpecBuilder()

				.expectContentType("application/json")

				.build();
	}

	public static RequestSpecification multipartSpec() {

		return new RequestSpecBuilder()

				.setBaseUri(Config.get("base.url")).setAccept("application/json")
				.setConfig(RestAssured.config().encoderConfig(
						EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
				.build();
	}

}
