package com.petstore.api.api;

import io.restassured.response.Response;
import java.util.*;

import com.petstore.api.models.User;

public class UserApi {
	private final ApiClient c = new ApiClient();

	public Response create(User u) {
		return c.post("/user", u);
	}

	public Response createRaw(Object body) {
		return c.postRaw("/user", body);
	}

	public Response createList(List<User> users) {
		return c.post("/user/createWithList", users);
	}

	public Response login(String username, String password) {
		Map<String, Object> q = new HashMap<>();
		if (username != null)
			q.put("username", username);
		if (password != null)
			q.put("password", password);
		return c.get("/user/login", q);
	}

	public Response logout() {
		return c.get("/user/logout");
	}

	public Response get(String username) {
		return c.get("/user/" + username);
	}

	public Response update(String username, User u) {
		return c.put("/user/" + username, u);
	}

	public Response delete(String username) {
		return c.delete("/user/" + username);
	}
}
