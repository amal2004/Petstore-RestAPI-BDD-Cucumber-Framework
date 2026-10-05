package com.petstore.api.utils;

import com.github.javafaker.Faker;

public class FakerUtils {

	private static final Faker faker = new Faker();

	private FakerUtils() {
	}

	public static String petName() {

		return faker.animal().name();
	}

	public static String uniquePetName() {

		return faker.animal().name() + "_" + System.currentTimeMillis();
	}

	public static String photoUrl() {

		return "https://example.com/" + faker.internet().uuid() + ".jpg";
	}

}
