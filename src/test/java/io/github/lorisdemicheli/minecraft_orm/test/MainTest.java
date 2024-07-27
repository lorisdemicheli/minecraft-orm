package io.github.lorisdemicheli.minecraft_orm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BeanService.class,
	EntityServiceTest.class,
	QueryTest.class
})
public class MainTest {

}
