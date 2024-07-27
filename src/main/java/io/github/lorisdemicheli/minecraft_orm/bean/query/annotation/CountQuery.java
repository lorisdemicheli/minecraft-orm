package io.github.lorisdemicheli.minecraft_orm.bean.query.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface CountQuery {
	String value();

	boolean nativeSql() default false;
}
