package io.github.lorisdemicheli.minecraft_orm.bean.query;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.CountQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Filter;
import io.github.lorisdemicheli.minecraft_orm.bean.query.exception.ParameterException;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.AbstractQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.CriteriaQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.JpqlQuery;
import io.github.lorisdemicheli.minecraft_orm.bean.query.type.NativeQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class QueryBuilder {
	
	private EntityManager em;
	
	public QueryBuilder(EntityManager em) {
		this.em = em;
	}

	public <T extends Serializable> Query buildSelect(QueryType<T> queryFilter) {
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
		Query query = abstractQuery.buildSelect(queryFilter);
		setParameters(query, queryFilter);
		return query;
	}
	
	public <T extends Serializable> Query buildCount(QueryType<T> queryFilter) {
		AbstractQuery<T,? extends TypedQuery<T>,?extends TypedQuery<Long>,? extends TypedQuery<Boolean>> abstractQuery;
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
		Query query = abstractQuery.buildCount(queryFilter);
		setParameters(query, queryFilter);
		return query;
	}
	
	private <T extends Serializable> void setParameters(Query query, QueryType<T> queryFilter) {
		for(Field field : FieldUtils.getFieldsWithAnnotation(queryFilter.getClass(), Filter.class)) {
			try {
				query.setParameter(field.getName(),FieldUtils.readField(field, queryFilter, true));
			} catch (IllegalAccessException e) {
				throw new ParameterException(String.format("Unable to read value of %s",field.getName()),e);
			}
		}
	}
}
