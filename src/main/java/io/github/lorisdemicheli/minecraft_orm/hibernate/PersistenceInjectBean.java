package io.github.lorisdemicheli.minecraft_orm.hibernate;

import org.bukkit.plugin.Plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.persist.jpa.JpaPersistOptions;

import jakarta.persistence.spi.PersistenceProviderResolver;
import jakarta.persistence.spi.PersistenceProviderResolverHolder;

public class PersistenceInjectBean extends AbstractModule {

	private Plugin plugin;
	private DatabaseConfiguration configuration;
	private String jpaUnitName;
	private PersistenceProviderResolver resolver;
	

	public PersistenceInjectBean(Plugin plugin, DatabaseConfiguration configuration) {
		this(plugin, configuration, new HibernatePersistenceProviderResolver(plugin));
	}
	
	public PersistenceInjectBean(Plugin plugin, DatabaseConfiguration configuration, PersistenceProviderResolver resolver) {
		this.plugin = plugin;
		this.configuration = configuration;
		this.jpaUnitName = "runtime-" + this.plugin.getName();
		this.resolver = resolver;
	}

	@Override
	protected void configure() {
		JpaPersistOptions options = JpaPersistOptions.builder().setAutoBeginWorkOnEntityManagerCreation(true).build();
		JpaPersistModule module = new JpaPersistModule(jpaUnitName, options);
		if(resolver != null) {
			PersistenceProviderResolverHolder.setPersistenceProviderResolver(resolver);
		}
		module.properties(this.configuration.generateProperties());
		install(module);
		bind(JpaInitializer.class).asEagerSingleton();
	}

	public static class JpaInitializer {
		@Inject
		public JpaInitializer(PersistService persistService) {
			persistService.start();
		}
	}
}
