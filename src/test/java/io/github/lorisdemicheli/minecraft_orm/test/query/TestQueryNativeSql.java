package io.github.lorisdemicheli.minecraft_orm.test.query;

import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryType;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.CountQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Filter;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.HasResultQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Query;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;

@Query(value = "SELECT t.* FROM tabe t " + //
		"WHERE (t.id = :id OR :id IS NULL) " + //
		"AND (t.description LIKE CONCAT('%',:description,'%') OR :description IS NULL) " + //
		"ORDER BY t.id ASC", nativeSql = true)
@CountQuery(value = "SELECT COUNT(t.id) FROM tabe t " +
		"WHERE (t.id = :id OR :id IS NULL) " + //
		"AND (t.description LIKE CONCAT('%',:description,'%') OR :description IS NULL) ", //
		nativeSql = true)
@HasResultQuery(value = "SELECT COUNT(t.id) > 0 FROM tabe t " +
		"WHERE (t.id = :id OR :id IS NULL) " + //
		"AND (t.description LIKE CONCAT('%',:description,'%') OR :description IS NULL) ", //
		nativeSql = true)
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
