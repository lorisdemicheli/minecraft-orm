package io.github.lorisdemicheli.minecraft_orm.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistentUnit;

public class BeanStore {

	private static final Map<String, Object> beans = new HashMap<>();
	private static Injector injector;

	private static final String FILENAME_PLUGIN_YML = "plugin.yml";
	private static final String FILENAME_DBCONFIG_YML = "dbconfig.yml";

	public static <T> T getOrCreateBean(Class<T> classBean) {
		return getOrCreateBean(classBean, null);
	}

	public static <T> T getOrCreateBean(Class<T> classBean, String beanName) {
		beanName = getBeanName(classBean, beanName);
		T bean;
		if (beans.containsKey(beanName)) {
			bean = classBean.cast(beans.get(beanName));
		} else {
			bean = createBean(classBean, beanName);
		}
		return bean;
	}

	public static <T> T createBean(Class<T> classBean, String beanName) {
		generateInjectorIfNull();
		T instance = injector.getInstance(classBean);
		beans.put(getBeanName(classBean, beanName), instance);
		return instance;
	}

	private static void generateInjectorIfNull() {
		if (injector == null) {
			try {
				Injector tmpInjector = Guice.createInjector(new PersistenceInjectBean(getPersistentUnit()));
				PersistentUnitTester tester = tmpInjector.getInstance(PersistentUnitTester.class);
				if (!tester.isConnectionActive()) {
					Plugin plugin = getCurrentPlugin();
					plugin.getLogger().warning("Connection to database failed, stopping plugin");
					Bukkit.getPluginManager().disablePlugin(plugin);
				} else {
					injector = tmpInjector;
				}
			} catch (IllegalArgumentException | SecurityException | FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static PersistentUnit getPersistentUnit() throws FileNotFoundException {
		Plugin plugin = getCurrentPlugin();
		File configPlugin = new File(plugin.getDataFolder(), FILENAME_DBCONFIG_YML);
		LoaderOptions options = new LoaderOptions();
		Yaml yaml = new Yaml(new Constructor(PersistentUnit.class, options));
		if(!configPlugin.exists()) {
			plugin.getDataFolder().mkdirs();
			try {
				configPlugin.createNewFile();
				yaml.dump(new PersistentUnit(), new FileWriter(configPlugin));
			} catch (IOException e) {
				plugin.getLogger().warning(String.format("unable to write default file %s",FILENAME_DBCONFIG_YML));
			}
		}
		return yaml.load(new FileInputStream(configPlugin));
	}

	private static Plugin getCurrentPlugin() {
		return Bukkit.getPluginManager().getPlugin(pluginName());
	}

	private static String pluginName() {
		InputStream input = BeanStore.class.getResourceAsStream(FILENAME_PLUGIN_YML);
		Yaml yaml = new Yaml();
		Map<String, Object> parsedYaml = yaml.load(input);
		return (String) parsedYaml.get("name");
	}

	private static String getBeanName(Class<?> classBean, String beanName) {
		if (beanName == null) {
			beanName = classBean.getSimpleName();
		}
		return beanName;
	}

}
