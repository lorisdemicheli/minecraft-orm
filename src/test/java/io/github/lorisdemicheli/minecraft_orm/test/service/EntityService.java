package io.github.lorisdemicheli.minecraft_orm.test.service;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;
import jakarta.persistence.EntityManager;

public class EntityService {

	@Inject
	private EntityManager em;
	
	public TestEntity getTestEntity(Long id) {
		TestEntity te = em.find(TestEntity.class, id);
		return te;
	}
	
	@Transactional
	public TestEntity saveOrUpdate(TestEntity testEntity) {
		if(testEntity.getId() != null) {
			testEntity = em.merge(testEntity);
		} else {
			em.persist(testEntity);
			testEntity = getTestEntity(testEntity.getId());
		}
		return testEntity;
	}
	
	public List<TestEntity> testEntities() {
		return em.createQuery("SELECT te FROM TestEntity te", TestEntity.class).getResultList();
	}
}
