package xyz.derkades.minigames.games;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.AutoRotate;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public abstract class Game implements Listener {
	
	public static final Game[] GAMES = new Game[] {
			//new CowProtect(),
			new DigDug(),
			new Dropper(),
			new Dropper(),
			//new Elytra(),
			new JungleRun(),
			//new MazePvp(),
			//new Mine(),
			//new NyanCat(),
			new Platform(),
			new RegeneratingSpleef(),
			new SaveTheSnowman(),
			new SnowFight(),
			new Speedrun(),			
			new TntRun(),
			new TntTag(),
	};
	
	private String name;
	private String[] description;
	private int requiredPlayers;
	private int minPoints;
	private int maxPoints;
	
	Game(String name, String[] description, int requiredPlayers, int minPoints, int maxPoints) {
		this.name = name;
		this.description = description;
		this.requiredPlayers = requiredPlayers;
		this.minPoints = minPoints;
		this.maxPoints = maxPoints;
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
	
	abstract void begin();
	
	void sendMessage(String message){
		Bukkit.broadcastMessage(DARK_GRAY + "[" + DARK_AQUA + getName() + DARK_GRAY + "] " + AQUA + message);
	}
	
	void sendConsoleCommand(String command){
		Console.sendCommand(command);
	}
	
	@SuppressWarnings("deprecation")
	void startNextGame(List<Player> winners){
		Minigames.IS_IN_GAME = false;
		HandlerList.unregisterAll(this); //Unregister events
		
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			for (Player player2 : Bukkit.getOnlinePlayers()) {
				player1.showPlayer(player2);
			}
		}
		
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
				int points = Random.getRandomInteger(this.getMinimumPoints(), this.getMaximumPoints());
				Points.addPoints(player, points);
				player.sendTitle(DARK_AQUA + "You've won",  AQUA + "+" + points + " points");
			} else {
				Points.addPoints(player, 1);
				player.sendTitle(DARK_AQUA + "You've lost", AQUA + "+1 point");
			}
			player.sendMessage(DARK_AQUA + "You currently have " + AQUA + Points.getPoints(player) + DARK_AQUA + " points.");
		}
		
		for (Player player : Bukkit.getOnlinePlayers()){			
			player.teleport(Var.LOBBY_LOCATION);
			player.setAllowFlight(false);
			
			Utils.clearPotionEffects(player);	
			Utils.clearInventory(player);
			player.setHealth(20);
			
			Minigames.setCanTakeDamage(player, false);
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 3, 0, true));
		}

		if (Minigames.STOP_GAMES){
			Scheduler.runTaskLater(3*20, () -> {
				Bukkit.broadcastMessage(RED + "An admin stopped the next game from starting. This is probably because some maintenance needs to be done.");
			});
			return;
		}
		
		Scheduler.runTaskLater(8*20, () -> {
			AutoRotate.startNewRandomGame();
		});
	}
	
	public void startGame(){
		//Send description
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (Minigames.getInstance().getConfig().getStringList("disabled-description").contains(player.getUniqueId().toString())) continue;
			
			player.sendMessage(DARK_GRAY + "-----------------------------------------");
			for (String line : getDescription()) player.sendMessage(DARK_AQUA + line);
			player.sendMessage(DARK_AQUA + "Points: " + AQUA + this.getMinimumPoints() + "-" + this.getMaximumPoints());
			player.sendMessage(DARK_GRAY + "-----------------------------------------");
		}
		
		startCountdown();
		
		Minigames.getInstance().getConfig().set("last-game", this.getName());
		
		for (Player player : Bukkit.getOnlinePlayers()){
			Utils.clearInventory(player);
		}
	}
	
	private void startCountdown(){
		Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); //PING (5)
		Utils.setXpBarValue(1.0f, 5);
		
		Scheduler.runTaskLater(10, () -> {
			Utils.setXpBarValue(0.9f, 5);
		});

		Scheduler.runTaskLater(1 * 20, () -> {
			Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); // PING (4)
			Utils.setXpBarValue(0.8f, 4);
		});

		Scheduler.runTaskLater(1 * 20 + 10, () -> {
			Utils.setXpBarValue(0.7f, 4);
		});

		Scheduler.runTaskLater(2 * 20, () -> {
			Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); // PING (3)
			Utils.setXpBarValue(0.6f, 3);
		});

		Scheduler.runTaskLater(2 * 20 + 10, () -> {
			Utils.setXpBarValue(0.5f, 3);
		});

		Scheduler.runTaskLater(3 * 20, () -> {
			Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); // PING (2)
			Utils.setXpBarValue(0.4f, 2);
		});

		Scheduler.runTaskLater(3 * 20 + 10, () -> {
			Utils.setXpBarValue(0.3f, 2);
		});

		Scheduler.runTaskLater(4 * 20, () -> {
			Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); // PING (1)
			Utils.setXpBarValue(0.2f, 1);
		});

		Scheduler.runTaskLater(4 * 20 + 10, () -> {
			Utils.setXpBarValue(0.1f, 1);
		});
		
		Scheduler.runTaskLater(5 * 20, () -> {
			Bukkit.getOnlinePlayers().forEach((player) -> Utils.clearPotionEffects(player));

			begin();
			Minigames.IS_IN_GAME = true;
			
			Bukkit.getPluginManager().registerEvents(this, Minigames.getInstance());
			
			Utils.setXpBarValue(0f, 0);
			
			
			new BukkitRunnable() { // Small delay for last sound, because it needs to be played at the new player location
				public void run() {
					Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 1.0f); // PING (GO)
				}
			}.runTaskLater(Minigames.getInstance(), 3L);
		});
	}
	
	public class GamePoints {
		
		private int min;
		private int max;
		
		GamePoints(int min, int max) {
			this.min = min;
			this.max = max;
		}
		
		public int getMinimum(){
			return min;
		}
		
		public int getMaximum(){
			return max;
		}
		
	}
	
	public static Game getRandomGame(){
		return ListUtils.getRandomValueFromArray(GAMES);
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
