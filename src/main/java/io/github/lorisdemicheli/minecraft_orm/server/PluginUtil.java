package io.github.lorisdemicheli.minecraft_orm.server;

import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUtil {


	public static Plugin getPluginFromClass(Class<?> clazz) {
		return Stream.of(Bukkit.getPluginManager().getPlugins())
				.filter(p->p.getClass().getClassLoader().equals(clazz.getClassLoader()))
				.findFirst()
				.get();
	}
	
}
