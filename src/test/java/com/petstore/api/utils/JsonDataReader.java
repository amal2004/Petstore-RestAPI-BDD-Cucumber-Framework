package com.petstore.api.utils;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonDataReader {

	private static final ObjectMapper mapper = new ObjectMapper();

	private JsonDataReader() {
	}

	public static JsonNode read(String fileName) {

		try (InputStream input = JsonDataReader.class.getClassLoader().getResourceAsStream("testdata/" + fileName)) {

			if (input == null) {
				throw new RuntimeException("Test data file not found: " + fileName);
			}

			return mapper.readTree(input);

		} catch (Exception e) {
			throw new RuntimeException("Unable to read JSON test data", e);
		}
	}
}
