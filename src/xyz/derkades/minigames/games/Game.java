package xyz.derkades.minigames.games;

import static net.md_5.bungee.api.ChatColor.DARK_GRAY;
import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.AutoRotate;
import xyz.derkades.minigames.ChatPoll.Poll;
import xyz.derkades.minigames.ChatPoll.PollAnswer;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Queue;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public abstract class Game<M extends GameMap> implements Listener {

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
			//new Mine(),
			new MolePvP(),
			//new NyanCat(),
			new OneInTheQuiver(),
			new Platform(),
			new RegeneratingSpleef(),
			//new Rooms(),
			new Parkour(),
			//new SnowFight(),
			//new Speedrun(),
			new TeamsBowBattle(),
			new TntRun(),
			//new TntTag(),
			new Tron(),
	};

	public abstract String getName();

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

	protected M map = null;

	// Can be used by listeners in game classes to check if the game has started.
	protected boolean started = false;

	public void start() {
		this.started = false;

		// Pick random map
		if (this.getGameMaps() == null) {
			this.map = null;
			Bukkit.broadcastMessage("Warning, no map!");
		} else {
			this.map = ListUtils.getRandomValueFromArray(this.getGameMaps());
		}

		// Send description
		for (final Player player : Bukkit.getOnlinePlayers()) {

			double weight = Minigames.getInstance().getConfig().contains("game-voting." + this.getName())
					? Minigames.getInstance().getConfig().getDouble("game-voting." + this.getName())
					: 1;

			weight = Math.round(weight * 100.0) / 100.0;

			final String prefix = Utils.getChatPrefix(GOLD, 'G');

			player.sendMessage(prefix + DARK_GRAY + "-----------------------------------------");
			player.spigot().sendMessage(new ComponentBuilder("").appendLegacy(Utils.getChatPrefix(GOLD, 'G'))
					.append(this.getName()).bold(true).color(GOLD).append(" (" + weight + ")")
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
				for (final String line : this.getDescription())
					player.sendMessage(prefix + line);
				player.sendMessage(prefix + "Minimum players: " + YELLOW + this.getRequiredPlayers());
			}

			if (this.map != null)
				player.sendMessage(prefix + "Map: " + YELLOW + this.map.getName());

			player.sendMessage(prefix + DARK_GRAY + "-----------------------------------------");
		}

		// Set current game name. This is used to check prevent the same game from starting again after this.
		Minigames.LAST_GAME_NAME = this.getName();

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
				}

				this.timeLeft--;

				if (this.timeLeft < 5) {
					this.cancel();

					for (final MPlayer player : Minigames.getOnlinePlayers()) {
						player.clearPotionEffects();
						player.clearInventory();
						player.setLevel(0);
						player.setExp(0);
					}

					Minigames.IS_IN_GAME = true;
					Bukkit.getPluginManager().registerEvents(Game.this, Minigames.getInstance());
					Game.this.begin();
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}

	private void begin() {
		this.onPreStart();

		this.sendMessage(String.format("The game will start in %s seconds.", this.getPreDuration()));

		new BukkitRunnable() {

			private int secondsLeft = Game.this.getDuration() + Game.this.getPreDuration();

			@Override
			public void run() {
				this.secondsLeft--;

				// pre-start countdown
				if (this.secondsLeft > Game.this.getDuration()) {
					return;
				}

				if (this.secondsLeft == Game.this.getDuration()) {
					Game.this.sendMessage("The game has started.");
					Game.this.onStart();
					Game.this.started = true;
					return;
				}

				final int newSecondsLeft = Game.this.gameTimer(this.secondsLeft);
				this.secondsLeft = newSecondsLeft > 0 ? newSecondsLeft : this.secondsLeft;

				if (this.secondsLeft <= 0) {
					this.cancel();
					Game.this.onEnd();
					return;
				}

				if (this.secondsLeft == 60 || this.secondsLeft == 30 || this.secondsLeft == 10 || this.secondsLeft <= 5) {
					Game.this.sendMessage(String.format("%s seconds left", this.secondsLeft));
				}
			}

		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}


	void sendMessage(final String message){
		Bukkit.broadcastMessage(Utils.getChatPrefix(ChatColor.GOLD, 'G') + message);
	}

	void endGame() {
		this.endGame(Arrays.asList());
	}

	void endGame(final UUID winner) {
		this.endGame(Utils.getPlayerListFromUUIDList(Arrays.asList(winner)));
	}

	void endGame(final Player winner) {
		this.endGame(Arrays.asList(winner));
	}

	void endGame(final List<Player> winners){ // This method is called by the game, usually in onEnd()
		Minigames.IS_IN_GAME = false;
		HandlerList.unregisterAll(this); //Unregister events

		// Announce winners
		final List<String> winnerNames = new ArrayList<String>();
		for (final Player winner : winners) winnerNames.add(winner.getName());
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
			//player.sendMessage(DARK_AQUA + "You currently have " + AQUA + Points.getPoints(player) + DARK_AQUA + " points.");
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

		int nextGameDelay = 2;

		if (Minigames.VOTE_MENU_CHANCE > Random.getRandomFloat()) {
			nextGameDelay = 6;

			Scheduler.delay(20, () -> {
				final Poll poll = new Poll("Did you enjoy this game?", (player, option) -> {
					double multiplier = Minigames.getInstance().getConfig().contains("game-voting." + Game.this.getName())
							? Minigames.getInstance().getConfig().getDouble("game-voting." + Game.this.getName())
							: 1;

					if (option == 1) {
						multiplier *= 1.1; //Increase chance factor a bit (e.g. from to 1.5 to 1.65)
					} else if (option == 2){
						multiplier *= 0.9; //Decrease chance factor a bit (e.g. from 1.5 to 1.35)
					}

					player.sendMessage(ChatColor.GRAY + "Your vote has been registered.");

					if (multiplier > 5) {
						multiplier = 5;
					}

					Minigames.getInstance().getConfig().set("game-voting." + Game.this.getName(), multiplier);
					Minigames.getInstance().saveConfig();
				}, new PollAnswer(1, "Yes", ChatColor.GREEN, "The game will be picked more often"),
						new PollAnswer(2, "No", ChatColor.RED, "The game will be picked less often"));

				Bukkit.getOnlinePlayers().forEach(poll::send);
			});
		}

		Scheduler.delay(nextGameDelay * 20, () -> {
			// Unload world from previous game. It can be done now, because all players should
			// be teleported to the lobby by now.
			if (this.map != null) {
				if (this.map.getGameWorld() != null) {
					this.map.getGameWorld().unload();
				} else {
					Bukkit.broadcastMessage("[warning] game is not in dedicated world");
				}
			} else {
				Bukkit.broadcastMessage("[warning] game does not have map support");
			}

			AutoRotate.startNewRandomGame();
		});
	}

	public static Game<? extends GameMap> getRandomGame(){
		final Map<Game<? extends GameMap>, Double> weightedList = new HashMap<>();

		// Populate hashmap
		for (final Game<?> game : GAMES) {
			final String gameName = game.getName();
			final double weight = Minigames.getInstance().getConfig().contains("game-voting." + gameName)
					? Minigames.getInstance().getConfig().getDouble("game-voting." + gameName)
					: 1;
			weightedList.put(game, weight);
		}

		final Game<?> random = Utils.getWeightedRandom(weightedList);

		return random;
	}

	public static Game<? extends GameMap> fromString(String string) {
		if (string.equalsIgnoreCase("oitq")) {
			string = "one in the quiver";
		} else if (string.equalsIgnoreCase("tbb")) {
			string = "teams bow battle";
		} else if (string.equalsIgnoreCase("btb")) {
			string = "break the block";
		} else if (string.equalsIgnoreCase("spleef")) {
			string = "regenerating spleef";
		} else if (string.equalsIgnoreCase("hg")) {
			string = "hunger games";
		}

		for (final Game<? extends GameMap> game : GAMES){
			if (game.getName().equalsIgnoreCase(string)){
				return game;
			}
		}
		return null;
	}

}