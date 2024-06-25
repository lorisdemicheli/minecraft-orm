package io.github.lorisdemicheli.minecraft_orm.bean;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistentUnit;

public class PersistenceInjectBean extends AbstractModule {
	
	private PersistentUnit persistentUnit;
	
	public PersistenceInjectBean(PersistentUnit persistentUnit) {
		this.persistentUnit = persistentUnit;
	}

	@Override
	protected void configure() {
		JpaPersistModule module = new JpaPersistModule("runtime-unit");
		module.properties(persistentUnit.generateProperties());
		install(module);
	}
}
