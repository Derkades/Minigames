package xyz.derkades.minigames.games;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.AutoRotate;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.menu.VoteMenu;
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
			new JungleRun(),
			//new MazePvp(),
			//new Mine(),
			new MolePvP(),
			//new NyanCat(),
			new OneInTheQuiver(),
			new Platform(),
			new RegeneratingSpleef(),
			//new Rooms(),
			new SaveTheSnowman(),
			//new SnowFight(),
			//new Speedrun(),
			new TeamsBowBattle(),
			new TntRun(),
			new TntTag(),
	};
	
	private String name;
	private String[] description;
	private int requiredPlayers;
	private int minPoints;
	private int maxPoints;
	private GameMap[] maps;
	
	Game(String name, String[] description, int requiredPlayers, int minPoints, int maxPoints, GameMap[] maps) {
		this.name = name;
		this.description = description;
		this.requiredPlayers = requiredPlayers;
		this.minPoints = minPoints;
		this.maxPoints = maxPoints;
		this.maps = maps;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String[] getDescription() {
		return description;
	}
	
	public final int getRequiredPlayers() {
		return requiredPlayers;
	}
	
	public final int getMinimumPoints() {
		return minPoints;
	}
	
	public final int getMaximumPoints() {
		return maxPoints;
	}
	
	public final GameMap[] getGameMaps() {
		return maps;
	}
	
	abstract void begin(final GameMap genericMap);
	
	void sendMessage(String message){
		Bukkit.broadcastMessage(DARK_GRAY + "[" + DARK_AQUA + getName() + DARK_GRAY + "] " + AQUA + message);
	}
	
	void startNextGame(List<Player> winners){
		Minigames.IS_IN_GAME = false;
		HandlerList.unregisterAll(this); //Unregister events
		
		Utils.showEveryoneToEveryone();
		
		List<String> winnerNames = new ArrayList<String>();
		for (Player winner : winners) winnerNames.add(winner.getName());
		String winnersText = String.join(", ", winnerNames);
		
		if (winners.isEmpty()){
			Bukkit.broadcastMessage(DARK_AQUA + "The " + getName() + " game has ended.");
		} else if (winners.size() == 1){
			Bukkit.broadcastMessage(DARK_AQUA + "The " + getName() + " game has ended! Winner: " + AQUA + winnersText);
		} else {
			Bukkit.broadcastMessage(DARK_AQUA + "The " + getName() + " game has ended! Winners: " + AQUA + winnersText);
		}
		
		for (Player player : Bukkit.getOnlinePlayers()){
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
				//Minigames.economy.depositPlayer(player, 0);
				Utils.sendTitle(player, DARK_AQUA + "You've lost", AQUA + "+1 point");
			}
			player.sendMessage(DARK_AQUA + "You currently have " + AQUA + Points.getPoints(player) + DARK_AQUA + " points.");
		}
		
		Utils.delayedTeleport(Var.LOBBY_LOCATION, Bukkit.getOnlinePlayers());
		
		for (Player player : Bukkit.getOnlinePlayers()){			
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
		
		Scheduler.delay(5, () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (Minigames.VOTE_MENU_CHANCE > Random.getRandomFloat()) {
					new VoteMenu(player, this.getName()).open();
				}
			}
		});
		
		Scheduler.delay(3*20, () -> {
			AutoRotate.startNewRandomGame();
		});
	}
	
	public void startGame(){
		// Choose random map
		final GameMap map = maps == null ? null : ListUtils.getRandomValueFromArray(maps);
		
		// Send description
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			double weight = Minigames.getInstance().getConfig().contains("game-voting." + this.getName())
					? Minigames.getInstance().getConfig().getDouble("game-voting." + this.getName())
					: 1;
			
			weight = Math.round(weight * 100.0) / 100.0;			
			
			player.sendMessage(DARK_GRAY + "-----------------------------------------");
			player.sendMessage(ChatColor.GOLD + "  " + ChatColor.BOLD + this.getName() + ChatColor.GRAY +  " (Current weight: " + weight + ")");
			
			if (!Minigames.getInstance().getConfig().getStringList("disabled-description")
					.contains(player.getUniqueId().toString())) {
				for (String line : getDescription()) player.sendMessage(DARK_AQUA + line);
				//player.sendMessage(DARK_AQUA + "Points: " + AQUA + this.getMinimumPoints() + "-" + this.getMaximumPoints());
			}
			
			if (map != null)
				player.sendMessage(DARK_AQUA + "Map: " + AQUA + map.getName());
			
			player.sendMessage(DARK_GRAY + "-----------------------------------------");
		}
		
		Minigames.LAST_GAME_NAME = this.getName();
		
		for (Player player : Bukkit.getOnlinePlayers()){
			Utils.clearInventory(player);
		}
		
		new BukkitRunnable() {
			int timeLeft = 200;
			
			@Override
			public void run() {
				Utils.setXpBarValue((float) ((timeLeft + 10 ) / 210.0f), (int) ((timeLeft + 10) / 20.0));
				
				if (timeLeft < 90 && (timeLeft + 10) % 20 == 0) {
					Utils.playSoundForAllPlayers(Sound.ENTITY_ARROW_HIT_PLAYER, 0.1f);
				}
				
				timeLeft--;
				
				if (timeLeft < 20) {
					Utils.playSoundForAllPlayers(Sound.ENTITY_ARROW_HIT_PLAYER, Random.getRandomFloat());
				}
				
				if (timeLeft < 1) {
					this.cancel();
					
					
					
					Utils.clearPotionEffects();

					begin(map);
					
					Minigames.IS_IN_GAME = true;
					
					Bukkit.getPluginManager().registerEvents(Game.this, Minigames.getInstance());
					
					Utils.setXpBarValue(0f, 0);
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}
	
	public static Game getRandomGame(){
		Map<Game, Double> weightedList = new HashMap<>();
		
		// Populate hashmap
		for (Game game : GAMES) {
			String gameName = game.getName();
			double weight = Minigames.getInstance().getConfig().contains("game-voting." + gameName)
					? Minigames.getInstance().getConfig().getDouble("game-voting." + gameName)
					: 1;
			weightedList.put(game, weight);
		}
		
		Game random = Utils.getWeightedRandom(weightedList);
		
		return random;
	}
	
	public static Game fromString(String string){
		for (Game game : GAMES){
			if (game.getName().equalsIgnoreCase(string)){
				return game;
			}
		}
		return null;
	}
	
}
