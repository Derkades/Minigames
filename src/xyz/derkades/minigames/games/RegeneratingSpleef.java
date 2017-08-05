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

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class RegeneratingSpleef extends Game {

	@Override
	String[] getDescription() {
		return new String[]{
				"Regenerating Spleef is very similar to the",
				"classic spleef game. One twist: the blocks",
				"you break regenerate after 2 seconds, and",
				"the arena is pretty small."
				};
	}

	@Override
	public String getName() {
		return "Regenerating Spleef";
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}
	
	@Override
	public GamePoints getPoints() {
		return new GamePoints(3, 7);
	}

	@Override
	public void resetHashMaps(Player player) {}

	List<UUID> dead;
	
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

		Scheduler.runTaskLater(20*20, () -> {
			sendMessage("5 seconds left!");
			Scheduler.runTaskLater(5*20, () -> {
				//End game
				Utils.setGameRule("doTileDrops", true);
				super.startNextGame(Utils.getWinnersFromDeadList(dead));
			});
		});
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
			sendMessage(player.getName() + " has been eliminated from the game!");
			player.teleport(new Location(Var.WORLD, 156.5, 89, 260.5, -90, 90));
			player.getInventory().clear();
			dead.add(player.getUniqueId());
		}
	}

}
