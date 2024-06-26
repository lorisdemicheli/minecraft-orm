package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PersistentUnit implements Serializable {

	private static final long serialVersionUID = -2509458984724931759L;
	
	private String url = "localhost";
	private int port = 3306;
	private String database = "minecraft";
	private transient String databaseType = "mysql";
	private String user = "root";
	private String password = "root";
	private transient String driver = "com.mysql.cj.jdbc.Driver";
	private transient String dialect = "org.hibernate.dialect.MySQLDialect";
	private transient String databaseGeneration = "update";

	public Map<String, String> generateProperties() {
		Map<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.jdbc.url", String.format("jdbc:%s://%s:%d/%s", databaseType, url, port, database));
		properties.put("javax.persistence.jdbc.user", user);
		properties.put("javax.persistence.jdbc.password", password);
		properties.put("javax.persistence.jdbc.driver", driver);
		properties.put("hibernate.dialect", dialect);
		properties.put("hibernate.hbm2ddl.auto", databaseGeneration);
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

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getDatabaseGeneration() {
		return databaseGeneration;
	}

	public void setDatabaseGeneration(String databaseGeneration) {
		this.databaseGeneration = databaseGeneration;
	}

}
