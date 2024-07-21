package io.github.lorisdemicheli.minecraft_orm.test;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import io.github.lorisdemicheli.minecraft_orm.bean.BeanStore;
import io.github.lorisdemicheli.minecraft_orm.hibernate.DatabaseConfiguration;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;

public class ConnectionTest {

	private BeanStore bs;

	@Test
	@Before
	public void generateBeanStore() {
		DatabaseConfiguration conn = new DatabaseConfiguration();
		setEnviroment(conn);
		conn.setDebug(true);
		bs = new BeanStore(TestPlugin.getInstance(), conn);
	}
	
	@Test
	public void saveNewEntity() {
		TestEntityService tes = bs.getOrCreateBean(TestEntityService.class);
		TestEntity te = new TestEntity();
		te.setId(UUID.randomUUID().toString());
		tes.saveOrUpdate(te);
	}
	
	private void setEnviroment(DatabaseConfiguration conn) {
		if(System.getenv("database_url") != null) {
			conn.setUrl(System.getenv("database_url"));
		}
		if(System.getenv("database_port") != null) {
			conn.setPort(Integer.parseInt(System.getenv("database_port")));
		}
		if(System.getenv("database_schema") != null) {
			conn.setSchema(System.getenv("database_schema"));
		}
		if(System.getenv("database_type") != null) {
			conn.setDatabaseType(System.getenv("database_type"));
		}
		if(System.getenv("database_user") != null) {
			conn.setUser(System.getenv("database_user"));
		}
		if(System.getenv("database_password") != null) {
			conn.setPassword(System.getenv("database_password"));
		}
	}
}
