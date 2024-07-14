package io.github.lorisdemicheli.minecraft_orm.test;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import io.github.lorisdemicheli.minecraft_orm.bean.BeanStore;
import io.github.lorisdemicheli.minecraft_orm.bean.DatabaseConfiguration;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;

public class ConnectionTest {

	private BeanStore bs;

	@Test
	@Before
	public void generateBeanStore() {
		DatabaseConfiguration conn = new DatabaseConfiguration();
		bs = new BeanStore(TestPlugin.getInstance(), conn);
	}
	
	@Test
	public void saveNewEntity() {
		TestEntityService tes = bs.getOrCreateBean(TestEntityService.class);
		TestEntity te = new TestEntity();
		te.setId(UUID.randomUUID().toString());
		tes.saveOrUpdate(te);
	}
	
	
	
}
