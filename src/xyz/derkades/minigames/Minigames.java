package xyz.derkades.minigames;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.task.RegenerateHunger;
import xyz.derkades.minigames.utils.Scheduler;

public class Minigames extends JavaPlugin implements Listener {

	public static final float VOTE_MENU_CHANCE = 0.25f;

	private static Minigames instance;

	private static final List<UUID> CAN_TAKE_DAMAGE = new ArrayList<>();

	public static boolean IS_IN_GAME = false;

	public static boolean STOP_GAMES = false;

	public static String LAST_GAME_NAME = null;

	/**
	 * Used by connector addon @see {@link #getCurrentGameName()}
	 */
	public static String CURRENT_GAME_NAME = "Error";

	/**
	 * Used to force the next game to be a certain game.
	 */
	public static Game NEXT_GAME = null;

	public static boolean BYPASS_PLAYER_MINIMUM_CHECKS = false;

	public static Economy economy = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		instance = this;

		Var.WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_LOCATION = new Location(Var.WORLD, 219.5, 64, 279.5, 180, 0);
		Var.IN_GAME_LOBBY_LOCATION = new Location(Var.WORLD, 203.5, 80, 245.5, 0, 0);
		Var.NO_SPECTATOR_LOCATION = new Location(Var.WORLD, 199.5, 81, 247.5, 0, 0);

		new RegenerateHunger().runTaskTimer(this, 1*20, 1*20);
		new Points.UpdateLeaderboard().runTaskTimer(this, 2*20, 10*20);

		this.getServer().getPluginManager().registerEvents(new GlobalListeners(), this);

		final File file = new File(this.getDataFolder(), "config.yml");
		if (!file.exists()){
			super.saveDefaultConfig();
		}

		this.getCommand("games").setExecutor(new Command());
		this.getCommand("bug").setExecutor(new BugCommand());

		Scheduler.repeat(20, () -> {
			Var.WORLD.setStorm(false);
		});

		if (!this.setupEconomy()) {
			this.getLogger().severe("Vault error");
		}

		ChatPoll.startup(this);

		Scheduler.repeat(60*20, () -> {
			economy.getBalance("Derkades");
		});
	}

	@Override
	public void onDisable(){
		instance = null;
	}

	public static Minigames getInstance(){
		return instance;
	}

	public static boolean canTakeDamage(final Player player){
		return CAN_TAKE_DAMAGE.contains(player.getUniqueId());
	}

	public static void setCanTakeDamage(final Player player, final boolean value){
		if (value) {
			CAN_TAKE_DAMAGE.add(player.getUniqueId());
		} else {
			CAN_TAKE_DAMAGE.remove(player.getUniqueId());
		}
	}

	/**
	 * Used by connector addon
	 */
	public static String getCurrentGameName() {
		if (!IS_IN_GAME) {
			return "None";
		}

		return CURRENT_GAME_NAME;
	}

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        final RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static void giveLobbyInventoryItems(final Player player) {
    	if (player.hasPermission("games.torch")) {
			player.getInventory().setItem(7, new ItemBuilder(Material.REDSTONE_TORCH)
					.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Staff lounge key")
					.lore(ChatColor.YELLOW + "Place in upper-south-east-corner on gray terracotta")
					.canPlaceOn("cyan_terracotta")
					.create());
		}

		player.getInventory().setItem(8, new ItemBuilder(Material.COMPARATOR)
				.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Menu")
				.lore(ChatColor.YELLOW + "Click to open menu")
				.create());
    }

}
