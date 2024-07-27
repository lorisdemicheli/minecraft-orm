package io.github.lorisdemicheli.minecraft_orm.minecraft;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import io.github.lorisdemicheli.minecraft_orm.bean.BeanStore;

public class PluginBean {

	private static Map<Plugin,BeanStore> mapStore = new HashMap<>();
	
	public static <T> T getBean(Class<T> clazz) {
		return getBean(clazz, clazz.getSimpleName());
	}
	
	public static <T> T getBean(Class<T> clazz, String name) {
		return getBeanStoreForClass(clazz).getOrCreateBean(clazz, name);
	}
	
	private static BeanStore getBeanStoreForClass(Class<?> clazz) {
		Plugin plugin = PluginUtil.getPluginFromClass(clazz);
		return mapStore.computeIfAbsent(plugin, key -> new BeanStore(plugin));
	}
}
