package io.github.lorisdemicheli.minecraft_orm.bean.query;

import java.io.Serializable;

public interface QueryType<T extends Serializable> {
	public Class<T> getType();
}