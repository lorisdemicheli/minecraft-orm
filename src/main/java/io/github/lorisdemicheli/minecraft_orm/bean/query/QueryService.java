package io.github.lorisdemicheli.minecraft_orm.bean.query;

import java.util.List;
import java.util.stream.Stream;

import com.google.inject.Inject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class QueryService {
	
	@Inject
	private EntityManager entityManager;

	public <T> List<T> getResultList(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter,true).getResultList();
	}
	
	public <T> T getFirstResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter,false).setMaxResults(1).getSingleResult();
	}
	
	public <T> Stream<T> getStreamResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter,true).getResultStream();
	}
	
	public <T> T getSingleResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildSelect(queryFilter,true).getSingleResult();
	}
	
	public <T> Long count(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildCount(queryFilter).getSingleResult();
	}
	
	public <T> Boolean hasResult(QueryType<T> queryFilter) {
		return new QueryBuilder(entityManager).buildHasResult(queryFilter).getSingleResult();
	}
	
	public <T> Page<T> getPagedResultList(QueryType<T> queryFilter, int pageNumber, int pageSize) {
		TypedQuery<T> query = new QueryBuilder(entityManager).buildSelect(queryFilter,false);
		query.setMaxResults(pageSize);
		query.setFirstResult(pageNumber * pageSize);
		Long totalElement = count(queryFilter);
		int tempSize =  (int) (totalElement / pageSize);
		if(totalElement % pageSize != 0) {
			tempSize++;
		}
		return new Page<T>(query.getResultList(), tempSize, pageNumber, totalElement);
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
