package io.github.lorisdemicheli.minecraft_orm.test.minecraft;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

public class TestPlugin implements Plugin {
	
	private static TestPlugin instance;
	
	static {
		instance = new TestPlugin();
	}
	
	public static TestPlugin getInstance() {
		return instance;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	@Override
	public File getDataFolder() {
		return null;
	}

	@Override
	public PluginDescriptionFile getDescription() {
		return null;
	}

	@Override
	public FileConfiguration getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResource(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveConfig() {	}

	@Override
	public void saveDefaultConfig() {	}

	@Override
	public void saveResource(String resourcePath, boolean replace) {	}

	@Override
	public void reloadConfig() {	}

	@Override
	public PluginLoader getPluginLoader() {
		return null;
	}

	@Override
	public Server getServer() {
		return null;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onEnable() {
	}

	@Override
	public boolean isNaggable() {
		return false;
	}

	@Override
	public void setNaggable(boolean canNag) {
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return null;
	}

	@Override
	public BiomeProvider getDefaultBiomeProvider(String worldName, String id) {
		return null;
	}

	@Override
	public Logger getLogger() {
		return Logger.getLogger(TestPlugin.class.getSimpleName());
	}

	@Override
	public String getName() {
		return "TestPlugin";
	}

}
