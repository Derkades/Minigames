package derkades.minigames.utils;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: Use utility from paper?
public class UUIDUtils implements Listener {

//	@NotNull
//	private final Plugin plugin;
	@NotNull
	private final File file;
	@NotNull
	private final YamlConfiguration config;

	public UUIDUtils(@NotNull final Plugin plugin, @NotNull final File file) {
//		this.plugin = plugin;
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(this.file);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

	}

//	private void reload(){
//		this.config = YamlConfiguration.loadConfiguration(this.file);
//	}

//	private YamlConfiguration getConfig(){
//		if (this.config == null) {
//			reload();
//		}
//		return this.config;
//	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(final PlayerJoinEvent event){
		final Player player = event.getPlayer();

		this.config.set("uuid." + player.getName(), player.getUniqueId().toString());
		this.config.set("name." + player.getUniqueId(), player.getName());

		try {
			this.config.save(this.file);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * Registers the PlayerJoinEvent and sets the name of the file where UUIDs will be saved. Run this method in your onEnable()
//	 * @param plugin Your main class (which extends JavaPlugin). If you don't know what this means, just use "this"
//	 * @param fileName The name of the file where UUIDs will be saved. This file will be in your plugin directory. For example "uuid" for "plugins/yourplugin/uuid.yml"
//	 */
//	public static void initialize(final Plugin plugin, final String fileName){
//
//
//		this.file = new File(plugin.getDataFolder(), fileName + ".yml");
//		UUIDUtils.plugin = plugin;
//	}

//	public static void initialize(final Plugin plugin, final String fileName, final String directory){
//		UUIDUtils.plugin.getServer().getPluginManager().registerEvents(new UUIDUtils(), plugin);
//		this.file = new File(plugin.getDataFolder() + "/" + directory, fileName + ".yml");
//	}

	/**
	 * Gets the UUID from a player with the name <em>playerName</em>. Returns null if no UUID has been cached for a player with that name.
	 * @param playerName (self explanatory)
	 * @return Player UUID or null
	 */
	public @Nullable UUID getUUID(@NotNull final String playerName) {
		if (!this.config.isSet("uuid." + playerName)) {
			return null;
		}

		final String uuidString = this.config.getString("uuid." + playerName);
		return UUID.fromString(uuidString);
	}

	/**
	 * Gets the name from a player with the UUID specified. Returns null if no name has been cached for a player with that UUID.
	 * @param uuid (self explanatory)
	 * @return Player name or null
	 */
	public @Nullable String getName(@NotNull final UUID uuid){
		if (!this.config.isSet("name." + uuid)) {
			return null;
		}

		return this.config.getString("name." + uuid);
	}

	public @Nullable OfflinePlayer getOfflinePlayer(@NotNull final String playerName){
		final UUID uuid = getUUID(playerName);
		return uuid == null ? null : Bukkit.getOfflinePlayer(uuid);
	}

}
