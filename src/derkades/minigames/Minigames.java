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
import org.bukkit.event.EventException;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import derkades.minigames.games.Game;
import derkades.minigames.games.maps.GameMap;
import derkades.minigames.modules.AutoReloader;
import derkades.minigames.modules.CancelInteract;
import derkades.minigames.modules.ChatPoll;
import derkades.minigames.modules.DisableInventoryItemMove;
import derkades.minigames.modules.InfoBar;
import derkades.minigames.modules.JazzRoom;
import derkades.minigames.modules.LobbyMenuOpen;
import derkades.minigames.modules.LobbyStormDisabler;
import derkades.minigames.modules.RegenerateHunger;
import derkades.minigames.modules.ResetPlayersOnEnable;
import derkades.minigames.modules.ResourcePack;
import derkades.minigames.modules.SneakPrevention;
import derkades.minigames.modules.SpawnZombieShooter;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.PluginUnloadEvent;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.queue.TaskQueue;
import derkades.minigames.worlds.GameWorld;
import derkades.minigames.worlds.WorldTeleportCommand;
import derkades.minigames.worlds.WorldTeleportCommandCompleter;
import net.md_5.bungee.api.ChatColor;

public class Minigames extends JavaPlugin implements Listener {

	private static Minigames instance;

	public static boolean STOP_GAMES = false;

	public static boolean BYPASS_PLAYER_MINIMUM_CHECKS = false;

	public static ChatPoll CHAT_POLL = null;

	static {
		MinecraftVersion.disableUpdateCheck();

		for (final Game<?> game : Game.GAMES) {
			Validate.notNull(game.getIdentifier(), "game identifier null: " + game.getClass().getName());
			Validate.notNull(game.getName(), "game name null: " + game.getClass().getName());
			if (game.getGameMaps() != null) {
				for (final GameMap map : game.getGameMaps()) {
					Validate.notNull(map.getIdentifier(), "map identifier null: " + game.getClass().getName() + " " + map.getClass().getName());
					Validate.notNull(map.getName(), "map name is null: " + map.getIdentifier());
				}
			}
		}
	}

//	public static Economy economy = null;

	@Override
	public void onEnable(){
		instance = this;

		super.saveDefaultConfig();

		Logger.info("Plugin enabled");

		Var.LOBBY_WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_LOCATION = new Location(Var.LOBBY_WORLD, 219.5, 64, 279.5, 180, 0);

		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);

		getCommand("spectate").setExecutor(new SpectatorCommand());
		getCommand("games").setExecutor(new Command());
		getCommand("games").setTabCompleter(new CommandTabCompleter());
		getCommand("bug").setExecutor(new BugCommand());
		getCommand("wtp").setExecutor(new WorldTeleportCommand());
		getCommand("wtp").setTabCompleter(new WorldTeleportCommandCompleter());
		getCommand("voteskip").setExecutor(new VoteSkipCommand());

//		if (!setupEconomy()) {
//			getLogger().severe("Vault error");
//		}

		new AutoReloader();
		new CancelInteract();
		CHAT_POLL = new ChatPoll();
		new DisableInventoryItemMove();
		new InfoBar();
		new JazzRoom();
		new LobbyMenuOpen();
		new LobbyStormDisabler();
		new RegenerateHunger();
		new ResetPlayersOnEnable();
		new ResourcePack();
		new SneakPrevention();
		new SpawnZombieShooter();

		TaskQueue.start();

		Scheduler.delay(20, () -> {
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

		Bukkit.getPluginManager().callEvent(new PluginLoadEvent());
	}

	@Override
	public void onDisable(){
		Logger.debug("Unloading worlds");

		for (final GameWorld gWorld : GameWorld.values()) {
			gWorld.unload();
		}

		// Hacky solution to call events, regular callEvent doesn't work when plugin is disabled
		final PluginUnloadEvent event = new PluginUnloadEvent();
		for (final RegisteredListener listener : PluginUnloadEvent.getHandlerList().getRegisteredListeners()) {
			try {
				listener.callEvent(event);
			} catch (final EventException e) {
				e.printStackTrace();
			}
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
	@Deprecated
	public static String getCurrentGameName() {
		if (GameState.getCurrentState().hasGame()) {
			return GameState.getCurrentGame().getName();
		} else {
			return "None";
		}
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

    public static List<MPlayer> getOnlinePlayersInRandomOrder() {
    	final List<MPlayer> players = getOnlinePlayers();
    	Collections.shuffle(players);
    	return players;
    }

    public static void shutdown(final ShutdownReason reason, final String text) {
    	if (reason == ShutdownReason.EMERGENCY_AUTOMATIC) {
    		Bukkit.broadcastMessage(ChatColor.RED + "Something went wrong, so an emergency shutdown been performed automatically. "
    				+ "Please notify a server administrator if they are not online.");
    	} else if (reason == ShutdownReason.EMERGENCY_MANUAL) {
    		Bukkit.broadcastMessage(ChatColor.RED + "An administrator has performed an emergency shutdown.");
    	} else {
    		shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Shutdown reason is invalid or null. Original text: " + text);
    		return;
    	}

    	Logger.warning("Shutdown reason: " + text);

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
