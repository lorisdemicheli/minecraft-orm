package io.github.lorisdemicheli.minecraft_orm.bean.query.type;

import java.lang.reflect.Field;

import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public abstract class AbstractQuery<R,
	Q extends TypedQuery<R>,
	CQ extends TypedQuery<Long>, 
	HRQ extends TypedQuery<Boolean>> {

	protected EntityManager entityManager;

	public AbstractQuery(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public abstract Q buildSelect(QueryType<R> queryFilter);

	public abstract CQ buildCount(QueryType<R> queryFilter);
	
	public abstract HRQ buildHasResult(QueryType<R> queryFilter);
	
	public abstract boolean filterValidation(Field field);
	
	public abstract boolean canFetch();
}
