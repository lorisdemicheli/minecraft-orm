package io.github.lorisdemicheli.minecraft_orm.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.github.lorisdemicheli.minecraft_orm.hibernate.Database;

public class DatabaseConfiguration implements Serializable {

	private static final long serialVersionUID = -2509458984724931759L;

	private String url = "localhost";
	private int port = 3306;
	private String schema = "minecraft";
	private String databaseType = "MARIADB";
	private String user = "root";
	private String password = "root";

	public Map<String, String> generateProperties() throws IllegalArgumentException {
		Database databaseTypeConf;
		if(databaseType == null || (databaseTypeConf = Database.valueOf(databaseType.toUpperCase())) == null) {
			throw new IllegalArgumentException(String.format("Database type %s not found", databaseType));
		}
		Map<String, String> properties = new HashMap<>();
		properties.put("jakarta.persistence.jdbc.url",
				String.format("jdbc:%s://%s:%d/%s", databaseType.toLowerCase(), url, port, schema));
		properties.put("jakarta.persistence.jdbc.user", user);
		properties.put("jakarta.persistence.jdbc.password", password);
		properties.put("jakarta.persistence.jdbc.driver", databaseTypeConf.getDriver());
		properties.put("hibernate.dialect", databaseTypeConf.getDialect());
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.connection.autocommit", "true");
		properties.put("hibernate.show_sql", "true");
		return properties;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
}
