package xyz.derkades.minigames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import xyz.derkades.minigames.board.BoardPlayer;
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

		Var.WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_WORLD = Bukkit.getWorld("minigames");
		Var.LOBBY_LOCATION = new Location(Var.WORLD, 219.5, 64, 279.5, 180, 0);
//		Var.IN_GAME_LOBBY_LOCATION = new Location(Var.WORLD, 203.5, 80, 245.5, 0, 0);
		Var.NO_SPECTATOR_LOCATION = new Location(Var.WORLD, 199.5, 81, 247.5, 0, 0);

		Logger.debugMode = getConfig().getBoolean("debug_mode");

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

//		SpawnZombieShooter.init();

		ChatPoll.startup(this);

		new SneakPrevention(this);

		Queue.start();

		// Delete all NPCs
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		registry.forEach((n) -> {
			Logger.debug("Unregistering NPC with id %s", n.getId());
			n.destroy();
			registry.deregister(n);
		});

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

			Minigames.getOnlinePlayers().stream().map(BoardPlayer::new)
			.forEach(p -> {
				p.createNpc();
				p.teleportToBoard(true);
			});
		});

		Logger.info("Plugin enabled");
	}

	@Override
	public void onDisable(){
		CURRENT_GAME = null;

		for (final GameWorld gWorld : GameWorld.values()) {
			gWorld.unload();
		}

		Logger.info("Plugin disabled");

		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final List<NPC> npcsToUnregister = new ArrayList<>();
		registry.forEach((npc) -> {
			npc.despawn(DespawnReason.REMOVAL);
			npc.destroy();
			npcsToUnregister.add(npc);
			Logger.debug("Removed NPC %s id %s", npc.getName(), npc.getId());
		});

		instance = null;
	}

	public static Minigames getInstance(){
		return instance;
	}

	/**
	 * Used by connector addon
	 */
	public static String getCurrentGameName() {
		if (CURRENT_GAME == null)
			return "None";

		return CURRENT_GAME.getName();
	}

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
			return false;
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
