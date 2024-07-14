package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.util.List;

import org.bukkit.plugin.Plugin;

import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceProviderResolver;

public class HibernatePersistenceProviderResolver implements PersistenceProviderResolver {
	
	private Plugin plugin;
	
	public HibernatePersistenceProviderResolver(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<PersistenceProvider> getPersistenceProviders() {
		return List.of(new MinecraftHibernatePeristenceProvider(this.plugin));
	}

	@Override
	public void clearCachedProviders() {	}

}
