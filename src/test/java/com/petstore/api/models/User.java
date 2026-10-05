package com.petstore.api.models;

public class User {
	public Long id;
	public String username;
	public String firstName;
	public String lastName;
	public String email;
	public String password;
	public String phone;
	public Integer userStatus;

	public static User valid(String username) {
		User u = new User();
		u.id = Math.abs((long) username.hashCode());
		u.username = username;
		u.firstName = "API";
		u.lastName = "Tester";
		u.email = username + "@example.test";
		u.password = "P@ss12345";
		u.phone = "1234567890";
		u.userStatus = 1;
		return u;
	}
}
