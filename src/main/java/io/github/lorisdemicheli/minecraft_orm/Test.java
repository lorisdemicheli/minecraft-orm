package io.github.lorisdemicheli.minecraft_orm;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tabe")
public class Test implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7877322542264861540L;
	@Id
	private String id;
	
	public Test() { }
	
	public Test(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
