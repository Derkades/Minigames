package xyz.derkades.minigames;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.task.RegenerateHunger;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Queue;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.worlds.GameWorld;
import xyz.derkades.minigames.worlds.WorldTeleportCommand;
import xyz.derkades.minigames.worlds.WorldTeleportCommandCompleter;

public class Minigames extends JavaPlugin implements Listener {

	public static final float VOTE_MENU_CHANCE = 0.3f;

	private static Minigames instance;

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
	public static Game<? extends GameMap> NEXT_GAME = null;

	public static boolean BYPASS_PLAYER_MINIMUM_CHECKS = false;

	public static Economy economy = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		instance = this;

		Var.WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_WORLD = Bukkit.getWorld("minigames");
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
		this.getCommand("games").setTabCompleter(new CommandTabCompleter());
		this.getCommand("bug").setExecutor(new BugCommand());
		this.getCommand("wtp").setExecutor(new WorldTeleportCommand());
		this.getCommand("wtp").setTabCompleter(new WorldTeleportCommandCompleter());

		Scheduler.repeat(20, () -> {
			Var.WORLD.setStorm(false);
		});

		if (!this.setupEconomy()) {
			this.getLogger().severe("Vault error");
		}

		SpawnZombieShooter.init();

		ChatPoll.startup(this);

		new SneakPrevention(this);

		Queue.start();

		Scheduler.delay(20, () -> {
			GameWorld.init();

			if (Bukkit.getOnlinePlayers().size() == 0) {
				Bukkit.broadcastMessage("[System] No players online, starting games automatically");
				AutoRotate.startNewRandomGame();
			} else {
				Bukkit.broadcastMessage("[System] Players online, not starting games automatically");
			}

			for (final MPlayer player : getOnlinePlayers()) {
				player.applyLobbySettings();
			}
		});

		// To keep database connection alive
		Scheduler.repeat(60*20, () -> {
			Queue.add(() -> economy.getBalance("Derkades"));
		});

		Bukkit.broadcastMessage("enabled");
	}

	@Override
	public void onDisable(){
		instance = null;

		for (final GameWorld gWorld : GameWorld.values()) {
			gWorld.unload();
		}

		Bukkit.broadcastMessage("disabled");
	}

	public static Minigames getInstance(){
		return instance;
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

    public static List<MPlayer> getOnlinePlayers() {
    	return Bukkit.getOnlinePlayers().stream().map(MPlayer::new).collect(Collectors.toList());
    }

    public static List<MPlayer> getOnlinePlayersInRandomOrder(){
    	final List<MPlayer> players = getOnlinePlayers();
    	Collections.shuffle(players);
    	return players;
    }

}
