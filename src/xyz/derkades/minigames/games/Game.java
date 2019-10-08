package xyz.derkades.minigames.games;

import static net.md_5.bungee.api.ChatColor.DARK_GRAY;
import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import xyz.derkades.derkutils.NumberUtils;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.ChatPoll.Poll;
import xyz.derkades.minigames.ChatPoll.PollAnswer;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Minigames.ShutdownReason;
import xyz.derkades.minigames.board.Board;
import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.constants.BoardConfig;
import xyz.derkades.minigames.constants.VoteConfig;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.random.RandomPicking;
import xyz.derkades.minigames.random.RandomlyPickable;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public abstract class Game<M extends GameMap> implements Listener, RandomlyPickable {

	public static final Game<? extends GameMap>[] GAMES = new Game<?>[] {
			new BreakTheBlock(),
			new CreeperAttack(),
			new DigDug(),
			new Dropper(),
			new Elytra(),
			//new Harvest(),
			new HungerGames(),
			new IcyBlowback(),
			new GladeRoyale(),
			//new MazePvp(),
			new MolePvP(),
			new MurderyMister(),
			new OneInTheQuiver(),
			new Platform(),
			new RegeneratingSpleef(),
			new Parkour(),
			//new SnowFight(),
			//new Speedrun(),
			new TeamsBowBattle(),
			new TntRun(),
			//new TntTag(),
			new Tron(),
	};

	public abstract String getName();

	public String getAlias(){ return ""; }

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

	public abstract void onPlayerJoin(MPlayer player); // TODO call on join and cancel teleport

	public abstract void onPlayerQuit(MPlayer player); // TODO call on quit

	protected M map = null;

	// Can be used by listeners in game classes to check if the game has started.
	protected boolean started = false;

	@SuppressWarnings("unchecked")
	public void start() {
		this.started = false;

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
							new ComponentBuilder("The number shown after the game name in parentheses\n"
									+ "is the game weight. A higher weight means that the\n"
									+ "minigame has a higher chance of being picked. The\n"
									+ "game weight can be increased or decreased by voting\n"
									+ "on the poll at the end of the game.").color(GRAY).create()))
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

					//if (this.timeLeft < 35) { Hotbar scrolling joke
					//	player.getInventory().setHeldItemSlot(Math.abs((16 - this.timeLeft % 16)) / 2);
					//}
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

//					Minigames.IS_IN_GAME = true;
					Minigames.CURRENT_GAME = Game.this;
					Bukkit.getPluginManager().registerEvents(Game.this, Minigames.getInstance());
					Game.this.begin();
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}

	private void begin() {
		this.onPreStart();
		this.map.onPreStart();

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
					Game.this.onStart();
					Game.this.map.onStart();
					Game.this.started = true;
					return;
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

				if (this.secondsLeft == 60 || this.secondsLeft == 30 || this.secondsLeft == 10 || this.secondsLeft <= 5) {
					Game.this.sendMessage(String.format("%s seconds left", this.secondsLeft));
				}
			}

		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}


	void sendMessage(final String message){
		Bukkit.broadcastMessage(Utils.getChatPrefix(ChatColor.AQUA, 'G') + message);
	}

	/*void endGame(final List<Player> winners){ // This method is called by the game, usually in onEnd()
		Minigames.IS_IN_GAME = false;
		HandlerList.unregisterAll(this); //Unregister events

		// Announce winners
		final List<String> winnerNames = new ArrayList<>();
		for (final Player winner : winners) {
			winnerNames.add(winner.getName());
		}
		final String winnersText = String.join(", ", winnerNames);

		if (winners.isEmpty()){
			this.sendMessage("The " + this.getName() + " game has ended.");
		} else if (winners.size() == 1){
			this.sendMessage("The " + this.getName() + " game has ended! Winner: " + YELLOW + winnersText);
		} else {
			this.sendMessage("The " + this.getName() + " game has ended! Winners: " + YELLOW + winnersText);
		}

		// Give rewards
		for (final MPlayer player : Minigames.getOnlinePlayers()){
			if (winnerNames.contains(player.getName())){
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
				Queue.add(() -> Minigames.economy.depositPlayer(player.bukkit(), points));
				player.sendTitle(GOLD + "You've won",  YELLOW + "+" + points + " points");
			} else {
				player.addPoints(1);
				player.sendTitle(GOLD + "You've lost", YELLOW + "+1 point");
			}
		}

		Utils.showEveryoneToEveryone();

		for (final MPlayer player : Minigames.getOnlinePlayers()){
			// Teleport the player and give them a bit of forwards and sidewards velocity
			Queue.add(() -> {
				player.teleport(Var.LOBBY_LOCATION);
				player.bukkit().setVelocity(new Vector(Random.getRandomDouble() - 0.5, 0.3, -0.8));
				player.giveEffect(PotionEffectType.INVISIBILITY, 40, 0);
				player.applyLobbySettings();
			});
		}
	}*/

	protected void endGame() {
		this.endGame(Arrays.asList());
	}

	protected void endGame(final UUID winner) {
		this.endGame(Arrays.asList(winner));
	}

	protected void endGame(final List<UUID> winners, final boolean multipleWinnersIsNoWinner) {
		if (!multipleWinnersIsNoWinner) {
			endGame(winners);
		}

		if (winners.size() == 1) {
			endGame(winners);
		} else {
			endGame(new ArrayList<>());
		}

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

	protected void endGame(final List<UUID> winners) {
		Minigames.CURRENT_GAME = null;
		HandlerList.unregisterAll(this); //Unregister events

		Utils.showEveryoneToEveryone();

		final List<Player> players = Bukkit.getOnlinePlayers().stream().filter((p) -> winners.contains(p.getUniqueId())).collect(Collectors.toList());
		final String winnersText = String.join(", ", players.stream().map(Player::getName).collect(Collectors.toList()));

		if (winners.isEmpty()){
			this.sendMessage("The " + this.getName() + " game has ended.");
		} else if (winners.size() == 1){
			this.sendMessage("The " + this.getName() + " game has ended! Winner: " + YELLOW + winnersText);
		} else {
			this.sendMessage("The " + this.getName() + " game has ended! Winners: " + YELLOW + winnersText);
		}

		this.sendMessage(String.format("Winners move %s-%s steps forward, other players move %s-%s steps forward.",
				BoardConfig.DIE_WINNER_STEPS_MIN, BoardConfig.DIE_WINNER_STEPS_MAX, BoardConfig.DIE_LOSER_STEPS_MIN, BoardConfig.DIE_LOSER_STEPS_MAX));

		this.showPolls();

		Minigames.getOnlinePlayers().stream()
			.map(BoardPlayer::new)
			.forEach(p -> p.teleportToBoard(true));

		Board.performTurns(winners);
	}

	private void showPolls() {
		if (VoteConfig.VOTE_MENU_CHANCE > Random.getRandomFloat()) {
			Scheduler.delay(40, () -> {
				final boolean bool = Random.getRandomBoolean();

				if (bool && this.getRequiredPlayers() > 1) {
					final Poll poll = new Poll("Did you enjoy this game?", (player, option) -> {
						double weight = this.getWeight();

						if (option == 1) {
							weight *= 1.1; //Increase chance factor a bit (e.g. from to 1.5 to 1.65)
						} else if (option == 2){
							weight *= 0.9; //Decrease chance factor a bit (e.g. from 1.5 to 1.35)
						}

						player.sendMessage(ChatColor.GRAY + "Your vote has been registered.");

						if (weight > 5) {
							weight = 5;
						}

						this.setWeight(weight);
					}, new PollAnswer(1, "Yes", ChatColor.GREEN, "The game will be picked more often"),
							new PollAnswer(2, "No", ChatColor.RED, "The game will be picked less often"));

					Bukkit.getOnlinePlayers().forEach(poll::send);
				} else {
					final Poll poll = new Poll("Did you enjoy this map?", (player, option) -> {
						double weight = this.map.getWeight();

						if (option == 1) {
							weight *= 1.1; //Increase chance factor a bit (e.g. from to 1.5 to 1.65)
						} else if (option == 2){
							weight *= 0.9; //Decrease chance factor a bit (e.g. from 1.5 to 1.35)
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
		if (this.getName() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Game name is null");
			return;
		}

		final String configPath = "game-voting." + this.getName();
		Minigames.getInstance().getConfig().set(configPath, weight);
		Minigames.getInstance().saveConfig();
	}

	@Override
	public double getWeight() {
		if (this.getName() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Game name is null");
			return 0;
		}

		final String configPath = "game-voting." + this.getName();
		return Minigames.getInstance().getConfig().getDouble(configPath, 1);
	}

	@Override
	public Size getSize() {
		if (this.getRequiredPlayers() > 4)
			return Size.LARGE;
		else if (this.getRequiredPlayers() > 2)
			return Size.NORMAL;
		else
			return Size.SMALL;
	}

	public static Game<? extends GameMap> fromString(final String string) {
		for (final Game<? extends GameMap> game : GAMES){
			if (game.getName().equalsIgnoreCase(string))
				return game;

			if (string.equalsIgnoreCase(game.getAlias()))
				return game;
		}
		return null;
	}

}