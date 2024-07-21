package io.github.lorisdemicheli.minecraft_orm.bean.query.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.github.lorisdemicheli.minecraft_orm.bean.query.Expression;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Filter {
	String path();
	Expression expression() default Expression.EQUAL;
	boolean negate() default false;
	boolean emptyExclude() default true;
	String disjunction() default "";
}