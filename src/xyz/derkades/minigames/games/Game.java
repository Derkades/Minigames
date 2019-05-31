package xyz.derkades.minigames.games;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.RED;

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
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.AutoRotate;
import xyz.derkades.minigames.ChatPoll;
import xyz.derkades.minigames.ChatPoll.Poll;
import xyz.derkades.minigames.ChatPoll.PollAnswer;
import xyz.derkades.minigames.ChatPoll.PollCallback;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.maps.MapPicking;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public abstract class Game implements Listener {

	public static final Game[] GAMES = new Game[] {
			//new CowProtect(),
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
	private final int minPoints;
	private final int maxPoints;
	private final GameMap[] maps;

	Game(final String name, final String[] description, final int requiredPlayers, final int minPoints, final int maxPoints, final GameMap[] maps) {
		this.name = name;
		this.description = description;
		this.requiredPlayers = requiredPlayers;
		this.minPoints = minPoints;
		this.maxPoints = maxPoints;
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

	public final int getMinimumPoints() {
		return this.minPoints;
	}

	public final int getMaximumPoints() {
		return this.maxPoints;
	}

	public final GameMap[] getGameMaps() {
		return this.maps;
	}

	abstract void begin(final GameMap genericMap);

	void sendMessage(final String message){
		//Bukkit.broadcastMessage(DARK_GRAY + "[" + DARK_AQUA + this.getName() + DARK_GRAY + "] " + AQUA + message);
		Bukkit.broadcastMessage(String.format("%s[%sG%s] %s| %s%s", ChatColor.BLACK, ChatColor.GOLD, ChatColor.BLACK, ChatColor.DARK_GRAY, ChatColor.GRAY, message));
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
			Bukkit.broadcastMessage(DARK_AQUA + "The " + this.getName() + " game has ended.");
		} else if (winners.size() == 1){
			Bukkit.broadcastMessage(DARK_AQUA + "The " + this.getName() + " game has ended! Winner: " + AQUA + winnersText);
		} else {
			Bukkit.broadcastMessage(DARK_AQUA + "The " + this.getName() + " game has ended! Winners: " + AQUA + winnersText);
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
				Utils.sendTitle(player, DARK_AQUA + "You've won",  AQUA + "+" + points + " points");
			} else {
				Points.addPoints(player, 1);
				Minigames.economy.depositPlayer(player, 0);
				Utils.sendTitle(player, DARK_AQUA + "You've lost", AQUA + "+1 point");
			}
			player.sendMessage(DARK_AQUA + "You currently have " + AQUA + Points.getPoints(player) + DARK_AQUA + " points.");
		}

		Utils.delayedTeleport(Var.LOBBY_LOCATION, (player) -> {
			player.setVelocity(new Vector(Random.getRandomDouble() - 0.5, 0.3, -0.3));
		}, Bukkit.getOnlinePlayers());

		for (final Player player : Bukkit.getOnlinePlayers()){
			player.setAllowFlight(false);

			Utils.clearPotionEffects(player);
			Utils.clearInventory(player);
			player.setHealth(20);

			Minigames.setCanTakeDamage(player, false);

			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 3, 0, true));
		}

		if (Minigames.STOP_GAMES){
			Scheduler.delay(1*20, () -> {
				Bukkit.broadcastMessage(RED + "An admin stopped the next game from starting. This is probably because some maintenance needs to be done.");
			});
			Minigames.STOP_GAMES = false;
			return;
		}

		int nextGameDelay = 2;

		if (Minigames.VOTE_MENU_CHANCE > Random.getRandomFloat()) {
			nextGameDelay = 6;

			Scheduler.delay(20, () -> {
				for (final Player player : Bukkit.getOnlinePlayers()) {
					final Poll poll = new Poll("Did you enjoy this game?", new PollCallback() {

						@Override
						public void callback(final Player player, final int option) {
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
						}

					}, new PollAnswer(1, "Yes", ChatColor.GREEN, "The game will be picked more often"),
							new PollAnswer(2, "No", ChatColor.RED, "The game will be picked less often"));
					ChatPoll.sendPoll(player, poll);
				}
			});
		}

		Scheduler.delay(nextGameDelay*20, () -> {
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

			player.sendMessage(DARK_GRAY + "-----------------------------------------");
			player.sendMessage(ChatColor.GOLD + "  " + ChatColor.BOLD + this.getName() + ChatColor.GRAY +  " (Current weight: " + weight + ")");

			if (!Minigames.getInstance().getConfig().getStringList("disabled-description")
					.contains(player.getUniqueId().toString())) {
				for (final String line : this.getDescription()) player.sendMessage(DARK_AQUA + line);
				player.sendMessage(DARK_AQUA + "Minimum players: " + AQUA + this.getRequiredPlayers());
			}

			if (map != null)
				player.sendMessage(DARK_AQUA + "Map: " + AQUA + map.getName());

			player.sendMessage(DARK_GRAY + "-----------------------------------------");
		}

		Minigames.LAST_GAME_NAME = this.getName();

		for (final Player player : Bukkit.getOnlinePlayers()){
			Utils.clearInventory(player);
		}

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

	public static Game fromString(final String string){
		for (final Game game : GAMES){
			if (game.getName().equalsIgnoreCase(string)){
				return game;
			}
		}
		return null;
	}

}
