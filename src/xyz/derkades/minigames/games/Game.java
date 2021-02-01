package xyz.derkades.minigames.games;

import static net.md_5.bungee.api.ChatColor.DARK_GRAY;
import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.YELLOW;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.gson.stream.JsonWriter;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import xyz.derkades.derkutils.Hastebin;
import xyz.derkades.derkutils.NumberUtils;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.AutoRotate;
import xyz.derkades.minigames.ChatPoll.Poll;
import xyz.derkades.minigames.ChatPoll.PollAnswer;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Minigames.ShutdownReason;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.constants.SkipConfig;
import xyz.derkades.minigames.constants.VoteConfig;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.random.RandomPicking;
import xyz.derkades.minigames.random.RandomlyPickable;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.utils.queue.TaskQueue;

public abstract class Game<M extends GameMap> implements Listener, RandomlyPickable {

	public static final Game<? extends GameMap>[] GAMES = new Game<?>[] {
			new BowSpleef(),
			new BreakTheBlock(),
			new BuildCopy(),
			new CreeperAttack(),
			new Decay(),
			new DigDug(),
			new Dropper(),
			new Elytra(),
			new Harvest(),
			new HungerGames(),
			new IcyBlowback(),
			new GladeRoyale(),
			//new MazePvp(),
			new MissileRacer(),
//			new MissileWars(),
			new MolePvP(),
			new MurderyMister(),
			new OneInTheQuiver(),
			new Platform(),
			new ControlPoints(),
			new RegeneratingSpleef(),
			new Parkour(),
			//new SnowFight(),
			new Speedrun(),
			new TeamsBowBattle(),
			new TntRun(),
			//new TntTag(),
			new Tron(),
	};

	public abstract String getIdentifier();
	
	public abstract String getName();
	
	@Override
	public String toString() {
		return getName();
	}

	public String getAlias(){ return null; }

	public abstract String[] getDescription();

	public abstract int getRequiredPlayers();

	public abstract M[] getGameMaps();

	public abstract int getDuration();

	public int getPreDuration() {
		return 10;
	}

	public abstract void onPreStart();

	public abstract void onStart();

	public abstract int gameTimer(int secondsLeft);

	public abstract void onEnd();

	public abstract void onPlayerJoin(MPlayer player);

	public abstract void onPlayerQuit(MPlayer player);
	
	protected String getGameSpecificResultJson() {
		return null;
	}

	protected M map = null;

	// Can be used by listeners in game classes to check if the game has started.
	protected boolean started = false;
	
	private long preStartTime;
	private long startTime;
	
	// This is set in the timer for loop, modifying the variable has no effect
	private int secondsLeft;
	
	public int getSecondsLeft() {
		return this.secondsLeft;
	}
	
	private Set<UUID> gameSkipVotes;
	private boolean skipped = false;
	
	public boolean voteGameSkip(final UUID uuid) {
		return this.gameSkipVotes.add(uuid);
	}

	@SuppressWarnings("unchecked")
	public void start() {
		this.started = false;
		this.gameSkipVotes = new HashSet<>();
		this.skipped = false;

		// Pick random map
		if (this.getGameMaps() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Minigame does not have a map - " + this.getName());
		} else {
			this.map = (M) RandomPicking.getRandomMap(Arrays.asList(this.getGameMaps()));
		}

		// Send description
		for (final Player player : Bukkit.getOnlinePlayers()) {
			final String prefix = Utils.getChatPrefix(ChatColor.AQUA, 'G');

			player.sendMessage(prefix + DARK_GRAY + "-----------------------------------------");
			player.spigot().sendMessage(new ComponentBuilder("").appendLegacy(prefix)
					.append(this.getName()).bold(true).color(GOLD).append(" (" + NumberUtils.roundApprox(this.getWeight(), 1) + ")")
					.color(GRAY).bold(false).append(" [hover for help]").color(YELLOW)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new Text(new ComponentBuilder("The number shown after the game name in parentheses\n"
									+ "is the game weight. A higher weight means that the\n"
									+ "minigame has a higher chance of being picked. The\n"
									+ "game weight can be increased or decreased by voting\n"
									+ "on the poll at the end of the game.").color(GRAY).create())))
					.create());

			if (!Minigames.getInstance().getConfig().getStringList("disabled-description")
					.contains(player.getUniqueId().toString())) {
				for (final String line : this.getDescription()) {
					player.sendMessage(prefix + line);
				}
				player.sendMessage(prefix + "Minimum players: " + YELLOW + this.getRequiredPlayers());
			}

			if (this.map != null) {
				player.sendMessage(prefix + "Map: " + YELLOW + this.map.getName() + GRAY + " (" + NumberUtils.roundApprox(this.map.getWeight(), 1) + ")");
			}

			player.sendMessage(prefix + DARK_GRAY + "-----------------------------------------");
		}
		
		// Load world now to avoid lag spike when teleporting
		if (this.map.getGameWorld() != null) {
			this.map.getGameWorld().load();
		}
		
		// Countdown using sounds and the XP bar
		new BukkitRunnable() {
			int timeLeft = 200;

			@Override
			public void run() {
				for (final MPlayer player : Minigames.getOnlinePlayers()) {
					player.setExp((this.timeLeft + 10 ) / 210.0f);
					player.setLevel((int) ((this.timeLeft + 10) / 20.0));
					if (this.timeLeft < 90 && (this.timeLeft + 10) % 20 == 0) {
						player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 0.1f);
					}
					if (this.timeLeft == 20) {
						player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.5f);
					}

					if (this.timeLeft < 35) { // Hotbar scrolling joke
						player.getInventory().setHeldItemSlot(Math.abs((16 - this.timeLeft % 16)) / 2);
					}
				}

				if (this.timeLeft == 100) {
					final int online = Bukkit.getOnlinePlayers().size();
					if (online < 4) {
						Game.this.sendMessage("Many games require more players. Larger games are generally more fun, so get a few more friends online to play them!");
					} else if (online < 5) {
						Game.this.sendMessage("Some games require more players. Get a few more friends online to play them!");
					}
				}

				this.timeLeft--;

				if (this.timeLeft < 5) {
					cancel();

					for (final MPlayer player : Minigames.getOnlinePlayers()) {
						player.clearPotionEffects();
						player.clearInventory();
						player.setLevel(0);
						player.setExp(0);
						player.getInventory().setHeldItemSlot(0);
					}

					Minigames.CURRENT_GAME = Game.this;
					Bukkit.getPluginManager().registerEvents(Game.this, Minigames.getInstance());
					Game.this.begin();
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}

	private void begin() {
		this.preStartTime = System.currentTimeMillis();
		this.onPreStart();
		this.map.onPreStart();
		
		this.map.getWorld().getEntitiesByClass(Arrow.class).forEach(Entity::remove);
		this.map.getWorld().getEntitiesByClass(Trident.class).forEach(Entity::remove);
		this.map.getWorld().getEntitiesByClass(Item.class).forEach(Entity::remove);

		new BukkitRunnable() {

			private int secondsLeft = Game.this.getDuration() + Game.this.getPreDuration();

			@Override
			public void run() {
				this.secondsLeft--;

				// pre-start countdown
				if (this.secondsLeft > Game.this.getDuration()) {
					if (Game.this.getPreDuration() > 2) {
						final int preSeconds = this.secondsLeft - Game.this.getDuration();
						for (final MPlayer player : Minigames.getOnlinePlayers()) {
							if (preSeconds > 3) {
								player.sendTitle("", GRAY + "" + preSeconds);
							} else {
								player.sendTitle(GOLD + "" + preSeconds, "");
							}
						}
					}

					return;
				}

				if (this.secondsLeft == Game.this.getDuration()) {
					Minigames.getOnlinePlayers().forEach((p) -> p.sendTitle("", ""));
					Game.this.sendMessage("The game has started.");
					Game.this.startTime = System.currentTimeMillis();
					Game.this.onStart();
					Game.this.map.onStart();
					Game.this.started = true;
					return;
				}
				
//				Logger.debug("skip: %s", ((float) Game.this.gameSkipVotes.size()) / Bukkit.getOnlinePlayers().size());
				final boolean skip = (float) Game.this.gameSkipVotes.size() / Bukkit.getOnlinePlayers().size() > SkipConfig.SKIP_VOTE_PERCENTAGE;
				if (skip && this.secondsLeft > SkipConfig.SKIP_TO_SECONDS_LEFT) {
					sendMessage(String.format(SkipConfig.SKIP_MESSAGE, Game.this.gameSkipVotes.size()));
					this.secondsLeft = SkipConfig.SKIP_TO_SECONDS_LEFT;
					Game.this.skipped = true;
				}
				
				final int newSecondsLeft = Game.this.gameTimer(this.secondsLeft);
				this.secondsLeft = newSecondsLeft > 0 ? newSecondsLeft : this.secondsLeft;
				Game.this.map.onTimer(this.secondsLeft);

				if (this.secondsLeft <= 0) {
					cancel();
					Game.this.onEnd();
					Game.this.map.onEnd();
					return;
				}

				if (this.secondsLeft == 60 || this.secondsLeft == 30 || this.secondsLeft == 10 || this.secondsLeft == 5 || this.secondsLeft <= 3) {
					Game.this.sendMessage(String.format("%s seconds left", this.secondsLeft));
				}
			}

		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}


	public void sendMessage(final String message){
		Bukkit.broadcastMessage(Utils.getChatPrefix(ChatColor.AQUA, 'G') + message);
	}

	protected void endGame() {
		this.endGame(Arrays.asList());
	}

	protected void endGame(final UUID winner) {
		this.endGame(Arrays.asList(winner));
	}

	protected void endGame(final List<UUID> winners, final boolean multipleWinnersIsNoWinner) {
		if (!multipleWinnersIsNoWinner) {
			endGame(winners);
			return;
		}

		if (winners.size() == 1) {
			endGame(winners);
		} else {
			endGame(new ArrayList<>());
		}
	}

	protected void endGame(final List<UUID> winners) {
		Minigames.CURRENT_GAME = null;
		HandlerList.unregisterAll(this); // Unregister events
		this.gameSkipVotes = null;

		Utils.showEveryoneToEveryone();

		final List<Player> winnersPlayers;
		String winnersText = null;
		if (this.skipped) {
			winnersPlayers = Collections.emptyList();
		} else {
			winnersPlayers = Bukkit.getOnlinePlayers().stream().filter((p) -> winners.contains(p.getUniqueId())).collect(Collectors.toList());
			winnersText = winnersPlayers.stream().map(Player::getName).collect(Collectors.joining(", "));
		}
		
		if (this.skipped) {
			this.sendMessage("The " + this.getName() + " game has ended. There are no winners, this game was skipped.");
		} else if (winners.isEmpty()){
			this.sendMessage("The " + this.getName() + " game has ended.");
		} else if (winners.size() == 1){
			this.sendMessage("The " + this.getName() + " game has ended! Winner: " + YELLOW + winnersText);
		} else {
			this.sendMessage("The " + this.getName() + " game has ended! Winners: " + YELLOW + winnersText);
		}
		
		saveGameResult(winnersPlayers);
		
		// Give rewards
		for (final MPlayer player : Minigames.getOnlinePlayers()){
			if (winners.contains(player.getUniqueId())){
				//If player has won
				final int onlinePlayers = Bukkit.getOnlinePlayers().size();

				final int points;

				if (onlinePlayers < 3) {
					points = 3;
				} else if (onlinePlayers < 5){
					points = 4;
				} else {
					points = 5;
				}

				player.addPoints(points);
//				Queue.add(() -> Minigames.economy.depositPlayer(player.bukkit(), points));
				player.sendTitle(GOLD + "You've won",  YELLOW + "+" + points + " points");
			} else {
				player.addPoints(1);
				player.sendTitle(GOLD + "You've lost", YELLOW + "+1 point");
			}
		}

		this.showPolls();

		for (final MPlayer player : Minigames.getOnlinePlayers()){
			// Teleport the player and give them a bit of forwards and sidewards velocity
			TaskQueue.add(() -> {
				player.teleport(Var.LOBBY_LOCATION);
				player.bukkit().setVelocity(new Vector(Random.getRandomDouble() - 0.5, 0.3, -0.8));
				player.giveEffect(PotionEffectType.INVISIBILITY, 30, 0);
				player.applyLobbySettings();
			});
		}
		
		Scheduler.delay(10*20, () -> {
			AutoRotate.startNewRandomGame();
		});
		
		Scheduler.delay(20*20, () -> {
			// Unload world from previous game. It can be done now, because all players should
			// be teleported to the lobby by now.
			if (this.map.getGameWorld() != null) {
				this.map.getGameWorld().unload();
			} else {
				Logger.warning("Game %s is still in lobby world", this.getName());
			}
		});
	}
	
	private void saveGameResult(final List<Player> winners) {
		final File gameResultsDir = new File("game_results");
		if (!gameResultsDir.exists()) {
			Logger.warning("Skipped saving game data, directory '%s' does not exist.", gameResultsDir.getAbsolutePath());
			return;
		}
		
		final int gameNumber = Minigames.getInstance().getConfig().getInt("last-game-number", -1) + 1;
		
		final byte[] content;
		try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				Writer writer = new OutputStreamWriter(byteStream);
				JsonWriter json = new JsonWriter(writer)) {
			json.beginObject();
			json.name("format_version");
			json.value(1);
			
			json.name("time_pre_start");
			json.value(this.preStartTime / 1000);
			json.name("time_start");
			json.value(this.startTime / 1000);
			json.name("time_end");
			json.value(System.currentTimeMillis() / 1000);
			
			json.name("debug");
			json.value(Logger.debugModeEnabled());
			
			json.name("game");
			json.beginObject();
			json.name("identifier");
			json.value(this.getIdentifier());
			json.name("name");
			json.value(this.getName());
			json.name("size");
			json.value(this.getSize() == null ? null : this.getSize().name());
			json.name("weight");
			json.value(this.getWeight());
			json.endObject();
			
			json.name("map");
			json.beginObject();
			json.name("identifier");
			json.value(this.map.getIdentifier());
			json.name("name");
			json.value(this.map.getName());
			json.name("size");
			json.value(this.map.getSize() == null ? null : this.map.getSize().name());
			json.name("weight");
			json.value(this.map.getWeight());
			json.endObject();
			
			json.name("winners");
			writePlayersJson(json, winners);
			json.name("online_players");
			writePlayersJson(json, Bukkit.getOnlinePlayers());
			json.name("skipped");
			json.value(this.skipped);
			
			final String gameSpecific = this.getGameSpecificResultJson();
			if (gameSpecific != null) {
				json.name("game_specific");
				json.jsonValue(gameSpecific);
			}
			json.endObject();
			json.flush();
			content = byteStream.toByteArray();
		} catch (final IOException e) {
			Logger.warning("Failed to generate game result: %s", e.getMessage());
			e.printStackTrace();
			return;
		}
		
		// Go async for file/network I/O
		Scheduler.async(() -> {
			// Save to json file
			final File file = new File(gameResultsDir, gameNumber + ".json");
			try (OutputStream out = new FileOutputStream(file)){
				out.write(content);
			} catch (final IOException e) {
				Logger.warning("Failed to save game result: %s", e.getMessage());
				e.printStackTrace();
				return; // File not saving is bad! Don't continue
			}
			
			// Upload to hastebin
			String url;
			try {
				final String key = Hastebin.createPaste(content, "paste.derkad.es");
				url = "https://paste.derkad.es/" + key + ".json";
			} catch (final IOException e) {
				Logger.warning("Error while uploading game result to hastebin");
				e.printStackTrace();
				url = null; // Not uploading to hastebin is not a big deal, continue
			}
		
			final String fUrl = url;
			Scheduler.run(() -> {
				Logger.info("Game result: %s", fUrl);
				// Increment number now that we know everything went well
				Minigames.getInstance().getConfig().set("last-game-number", gameNumber);
				Minigames.getInstance().saveConfig();
			});
		});
	}
	
	private void writePlayersJson(final JsonWriter json, final Collection<? extends Player> players) throws IOException {
		json.beginArray();
		for (final Player player : players) {
			json.beginObject();
			json.name("name");
			json.value(player.getName());
			json.name("uuid");
			json.value(player.getUniqueId().toString());
			json.endObject();
		}
		json.endArray();
	}

	private void showPolls() {
		if (VoteConfig.VOTE_MENU_CHANCE > Random.getRandomFloat()) {
			Scheduler.delay(40, () -> {
				final boolean bool = Random.getRandomBoolean();

				if (bool && this.getRequiredPlayers() > 1) {
					final Poll poll = new Poll("Did you enjoy this game?", (player, option) -> {
						double weight = this.getWeight();

						if (option == 1) {
							weight *= 1.1; // Increase chance factor a bit (e.g. from to 1.5 to 1.65)
						} else if (option == 2){
							weight *= 0.9; // Decrease chance factor a bit (e.g. from 1.5 to 1.35)
						}

						player.sendMessage(ChatColor.GRAY + "Your vote has been registered.");

						if (weight > VoteConfig.SCORE_MAX) {
							weight = VoteConfig.SCORE_MAX;
						}

						this.setWeight(weight);
					}, new PollAnswer(1, "Yes", ChatColor.GREEN, "The game will be picked more often"),
							new PollAnswer(2, "No", ChatColor.RED, "The game will be picked less often"));

					Bukkit.getOnlinePlayers().forEach(poll::send);
				} else {
					final Poll poll = new Poll("Did you like this map?", (player, option) -> {
						double weight = this.map.getWeight();

						if (option == 1) {
							weight *= 1.1; // Increase chance factor a bit (e.g. from to 1.5 to 1.65)
						} else if (option == 2){
							weight *= 0.9; // Decrease chance factor a bit (e.g. from 1.5 to 1.35)
						}

						player.sendMessage(ChatColor.GRAY + "Your vote has been registered.");

						if (weight > 5) {
							weight = 5;
						}

						this.map.setWeight(weight);
					}, new PollAnswer(1, "Yes", ChatColor.GREEN, "The map will be picked more often"),
							new PollAnswer(2, "No", ChatColor.RED, "The map will be picked less often"));

					Bukkit.getOnlinePlayers().forEach(poll::send);
				}
			});
		}
	}

	@Override
	public void setWeight(final double weight) {
		if (this.getIdentifier() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Game name is null");
			return;
		}

		final String configPath = "game-voting." + this.getIdentifier();
		Minigames.getInstance().getConfig().set(configPath, weight);
		Minigames.getInstance().saveConfig();
	}

	@Override
	public double getWeight() {
		if (this.getIdentifier() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Game name is null");
			return 0;
		}

		final String configPath = "game-voting." + this.getIdentifier();
		return Minigames.getInstance().getConfig().getDouble(configPath, 1);
	}

	@Override
	public Size getSize() {
		return null;
//		if (this.getRequiredPlayers() > 4) {
//			return Size.LARGE;
//		} else if (this.getRequiredPlayers() > 2) {
//			return Size.NORMAL;
//		} else {
//			return Size.SMALL;
//		}
	}

	public static Game<? extends GameMap> fromString(final String string) {
		for (final Game<? extends GameMap> game : GAMES){
			if (game.getName().equalsIgnoreCase(string) ||
					string.equalsIgnoreCase(game.getAlias())) {
				return game;
			}
		}
		return null;
	}

}