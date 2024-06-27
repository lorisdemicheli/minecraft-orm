package io.github.lorisdemicheli.minecraft_orm.hibernate;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import io.github.lorisdemicheli.minecraft_orm.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


public class PersistentUnitTester {

	@Inject
	private EntityManager entityManager;

	@Transactional
	public void saveOrUpdate(Test t) {
		entityManager.persist(t);
	}
}
