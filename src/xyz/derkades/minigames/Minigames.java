package xyz.derkades.minigames;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.task.RegenerateHunger;
import xyz.derkades.minigames.utils.Scheduler;

public class Minigames extends JavaPlugin implements Listener {

	public static final float VOTE_MENU_CHANCE = 0.4f;
	
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
	
	@Override
	public void onEnable(){
		instance = this;
		
		Var.WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_LOCATION = new Location(Var.WORLD, 219.5, 64, 279.5, 180, 0);
		
		new RegenerateHunger().runTaskTimer(this, 1*20, 1*20);
		new Points.UpdateLeaderboard().runTaskTimer(this, 2*20, 10*20);
		
		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);
		//getServer().getPluginManager().registerEvents(new Votifier(), this);
		
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()){
			super.saveDefaultConfig();
		}
		
		getCommand("games").setExecutor(new Command());
		
		Scheduler.repeat(20, () -> {
			Var.WORLD.setStorm(false);
		});
	}
	
	@Override
	public void onDisable(){
		instance = null;
	}
	
	public static Minigames getInstance(){
		return instance;
	}
	
	public static boolean canTakeDamage(Player player){
		return CAN_TAKE_DAMAGE.contains(player.getUniqueId());
	}
	
	public static void setCanTakeDamage(Player player, boolean value){
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
	
}
