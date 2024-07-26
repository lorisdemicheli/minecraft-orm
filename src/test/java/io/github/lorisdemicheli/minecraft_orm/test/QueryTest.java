package io.github.lorisdemicheli.minecraft_orm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
	@TestOrder(0)
	public void testCriteria() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryCriteria query = new TestQueryCriteria();
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(1)
	public void testCriteriaCount() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryCriteria query = new TestQueryCriteria();
		assertEquals(Long.valueOf(1),qs.count(query));
	}
	
	@Test
	@TestOrder(2)
	public void testCriteriaHasReqult() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryCriteria query = new TestQueryCriteria();
		assertTrue(qs.hasResult(query));
	}
	
	@Test
	@TestOrder(3)
	public void testCriteriaPaged() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryCriteria query = new TestQueryCriteria();
		assertEquals(1,qs.getPagedResultList(query,0,10).size());
	}

	@Test
	@TestOrder(10)
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
	@TestOrder(100)
	public void testJpql() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(101)
	public void testJpqlCount() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		assertEquals(Long.valueOf(1),qs.count(query));
	}
	
	@Test
	@TestOrder(102)
	public void testJpqlReqult() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		assertTrue(qs.hasResult(query));
	}
	
	@Test
	@TestOrder(103)
	public void testJpqlPaged() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		assertEquals(1,qs.getPagedResultList(query,0,10).size());
	}
	
	@Test
	@TestOrder(110)
	public void testJpqlEqual() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		query.setId(1L);
		assertNotNull(qs.getResultList(new TestQueryJpql()));
	}
	
	@Test
	@TestOrder(111)
	public void testJpqlLike() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryJpql query = new TestQueryJpql();
		query.setDescriptionLike("desc");
		assertNotNull(qs.getResultList(new TestQueryJpql()));
	}
	
	@Test
	@TestOrder(200)
	public void testNative() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(201)
	public void testNativeCount() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		assertEquals(Long.valueOf(1),qs.count(query));
	}
	
	@Test
	@TestOrder(202)
	public void testNativeHasResult() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		assertTrue(qs.hasResult(query));
	}
	
	@Test
	@TestOrder(203)
	public void testNativePaged() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		assertEquals(1,qs.getPagedResultList(query,0,10).size());
	}
	
	@Test
	@TestOrder(210)
	public void testNativeEqual() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		query.setId(1L);
		assertNotNull(qs.getResultList(query));
	}
	
	@Test
	@TestOrder(211)
	public void testNativeLike() {
		QueryService qs = BeanService.getBeanStore().getOrCreateBean(QueryService.class);
		TestQueryNativeSql query = new TestQueryNativeSql();
		query.setDescriptionLike("desc");
		assertNotNull(qs.getResultList(query));
	}
}
