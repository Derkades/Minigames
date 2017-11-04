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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class RegeneratingSpleef extends Game {

	RegeneratingSpleef() {
		super("Regenerating Spleef",
				new String[] { 
						"Regenerating Spleef is very similar to the",
						"classic spleef game. One twist: the blocks", "you break regenerate after 2 seconds, and",
						"the arena is pretty small." 
		}, 2, 3, 7);
	}

	private static final int DURATION = 30;
	
	private static final String SECONDS_LEFT = "%s seconds left.";
	//private static final String ELIMINATED = "%s has been eliminated from the game.";

	private List<UUID> dead;
	
	@Override
	void begin() {
		dead = new ArrayList<>();
		
		Utils.setGameRule("doTileDrops", false);

		BlockUtils.fillArea(149, 80, 253, 163, 80, 267, Material.SNOW_BLOCK);
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 156.5, 82, 260.5, -90, 90));
		}
		
		Scheduler.runTaskLater(2*20, () -> {
			Console.sendCommand("replaceitem entity @a slot.hotbar.0 minecraft:diamond_shovel 1 0 {display:{Name:\"Spleefanator 8000\"},Unbreakable:1,ench:[{id:32,lvl:10}],CanDestroy:[\"minecraft:snow\"]}");
			sendMessage("The game has started!");
		});
		
		new BukkitRunnable() {
			
			int secondsLeft = DURATION;
			
			public void run() {
				if (Utils.getAliveCountFromDeadList(dead) <= 1 && secondsLeft > 2) {
					secondsLeft = 2;
				}
				
				if (secondsLeft <= 0) {
					this.cancel();
					
					//End game
					Utils.setGameRule("doTileDrops", true);
					startNextGame(Utils.getWinnersFromDeadList(dead));
					
					return;
				}
				
				if (secondsLeft == 30 || secondsLeft <= 5) {
					sendMessage(String.format(SECONDS_LEFT, secondsLeft));
				}
				
				secondsLeft--;
			}
			
		}.runTaskTimer(Minigames.getInstance(), 0, 1*20);
	}
	
	@EventHandler
	public void spleefBlock(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PlayerInventory inv = player.getInventory();
		if (inv.getItemInHand().getType() == Material.DIAMOND_SPADE){
			final Block block = event.getBlock();	
			if (block.getType() == Material.SNOW_BLOCK){
				block.setType(Material.AIR);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Minigames.getInstance(), new Runnable() {
					public void run() {
						block.setType(Material.SNOW_BLOCK);
					}
				}, 3*20);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK){
			//sendMessage(String.format(ELIMINATED, player.getName()));
			player.teleport(new Location(Var.WORLD, 156.5, 89, 260.5, -90, 90));
			player.getInventory().clear();
			dead.add(player.getUniqueId());
		}
	}

}
