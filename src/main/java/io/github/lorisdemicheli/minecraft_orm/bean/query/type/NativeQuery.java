package io.github.lorisdemicheli.minecraft_orm.bean.query.type;

import java.io.Serializable;

import org.hibernate.Session;

import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryType;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.CountQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.HasResultQuery;
import jakarta.persistence.EntityManager;

public class NativeQuery<T extends Serializable> extends AbstractQuery<T,
	org.hibernate.query.NativeQuery<T>,
	org.hibernate.query.NativeQuery<Long>,
	org.hibernate.query.NativeQuery<Boolean>> {

	public NativeQuery(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public org.hibernate.query.NativeQuery<T> buildSelect(QueryType<T> queryFilter) {
		io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Query stringQuery = 
				queryFilter.getClass().getAnnotation(io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Query.class);
		Session session = entityManager.unwrap(Session.class);
		return session.createNativeQuery(stringQuery.value(), queryFilter.getType());
	}

	@Override
	public org.hibernate.query.NativeQuery<Long> buildCount(QueryType<T> queryFilter) {
		CountQuery stringCountQuery = queryFilter.getClass().getAnnotation(CountQuery.class);
		Session session = entityManager.unwrap(Session.class);
		return session.createNativeQuery(stringCountQuery.value(), Long.class);
	}

	@Override
	public org.hibernate.query.NativeQuery<Boolean> buildHasResult(QueryType<T> queryFilter) {
		HasResultQuery hasResultQuery = queryFilter.getClass().getAnnotation(HasResultQuery.class);
		Session session = entityManager.unwrap(Session.class);
		return session.createNativeQuery(hasResultQuery.value(), Boolean.class);
	}
}