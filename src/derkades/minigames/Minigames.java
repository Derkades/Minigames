package derkades.minigames;

import derkades.minigames.modules.CancelInteract;
import derkades.minigames.modules.ChatPoll;
import derkades.minigames.modules.CustomPlayerList;
import derkades.minigames.modules.DisableDamage;
import derkades.minigames.modules.DisableInventoryItemMove;
import derkades.minigames.modules.DynamicMotd;
import derkades.minigames.modules.FastDripleafReset;
import derkades.minigames.modules.GameWorldManagement;
import derkades.minigames.modules.HeadTextureCaching;
import derkades.minigames.modules.InfoBar;
import derkades.minigames.modules.JazzRoom;
import derkades.minigames.modules.LobbyEffects;
import derkades.minigames.modules.LobbyMenu;
import derkades.minigames.modules.LobbyStormDisabler;
import derkades.minigames.modules.RandomNameColor;
import derkades.minigames.modules.RegenerateHunger;
import derkades.minigames.modules.ResetPlayersOnEnable;
import derkades.minigames.modules.ResourcePack;
import derkades.minigames.modules.SneakPrevention;
import derkades.minigames.modules.SpawnZombieShooter;
import derkades.minigames.modules.TestWorld;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.PluginUnloadEvent;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.queue.TaskQueue;
import derkades.minigames.worlds.GameWorld;
import derkades.minigames.worlds.WorldTeleportCommand;
import derkades.minigames.worlds.WorldTeleportCommandCompleter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ocpsoft.prettytime.PrettyTime;

import java.net.http.WebSocket.Listener;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Minigames extends JavaPlugin implements Listener {

	public static final PrettyTime PRETTY_TIME = new PrettyTime();

	private static Minigames instance;

	public static boolean STOP_GAMES = false;

	public static boolean BYPASS_PLAYER_MINIMUM_CHECKS = false;

	public static ChatPoll CHAT_POLL = null;

//	static {
//		MinecraftVersion.disableUpdateCheck();
//		MinecraftVersion.disableBStats();
//	}

//	public static Economy economy = null;

	private boolean configDirty = false;

	private void registerCommand(@NotNull String name, @NotNull CommandExecutor executor, @Nullable TabCompleter completer) {
		PluginCommand command = getCommand(name);
		if (command == null){
			throw new IllegalStateException("Command missing from plugin.yml?" + name);
		} else {
			command.setExecutor(executor);
			if (completer != null) {
				command.setTabCompleter(completer);
			}
		}
	}

	@Override
	public void onEnable(){
		instance = this;

		super.saveDefaultConfig();

		Logger.info("Plugin enabled");

		Var.LOBBY_WORLD = Objects.requireNonNull(Bukkit.getWorld("minigames"), "Minigames world does not exist");
		Var.LOBBY_LOCATION = new Location(Var.LOBBY_WORLD, 219.5, 64, 279.5, 180, 0);
		Var.JAIL_LOCATION = new Location(Var.LOBBY_WORLD, 206.5, 65, 271.5);

		getServer().getPluginManager().registerEvents(new GlobalListeners(), this);

		registerCommand("spectate", new SpectatorCommand(), null);
		registerCommand("games", new Command(), new CommandTabCompleter());
		registerCommand("bug", new BugCommand(), null);
		registerCommand("wtp", new WorldTeleportCommand(), new WorldTeleportCommandCompleter());
		registerCommand("voteskip", new VoteSkipCommand(), null);

//		if (!setupEconomy()) {
//			getLogger().severe("Vault error");
//		}

		new CancelInteract();
		CHAT_POLL = new ChatPoll();
		new CustomPlayerList();
		new DisableDamage();
		new DisableInventoryItemMove();
		new DynamicMotd();
		new FastDripleafReset();
		new GameWorldManagement();
		new HeadTextureCaching();
		new InfoBar();
		new JazzRoom();
		new LobbyEffects();
		new LobbyMenu();
		new LobbyStormDisabler();
		new RandomNameColor();
		new RegenerateHunger();
		new ResetPlayersOnEnable();
		new ResourcePack();
		new SneakPrevention();
		new SpawnZombieShooter();
		new TestWorld();

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

		GameWorld.loadWorlds();
	}

	@Override
	public void onDisable() {
		// Hacky solution to call events, regular callEvent doesn't work when plugin is disabled
		final PluginUnloadEvent event = new PluginUnloadEvent();
		for (final RegisteredListener listener : PluginUnloadEvent.getHandlerList().getRegisteredListeners()) {
			try {
				listener.callEvent(event);
			} catch (final EventException e) {
				e.printStackTrace();
			}
		}

		if (this.configDirty) {
			Logger.debug("Saved config file");
			this.saveConfig();
		}

		Logger.info("Plugin disabled");
	}

	@NotNull
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

	public void queueConfigSave() {
		if (this.configDirty) {
			return;
		}

		Scheduler.delay(5*20, () -> {
			Logger.debug("Saved config file");
			this.saveConfig();
			this.configDirty = false;
		});
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

	@Nullable
    public static MPlayer getPlayer(@NotNull final UUID uuid) {
    	final Player player = Bukkit.getPlayer(uuid);
    	return player == null ? null : new MPlayer(player);
    }

    @SuppressWarnings("null")
	public static @NotNull List<@NotNull MPlayer> getOnlinePlayers() {
    	return Bukkit.getOnlinePlayers().stream().map(MPlayer::new).collect(Collectors.toList());
    }

    public static int getOnlinePlayerCount() {
    	return Bukkit.getOnlinePlayers().size();
    }

    public static @NotNull List<@NotNull MPlayer> getOnlinePlayersInRandomOrder() {
    	final @NotNull List<@NotNull MPlayer> players = getOnlinePlayers();
    	Collections.shuffle(players);
    	return players;
    }

    public static void shutdown(final ShutdownReason reason, final String text) {
    	if (reason == ShutdownReason.EMERGENCY_AUTOMATIC) {
			Bukkit.broadcast(
					Component
							.text("Something went wrong, so an emergency shutdown been performed automatically. Please "
									+ "notify a server administrator if they are not online.")
							.color(NamedTextColor.RED));
    	} else if (reason == ShutdownReason.EMERGENCY_MANUAL) {
    		Bukkit.broadcast(Component.text("An administrator has performed an emergency shutdown.").color(NamedTextColor.RED));
    	} else {
    		shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Shutdown reason is invalid or null. Original text: " + text);
    		return;
    	}

    	Logger.warning("Shutdown reason: " + text);

		Minigames.getOnlinePlayers().forEach((p) -> {
			p.clearInventory();
			p.setGameMode(GameMode.ADVENTURE);
//			p.teleport(Var.LOBBY_LOCATION);
//			p.teleportLobby();
			p.teleport(Var.JAIL_LOCATION);
		});

//		Bukkit.getPluginManager().

		Bukkit.reload();
    }

    public enum ShutdownReason {
    	EMERGENCY_MANUAL, EMERGENCY_AUTOMATIC
	}

}
