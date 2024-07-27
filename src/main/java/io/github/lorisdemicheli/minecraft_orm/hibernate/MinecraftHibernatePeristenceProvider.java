package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.bukkit.plugin.Plugin;
import org.hibernate.jpa.HibernatePersistenceProvider;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class MinecraftHibernatePeristenceProvider extends HibernatePersistenceProvider {

	private Plugin plugin;
	
	public MinecraftHibernatePeristenceProvider(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
		return createContainerEntityManagerFactory(archiverPersistenceUnitInfo(persistenceUnitName,plugin), properties);
	}

	private static PersistenceUnitInfo archiverPersistenceUnitInfo(String persistenceUnitName,Plugin plugin) {
		ClassLoader cl = plugin.getClass().getClassLoader();
		
		List<String> entitys = findEntityClasses(cl);

		return new PersistenceUnitInfo() {
			@Override
			public String getPersistenceUnitName() {
				return persistenceUnitName;
			}

			@Override
			public String getPersistenceProviderClassName() {
				return "org.hibernate.jpa.HibernatePersistenceProvider";
			}

			@Override
			public PersistenceUnitTransactionType getTransactionType() {
				return PersistenceUnitTransactionType.RESOURCE_LOCAL;
			}

			@Override
			public DataSource getJtaDataSource() {
				return null;
			}

			@Override
			public DataSource getNonJtaDataSource() {
				return null;
			}

			@Override
			public List<String> getMappingFileNames() {
				return Collections.emptyList();
			}

			@Override
			public List<URL> getJarFileUrls() {
				try {
					return Collections.list(this.getClass().getClassLoader().getResources(""));
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}

			@Override
			public URL getPersistenceUnitRootUrl() {
				return null;
			}

			@Override
			public List<String> getManagedClassNames() {
				return entitys;
			}

			@Override
			public boolean excludeUnlistedClasses() {
				return true;
			}

			@Override
			public SharedCacheMode getSharedCacheMode() {
				return SharedCacheMode.ALL;
			}

			@Override
			public ValidationMode getValidationMode() {
				return ValidationMode.AUTO;
			}

			@Override
			public Properties getProperties() {
				return new Properties();
			}

			@Override
			public String getPersistenceXMLSchemaVersion() {
				return null;
			}

			@Override
			public ClassLoader getClassLoader() {
				return plugin.getClass().getClassLoader();
			}

			@Override
			public void addTransformer(ClassTransformer transformer) {

			}

			@Override
			public ClassLoader getNewTempClassLoader() {
				return new ClassLoader("parent-" + plugin.getName() ,plugin.getClass().getClassLoader()) {	};
			}

			@Override
			public String getScopeAnnotationName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getQualifierAnnotationNames() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	private static List<String> findEntityClasses(ClassLoader cl) {
        try (ScanResult scanResult = new ClassGraph()
                .addClassLoader(cl)
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            return scanResult.getClassesWithAnnotation(Entity.class.getName()).stream()
            		.map(classInfo -> classInfo.getName())
            		.toList();
        }
    }
}
