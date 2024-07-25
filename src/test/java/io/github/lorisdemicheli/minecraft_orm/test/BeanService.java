package io.github.lorisdemicheli.minecraft_orm.test;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.lorisdemicheli.minecraft_orm.bean.BeanStore;
import io.github.lorisdemicheli.minecraft_orm.hibernate.DatabaseConfiguration;
import io.github.lorisdemicheli.minecraft_orm.test.annotation.TestOrder;
import io.github.lorisdemicheli.minecraft_orm.test.minecraft.TestPlugin;
import io.github.lorisdemicheli.minecraft_orm.test.util.OrderedRunner;

@RunWith(OrderedRunner.class)
public class BeanService {
	
	public static BeanStore beanStore;

	@Test
	@TestOrder(1)
	public void generateBeanStore() {
		DatabaseConfiguration conn = new DatabaseConfiguration();
		setEnviroment(conn);
		//conn.setHbm2ddl("create-drop");
		conn.setDebug(true);
		beanStore = new BeanStore(TestPlugin.getInstance(), conn);
	}
	
	public static BeanStore getBeanStore() {
		return beanStore;
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
