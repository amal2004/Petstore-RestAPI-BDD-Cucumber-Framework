package com.petstore.api.models;

public class Order {
	public Long id;
	public Long petId;
	public Integer quantity;
	public String shipDate;
	public String status;
	public Boolean complete;

	public static Order valid(long id, long petId) {
		Order o = new Order();
		o.id = id;
		o.petId = petId;
		o.quantity = 1;
		o.shipDate = "2026-01-01T12:00:00.000Z";
		o.status = "placed";
		o.complete = false;
		return o;
	}
}
