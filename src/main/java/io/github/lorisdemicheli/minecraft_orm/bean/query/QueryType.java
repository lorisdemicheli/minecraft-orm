package io.github.lorisdemicheli.minecraft_orm.bean.query;

public interface QueryType<T> {
	public Class<T> getType();
}