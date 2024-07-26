package io.github.lorisdemicheli.minecraft_orm.bean.query.type;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryType;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Alias;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Distinct;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Filter;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.GroupBy;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.Join;
import io.github.lorisdemicheli.minecraft_orm.bean.query.annotation.OrderBy;
import io.github.lorisdemicheli.minecraft_orm.bean.query.utils.QueryContext;
import io.github.lorisdemicheli.minecraft_orm.bean.query.utils.QueryUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CriteriaQuery<T>
		extends AbstractQuery<T, TypedQuery<T>, TypedQuery<Long>, TypedQuery<Boolean>> {

	public static final String DEFAULT_ROOT_ALIAS = "root";
	private Map<String, From<?, ?>> alias;

	public CriteriaQuery(EntityManager entityManager) {
		super(entityManager);
		alias = new HashMap<>();
	}

	@Override
	public TypedQuery<T> buildSelect(QueryType<T> queryFilter) {
		jakarta.persistence.criteria.CriteriaQuery<T> critieraQuery = critieraBuilder(queryFilter.getType(), queryFilter);
		return entityManager.createQuery(critieraQuery);
	}

	@Override
	public TypedQuery<Long> buildCount(QueryType<T> queryFilter) {
		jakarta.persistence.criteria.CriteriaQuery<Long> critieraQuery = critieraBuilder(Long.class, queryFilter);
		critieraQuery.select(entityManager.getCriteriaBuilder().count(alias.get(getRootAlias(queryFilter.getClass()))));
		return entityManager.createQuery(critieraQuery);
	}

	@Override
	public TypedQuery<Boolean> buildHasResult(QueryType<T> queryFilter) {
		jakarta.persistence.criteria.CriteriaQuery<Boolean> critieraQuery = critieraBuilder(Boolean.class, queryFilter);
		critieraQuery.select(entityManager.getCriteriaBuilder().greaterThan(
				entityManager.getCriteriaBuilder().count(alias.get(getRootAlias(queryFilter.getClass()))), 0L));
		return entityManager.createQuery(critieraQuery);
	}
	
	@Override
	public boolean filterValidation(Field field) {
		if(!field.isAnnotationPresent(Filter.class)) {
			return false;
		}
		Filter filter = field.getAnnotation(Filter.class);
		return !StringUtils.isEmpty(filter.path());
	}

	@SuppressWarnings("hiding")
	private <R, T> jakarta.persistence.criteria.CriteriaQuery<R> critieraBuilder(
			Class<R> resultClass, QueryType<T> queryFilter) {
		QueryContext ctx = new QueryContext(entityManager.getCriteriaBuilder(), alias);
		jakarta.persistence.criteria.CriteriaQuery<R> criteriaQuery = entityManager.getCriteriaBuilder()
				.createQuery(resultClass);
		Root<T> itemRoot = criteriaQuery.from(queryFilter.getType());

		// ROOT
		String rootAlias = getRootAlias(queryFilter.getClass());
		alias.put(rootAlias, itemRoot);
		itemRoot.alias(rootAlias);

		// DISTINCT
		if (queryFilter.getClass().isAnnotationPresent(Distinct.class)) {
			criteriaQuery.distinct(true);
		}

		// JOIN
		for (Join join : getJoins(queryFilter.getClass())) {
			String[] split = join.path().split("\\.");
			if (split.length > 2) {
				throw new IllegalArgumentException("Too much parameter for Join " + join.path());
			}
			String classTo = split[0];
			String attribute = split[1];
			jakarta.persistence.criteria.Join<?, ?> itemJoin = alias.get(classTo).join(attribute, join.type());
			itemJoin.alias(join.alias().value());
			alias.put(join.alias().value(), itemJoin);
		}

		// WHERE
		List<Predicate> wherePredicate = new ArrayList<>();
		Map<String, List<Field>> disjunctionMap = new HashMap<>();
		for (Field conditionField : QueryUtils.getParameterFields(this,queryFilter.getClass())) {
			Filter filter = conditionField.getAnnotation(Filter.class);
			if (QueryUtils.isParameterActive(queryFilter, conditionField)) {
				if (StringUtils.isNotEmpty(filter.disjunction())) {
					disjunctionMap.getOrDefault(filter.disjunction(), new ArrayList<>()).add(conditionField);
				} else {
					wherePredicate.add(QueryUtils.convertField(ctx, conditionField));
				}
			}
		}

		for (Entry<String, List<Field>> disjunctionEntry : disjunctionMap.entrySet()) {
			List<Predicate> disjunctionPredicate = new ArrayList<>();
			for (Field conditionDisjunctionField : disjunctionEntry.getValue()) {
				disjunctionPredicate.add(QueryUtils.convertField(ctx, conditionDisjunctionField));
			}
			wherePredicate.add(
					ctx.criteriaBuilder().or(disjunctionPredicate.toArray(new Predicate[disjunctionPredicate.size()])));
		}
		if (!wherePredicate.isEmpty()) {
			criteriaQuery.where(wherePredicate.toArray(new Predicate[wherePredicate.size()]));
		}

		// GROUP BY
		List<GroupBy> groupsBy = getGroupBy(queryFilter.getClass());
		criteriaQuery.groupBy(
				groupsBy.stream().map(g -> QueryUtils.aliasPath(ctx, g.value())).toArray(Expression<?>[]::new));

		// ORDER BY
		List<OrderBy> ordersBy = getOrderBy(queryFilter.getClass());
		criteriaQuery.orderBy(generateOrderBy(ctx, ordersBy));

		return criteriaQuery;
	}

	@SuppressWarnings("rawtypes")
	private String getRootAlias(Class<? extends QueryType> classQuery) {
		String rootAlias;
		Alias rootAliasAnnotation = classQuery.getAnnotation(Alias.class);
		if (rootAliasAnnotation != null) {
			rootAlias = rootAliasAnnotation.value();
		} else {
			rootAlias = DEFAULT_ROOT_ALIAS;
		}
		return rootAlias;
	}

	@SuppressWarnings("rawtypes")
	private List<Join> getJoins(Class<? extends QueryType> classQuery) {
		Join.List joinList = classQuery.getAnnotation(Join.List.class);
		Join joinSingle = classQuery.getAnnotation(Join.class);
		List<Join> joins = new ArrayList<>();
		if (joinList != null) {
			joins.addAll(Arrays.asList(joinList.joins()));
		} else if (joinSingle != null) {
			joins.add(joinSingle);
		}
		return joins;
	}

	@SuppressWarnings("rawtypes")
	private List<GroupBy> getGroupBy(Class<? extends QueryType> classQuery) {
		GroupBy.List groupByList = classQuery.getAnnotation(GroupBy.List.class);
		GroupBy groupBySingle = classQuery.getAnnotation(GroupBy.class);
		List<GroupBy> groupsBy = new ArrayList<>();
		if (groupByList != null) {
			groupsBy.addAll(Arrays.asList(groupByList.groupsBy()));
		} else if (groupBySingle != null) {
			groupsBy.add(groupBySingle);
		}
		return groupsBy;
	}

	@SuppressWarnings("rawtypes")
	private List<OrderBy> getOrderBy(Class<? extends QueryType> classQuery) {
		OrderBy.List orderByList = classQuery.getAnnotation(OrderBy.List.class);
		OrderBy orderBySingle = classQuery.getAnnotation(OrderBy.class);
		List<OrderBy> ordersBy = new ArrayList<>();
		if (orderByList != null) {
			ordersBy.addAll(Arrays.asList(orderByList.ordersBy()));
		} else if (orderBySingle != null) {
			ordersBy.add(orderBySingle);
		}
		return ordersBy;
	}

	private List<Order> generateOrderBy(QueryContext ctx, List<OrderBy> ordersBy) {
		return ordersBy.stream().map(o -> {
			if (o.asc()) {
				return ctx.criteriaBuilder().asc(QueryUtils.aliasPath(ctx, o.value()));
			} else {
				return ctx.criteriaBuilder().desc(QueryUtils.aliasPath(ctx, o.value()));
			}
		}).toList();
	}	
}
