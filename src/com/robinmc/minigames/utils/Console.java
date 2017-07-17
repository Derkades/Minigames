package com.robinmc.minigames.utils;

import org.bukkit.Bukkit;

public class Console {
	
	/**
	 * Execute a command as the console (without the /)
	 * @param cmd The command to be executed
	 */
	public static void sendCommand(String cmd){
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}

}