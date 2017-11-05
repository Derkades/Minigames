package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class Speedrun extends ParkourGame {

	Speedrun() {
		super("Speedrun", new String[] {
				"Jump to the finish with super speed"
		}, 1, 2, 4);
	}

	private List<UUID> finished;
	
	private boolean NO_ONE_FINISHED = true;

	@Override
	void begin(){
		finished = new ArrayList<>();
		this.NO_ONE_FINISHED = true;
		
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 140.0, 97, 306, -180, 0));
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
		player.teleport(new Location(Var.WORLD, 140.0, 97, 306, -180, 0));
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
		player.teleport(new Location(Var.WORLD, 128.0, 98, 274.5, -180, 0));
		player.setAllowFlight(true);
	}
	
	@SuppressWarnings("deprecation")
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
		if (type == Material.STAINED_CLAY && block.getData() == 14){
			playerDie(player);
		} else if(type == Material.STAINED_CLAY && block.getData() == 7){
			playerWin(player);
		}
	}
	
}
