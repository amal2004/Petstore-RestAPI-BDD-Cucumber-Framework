package com.petstore.api.models;

public class Category {
	
	public Long id;
	public String name;

	public Category() {
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
