package xyz.derkades.minigames.games;

import java.util.HashMap;
import java.util.Map;

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
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Utils;

public class RegeneratingSpleef extends Game {

	public static Map<String, Boolean> isDead = new HashMap<String, Boolean>();

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
	public void resetHashMaps(Player player) {
		isDead.put(player.getName(), false);
	}

	@Override
	void begin() {
		Utils.setGameRule("doTileDrops", false);
		Console.sendCommand("fill 163 80 267 149 80 253 snow"); //XXX Bukkit API
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 156.5, 82, 260.5, -90, 90));
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Minigames.getInstance(), new Runnable(){
			public void run(){
				Console.sendCommand("replaceitem entity @a slot.hotbar.0 minecraft:diamond_shovel 1 0 {display:{Name:\"Spleefanator 8000\"},Unbreakable:1,ench:[{id:32,lvl:10}],CanDestroy:[\"minecraft:snow\"]}");
				sendMessage("Game has started!");
			}
		}, 2*20);
		timer();
	}
	
	private void timer(){
		final Minigames instance = Minigames.getInstance();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
			public void run(){
				sendMessage("5 seconds left!");
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){
					public void run(){
						endGame();
					}
				}, 5 * 20);
			}
		}, 20 * 20);
	}
	
	private void endGame(){
		Utils.setGameRule("doTileDrops", true);
		
		super.startNextGame(Utils.getWinnersFromIsDeadHashMap(isDead));
	}
	
	private void playerDie(Player player){
		super.sendMessage(player.getName() + " has been eliminated from the game!");
		player.teleport(new Location(Var.WORLD, 156.5, 89, 260.5, -90, 90));
		player.getInventory().clear();
		isDead.put(player.getName(), true);
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
			playerDie(player);
		}
	}

}
