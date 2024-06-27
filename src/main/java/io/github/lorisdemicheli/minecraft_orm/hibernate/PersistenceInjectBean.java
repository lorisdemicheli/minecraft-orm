package io.github.lorisdemicheli.minecraft_orm.hibernate;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.persist.jpa.JpaPersistOptions;

import jakarta.persistence.spi.PersistenceProviderResolverHolder;

public class PersistenceInjectBean extends AbstractModule {
	
	private PersistentUnit persistentUnit;
	
	public PersistenceInjectBean(PersistentUnit persistentUnit) {
		this.persistentUnit = persistentUnit;
	}

	@Override
	protected void configure() {
		JpaPersistOptions options = JpaPersistOptions.builder()
				.setAutoBeginWorkOnEntityManagerCreation(true)
				.build();
		JpaPersistModule module = new JpaPersistModule("runtime-unit",options);
		PersistenceProviderResolverHolder.setPersistenceProviderResolver(new HibernatePersistenceProviderResolver());
		module.properties(persistentUnit.generateProperties());
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
