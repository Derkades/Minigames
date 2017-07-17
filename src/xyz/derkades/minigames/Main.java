package xyz.derkades.minigames;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.task.ClearWeather;
import xyz.derkades.minigames.task.RegenerateHunger;
import xyz.derkades.minigames.utils.Utils;

public class Main extends JavaPlugin implements Listener {

	private static Main instance;
	
	public static final Map<String, Boolean> CAN_TAKE_DAMAGE = new HashMap<>();
	
	public static boolean IS_IN_GAME = false;
	
	public static boolean STOP_GAMES = false;
	
	public static Game NEXT_GAME = null;
	
	@Override
	public void onEnable(){
		instance = this;
		
		for (Game game : Game.getAllGames())
			getServer().getPluginManager().registerEvents(game, this);

		new ClearWeather().runTaskTimer(this, 1*20, 1*20);
		new RegenerateHunger().runTaskTimer(this, 1*20, 1*20);
		new Points.UpdateLeaderboard().runTaskTimer(this, 2*20, 10*20);
		
		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);
		getServer().getPluginManager().registerEvents(new Votifier(), this);
		
		for (Player player : Bukkit.getOnlinePlayers()){
			Utils.setCanTakeDamage(player, false);
			for (Game game : Game.getAllGames()){
				game.resetHashMaps(player);
			}
		}
		
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()){
			super.saveDefaultConfig();
		}
		
		getCommand("games").setExecutor(new Command());
	}
	
	@Override
	public void onDisable(){
		instance = null;
	}
	
	public static Main getInstance(){
		return instance;
	}
	
}
