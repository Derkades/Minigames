package xyz.derkades.minigames.games;

import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.YELLOW;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.AutoRotate;
import xyz.derkades.minigames.ChatPoll.Poll;
import xyz.derkades.minigames.ChatPoll.PollAnswer;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.maps.MapPicking;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public abstract class Game implements Listener {

	public static final Game[] GAMES = new Game[] {
			new BreakTheBlock(),
			new CreeperAttack(),
			new DigDug(),
			new Dropper(),
			//new Elytra(),
			new IcyBlowback(),
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

	private final String name;
	private final String[] description;
	private final int requiredPlayers;
	private final GameMap[] maps;

	Game(final String name, final String[] description, final int requiredPlayers, final GameMap[] maps) {
		this.name = name;
		this.description = description;
		this.requiredPlayers = requiredPlayers;
		this.maps = maps;
	}

	public final String getName() {
		return this.name;
	}

	public final String[] getDescription() {
		return this.description;
	}

	public final int getRequiredPlayers() {
		return this.requiredPlayers;
	}

	public final GameMap[] getGameMaps() {
		return this.maps;
	}

	abstract void begin(final GameMap genericMap);

	void sendMessage(final String message){
		//Bukkit.broadcastMessage(DARK_GRAY + "[" + DARK_AQUA + this.getName() + DARK_GRAY + "] " + AQUA + message);
		//Bukkit.broadcastMessage(String.format("%s[%sG%s] %s| %s%s", ChatColor.BLACK, ChatColor.GOLD, ChatColor.BLACK, ChatColor.DARK_GRAY, ChatColor.GRAY, message));
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

	void endGame(final List<Player> winners){
		Minigames.IS_IN_GAME = false;
		HandlerList.unregisterAll(this); //Unregister events

		Utils.showEveryoneToEveryone();

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

		for (final Player player : Bukkit.getOnlinePlayers()){
			if (winnerNames.contains(player.getName())){
				//If player has won
				final int onlinePlayers = Bukkit.getOnlinePlayers().size();

				Minigames.economy.depositPlayer(player, 1);

				final int points;

				if (onlinePlayers < 3) {
					points = 3;
				} else if (onlinePlayers < 5){
					points = 4;
				} else {
					points = 5;
				}

				Points.addPoints(player, points);
				Minigames.economy.depositPlayer(player, points);
				Utils.sendTitle(player, GOLD + "You've won",  YELLOW + "+" + points + " points");
			} else {
				Points.addPoints(player, 1);
				Minigames.economy.depositPlayer(player, 0);
				Utils.sendTitle(player, GOLD + "You've lost", YELLOW + "+1 point");
			}
			//player.sendMessage(DARK_AQUA + "You currently have " + AQUA + Points.getPoints(player) + DARK_AQUA + " points.");
		}

		Utils.delayedTeleport(Var.LOBBY_LOCATION, (player) -> {
			player.setVelocity(new Vector(Random.getRandomDouble() - 0.5, 0.3, -0.8));
		}, Bukkit.getOnlinePlayers());

		for (final Player player : Bukkit.getOnlinePlayers()){
			player.setAllowFlight(false);

			Utils.clearPotionEffects(player);
			Utils.clearInventory(player);
			player.setHealth(20);

			Minigames.setCanTakeDamage(player, false);
			Minigames.setCanSneak(player, true);

			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0, true));

			Minigames.giveLobbyInventoryItems(player);
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
			AutoRotate.startNewRandomGame();
		});
	}

	public void startGame(){
		// Choose random map
		final GameMap map = this.maps == null ? null : MapPicking.pickRandomMap(this.maps);

		// Send description
		for (final Player player : Bukkit.getOnlinePlayers()) {

			double weight = Minigames.getInstance().getConfig().contains("game-voting." + this.getName())
					? Minigames.getInstance().getConfig().getDouble("game-voting." + this.getName())
					: 1;

			weight = Math.round(weight * 100.0) / 100.0;

			final String prefix = Utils.getChatPrefix(ChatColor.GOLD, 'G');

			player.sendMessage(prefix + DARK_GRAY + "-----------------------------------------");
			//player.sendMessage(prefix + ChatColor.GOLD + "" + ChatColor.BOLD + this.getName() + ChatColor.GRAY +  " (" + weight + ")");

			player.spigot().sendMessage(new ComponentBuilder("")
					.appendLegacy(Utils.getChatPrefix(ChatColor.GOLD, 'G'))
					.append(this.getName())
					.bold(true)
					.color(ChatColor.GOLD)
					.append(" (" + weight + ")")
					.color(ChatColor.GRAY)
					.bold(false)
					.append(" [hover for help]")
					.color(ChatColor.YELLOW)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
							"The number shown after the game name in parentheses\n"
							+ "is the game weight. A higher weight means that the\n"
							+ "minigame has a higher chance of being picked. The\n"
							+ "game weight can be increased or decreased by voting\n"
							+ "on the poll at the end of the game.")
							.color(ChatColor.GRAY).create()))
					.create());

			if (!Minigames.getInstance().getConfig().getStringList("disabled-description")
					.contains(player.getUniqueId().toString())) {
				for (final String line : this.getDescription()) player.sendMessage(prefix + line);
				player.sendMessage(prefix + "Minimum players: " + YELLOW + this.getRequiredPlayers());
			}

			if (map != null)
				player.sendMessage(prefix + "Map: " + YELLOW + map.getName());

			player.sendMessage(prefix + DARK_GRAY + "-----------------------------------------");
		}

		Minigames.LAST_GAME_NAME = this.getName();

		new BukkitRunnable() {
			int timeLeft = 200;

			@Override
			public void run() {
				Utils.setXpBarValue((this.timeLeft + 10 ) / 210.0f, (int) ((this.timeLeft + 10) / 20.0));

				if (this.timeLeft < 90 && (this.timeLeft + 10) % 20 == 0) {
					Utils.playSoundForAllPlayers(Sound.ENTITY_ARROW_HIT_PLAYER, 0.1f);
				}

				this.timeLeft--;

				if (this.timeLeft < 20) {
					Utils.playSoundForAllPlayers(Sound.ENTITY_ARROW_HIT_PLAYER, Random.getRandomFloat());
				}

				if (this.timeLeft < 1) {
					this.cancel();

					Utils.clearPotionEffects();
					Utils.clearInventory();

					Game.this.begin(map);
					Minigames.IS_IN_GAME = true;
					Bukkit.getPluginManager().registerEvents(Game.this, Minigames.getInstance());

					Utils.setXpBarValue(0f, 0);
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}

	public static Game getRandomGame(){
		final Map<Game, Double> weightedList = new HashMap<>();

		// Populate hashmap
		for (final Game game : GAMES) {
			final String gameName = game.getName();
			final double weight = Minigames.getInstance().getConfig().contains("game-voting." + gameName)
					? Minigames.getInstance().getConfig().getDouble("game-voting." + gameName)
					: 1;
			weightedList.put(game, weight);
		}

		final Game random = Utils.getWeightedRandom(weightedList);

		return random;
	}

	public static Game fromString(String string) {
		if (string.equalsIgnoreCase("oitq")) {
			string = "one_in_the_quiver";
		} else if (string.equalsIgnoreCase("tbb")) {
			string = "teamsbowbattle";
		} else if (string.equalsIgnoreCase("btb")) {
			string = "break_the_block";
		}

		for (final Game game : GAMES){
			if (game.getName().equalsIgnoreCase(string)){
				return game;
			}
		}
		return null;
	}

}
