package derkades.minigames;

import java.net.http.WebSocket.Listener;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import derkades.minigames.games.Game;
import derkades.minigames.games.maps.GameMap;
import derkades.minigames.task.RegenerateHunger;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.SneakPrevention;
import derkades.minigames.utils.queue.TaskQueue;
import derkades.minigames.worlds.GameWorld;
import derkades.minigames.worlds.WorldTeleportCommand;
import derkades.minigames.worlds.WorldTeleportCommandCompleter;
import net.md_5.bungee.api.ChatColor;

public class Minigames extends JavaPlugin implements Listener {

	private static Minigames instance;

	public static Game<? extends GameMap> CURRENT_GAME = null;

	public static boolean STOP_GAMES = false;

	public static boolean BYPASS_PLAYER_MINIMUM_CHECKS = false;
	
	static {
		MinecraftVersion.disableUpdateCheck();
	}
	
//	public static Economy economy = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		instance = this;

		super.saveDefaultConfig();
		
		integrityCheck();

		Logger.debugMode = getConfig().getBoolean("debug_mode");
		
		Logger.info("Plugin enabled");

		Var.WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_LOCATION = new Location(Var.WORLD, 219.5, 64, 279.5, 180, 0);
		GameMap.init();

		new RegenerateHunger().runTaskTimer(this, 40, 40);

		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);

		getCommand("spectate").setExecutor(new SpectatorCommand());
		getCommand("games").setExecutor(new Command());
		getCommand("games").setTabCompleter(new CommandTabCompleter());
		getCommand("bug").setExecutor(new BugCommand());
		getCommand("wtp").setExecutor(new WorldTeleportCommand());
		getCommand("wtp").setTabCompleter(new WorldTeleportCommandCompleter());
		getCommand("voteskip").setExecutor(new VoteSkipCommand());

		Scheduler.repeat(20, () -> {
			Var.WORLD.setStorm(false);
		});

//		if (!setupEconomy()) {
//			getLogger().severe("Vault error");
//		}

		ChatPoll.startup(this);
		SpawnZombieShooter.init();
		new SneakPrevention(this);
		new JazzRoom();

		TaskQueue.start();

		if (Logger.debugMode) {
			Logger.info("Debug mode is enabled, only going to reset players who are not in creative mode.");
			Minigames.getOnlinePlayers().stream().filter(p -> p.getGameMode() != GameMode.CREATIVE).forEach((p) -> {
				Logger.debug("Resetting player %s (debug mode, adventure)", p.getName());
				p.applyLobbySettings();
				p.queueTeleport(Var.LOBBY_LOCATION);
			});
		} else {
			Minigames.getOnlinePlayers().forEach((p) -> {
				Logger.debug("Resetting player %s (no debug mode)", p.getName());
				p.applyLobbySettings();
				p.queueTeleport(Var.LOBBY_LOCATION);
			});
		}

		Scheduler.delay(20, () -> {
//			GameWorld.init();

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
	
	private void integrityCheck() {
		for (final Game<?> game : Game.GAMES) {
			Validate.notNull(game.getIdentifier(), game.getClass().getName());
			Validate.notNull(game.getName(), game.getClass().getName());
			if (game.getGameMaps() != null) {
				for (final GameMap map : game.getGameMaps()) {
					Validate.notNull(game.getIdentifier(), map.getClass().getName());
					Validate.notNull(map.getName(), map.getClass().getName());
				}
			}
		}
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

//    private boolean setupEconomy() {
//        if (getServer().getPluginManager().getPlugin("Vault") == null) {
//			return false;
//		}
//        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
//        if (rsp == null) {
//			return false;
//		}
//        economy = rsp.getProvider();
//        return economy != null;
//    }
    
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
