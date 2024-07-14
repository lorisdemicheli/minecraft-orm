package io.github.lorisdemicheli.minecraft_orm.test;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;
import jakarta.persistence.EntityManager;

public class TestEntityService {

	@Inject
	private EntityManager em;
	
	public TestEntity getTestEntity(String uuid) {
		TestEntity te = em.find(TestEntity.class, uuid);
		return te;
	}
	
	@Transactional
	public TestEntity saveOrUpdate(TestEntity testEntity) {
		if(getTestEntity(testEntity.getId()) != null) {
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
