package com.petstore.api.api;

import io.restassured.response.Response;
import java.util.*;

import com.petstore.api.models.Order;

public class StoreApi {
	private final ApiClient c = new ApiClient();

	public Response inventory(String key) {
		return c.getWithHeaders("/store/inventory", Map.of("api_key", key == null ? "" : key));
	}

	public Response createOrder(Order o) {
		return c.post("/store/order", o);
	}

	public Response getOrder(long id) {
		return c.get("/store/order/" + id);
	}

	public Response deleteOrder(long id) {
		return c.delete("/store/order/" + id);
	}

	public Response deleteOrder(String orderId) {
		return c.delete("/store/order/" + orderId);
	}

	// For non-integer / invalid IDs
	public Response getOrder(String orderId) {
		return c.get("/store/order/" + orderId);
	}
}
