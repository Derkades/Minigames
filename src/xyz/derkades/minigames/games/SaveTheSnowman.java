package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class SaveTheSnowman extends ParkourGame {

	@Override
	String[] getDescription() {
		return new String[]{
				"Save the Snowman is a small parkour game. The goal of",
				"this game is to make it to the finish. You",
				"have 45 seconds, when you fall you will be", 
				"teleported back to the start."
		};
	}

	@Override
	public String getName() {
		return "Save the Snowman";
	}

	@Override
	public int getRequiredPlayers() {
		return 1;
	}
	
	@Override
	public GamePoints getPoints() {
		return new GamePoints(3, 5);
	}

	@Override
	public void resetHashMaps(Player player) {}

	List<UUID> finished;
	
	@Override
	void begin() {
		finished = new ArrayList<>();
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 277.5, 70, 273.5, -90, 0));
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 0, true));
		}
		
		new BukkitRunnable(){
			public void run(){
				sendMessage("5 seconds left!");
				new BukkitRunnable(){
					public void run(){
						endGame();
					}
				}.runTaskLater(Minigames.getInstance(), 5*20);
			}
		}.runTaskLater(Minigames.getInstance(), 30*20);
	}
	
	private void endGame(){
		super.startNextGame(Utils.getPlayerListFromUUIDList(finished));
	}
	
	private void playerWin(Player player){
		if (finished.isEmpty()) {
			super.sendMessage(player.getName() + " finished first and got an extra point!");
			Points.addPoints(player, 1);
		} else {
			sendMessage(player.getName() + " has made it to the finish!");
		}
		
		finished.add(player.getUniqueId());
		
		player.teleport(new Location(Var.WORLD, 320, 81, 274, 90, 0));
		//Console.sendCommand("execute @a ~ ~ ~ playsound entity.player.levelup master @p");
		Utils.playSoundForAllPlayers(Sound.LEVEL_UP, 1);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		Material type = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

		if (type == Material.SNOW_BLOCK || type == Material.ICE)
			player.teleport(new Location(Var.WORLD, 277.5, 70, 273.5, -90, 0)); // Teleport back to start

		if (type == Material.DIAMOND_BLOCK)
			playerWin(player);
	}
	
}
