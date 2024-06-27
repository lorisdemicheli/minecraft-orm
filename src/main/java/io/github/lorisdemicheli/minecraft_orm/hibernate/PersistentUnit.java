package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PersistentUnit implements Serializable {

	private static final long serialVersionUID = -2509458984724931759L;
	
	private String url = "localhost";
	private int port = 3306;
	private String database = "test";
	private String databaseType = "mariadb";
	private String user = "root";
	private String password = "root";
	private transient String driver = "org.mariadb.jdbc.Driver";
	private transient String dialect = "org.hibernate.dialect.MariaDBDialect";
	private transient String databaseGeneration = "update";
	
	//TODO usere Database.MARIADB.

	public Map<String, String> generateProperties() {
		Map<String, String> properties = new HashMap<>();
		properties.put("jakarta.persistence.jdbc.url", String.format("jdbc:%s://%s:%d/%s", databaseType, url, port, database));
		properties.put("jakarta.persistence.jdbc.user", user);
		properties.put("jakarta.persistence.jdbc.password", password);
		properties.put("jakarta.persistence.jdbc.driver", driver);
		properties.put("hibernate.dialect", dialect);
		properties.put("hibernate.hbm2ddl.auto", databaseGeneration);
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
