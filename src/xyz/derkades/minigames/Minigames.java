package xyz.derkades.minigames;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.derkades.minigames.games.DigDug;
import xyz.derkades.minigames.games.Dropper;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.JungleRun;
import xyz.derkades.minigames.games.Platform;
import xyz.derkades.minigames.games.RegeneratingSpleef;
import xyz.derkades.minigames.games.SaveTheSnowman;
import xyz.derkades.minigames.games.SnowFight;
import xyz.derkades.minigames.games.Speedrun;
import xyz.derkades.minigames.task.RegenerateHunger;
import xyz.derkades.minigames.utils.Scheduler;

public class Minigames extends JavaPlugin implements Listener {

	private static Minigames instance;
	
	//public static final Map<String, Boolean> CAN_TAKE_DAMAGE = new HashMap<>();
	private static final List<UUID> CAN_TAKE_DAMAGE = new ArrayList<>();
	
	public static boolean IS_IN_GAME = false;
	
	public static boolean STOP_GAMES = false;
	
	/**
	 * Used to force the next game to be a certain game.
	 */
	public static Game NEXT_GAME = null;
	
	public static final Game[] GAMES = new Game[] {
			new Dropper(),
			new Platform(),
			new JungleRun(),
			new RegeneratingSpleef(),
			new SaveTheSnowman(),
			new SnowFight(),
			//new MazePvP(),
			//new Elytra(),
			new Speedrun(),
			new DigDug(),
			//new Mine(),
	};
	
	@Override
	public void onEnable(){
		instance = this;
		
		Var.WORLD = Bukkit.getWorld("minigames");

		new RegenerateHunger().runTaskTimer(this, 1*20, 1*20);
		new Points.UpdateLeaderboard().runTaskTimer(this, 2*20, 10*20);
		
		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);
		getServer().getPluginManager().registerEvents(new Votifier(), this);
		
		for (Player player : Bukkit.getOnlinePlayers()){
			for (Game game : Minigames.GAMES){
				game.resetHashMaps(player);
			}
		}
		
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()){
			super.saveDefaultConfig();
		}
		
		getCommand("games").setExecutor(new Command());
		
		Scheduler.runSyncRepeatingTask(20, () -> {
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
	
}
