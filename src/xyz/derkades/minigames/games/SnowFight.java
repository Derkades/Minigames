package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Random;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.snowfight.SnowFightMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class SnowFight extends Game {
	
	private static final int MAX_DURATION = 70;
	
	SnowFight() {
		super("Snow Fight", new String[] {
				"Kill other players using snowballs. Get snowballs",
				"by breaking snow with your shovel.",
				"The player with the most kills wins.",
		}, 2, 4, 9, SnowFightMap.MAPS);
	}
	
	private List<UUID> dead;
	private SnowFightMap map;
	
	private Map<UUID, Integer> kills;
	
	private Sidebar sidebar;
	
	@Override
	void begin(GameMap genericMap) {
		dead = new ArrayList<>();
		map = (SnowFightMap) genericMap;
		kills = new HashMap<>();
		
		sidebar = new Sidebar(ChatColor.DARK_AQUA + "" + ChatColor.DARK_AQUA + "Kills", 
				Minigames.getInstance(), Integer.MAX_VALUE, new SidebarString[] {new SidebarString("Loading...")});
		
		Utils.setGameRule("doTileDrops", false);
		
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(map.getSpawnLocation());
			
			kills.put(player.getUniqueId(), 0);
			
			Minigames.setCanTakeDamage(player, true);
		}
		
		ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.unbreakable()
				.name("Snow Shoveler")
				.lore("Break snow to get snowballs")
				.canDestroy("snow")
				.create();
		
		shovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
		
		new GameTimer(this, MAX_DURATION, 2) {

			@Override
			public void onStart() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.getInventory().addItem(shovel);
					sidebar.showTo(player);
					Minigames.setCanTakeDamage(player, true);
				}
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				updateSidebar(secondsLeft);
				
				//End the game if everyone is a spectator except one player (or everyone is a spectator)
				if (dead.size() >= (Bukkit.getOnlinePlayers().size() - 1) && secondsLeft > 2) {
					return 2;
				}
				
				return secondsLeft;
			}

			@Override
			public void onEnd() {								
				for (Player player : Bukkit.getOnlinePlayers()) {
					sidebar.hideFrom(player);
				}
				
				startNextGame(Utils.getWinnersFromPointsHashmap(kills));
			}
			
		};
	}
	
	/*@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if (event.getEntity().getLastDamageCause().getCause() == DamageCause.PROJECTILE) {
			event.setDamage(4);
		} else {
			event.setCancelled(true);
		}
	}*/
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		//Block block = event.getBlock();
		
		event.setCancelled(true);
		
		//if (block.getType() != Material.SNOW){
		//	return;
		//}
			
		Player player = event.getPlayer();
		Inventory inv = player.getInventory();
		
		if (!inv.contains(new ItemStack(Material.SNOWBALL, 16))) {
			int amount = Random.getRandomInteger(1, 3);
			
			if (amount + inv.getItem(0).getAmount() > 16) {
				amount = 16;
			}
			
			inv.addItem(new ItemStack(Material.SNOWBALL, amount));
		}
		
		//Snow snow = (Snow) block.getBlockData();

		//new BukkitRunnable() {
		//	public void run() {
		//		snow.setLayers(Random.getRandomInteger(1, 4));
		//	}
		//}.runTaskLater(Minigames.getInstance(), 3 * 20);
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event){
		Player killer = event.getEntity().getKiller();
		
		kills.put(killer.getUniqueId(), kills.get(killer.getUniqueId()) + 1);
		
		if (event.getEntityType() == EntityType.PLAYER){
			Player player = event.getEntity().getPlayer();
			//event.setDeathMessage(DARK_GRAY + "[" + DARK_AQUA + getName() + DARK_GRAY + "] " + AQUA + killer.getName() + " has killed " + pn + "!");
			event.setDeathMessage("");
			sendMessage(killer.getName() + " has killed " + player.getName() + "!");
			
			Scheduler.delay(1, () -> {
				player.spigot().respawn();
				player.teleport(Var.LOBBY_LOCATION);
				
				//Utils.hideForEveryoneElse(player);
				//Utils.giveInvisibility(player);
				
				dead.add(player.getUniqueId());
				Utils.clearInventory(player);
				Minigames.setCanTakeDamage(player, false);
			});
		}
	}
	
	private void updateSidebar(int secondsLeft) {
		kills = Utils.sortByValue(kills);
		
		List<SidebarString> sidebarStrings = new ArrayList<>();
	
		for (Player player : Bukkit.getOnlinePlayers()) {
			try {
				int points = kills.get(player.getUniqueId());
				sidebarStrings.add(new SidebarString(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points));
			} catch (NullPointerException e) { continue; }
		}
		
		sidebar.setEntries(sidebarStrings)
			.addEmpty()
			.addEntry(new SidebarString(ChatColor.GRAY + "Time left: " + secondsLeft + " seconds."))
			.update();
	}
	
}
