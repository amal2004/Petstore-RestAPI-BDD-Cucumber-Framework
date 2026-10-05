package com.petstore.api.utils;

import io.restassured.response.Response;
import java.util.*;

public class ScenarioContext {
	
	private final Map<String, Object> data = new HashMap<>();
	private Response response;

	public void put(String k, Object v) {
		data.put(k, v);
	}

	public <T> T get(String k, Class<T> c) {
		return c.cast(data.get(k));
	}

	public Object get(String k) {
		return data.get(k);
	}

	public Response response() {
		return response;
	}

	public void response(Response r) {
		response = r;
	}

	public void clear() {
		data.clear();
		response = null;
	}
}
