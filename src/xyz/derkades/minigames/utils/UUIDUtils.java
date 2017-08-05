package xyz.derkades.minigames.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class UUIDUtils implements Listener {
	
	private static File file;
	private static Plugin plugin;
	
	private static YamlConfiguration config;
	
	private static void reload(){
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	private static YamlConfiguration getConfig(){
		if (config == null) reload();
		return config;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		getConfig().set("uuid." + player.getName(), player.getUniqueId().toString());
		getConfig().set("name." + player.getUniqueId(), player.getName());
		
		try {
			getConfig().save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers the PlayerJoinEvent and sets the name of the file where UUIDs will be saved. Run this method in your onEnable()
	 * @param plugin Your main class (which extends JavaPlugin). If you don't know what this means, just use "this"
	 * @param fileName The name of the file where UUIDs will be saved. This file will be in your plugin directory. For example "uuid" for "plugins/yourplugin/uuid.yml"
	 */
	public static void initialize(Plugin plugin, String fileName){
		plugin.getServer().getPluginManager().registerEvents(new UUIDUtils(), plugin);
		
		file = new File(plugin.getDataFolder(), fileName + ".yml");
		UUIDUtils.plugin = plugin;
	}
	
	public static void initialize(Plugin plugin, String fileName, String directory){
		UUIDUtils.plugin.getServer().getPluginManager().registerEvents(new UUIDUtils(), plugin);
		file = new File(plugin.getDataFolder() + "/" + directory, fileName + ".yml");
	}
	
	/**
	 * Gets the UUID from a player with the name <em>playerName</em>. Returns null if no UUID has been cached for a player with that name.
	 * @param playerName (self explanatory)
	 * @return Player UUID or null
	 */
	public static UUID getUUID(String playerName){
		if (!getConfig().isSet("uuid." + playerName))
			return null;
		
		String uuidString = getConfig().getString("uuid." + playerName);
		return UUID.fromString(uuidString);
	}
	
	/**
	 * Gets the name from a player with the UUID specified. Returns null if no name has been cached for a player with that UUID.
	 * @param uuid (self explanatory)
	 * @return Player name or null
	 */
	public static String getName(UUID uuid){
		if (!getConfig().isSet("name." + uuid))
			return null;
		
		return getConfig().getString("name." + uuid);
	}
	
	/**
	 * Returns Bukkit.getOfflinePlayer({@link #getUUID(String) getUUID(playerName)}) with <em>playerName</em> being <em>player.getName()</em>. 
	 * <br><br>
	 * Why use this instead of {@link Bukkit#getPlayer(String) Bukkit#getPlayer(playerName)}?
	 * <ul>
	 * 	<li>That method is deprecated.</li>
	 * 	<li>This method works even when Mojang's public API is down</li>
	 * 	<li>This method doesn't use up any bandwidth, because UUID info is stored offline.
	 * </ul>
	 * @param player
	 * @return
	 */
	public static OfflinePlayer getOfflinePlayer(String playerName){
		UUID uuid = getUUID(playerName);
		return Bukkit.getOfflinePlayer(uuid);
	}

}
