package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.util.List;

import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceProviderResolver;

public class HibernatePersistenceProviderResolver implements PersistenceProviderResolver {

	@Override
	public List<PersistenceProvider> getPersistenceProviders() {
		return List.of(new MinecraftHibernatePeristenceProvider());
	}

	@Override
	public void clearCachedProviders() {	}

}
