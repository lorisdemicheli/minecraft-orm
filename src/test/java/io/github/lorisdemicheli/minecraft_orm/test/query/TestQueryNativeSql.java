package io.github.lorisdemicheli.minecraft_orm.test.query;

import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryType;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Filter;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Query;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;

@Query(value = "SELECT t.* FROM tabe t " + //
		"WHERE (t.id = :id OR :id IS NULL) " + //
		"AND (t.description LIKE CONCAT('%',:description,'%') OR t.description) " + //
		"ORDER BY t.id ASC", nativeSql = true)
public class TestQueryNativeSql implements QueryType<TestEntity> {

	@Filter(name = "id")
	private Long id;
	@Filter(name = "description")
	private String descriptionLike;

	@Override
	public Class<TestEntity> getType() {
		return TestEntity.class;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescriptionLike() {
		return descriptionLike;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

}
