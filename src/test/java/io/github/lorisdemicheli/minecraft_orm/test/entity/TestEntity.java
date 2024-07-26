package io.github.lorisdemicheli.minecraft_orm.test.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tabe")
public class TestEntity implements Serializable {

	private static final long serialVersionUID = 3107941434906207438L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "description")
	private String description;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "entity")
	private List<TestSecondEntity> second = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<TestSecondEntity> getSecond() {
		return second;
	}
	
	@Override
	public String toString() {
		return id.toString() + " - " + description;
	}
}
