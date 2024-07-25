package io.github.lorisdemicheli.minecraft_orm.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.lorisdemicheli.minecraft_orm.bean.query.QueryService;
import io.github.lorisdemicheli.minecraft_orm.test.annotation.TestOrder;
import io.github.lorisdemicheli.minecraft_orm.test.query.TestQueryCriteria;
import io.github.lorisdemicheli.minecraft_orm.test.query.TestQueryJpql;
import io.github.lorisdemicheli.minecraft_orm.test.query.TestQueryNativeSql;
import io.github.lorisdemicheli.minecraft_orm.test.util.OrderedRunner;

@RunWith(OrderedRunner.class)
public class QueryTest {

	@Test
	@TestOrder(10)
	public void testCriteria() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryCriteria query = new TestQueryCriteria();
		assertNotNull(qs.getResultList(query));
	}

	@Test
	@TestOrder(11)
	public void testCriteriaEqual() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryCriteria query = new TestQueryCriteria();
		query.setId(1L);
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(11)
	public void testCriteriaLike() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryCriteria query = new TestQueryCriteria();
		query.setDescriptionLike("desc");
		assertNotNull(qs.getResultList(query));
	}

	@Test
	@TestOrder(20)
	public void testJpql() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(21)
	public void testJpqlEqual() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		query.setId(1L);
		assertNotNull(qs.getResultList(new TestQueryJpql()));
	}
	
	@Test
	@TestOrder(22)
	public void testJpqlLike() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		query.setDescriptionLike("desc");
		assertNotNull(qs.getResultList(new TestQueryJpql()));
	}
	
	@Test
	@TestOrder(30)
	public void testNative() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(31)
	public void testNativeEqual() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		query.setId(1L);
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(32)
	public void testNativeLike() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		query.setDescriptionLike("desc");
		assertNotNull(qs.getResultList(query));
	}
}
