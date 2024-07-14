package io.github.lorisdemicheli.minecraft_orm.test.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tabe")
public class TestEntity implements Serializable {

	private static final long serialVersionUID = 3107941434906207438L;
	
	@Id
	private String id;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
