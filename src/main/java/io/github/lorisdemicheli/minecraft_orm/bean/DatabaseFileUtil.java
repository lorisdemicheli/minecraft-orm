package io.github.lorisdemicheli.minecraft_orm.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;


public class DatabaseFileUtil {
	
	public static final String FILENAME_DBCONFIG_YML = "dbconfig.yml";
	
	public static DatabaseConfiguration getOrGenerateDatabaseConfiguration(Plugin plugin) {
		File configPlugin = new File(plugin.getDataFolder(), FILENAME_DBCONFIG_YML);
		DumperOptions yamlDumperOptions = new DumperOptions();
        yamlDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        LoaderOptions yamlLoaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(DatabaseConfiguration.class,yamlLoaderOptions);
        YamlRepresenter representer = new YamlRepresenter(yamlDumperOptions);
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        representer.addClassTag(DatabaseConfiguration.class, Tag.MAP);

        Yaml yaml = new Yaml(constructor, representer, yamlDumperOptions, yamlLoaderOptions);
		if (!configPlugin.exists()) {
			plugin.getDataFolder().mkdirs();
			try {
				configPlugin.createNewFile();
				yaml.dump(new DatabaseConfiguration(), new FileWriter(configPlugin));
			} catch (IOException e) {
				plugin.getLogger()
						.warning(String.format("unable to write dbconfig default file %s", FILENAME_DBCONFIG_YML));
			}
		}
		try {
			return yaml.load(new FileInputStream(configPlugin));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}	

}
