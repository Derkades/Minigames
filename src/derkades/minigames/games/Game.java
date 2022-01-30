package derkades.minigames.games;

import com.google.gson.stream.JsonWriter;
import derkades.minigames.*;
import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.constants.SkipConfig;
import derkades.minigames.constants.VoteConfig;
import derkades.minigames.modules.ChatPoll.Poll;
import derkades.minigames.modules.ChatPoll.PollAnswer;
import derkades.minigames.random.RandomPicking;
import derkades.minigames.random.RandomlyPickable;
import derkades.minigames.random.Size;
import derkades.minigames.utils.Disableable;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.event.GameResultSaveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.Hastebin;
import xyz.derkades.derkutils.NumberUtils;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.*;

public abstract class Game<M extends GameMap> implements Listener, RandomlyPickable, Disableable {

	private final @NotNull String identifier;
	private final @NotNull String name;
	private final @NotNull String@NotNull[] description;
	private final @NotNull Material material;
	private final @NotNull M@NotNull[] gameMaps;
	private final int requiredPlayers;
	private final int duration;
	private final @NotNull Set<@NotNull GameLabel> gameLabels;

	public Game(
			@NotNull String identifier,
			@NotNull String name,
			@NotNull String@NotNull[] description,
			@NotNull Material material,
			@NotNull M@NotNull[] gameMaps,
			int requiredPlayers,
			int duration,
			@NotNull Set<@NotNull GameLabel> gameLabels
	) {
		this.identifier = identifier;
		this.name = name;
		this.description = description;
		this.material = material;
		this.gameMaps = gameMaps;
		this.requiredPlayers = requiredPlayers;
		this.duration = duration;
		this.gameLabels = gameLabels;
	}

	public final @NotNull String getIdentifier() {
		return this.identifier;
	}

	public final @NotNull String getName() {
		return this.name;
	}

	public final @NotNull String@NotNull[] getDescription() {
		return this.description;
	}

	public final @NotNull Material getMaterial() {
		return this.material;
	}

	public final @NotNull M@NotNull[] getGameMaps() {
		return this.gameMaps;
	}

	public final int getRequiredPlayers() {
		return this.requiredPlayers;
	}

	public final int getDuration() {
		return this.duration;
	}

	public final @NotNull Set<@NotNull GameLabel> getGameLabels() {
		return this.gameLabels;
	}

	@Override
	public final String toString() {
		return "Game[" + getIdentifier() + "]";
	}

	public int getPreDuration() {
		return 10;
	}

	protected abstract void onPreStart();

	protected abstract void onStart();

	public abstract int gameTimer(int secondsLeft);

	public abstract boolean endEarly();

	protected abstract void onEnd();

	public abstract void onPlayerJoin(MPlayer player);

	public abstract void onPlayerQuit(MPlayer player);

	@Nullable
	protected String getGameSpecificResultJson() {
		return null;
	}

	@SuppressWarnings("null")
	protected M map = null;

	// Used by saveGameResult
	private long preStartTime;
	private long startTime;

	// This is set in the timer for loop, modifying the variable has no effect
	private int secondsLeft;
	public int getSecondsLeft() {
		return this.secondsLeft;
	}

	private Set<UUID> gameSkipVotes;

	public boolean voteGameSkip(final Player player) {
		if (this.gameSkipVotes.add(player.getUniqueId())) {
			sendFormattedPlainMessage("%s voted to skip this game using /voteskip.", player.getName());
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void start() {
		this.gameSkipVotes = new HashSet<>();

		// Pick random map
		if (this.getGameMaps() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Minigame does not have a map - " + this.getName());
		} else {
			this.map = (M) RandomPicking.getRandomMap(Arrays.asList(this.getGameMaps()));
		}

		// Send description
		for (@NotNull final Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(DARK_GRAY + "-----------------------------------------");
			player.sendMessage(Component.text().append(
					Component.text(this.getName()).decorate(TextDecoration.BOLD).color(NamedTextColor.GOLD)).append(
							Component.text(String.format(" (multiplier %.1f, hover for help)", this.getWeight()))
									.color(NamedTextColor.GRAY)
									.hoverEvent(HoverEvent.showText(Component
											.text("""
													The number shown after the game name in parentheses
													is the game weight. A higher weight means that the
													minigame has a higher chance of being picked. The
													game weight can be increased or decreased by voting
													on the poll at the end of the game.""")
											.color(NamedTextColor.GRAY)))));

			if (!Minigames.getInstance().getConfig().getStringList("disabled-description")
					.contains(player.getUniqueId().toString())) {
				if (this.getDescription() != null) {
					Arrays.stream(this.getDescription()).forEach(player::sendMessage);
				} else {
					Logger.warning("No description for game %s", this.getName());
				}
				player.sendMessage("Minimum players: " + YELLOW + this.getRequiredPlayers());
			}

			if (this.map != null) {
				player.sendMessage("Map: " + YELLOW + this.map.getName() + GRAY + " (" + NumberUtils.roundApprox(this.map.getWeight(), 1) + ")");
			}

			player.sendMessage(DARK_GRAY + "-----------------------------------------");
		}

		GameState.setState(GameState.COUNTDOWN, this);

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

//					if (this.timeLeft < 35) { // Hotbar scrolling joke
//						player.getInventory().setHeldItemSlot(Math.abs((16 - this.timeLeft % 16)) / 2);
//					}
				}

//				if (this.timeLeft == 100) {
//					final int online = Bukkit.getOnlinePlayers().size();
//					if (online < 4) {
//						Game.this.sendPlainMessage("Many games require more players. Larger games are generally more fun, so get a few more friends online to play them!");
//					} else if (online < 5) {
//						Game.this.sendPlainMessage("Some games require more players. Get a few more friends online to play them!");
//					}
//				}

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

					GameState.setState(GameState.RUNNING_COUNTDOWN, Game.this);
					Bukkit.getPluginManager().registerEvents(Game.this, Minigames.getInstance());
					Game.this.begin();
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}

	private void begin() {
		this.preStartTime = System.currentTimeMillis();
		if (this instanceof TeamGame teamGame) {
			Logger.debug("init teams backend");
			teamGame.initTeamsBackend();
		}

		this.onPreStart();
		this.map.onPreStart();

		this.map.getWorld().getEntitiesByClass(Arrow.class).forEach(Entity::remove);
		this.map.getWorld().getEntitiesByClass(Trident.class).forEach(Entity::remove);
		this.map.getWorld().getEntitiesByClass(Item.class).forEach(Entity::remove);

		new BukkitRunnable() {

			private int secondsLeft = Game.this.getDuration() + Game.this.getPreDuration() + 1; // + 1 to compensate for -- at first iteration

			@Override
			public void run() {
				Game.this.secondsLeft = --this.secondsLeft;

				// pre-start countdown
				if (this.secondsLeft > Game.this.getDuration()) {
					if (Game.this.getPreDuration() > 2) {
						final int preSeconds = this.secondsLeft - Game.this.getDuration();
						if (preSeconds < 6){
							for (final MPlayer player : Minigames.getOnlinePlayers()) {
								player.sendTitle(Title.title(
										Component.empty(),
										Component.text(preSeconds, preSeconds > 3 ? NamedTextColor.GRAY : NamedTextColor.GOLD),
										Times.of(Duration.ofMillis(100), Duration.ofMillis(800), Duration.ofMillis(100))
								));
							}
						}
					}

					return;
				}

				if (this.secondsLeft == Game.this.getDuration()) {
//					Game.this.sendMessage("The game has started.");
					Game.this.startTime = System.currentTimeMillis();
					Game.this.onStart();
					Game.this.map.onStart();
					GameState.setState(GameState.RUNNING_STARTED, Game.this);
					return;
				}

				final boolean skip = (float) Game.this.gameSkipVotes.size() / Bukkit.getOnlinePlayers().size() > SkipConfig.SKIP_VOTE_PERCENTAGE;
				if (skip && this.secondsLeft > SkipConfig.SKIP_TO_SECONDS_LEFT) {
					sendFormattedPlainMessage("Ending this game early, %s players voted to skip.", Game.this.gameSkipVotes.size());
					this.secondsLeft = SkipConfig.SKIP_TO_SECONDS_LEFT;
					GameState.setState(GameState.RUNNING_SKIPPED, Game.this);
				}

				final int newSecondsLeft = Game.this.gameTimer(this.secondsLeft);
				this.secondsLeft = newSecondsLeft > 0 ? newSecondsLeft : this.secondsLeft;
				if (this.secondsLeft > 5 &&
						GameState.getCurrentState() != GameState.RUNNING_ENDED_EARLY &&
						GameState.getCurrentState() != GameState.RUNNING_SKIPPED &&
						Game.this.endEarly()) {
					this.secondsLeft = 5;
					GameState.setState(GameState.RUNNING_ENDED_EARLY, Game.this);
				}

				Game.this.map.onTimer(this.secondsLeft);

				if (this.secondsLeft <= 0) {
					cancel();
					Game.this.onEnd();
					Game.this.map.onEnd();

					if (Game.this instanceof TeamGame teamGame) {
						Logger.debug("destroy teams backend");
						teamGame.destroyTeamsBackend();
					}
				}
			}

		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}

	protected void sendPlainMessage(final String message) {
		Bukkit.broadcast(Component.text(message).color(NamedTextColor.GRAY));
	}

	protected void sendFormattedPlainMessage(final String message, final Object... replacements) {
		Bukkit.broadcast(Component.text(String.format(message, replacements)).color(NamedTextColor.GRAY));
	}

	protected void sendMessage(final Component message) {
		Bukkit.broadcast(Component.text().append(message).build());
	}

	protected void endGame() {
		this.endGame(Collections.emptySet());
	}

	protected void endGame(final UUID winner) {
		if (winner == null) {
			this.endGame(Collections.emptySet());
		} else {
			this.endGame(Collections.singleton(winner));
		}
	}

	protected void endGame(final Set<UUID> winners, final boolean multipleWinnersIsNoWinner) {
		if (!multipleWinnersIsNoWinner) {
			endGame(winners);
			return;
		}

		if (winners.size() == 1) {
			endGame(winners);
		} else {
			endGame();
		}
	}

	protected void endGame(final Set<UUID> winners) {
		Validate.noNullElements(winners);

		final boolean skipped = GameState.getCurrentState() == GameState.RUNNING_SKIPPED;
		GameState.setState(GameState.IDLE);
		HandlerList.unregisterAll(this); // Unregister events
		this.gameSkipVotes = null;

		Utils.showEveryoneToEveryone();

		final Set<Player> winnersPlayers;
		if (skipped) {
			winnersPlayers = Collections.emptySet();
		} else {
			winnersPlayers = Bukkit.getOnlinePlayers().stream().filter((p) -> winners.contains(p.getUniqueId())).collect(Collectors.toSet());
		}

		if (skipped) {
			this.sendFormattedPlainMessage("The %s game has ended. There are no winners, this game was skipped.", this.getName());
		} else if (winnersPlayers.isEmpty()) {
			this.sendFormattedPlainMessage("The %s game has ended.", this.getName());
		} else {
			this.sendMessage(Component.text("The " + this.getName() + " game has ended! Winner" + (winnersPlayers.size() == 1 ? "" : "s") + ": ")
					.append(Component.text(winnersPlayers.stream().map(Player::getName).collect(Collectors.joining(", "))).color(NamedTextColor.YELLOW)));
		}

		saveGameResult(winnersPlayers, skipped);

		// Give rewards
		for (final MPlayer player : Minigames.getOnlinePlayers()){
			if (winners.contains(player.getUniqueId())){
				// If player has won
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
				player.sendTitle(
						Component.text("You've won", NamedTextColor.GOLD),
						Component.text("+" + points + " points", NamedTextColor.YELLOW)
						);
			} else {
				player.addPoints(1);
				player.sendTitle(
						Component.text("You've lost", NamedTextColor.GOLD),
						Component.text("+1 point", NamedTextColor.YELLOW)
						);
			}
		}

		this.showPolls();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (winners.contains(player.getUniqueId())) {
				player.queueLobbyTeleport(p -> p.setArmor(Material.GOLDEN_HELMET, null, null, null));
			} else {
				player.queueLobbyTeleport();
			}
		}

		Scheduler.delay(5*20, UpdateSigns::updateLeaderboard);

		Scheduler.delay(10*20, AutoRotate::startNewRandomGame);
	}

	private void saveGameResult(final Set<Player> winners, final boolean skipped) {
		Validate.noNullElements(winners);

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
			json.value(skipped);

			GameResultSaveEvent event = new GameResultSaveEvent(this, winners, json);
			Bukkit.getPluginManager().callEvent(event);

//			final String gameSpecific = this.getGameSpecificResultJson();
//			if (gameSpecific != null) {
//				json.name("game_specific");
//				json.jsonValue(gameSpecific);
//			}
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

			// Increment number now that we know everything went well
			Minigames.getInstance().getConfig().set("last-game-number", gameNumber);
			Minigames.getInstance().queueConfigSave();

			// Upload to hastebin
			try {
				final String key = Hastebin.createPaste(content, "paste.rkslot.nl");
				final String url = "https://paste.derkad.es/" + key + ".json";
				Logger.info("Game result: %s", url);
			} catch (final IOException e) {
				Logger.warning("Error while uploading game result to hastebin");
				e.printStackTrace();
				// Not uploading to hastebin is not a big deal, continue
			}
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
		//noinspection ConstantConditions
		if (VoteConfig.VOTE_MENU_CHANCE > ThreadLocalRandom.current().nextFloat()) {
			Scheduler.delay(40, () -> {
				final boolean game = ThreadLocalRandom.current().nextBoolean() && Bukkit.getOnlinePlayers().size() > 1;
				final String typeString = game ? "game" : "map";

				final Poll poll = Minigames.CHAT_POLL.new Poll("Did you enjoy this " + typeString + "?", (player, option) -> {
					double weight = game ? this.getWeight() : this.map.getWeight();

					if (option == 1) {
						weight *= 1.1; // Increase chance factor a bit (e.g. from to 1.5 to 1.65)
					} else if (option == 2){
						weight *= 0.9; // Decrease chance factor a bit (e.g. from 1.5 to 1.35)
					}

					player.sendMessage(ChatColor.GRAY + "Your vote has been registered.");

					if (weight > VoteConfig.SCORE_MAX) {
						weight = VoteConfig.SCORE_MAX;
					}

					if (game) {
						this.setWeight(weight);
					} else {
						this.map.setWeight(weight);
					}
				}, new PollAnswer(1, "Yes", NamedTextColor.GREEN, "The " + typeString + " will be picked more often"),
						new PollAnswer(2, "No", NamedTextColor.RED, "The " + typeString + " will be picked less often"));

				Minigames.getOnlinePlayers().forEach(poll::send);
			});
		}
	}

	@SuppressWarnings({ "unused", "null" })
	@Override
	public void setWeight(final double weight) {
		final String configPath = "game-voting." + this.getIdentifier();
		Minigames.getInstance().getConfig().set(configPath, weight);
		Minigames.getInstance().queueConfigSave();
	}

	@SuppressWarnings({ "unused", "null" })
	@Override
	public double getWeight() {
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

	@Override
	public boolean isDisabled() {
		return false;
	}

}