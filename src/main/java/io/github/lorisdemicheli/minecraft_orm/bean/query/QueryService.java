package io.github.lorisdemicheli.minecraft_orm.bean.query;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import com.google.inject.Inject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class QueryService {
	
	@Inject
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public <T extends Serializable> List<T> getResultList(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getFirstResult(QueryType<T> queryFilter) {
		return (T) new QueryBuilder(entityManager).buildSelect(queryFilter).setMaxResults(1).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Serializable> Stream<T> getStreamResult(QueryType<T> queryFilter) {
		return (Stream<T>) new QueryBuilder(entityManager).buildSelect(queryFilter).getResultStream();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getSingleResult(QueryType<T> queryFilter) {
		return (T) new QueryBuilder(entityManager).buildSelect(queryFilter).getSingleResult();
	}
	
	public <T extends Serializable> Long count(QueryType<T> queryFilter) {
		return (Long) new QueryBuilder(entityManager).buildCount(queryFilter).getSingleResult();
	}
	
	public <T extends Serializable> T getFirstResultIfAny(QueryType<T> queryFilter) {
		try {
			return getFirstResult(queryFilter);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public <T extends Serializable> T getSingleResultIfAny(QueryType<T> queryFilter) {
		try {
			return getSingleResult(queryFilter);
		} catch (NoResultException e) {
			return null;
		}
	}
}
