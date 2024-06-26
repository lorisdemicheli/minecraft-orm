package io.github.lorisdemicheli.minecraft_orm.hibernate;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class PersistentUnitTester {

	@Inject
	private EntityManagerFactory entityManagerFactory;

	public boolean isConnectionActive() {
		return entityManagerFactory.createEntityManager() != null;
	}
}