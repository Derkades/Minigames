package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.games.speedrun.SpeedrunMap;
import xyz.derkades.minigames.utils.Utils;

public class Speedrun extends Game {

	Speedrun() {
		super("Speedrun", new String[] {
				"Jump to the finish with super speed"
		}, 1, 2, 4, SpeedrunMap.MAPS);
	}

	private List<UUID> finished;
	
	private boolean NO_ONE_FINISHED = true;
	
	private SpeedrunMap map;

	@Override
	void begin(GameMap genericMap){
		finished = new ArrayList<>();
		this.NO_ONE_FINISHED = true;
		map = (SpeedrunMap) genericMap;
		
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(map.getStartLocation());
			Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 30);
		}
		
		timer();
	}
	
	private void timer(){
		new BukkitRunnable(){
			public void run(){
				sendMessage("5 seconds left!");
				new BukkitRunnable(){
					public void run(){
						endGame();
					}
				}.runTaskLater(Minigames.getInstance(), 5*20);
			}
		}.runTaskLater(Minigames.getInstance(), 40*20);
	}
	
	private void endGame(){
		super.startNextGame(Utils.getPlayerListFromUUIDList(finished));
	}
	
	private void playerDie(Player player){
		player.teleport(map.getStartLocation());
	}
	
	private void playerWin(Player player){
		Utils.clearPotionEffects(player);
		if (NO_ONE_FINISHED){
			NO_ONE_FINISHED = false;
			super.sendMessage(player.getName() + " finished first and got an extra point!");
			Points.addPoints(player, 1);
			finished.add(player.getUniqueId());
		} else {
			super.sendMessage(player.getName() + " has finished!");
			finished.add(player.getUniqueId());
		}
		player.teleport(map.getSpectatorLocation());
		player.setAllowFlight(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		
		if (finished.contains(player.getUniqueId())){
			return;
		}
		
		if (player.isSneaking()){
			player.sendMessage(ChatColor.RED + "You cannot sneak!");
			playerDie(player);
			return;
		}
		
		if (player.isSprinting()){
			player.sendMessage(ChatColor.RED + "You cannot sprint!");
			playerDie(player);
			return;
		}
		
		Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
		Material type = block.getType();
		if (type == map.getFloorBlock()){
			playerDie(player);
		} else if(type == map.getEndBlock()){
			playerWin(player);
		}
	}
	
}
