package com.petstore.api.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.petstore.api.config.Config;

public class ApiClient {
	private final RequestSpecification base;

	public ApiClient() {
		base = new RequestSpecBuilder().setBaseUri(Config.baseUrl()).setRelaxedHTTPSValidation()
				.setContentType(ContentType.JSON).build();
	}

	public Response get(String path) {
		return RestAssured.given(base).get(path);
	}

	public Response get(String path, Map<String, ?> query) {
		return RestAssured.given(base).queryParams(query).get(path);
	}

	public Response getWithHeaders(String path, Map<String, String> headers) {
		return RestAssured.given(base).headers(headers).get(path);
	}

	public Response post(String path, Object body) {
		return RestAssured.given(base).body(body).post(path);
	}

	public Response post(String path, Object body, String contentType) {

		RequestSpecification request = RestAssured.given().spec(base).contentType(contentType);

		if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {

			if (!(body instanceof Map)) {
				throw new IllegalArgumentException("Form-urlencoded requests require a Map<String, ?> body. "
						+ "Received: " + (body == null ? "null" : body.getClass().getName()));
			}

			Map<?, ?> formData = (Map<?, ?>) body;

			for (Map.Entry<?, ?> entry : formData.entrySet()) {

				if (entry.getKey() == null) {
					continue;
				}

				request.formParam(String.valueOf(entry.getKey()), entry.getValue());
			}

			return request.post(path);
		}

		if ("application/xml".equalsIgnoreCase(contentType) || "text/xml".equalsIgnoreCase(contentType)) {

			XmlMapper xmlMapper = new XmlMapper();

			try {

				String xmlBody = xmlMapper.writeValueAsString(body);

				return request.body(xmlBody).post(path);

			} catch (Exception e) {

				throw new IllegalStateException("Unable to serialize request body to XML", e);
			}
		}

		// JSON and other supported body formats
		return request.body(body).post(path);
	}

	public Response post(String path) {
		return RestAssured.given(base).post(path);
	}

	public Response postWithQuery(String path, Map<String, ?> query) {
		return RestAssured.given(base).queryParams(query).post(path);
	}

	public Response put(String path, Object body) {
		return RestAssured.given(base).body(body).put(path);
	}

	public Response put(String path, Object body, String contentType) {

		return RestAssured.given(base).contentType(contentType).body(body).put(path);
	}

	public Response postRaw(String path, Object body) {
		return RestAssured.given(base).contentType(ContentType.JSON).body(body).post(path);
	}

	public Response delete(String path) {
		return RestAssured.given().spec(base).when().delete(path);
	}

	public Response delete(String path, Map<String, String> headers) {
		return RestAssured.given(base).headers(headers).delete(path);
	}

	public Response upload(String path, File file, String metadata) {

		RequestSpecification req = RestAssured.given(base).contentType("application/octet-stream");

		if (metadata != null) {
			req.queryParam("additionalMetadata", metadata);
		}

		if (file != null) {
			req.body(file);
		}

		return req.post(path);
	}
}
