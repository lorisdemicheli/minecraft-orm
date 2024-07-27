package io.github.lorisdemicheli.minecraft_orm.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.lorisdemicheli.minecraft_orm.test.annotation.TestOrder;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestEntity;
import io.github.lorisdemicheli.minecraft_orm.test.entity.TestSecondEntity;
import io.github.lorisdemicheli.minecraft_orm.test.service.EntityService;
import io.github.lorisdemicheli.minecraft_orm.test.util.OrderedRunner;

@RunWith(OrderedRunner.class)
public class EntityServiceTest {

	@Test
	@TestOrder(1)
	public void createEntity() {
		EntityService service = BeanService.getBeanStore().getOrCreateBean(EntityService.class);
		TestEntity entity = new TestEntity();
		entity.setDescription("Description 1");
		TestSecondEntity second = new TestSecondEntity();
		second.setEntity(entity);
		second.setName("pippo");
		entity.getSecond().add(second);
		TestEntity saved = service.saveOrUpdate(entity);
		assertEquals(Long.valueOf(1L),saved.getId());
	}
	
	@Test
	@TestOrder(2)
	public void updateEntity() {
		EntityService service = BeanService.getBeanStore().getOrCreateBean(EntityService.class);
		TestEntity entity = service.getTestEntity(1L);
		entity.setDescription("Description 2");
		TestEntity saved = service.saveOrUpdate(entity);
		assertEquals("Description 2",saved.getDescription());
	}
}
