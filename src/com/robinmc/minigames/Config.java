package com.robinmc.minigames;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	
	public static FileConfiguration getConfig(){
		return Main.getInstance().getConfig();
	}

}
