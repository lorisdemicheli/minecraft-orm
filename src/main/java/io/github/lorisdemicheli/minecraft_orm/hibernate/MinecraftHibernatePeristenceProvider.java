package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.bukkit.plugin.Plugin;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import io.github.lorisdemicheli.minecraft_orm.server.PluginUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class MinecraftHibernatePeristenceProvider extends HibernatePersistenceProvider {

	@Override
	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
		return createContainerEntityManagerFactory(archiverPersistenceUnitInfo(persistenceUnitName,PluginUtil.getCurrentPlugin()), properties);
	}

	private static PersistenceUnitInfo archiverPersistenceUnitInfo(String persistenceUnitName,Plugin plugin) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addClassLoaders(plugin.getClass().getClassLoader()));
		List<String> entitys = new ArrayList<>();//reflections.getTypesAnnotatedWith(Entity.class).stream().map(c->c.getCanonicalName()).toList();
		System.out.println("ENTITY");
		System.out.println(entitys);
		entitys.add("io.github.lorisdemicheli.frinds_ui.friends.entity.Player");
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
				return false;
			}

			@Override
			public SharedCacheMode getSharedCacheMode() {
				return null;
			}

			@Override
			public ValidationMode getValidationMode() {
				return null;
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
				return plugin.getClass().getClassLoader();
			}
		};
	}
}
