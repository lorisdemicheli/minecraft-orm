package io.github.lorisdemicheli.minecraft_orm.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistenceInjectBean;
import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistentUnit;
import io.github.lorisdemicheli.minecraft_orm.hibernate.PersistentUnitTester;

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

	private static Plugin getCurrentPlugin() {
		return Bukkit.getPluginManager().getPlugin(pluginName());
	}

	private static String pluginName() {
		InputStream input =  findRealClass().getResourceAsStream("/" + FILENAME_PLUGIN_YML);
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

	//TODO trovare modo migliore, così non va benissimo
	private static Class<?> findRealClass() {
		List<StackTraceElement> stackTrace = Arrays.asList(Thread.currentThread().getStackTrace());
		try {
			StackTraceElement element = stackTrace.stream()
					.filter(e -> excludeStart.stream().noneMatch(c -> e.getClassName().startsWith(c))).findFirst()
					.get();
			ClassLoader classLoader = Stream.of(Bukkit.getPluginManager().getPlugins())
					.map(p->p.getClass().getClassLoader())
					.filter(cl -> {
						try {
							cl.loadClass(element.getClassName());
							return true;
						} catch (ClassNotFoundException e) {
							return false;
						}
					})
					.findFirst().get();
			return classLoader.loadClass(element.getClassName());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
