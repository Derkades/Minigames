package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.spleef.SpleefMap;
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
	
	private SpleefMap map;
	
	@Override
	void begin() {
		dead = new ArrayList<>();
		
		map = ListUtils.getRandomValueFromArray(SpleefMap.MAPS);
		
		Utils.setGameRule("doTileDrops", false);
		
		map.fill();
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(map.getStartLocation());
		}
		
		Scheduler.delay(3*20, () -> {
			//Console.sendCommand("replaceitem entity @a slot.hotbar.0 minecraft:diamond_shovel 1 0 {display:{Name:\"Spleefanator 8000\"},Unbreakable:1,ench:[{id:32,lvl:10}],CanDestroy:[\"minecraft:snow\"]}");
			sendMessage("The game has started!");
			ItemStack shovel = new ItemBuilder(Material.DIAMOND_SPADE)
					.name("Spleefanator 8000")
					.enchant(Enchantment.DIG_SPEED, 5)
					.create();
			net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(shovel);
			NBTTagCompound nbt = nms.getTag();
			nbt.setInt("Unbreakable", 1);
			NBTTagList canDestroy = new NBTTagList();
			canDestroy.add(new NBTTagString("minecraft:snow"));
			nbt.set("CanDestroy", canDestroy);
			ItemStack shovel2 = CraftItemStack.asBukkitCopy(nms);
			Bukkit.getOnlinePlayers().forEach((player) -> player.getInventory().setItem(0, shovel2));
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
		if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK){
			//sendMessage(String.format(ELIMINATED, player.getName()));
			player.teleport(map.getSpectatorLocation());
			player.getInventory().clear();
			dead.add(player.getUniqueId());
		}
	}

}
