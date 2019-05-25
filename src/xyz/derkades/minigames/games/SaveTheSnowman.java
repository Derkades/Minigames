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
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Utils;

public class SaveTheSnowman extends Game {

	SaveTheSnowman() {
		super("Save the Snowman", new String[] {
				"A parkour game on ice. Try to make it to the",
				"snowman within the game duration."
		}, 1, 3, 5, null);
	}

	private List<UUID> finished;
	
	@Override
	void begin(GameMap genericMap) {
		finished = new ArrayList<>();
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 277.5, 70, 273.5, -90, 0));
			Utils.hideForEveryoneElse(player);
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

		Utils.playSoundForAllPlayers(Sound.ENTITY_PLAYER_LEVELUP, 1);
		
		Bukkit.getOnlinePlayers().forEach((player2) -> player.showPlayer(Minigames.getInstance(), player2));
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
