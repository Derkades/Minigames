package xyz.derkades.minigames;

import java.net.http.WebSocket.Listener;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.task.RegenerateHunger;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.SneakPrevention;
import xyz.derkades.minigames.utils.queue.TaskQueue;
import xyz.derkades.minigames.worlds.GameWorld;
import xyz.derkades.minigames.worlds.WorldTeleportCommand;
import xyz.derkades.minigames.worlds.WorldTeleportCommandCompleter;

public class Minigames extends JavaPlugin implements Listener {

	private static Minigames instance;

	public static Game<? extends GameMap> CURRENT_GAME = null;

	public static boolean STOP_GAMES = false;

	public static boolean BYPASS_PLAYER_MINIMUM_CHECKS = false;

	public static Economy economy = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		instance = this;

		super.saveDefaultConfig();
		
		Logger.info("Plugin enabled");

		Var.WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_LOCATION = new Location(Var.WORLD, 219.5, 64, 279.5, 180, 0);

		Logger.debugMode = getConfig().getBoolean("debug_mode");
		
		GameMap.init();

		new RegenerateHunger().runTaskTimer(this, 1*20, 1*20);
		new Points.UpdateLeaderboard().runTaskTimer(this, 2*20, 10*20);

		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);

		getCommand("spectate").setExecutor(new SpectatorCommand());
		getCommand("games").setExecutor(new Command());
		getCommand("games").setTabCompleter(new CommandTabCompleter());
		getCommand("bug").setExecutor(new BugCommand());
		getCommand("wtp").setExecutor(new WorldTeleportCommand());
		getCommand("wtp").setTabCompleter(new WorldTeleportCommandCompleter());

		Scheduler.repeat(20, () -> {
			Var.WORLD.setStorm(false);
		});

		if (!setupEconomy()) {
			getLogger().severe("Vault error");
		}

		ChatPoll.startup(this);
		SpawnZombieShooter.init();
		new SneakPrevention(this);
		new JazzRoom();

		TaskQueue.start();

		if (Logger.debugMode) {
			Logger.info("Debug mode is enabled, only going to reset players who are in adventure mode.");
			Minigames.getOnlinePlayers().stream().filter(p -> p.getGameMode() == GameMode.ADVENTURE).forEach((p) -> {
				p.applyLobbySettings();
				p.queueTeleport(Var.LOBBY_LOCATION);
			});
		} else {
			Minigames.getOnlinePlayers().forEach((p) -> {
				p.applyLobbySettings();
				p.queueTeleport(Var.LOBBY_LOCATION);
			});
		}

		Scheduler.delay(20, () -> {
			GameWorld.init();

			if (Bukkit.getOnlinePlayers().size() == 0) {
				if (!Logger.debugMode) {
					Logger.info("No players online, starting games automatically");
					AutoRotate.startNewRandomGame();
				} else {
					Logger.info("Debug mode on, not starting games automatically");
				}
			} else {
				Logger.info("Players online, not starting games automatically");
			}
		});
		
		new AutoReloader(this);
	}

	@Override
	public void onDisable(){
		CURRENT_GAME = null;
		
		Logger.debug("Unloading worlds");

		for (final GameWorld gWorld : GameWorld.values()) {
			gWorld.unload();
		}

		Logger.info("Plugin disabled");

		instance = null;
	}

	public static Minigames getInstance(){
		return instance;
	}

	/**
	 * Used by connector addon
	 */
	public static String getCurrentGameName() {
		if (CURRENT_GAME == null) {
			return "None";
		}

		return CURRENT_GAME.getName();
	}

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
			return false;
		}
        economy = rsp.getProvider();
        return economy != null;
    }
    
    public static MPlayer getPlayer(final UUID uuid) {
    	final Player player = Bukkit.getPlayer(uuid);
    	return player == null ? null : new MPlayer(player);
    }

    public static List<MPlayer> getOnlinePlayers() {
    	return Bukkit.getOnlinePlayers().stream().map(MPlayer::new).collect(Collectors.toList());
    }

    public static List<MPlayer> getOnlinePlayersInRandomOrder(){
    	final List<MPlayer> players = getOnlinePlayers();
    	Collections.shuffle(players);
    	return players;
    }

    public static void shutdown(final ShutdownReason reason, final String text) {
    	System.out.println(text);

    	if (reason == ShutdownReason.EMERGENCY_AUTOMATIC) {
    		Bukkit.broadcastMessage(ChatColor.RED + "Something went wrong, so an emergency shutdown been performed automatically. "
    				+ "Please notify a server administrator if they are not online.");
    	} else if (reason == ShutdownReason.EMERGENCY_MANUAL) {
    		Bukkit.broadcastMessage(ChatColor.RED + "An administrator has performed an emergency shutdown.");
    	} else {
    		shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Shutdown reason is invalid or null. Original text: " + text);
    	}

		Minigames.getOnlinePlayers().forEach((p) -> {
			p.clearInventory();
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(Var.LOBBY_LOCATION);
		});

		Bukkit.reload();
    }

    public enum ShutdownReason {
    	EMERGENCY_MANUAL, EMERGENCY_AUTOMATIC;
    }

}
