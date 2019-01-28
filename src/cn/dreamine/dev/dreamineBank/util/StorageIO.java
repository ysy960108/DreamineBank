package cn.dreamine.dev.dreamineBank.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.java.JavaPlugin;

import cn.dreamine.dev.dreamineBank.DreamineBank;

public class StorageIO {
	private DreamineBank plugin;

	public StorageIO(DreamineBank plugin){
		this.plugin = plugin;
	}
	public StorageIO(){
	}

	private FileConfiguration storageConfig = null;
	private File storageConfigFile = null;


	public static FileConfiguration load(JavaPlugin plugin, String fileName){
		File file = new File(plugin.getDataFolder(), fileName);
		if(!file.exists()){
			try{
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return YamlConfiguration.loadConfiguration(file);
	}

	public void reloadStorageConfig() throws FileNotFoundException, UnsupportedEncodingException {
		if (storageConfigFile == null) {
			storageConfigFile = new File(plugin.getDataFolder(), "storage.yml");
		}
		storageConfig = YamlConfiguration.loadConfiguration(storageConfigFile);

		// Look for defaults in the jar
		//Reader defConfigStream = null;
		/*InputStream in = new FileInputStream("storage.yml");*/
		//defConfigStream = new InputStreamReader(in);
		Reader defConfigStream = new InputStreamReader(plugin.getResource("storage.yml"), "UTF8");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			storageConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getStorageConfig() throws FileNotFoundException, UnsupportedEncodingException {
		if (storageConfig == null) {
			reloadStorageConfig();
		}
		return storageConfig;
	}

	public void saveStorageConfig() {
		if (storageConfig == null || storageConfigFile == null) {
			return;
		}
		try {
			getStorageConfig().save(storageConfigFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + storageConfigFile, ex);
		}
	}

	public void saveDefaultStorageConfig() {
		if (storageConfigFile == null) {
			storageConfigFile = new File(plugin.getDataFolder(), "storage.yml");
		}
		if (!storageConfigFile.exists()) {
			//plugin.saveResource("storage.yml", false);
		}
	}


}
