package io.github.lorisdemicheli.minecraft_orm.test.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tabo")
public class TestSecondEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6427923699247613447L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	@ManyToOne
	@JoinColumn(name = "tabe_id", nullable = false)
	private TestEntity entity;

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

	public TestEntity getEntity() {
		return entity;
	}

	public void setEntity(TestEntity entity) {
		this.entity = entity;
	}

}
