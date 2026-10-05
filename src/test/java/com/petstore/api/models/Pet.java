package com.petstore.api.models;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Pet")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pet {
	
	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "category")
	private Category category;
	@XmlElementWrapper(name = "photoUrls")
	@XmlElement(name = "photoUrl")
	private List<String> photoUrls;
	@XmlElementWrapper(name = "tags")
	@XmlElement(name = "tag")
	private List<Tag> tags;
	@XmlElement(name = "status")
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static Pet valid(String name, long id) {
		Pet p = new Pet();
		p.setId(id);
		p.setName(name);
		p.setPhotoUrls(new ArrayList<>(List.of("https://example.com/" + id + ".jpg")));
		p.setTags(new ArrayList<>(List.of(new Tag(id, "automation"))));
		p.setStatus("available");
		return p;
	}
}