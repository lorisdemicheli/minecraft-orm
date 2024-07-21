package io.github.lorisdemicheli.minecraft_orm.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.github.lorisdemicheli.minecraft_orm.hibernate.DatabaseConfiguration;
import io.github.lorisdemicheli.minecraft_orm.hibernate.DatabaseFileUtil;
import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistenceInjectBean;

public class BeanStore {

	private final Map<String, Object> beans = new HashMap<>();
	private Injector injector;
	private Plugin plugin;
	private DatabaseConfiguration configuration;

	public BeanStore(Plugin plugin) {
		this(plugin, DatabaseFileUtil.getOrGenerateDatabaseConfiguration(plugin));
	}
	
	public BeanStore(Plugin plugin, DatabaseConfiguration configuration) {
		this.configuration = configuration;
		this.plugin = plugin;
		try {
			this.injector = Guice.createInjector(new PersistenceInjectBean(this.plugin, this.configuration));
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Connection to database failed, stopping plugin", e);
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}

	public <T> T getOrCreateBean(Class<T> classBean) {
		return getOrCreateBean(classBean, classBean.getCanonicalName());
	}

	public <T> T getOrCreateBean(Class<T> classBean, String beanName) {
		T bean;
		if (beans.containsKey(beanName)) {
			bean = classBean.cast(beans.get(beanName));
		} else {
			bean = createBean(classBean, beanName);
		}
		return bean;
	}

	public <T> T createBean(Class<T> classBean, String beanName) {
		T instance = injector.getInstance(classBean);
		beans.put(beanName, instance);
		return instance;
	}

}
