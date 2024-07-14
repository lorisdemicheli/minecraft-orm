package io.github.lorisdemicheli.minecraft_orm.hibernate;

public enum Database {

	MARIADB("org.hibernate.dialect.MariaDBDialect", "org.mariadb.jdbc.Driver"),
    MYSQL("org.hibernate.dialect.MySQLDialect", "com.mysql.cj.jdbc.Driver"),
    POSTGRESQL("org.hibernate.dialect.PostgreSQLDialect", "org.postgresql.Driver"),
    ORACLE("org.hibernate.dialect.Oracle12cDialect", "oracle.jdbc.OracleDriver"),
    SQLSERVER("org.hibernate.dialect.SQLServerDialect", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    H2("org.hibernate.dialect.H2Dialect", "org.h2.Driver"),
    SQLITE("org.hibernate.dialect.SQLiteDialect", "org.sqlite.JDBC"),
    DB2("org.hibernate.dialect.DB2Dialect", "com.ibm.db2.jcc.DB2Driver"),
    HSQLDB("org.hibernate.dialect.HSQLDialect", "org.hsqldb.jdbc.JDBCDriver"),
    INFORMIX("org.hibernate.dialect.InformixDialect", "com.informix.jdbc.IfxDriver"),
    SYBASE("org.hibernate.dialect.SybaseDialect", "com.sybase.jdbc4.jdbc.SybDriver"),
    FIREBIRD("org.hibernate.dialect.FirebirdDialect", "org.firebirdsql.jdbc.FBDriver");
	
	private String dialect;
	private String driver;
	
	private Database(String dialect,String driver) {
		this.dialect = dialect;
		this.driver = driver; 
	}
	
	public String getDialect() {
		return dialect;
	}
	
	public String getDriver() {
		return driver;
	}
}
