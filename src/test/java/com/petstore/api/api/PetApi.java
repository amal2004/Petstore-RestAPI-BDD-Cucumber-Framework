package com.petstore.api.api;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.*;

import com.petstore.api.config.Config;
import com.petstore.api.models.Pet;
import com.petstore.api.specifications.ApiSpecifications;

public class PetApi {

	private final ApiClient c = new ApiClient();

	public Response add(Pet p) {
		return c.post("/pet", p);
	}

	public Response addForm(Map<String, Object> formData) {

		return c.post("/pet", formData, "application/x-www-form-urlencoded");
	}

	public Response addRaw(Object body) {
		return c.postRaw("/pet", body);
	}

	public Response add(Pet p, String ct) {
		return c.post("/pet", p, ct);
	}

	public Response update(Pet p) {
		return c.put("/pet", p);
	}

	public Response update(Pet p, String ct) {
		return c.put("/pet", p, ct);
	}

	public Response updateForm(long id, String name, String status) {
		RequestSpecification req = RestAssured.given().spec(ApiSpecifications.multipartSpec())
				.accept("application/json").contentType("application/x-www-form-urlencoded").pathParam("petId", id);
		if (name != null)
			req.formParam("name", name);
		if (status != null)
			req.formParam("status", status);
		return req.log().all().post("/pet/{petId}");
	}

	public Response findByStatus(String status) {
		return c.get("/pet/findByStatus", Map.of("status", status));
	}

	public Response findByTags(List<String> tags) {
		return c.get("/pet/findByTags", Map.of("tags", String.join(",", tags)));
	}

	public Response get(long id) {
		return c.get("/pet/" + id);
	}

	public Response getPet(String petId) {
		return c.get("/pet/" + petId);
	}

	public Response delete(long id, String key) {
		return key == null ? c.delete("/pet/" + id) : c.delete("/pet/" + id, Map.of("api_key", key));
	}
	
	public Response upload(Long petId, File file, String additionalMetadata) {
		RequestSpecification req = RestAssured.given().spec(ApiSpecifications.multipartSpec()); // base.url, no JSON
																								// content type

		if (file != null) {
			req.multiPart("file", file, "image/jpeg");
		}
		if (additionalMetadata != null) {
			req.multiPart("additionalMetadata", additionalMetadata, "text/plain");
		}
		if (file == null && additionalMetadata == null) {
			req.multiPart("additionalMetadata", "", "text/plain");
		}
		return req.log().all().post("/pet/{petId}/uploadImage", petId);
	}

}
