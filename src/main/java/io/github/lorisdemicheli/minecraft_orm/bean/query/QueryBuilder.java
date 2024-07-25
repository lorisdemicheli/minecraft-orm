package io.github.lorisdemicheli.minecraft_orm.bean.query;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.CountQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.HasResultQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.exception.ParameterException;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.AbstractQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.CriteriaQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.JpqlQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.NativeQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.utils.QueryUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class QueryBuilder {
	
	private EntityManager em;
	
	public QueryBuilder(EntityManager em) {
		this.em = em;
	}

	public <T> TypedQuery<T> buildSelect(QueryType<T> queryFilter) {
		AbstractQuery<T,? extends TypedQuery<T>,? extends TypedQuery<Long>,? extends TypedQuery<Boolean>> abstractQuery;
		io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Query stringQuery = 
				queryFilter.getClass().getAnnotation(io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Query.class);
		if (stringQuery != null) {
			if (stringQuery.nativeSql()) {
				abstractQuery = new NativeQuery<T>(em);
			} else {
				abstractQuery = new JpqlQuery<T>(em);
			}
		} else {
			abstractQuery = new CriteriaQuery<T>(em);
		}
		TypedQuery<T> query = abstractQuery.buildSelect(queryFilter);
		setParameters(query, abstractQuery, queryFilter);
		return query;
	}
	
	public <T> TypedQuery<Long> buildCount(QueryType<T> queryFilter) {
		AbstractQuery<T,? extends TypedQuery<T>,? extends TypedQuery<Long>,? extends TypedQuery<Boolean>> abstractQuery;
		CountQuery stringCountQuery = queryFilter.getClass().getAnnotation(CountQuery.class);
		if (stringCountQuery != null) {
			if (stringCountQuery.nativeSql()) {
				abstractQuery = new NativeQuery<T>(em);
			} else {
				abstractQuery = new JpqlQuery<T>(em);
			}
		} else {
			abstractQuery = new CriteriaQuery<T>(em);
		}
		TypedQuery<Long> query = abstractQuery.buildCount(queryFilter);
		setParameters(query, abstractQuery, queryFilter);
		return query;
	}
	
	public <T> TypedQuery<Boolean> buildHasResult(QueryType<T> queryFilter) {
		AbstractQuery<T,? extends TypedQuery<T>,? extends TypedQuery<Long>,? extends TypedQuery<Boolean>> abstractQuery;
		HasResultQuery hasResultQuery = queryFilter.getClass().getAnnotation(HasResultQuery.class);
		if (hasResultQuery != null) {
			if (hasResultQuery.nativeSql()) {
				abstractQuery = new NativeQuery<T>(em);
			} else {
				abstractQuery = new JpqlQuery<T>(em);
			}
		} else {
			abstractQuery = new CriteriaQuery<T>(em);
		}
		TypedQuery<Boolean> query = abstractQuery.buildHasResult(queryFilter);
		setParameters(query, abstractQuery, queryFilter);
		return query;
	}
	
	private <T> void setParameters(TypedQuery<?> query, 
			AbstractQuery<?,?,?,?> abstractQuery, QueryType<?> queryFilter) {
		for(Field field : QueryUtils.getParameterFields(abstractQuery, queryFilter.getClass())) {
			try {
				if(QueryUtils.isParameterActive(queryFilter, field)) {
					String name = QueryUtils.fieldName(field);
					query.setParameter(name,FieldUtils.readField(field, queryFilter, true));
				}
			} catch (IllegalAccessException e) {
				throw new ParameterException(String.format("Unable to read value of %s",field.getName()),e);
			}
		}
	}
}
