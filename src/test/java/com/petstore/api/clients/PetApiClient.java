package com.petstore.api.clients;

import com.petstore.api.constants.ApiConstants;
import com.petstore.api.models.Pet;
import com.petstore.api.specifications.ApiSpecifications;

import static io.restassured.RestAssured.given;

import java.io.File;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PetApiClient extends BaseApiClient {

	public Response createPet(Pet pet) {

		return given()

				.spec(ApiSpecifications.requestSpec())

				.body(pet)

				.when()

				.post(ApiConstants.PET);
	}

	public Response getPet(String petId) {

		return given()

				.spec(ApiSpecifications.requestSpec())

				.pathParam("petId", petId)

				.when()

				.get(ApiConstants.PET_BY_ID);
	}

	public Response updatePet(Pet pet) {

		return given()

				.spec(ApiSpecifications.requestSpec())

				.body(pet)

				.when()

				.put(ApiConstants.PET);
	}

	public Response deletePet(String petId) {

		return given()

				.spec(ApiSpecifications.requestSpec())

				.pathParam("petId", petId)

				.when()

				.delete(ApiConstants.PET_BY_ID);
	}

	public Response findByStatus(String status) {

		return given()

				.spec(ApiSpecifications.requestSpec())

				.queryParam("status", status)

				.when()

				.get(ApiConstants.FIND_BY_STATUS);
	}

	public Response upload(String path, File file, String metadata) {

		RequestSpecification req = given().log().all().spec(ApiSpecifications.multipartSpec()).multiPart("file", file,
				"image/jpg");

		if (metadata != null) {
			req.multiPart("additionalMetadata", metadata, "text/plain");
		}

		return req.when().post(path);
	}
}