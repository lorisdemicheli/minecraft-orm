package io.github.lorisdemicheli.minecraft_orm.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.plugin.Plugin;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.service.spi.ServiceException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.github.lorisdemicheli.minecraft_orm.Test;
import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistenceInjectBean;
import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistentUnit;
import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistentUnitTester;
import io.github.lorisdemicheli.minecraft_orm.server.PluginUtil;
import jakarta.persistence.Persistence;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class BeanStore {

	private static final Map<String, Object> beans = new HashMap<>();
	private static Injector injector;

	private static final String FILENAME_PLUGIN_YML = "plugin.yml";
	private static final String FILENAME_DBCONFIG_YML = "dbconfig.yml";

	private static final List<String> excludeStart;

	static {
		excludeStart = new ArrayList<String>();
		excludeStart.add("io.github.lorisdemicheli.minecraft_orm");
		excludeStart.add("java");
		excludeStart.add("net.minecraft");
		excludeStart.add("org.bukkit");
	}

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
				injector = Guice.createInjector(new PersistenceInjectBean(getPersistentUnit()));
			} catch (Exception e) {
				Plugin plugin = PluginUtil.getCurrentPlugin();
				plugin.getLogger().log(Level.SEVERE,"Connection to database failed, stopping plugin",e);
				Bukkit.getPluginManager().disablePlugin(plugin);
			}
		}
	}
	
//	public static void main(String[] args) {
//		Injector tmpInjector = Guice.createInjector(new PersistenceInjectBean(new PersistentUnit()));
//		PersistentUnitTester tester = tmpInjector.getInstance(PersistentUnitTester.class);
//		tester.saveOrUpdate(new Test("234"));
//		System.out.println("UE");
//	}

	private static PersistentUnit getPersistentUnit() throws FileNotFoundException {
		Plugin plugin = PluginUtil.getCurrentPlugin();
		File configPlugin = new File(plugin.getDataFolder(), FILENAME_DBCONFIG_YML);
		DumperOptions yamlDumperOptions = new DumperOptions();
        yamlDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        LoaderOptions yamlLoaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(PersistentUnit.class,yamlLoaderOptions);
        YamlRepresenter representer = new YamlRepresenter(yamlDumperOptions);
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        representer.addClassTag(PersistentUnit.class, Tag.MAP);

        Yaml yaml = new Yaml(constructor, representer, yamlDumperOptions, yamlLoaderOptions);
		if (!configPlugin.exists()) {
			plugin.getDataFolder().mkdirs();
			try {
				configPlugin.createNewFile();
				yaml.dump(new PersistentUnit(), new FileWriter(configPlugin));
			} catch (IOException e) {
				plugin.getLogger()
						.warning(String.format("unable to write dbconfig default file %s", FILENAME_DBCONFIG_YML));
			}
		}
		return yaml.load(new FileInputStream(configPlugin));
	}	

	private static String getBeanName(Class<?> classBean, String beanName) {
		if (beanName == null) {
			beanName = classBean.getSimpleName();
		}
		return beanName;
	}

	//TODO trovare modo migliore, così non va benissimo
//	private static Class<?> findRealClass() {
//		List<StackTraceElement> stackTrace = Arrays.asList(Thread.currentThread().getStackTrace());
//		try {
//			StackTraceElement element = stackTrace.stream()
//					.filter(e -> excludeStart.stream().noneMatch(c -> e.getClassName().startsWith(c))).findFirst()
//					.get();
//			ClassLoader classLoader = Stream.of(Bukkit.getPluginManager().getPlugins())
//					.map(p->p.getClass().getClassLoader())
//					.filter(cl -> {
//						try {
//							cl.loadClass(element.getClassName());
//							return true;
//						} catch (ClassNotFoundException e) {
//							return false;
//						}
//					})
//					.findFirst().get();
//			return classLoader.loadClass(element.getClassName());
//		} catch (ClassNotFoundException e) {
//			return null;
//		}
//	}
	
}
