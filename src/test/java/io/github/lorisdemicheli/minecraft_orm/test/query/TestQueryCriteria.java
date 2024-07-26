package io.github.lorisdemicheli.minecraft_orm.test.query;

import io.github.lorisdemicheli.minecraft_orm.bean.query.Expression;
import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryType;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Alias;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Filter;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.OrderBy;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;

@Alias("te")
@OrderBy("te.id")
public class TestQueryCriteria implements QueryType<TestEntity> {
	
	@Filter(path = "te.id",expression = Expression.EQUAL, emptyOrNullExclude = true)
	private Long id;
	@Filter(path = "te.description", expression = Expression.LIKE, emptyOrNullExclude = true)
	private String descriptionLike;

	@Override
	public Class<TestEntity> getType() {
		return TestEntity.class;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getDescriptionLike() {
		return descriptionLike;
	}
	
	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

}
