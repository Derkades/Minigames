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
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.AutoRotate;
import xyz.derkades.minigames.GameEnum;
import xyz.derkades.minigames.Main;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.utils.java.EnumUtils;
import xyz.derkades.minigames.utils.java.Random;

public abstract class Game implements Listener {
	
	private boolean isRunning = false;
	
	void setRunning(boolean running){
		isRunning = running;
		Main.IS_IN_GAME = running;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	abstract String[] getDescription();
	
	public abstract String getName();
	
	public abstract int getRequiredPlayers();
	
	public abstract GamePoints getPoints();
	
	public abstract void resetHashMaps(Player player);
	
	abstract void begin();
	
	void sendMessage(String message){
		Bukkit.broadcastMessage(DARK_GRAY + "[" + DARK_AQUA + getName() + DARK_GRAY + "] " + AQUA + message);
	}
	
	void sendConsoleCommand(String command){
		Console.sendCommand(command);
	}
	
	@SuppressWarnings("deprecation")
	void startNextGame(List<Player> winners){
		this.setRunning(false);
		
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
				int points = Random.getRandomInteger(this.getPoints().getMinimum(), this.getPoints().getMaximum());
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
			
			Main.CAN_TAKE_DAMAGE.put(player.getName(), false); //No more PvP
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 3, 0, true));
		}

		if (Main.STOP_GAMES){
			new BukkitRunnable(){
				public void run(){
					Bukkit.broadcastMessage(RED + "An admin stopped the next game from starting. This is probably because some maintenance needs to be done.");
				}
			}.runTaskLater(Main.getInstance(), 3*20);
			return;
		}
		
		new BukkitRunnable(){
			public void run(){
				Bukkit.broadcastMessage(DARK_AQUA + "A new game will start in 5 seconds.");
			}
		}.runTaskLater(Main.getInstance(), 7*20);
		
		Scheduler.runTaskLater(12*20, new Runnable(){
			public void run(){
				AutoRotate.startNewRandomGame();
			}
		});
	}
	
	public void startGame(){
		//Send description
		Bukkit.broadcastMessage(DARK_GRAY + "-----------------------------------------");
		for (String line : getDescription()) Bukkit.broadcastMessage(DARK_AQUA + line);
		Bukkit.broadcastMessage(DARK_AQUA + "Points: " + AQUA + this.getPoints().getMinimum() + "-" + this.getPoints().getMaximum());
		Bukkit.broadcastMessage(DARK_GRAY + "-----------------------------------------");
		
		startCountdown();
		
		Main.getInstance().getConfig().set("last-game", this.getName());
		
		for (Player player : Bukkit.getOnlinePlayers())
			resetHashMaps(player);
		
		for (Player player : Bukkit.getOnlinePlayers()){
			Utils.clearInventory(player);
		}
	}
	
	private void startCountdown(){
		Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); //PING (5)
		Utils.setXpBarValue(1.0f, 5);
		
		Scheduler.runTaskLater(10, new Runnable(){
			public void run(){
				Utils.setXpBarValue(0.9f, 5);
			}
		}); 
		
		Scheduler.runTaskLater(1*20, new Runnable(){
			public void run(){
				Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); //PING (4)
				Utils.setXpBarValue(0.8f, 4);
			}
		}); 
		
		Scheduler.runTaskLater(1*20 + 10, new Runnable(){
			public void run(){
				Utils.setXpBarValue(0.7f, 4);
			}
		});
		
		Scheduler.runTaskLater(2*20, new Runnable(){
			public void run(){
				Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); //PING (3)
				Utils.setXpBarValue(0.6f, 3);
			}
		});
		
		Scheduler.runTaskLater(2*20 + 10, new Runnable(){
			public void run(){
				Utils.setXpBarValue(0.5f, 3);
			}
		});
		
		Scheduler.runTaskLater(3*20, new Runnable(){
			public void run(){
				Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); //PING (2)
				Utils.setXpBarValue(0.4f, 2);
			}
		});
		
		Scheduler.runTaskLater(3*20 + 10, new Runnable(){
			public void run(){
				Utils.setXpBarValue(0.3f, 2);
			}
		});
		
		Scheduler.runTaskLater(4*20, new Runnable(){
			public void run(){
				Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 0.1f); //PING (1)
				Utils.setXpBarValue(0.2f, 1);
			}
		});
		
		Scheduler.runTaskLater(4*20 + 10, new Runnable(){
			public void run(){
				Utils.setXpBarValue(0.1f, 1);
			}
		});
		
		Scheduler.runTaskLater(5*20, new Runnable(){
			public void run(){
				begin();
				setRunning(true);
				
				Utils.setXpBarValue(0f, 0);
				
				new BukkitRunnable(){ //Small delay for last sound, because it needs to be played at the new player location
					public void run(){
						Utils.playSoundForAllPlayers(Sound.ARROW_HIT, 1.0f);  //PING (GO)
					}
				}.runTaskLater(Main.getInstance(), 3L);
			}
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
	
	public static List<Game> getAllGames(){
		List<Game> list = new ArrayList<Game>();
		for (GameEnum gameEnum : GameEnum.values()){
			Game game = gameEnum.getGame();
			list.add(game);
		}
		return list;
	}
	
	public static Game getRandomGame(){
		return EnumUtils.getRandomEnum(GameEnum.class).getGame();
	}
	
	public static Game fromString(String string){
		for (Game game : Game.getAllGames()){
			if (game.getName().equalsIgnoreCase(string)){
				return game;
			}
		}
		return null;
	}
	
}
