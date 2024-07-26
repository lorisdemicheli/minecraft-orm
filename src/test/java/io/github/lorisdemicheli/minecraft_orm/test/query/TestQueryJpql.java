package io.github.lorisdemicheli.minecraft_orm.test.query;

import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryType;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.CountQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Fetch;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Filter;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.HasResultQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Query;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;

@Query(value = "SELECT te FROM TestEntity te  " + //
		"WHERE (te.id = :id OR :id IS NULL) " + //
		"AND (te.description = :description OR :description IS NULL) " + //
		"ORDER BY te.id ASC", nativeSql = false)
@CountQuery(value = "SELECT COUNT(te) FROM TestEntity te " + //
		"WHERE (te.id = :id OR :id IS NULL) " + //
		"AND (te.description = :description OR :description IS NULL) ", //
		nativeSql = false)
@HasResultQuery(value = "SELECT COUNT(te)>0 FROM TestEntity te " + //
		"WHERE (te.id = :id OR :id IS NULL) " + //
		"AND (te.description = :description OR :description IS NULL) ", //
		nativeSql = false)
@Fetch.List(fetchs = {
		@Fetch(path = "root.second")
})
public class TestQueryJpql implements QueryType<TestEntity> {

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
