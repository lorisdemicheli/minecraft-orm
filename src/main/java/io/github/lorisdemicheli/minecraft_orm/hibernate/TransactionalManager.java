package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.lang.reflect.InvocationTargetException;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public class TransactionalManager {

	@SuppressWarnings("unchecked")
	public static <T> T createTransactionalProxy(T target) {
		try {
			return (T) new ByteBuddy() //
					.subclass(target.getClass()) //
					.method(ElementMatchers.any()) //
					.intercept(InvocationHandlerAdapter.of(new TransactionalInvocationHandler(target))) //
					.make() //
					.load(target.getClass().getClassLoader()) //
					.getLoaded() //
					.getDeclaredConstructor() //
					.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
