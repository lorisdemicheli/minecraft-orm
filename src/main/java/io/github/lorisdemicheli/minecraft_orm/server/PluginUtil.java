package io.github.lorisdemicheli.minecraft_orm.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

public class PluginUtil {
	
	private static final List<String> excludeStart;
	
	static {
		excludeStart = new ArrayList<String>();
		excludeStart.add("io.github.lorisdemicheli.minecraft_orm");
		excludeStart.add("java");
		excludeStart.add("net.minecraft");
		excludeStart.add("org.bukkit");
		excludeStart.add("com.google");
		excludeStart.add("jakarta.persistenc");
	}
	
	private static final String FILENAME_PLUGIN_YML = "plugin.yml";

	public static Plugin getCurrentPlugin() {
		return Bukkit.getPluginManager().getPlugin(pluginName());
	}
	
	public static ClassLoader getClassLoader() {
		return getClassLoader().getClass().getClassLoader();
	}
	
	public static Logger getLogger() {
		return getCurrentPlugin().getLogger();
	}

	private static String pluginName() {
		InputStream input =  findRealClass().getResourceAsStream("/" + FILENAME_PLUGIN_YML);
		Yaml yaml = new Yaml();
		Map<String, Object> parsedYaml = yaml.load(input);
		return (String) parsedYaml.get("name");
	}

	//TODO trovare modo migliore, così non va benissimo
	private static Class<?> findRealClass() {
	List<StackTraceElement> stackTrace = Arrays.asList(Thread.currentThread().getStackTrace());
	try {
		StackTraceElement element = stackTrace.stream()
				.filter(e -> excludeStart.stream().noneMatch(c -> e.getClassName().startsWith(c))).findFirst()
				.get();
		System.out.println(element);
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
