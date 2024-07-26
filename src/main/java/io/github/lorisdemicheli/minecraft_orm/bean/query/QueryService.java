package io.github.lorisdemicheli.minecraft_orm.bean.query;

import java.util.List;
import java.util.stream.Stream;

import com.google.inject.Inject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class QueryService {
	
	@Inject
	private EntityManager entityManager;

	public <T> List<T> getResultList(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter).getResultList();
	}
	
	public <T> T getFirstResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter).setMaxResults(1).getSingleResult();
	}
	
	public <T> Stream<T> getStreamResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter).getResultStream();
	}
	
	public <T> T getSingleResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter).getSingleResult();
	}
	
	public <T> Long count(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildCount(queryFilter).getSingleResult();
	}
	
	public <T> Boolean hasResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildHasResult(queryFilter).getSingleResult();
	}
	
	public <T> T getFirstResultIfAny(QueryType<T> queryFilter) {
		try {
			return getFirstResult(queryFilter);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public <T> T getSingleResultIfAny(QueryType<T> queryFilter) {
		try {
			return getSingleResult(queryFilter);
		} catch (NoResultException e) {
			return null;
		}
	}
}
