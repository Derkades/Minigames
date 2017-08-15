package xyz.derkades.minigames.games;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class JungleRun extends ParkourGame implements Listener {

	JungleRun() {
		super("Jungle Run", 
				new String[] {
						"Jungle Run is a small parkour game. The goal of",
						"this game is to make it to the finish. You",
						"have 25 seconds, when you fall you are out",
						"of the game."
				}, 1, 2, 4);
	}

	private HashMap<String, Boolean> hasFinished = new HashMap<>();
	private HashMap<String, Boolean> isSpectator = new HashMap<>();

	@Override
	void begin() {
		for (Player player: Bukkit.getOnlinePlayers()){
			hasFinished.put(player.getName(), false);
			isSpectator.put(player.getName(), false);
			
			player.teleport(new Location(Var.WORLD, 282.5, 67, 196.5, -90, 0)); //Teleport all online players to the arena
		}
		timer();
	}
	
	private void timer(){
		new BukkitRunnable(){
			public void run(){
				sendMessage("5 seconds left!");
				new BukkitRunnable(){
					public void run(){
						endGame(); //End the game after 25 seconds
					}
				}.runTaskLater(Minigames.getInstance(), 5 * 20);
			}
		}.runTaskLater(Minigames.getInstance(), 20 * 20);
	}
	
	private void endGame(){
		super.startNextGame(Utils.getWinnersFromFinishedHashMap(hasFinished));
	}
	
	private void playerDie(Player player){
		super.sendMessage(player.getName() + " has been eliminated from the game!");
		player.teleport(new Location(Var.WORLD, 296.5, 80, 204.5, 180, 0)); //Teleport player to spectator room
		player.setAllowFlight(true);
		isSpectator.put(player.getName(), true);
		//Console.sendCommand("effect " + player.getName() + " invisibility 10000 1 true");
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 0, true));
	}
	
	private void playerWin(Player player){
		super.sendMessage(player.getName() + " has made it to the finish!"); //Send message
		player.teleport(new Location(Var.WORLD, 296.5, 80, 204.5, 180, 0)); //Teleports player to the spectator room
		super.sendConsoleCommand("particle flame 327.5 72 196.5 0 2 2 0.1 1000"); //Plays particle effect at finish
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		hasFinished.put(player.getName(), true);
		isSpectator.put(player.getName(), true);
		player.setAllowFlight(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(!isSpectator.get(player.getName())){ //If the player is not a spectator
			Material type = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
			List<Material> die = Arrays.asList(
					Material.WOOL, 
					Material.SOUL_SAND, 
					Material.STAINED_CLAY
					);
			if (die.contains(type)){
				playerDie(player);
			} else if(type == Material.GOLD_BLOCK){
				playerWin(player); //If on gold block -> win
			}	
		}
	}

}
