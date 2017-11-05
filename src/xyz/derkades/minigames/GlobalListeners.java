package xyz.derkades.minigames;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.menu.NameColor;
import xyz.derkades.minigames.utils.Utils;

public class GlobalListeners implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Utils.teleportToLobby(player); //Teleport the player out of an arena they may be in
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false); //Just in case the player was spectator
		
		Minigames.setCanTakeDamage(player, false);
		
		player.setExp(0.0f);
		player.setLevel(0);
		
		//Console.sendCommand("scoreboard teams join all " + player.getName());
		
		String minMax = ChatColor.GRAY + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")";
		
		event.setJoinMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " has joined! " + minMax);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		event.setQuitMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " has left.");
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		event.setCancelled(true); //Cancel players dropping items
	}
	
	@EventHandler
	public void damage(EntityDamageEvent event){
		if (event.getEntity() instanceof Villager){
			event.setCancelled(true);
		}
		
		if (!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player) event.getEntity();
		if (!Minigames.canTakeDamage(player)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if (player.getName().equals("Derkades")){
			event.setFormat(DARK_GRAY + "[" + AQUA + Points.getPoints(player) + DARK_GRAY + "] " + 
					DARK_GRAY + "[" + ChatColor.GREEN + "Owner" + DARK_GRAY + "] " +
					NameColor.getNameColor(player) + "%s" + DARK_GRAY + 
					": " + GRAY + "%s");
		} else if (VIP.isStaff(player)){
			event.setFormat(DARK_GRAY + "[" + AQUA + Points.getPoints(player) + DARK_GRAY + "] " + 
					DARK_GRAY + "[" + ChatColor.AQUA + "Staff" + DARK_GRAY + "] " +
					NameColor.getNameColor(player) + "%s" + DARK_GRAY + 
					": " + GRAY + "%s");
		} else {
			event.setFormat(DARK_GRAY + "[" + AQUA + Points.getPoints(player) + DARK_GRAY + "] " + 
					NameColor.getNameColor(player) + "%s" + DARK_GRAY + 
					": " + GRAY + "%s");
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if (!Minigames.IS_IN_GAME){
			Material type = event.getTo().getBlock().getType();
			Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
			if ((type == Material.WATER || type == Material.STATIONARY_WATER) &&
					event.getPlayer().getGameMode() == GameMode.ADVENTURE){
				event.getPlayer().teleport(new Location(Var.WORLD, 217.0, 67, 258.0, 90, 0));
			} else if (below == Material.SLIME_BLOCK) {
				PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
				event.getPlayer().addPotionEffect(jump);
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (entity instanceof Villager){
			event.setCancelled(true);
			new MainMenu(player).open();
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) {
			return;
		}
		
		List<Material> unsafeBlocks = Arrays.asList(
			Material.ACACIA_DOOR,
			Material.ACACIA_FENCE_GATE,
			Material.BIRCH_DOOR,
			Material.BIRCH_FENCE_GATE,
			Material.DARK_OAK_DOOR,
			Material.DARK_OAK_FENCE_GATE,
			Material.JUNGLE_DOOR,
			Material.JUNGLE_FENCE_GATE,
			Material.SPRUCE_DOOR,
			Material.SPRUCE_FENCE_GATE,
			Material.TRAP_DOOR,
			Material.WOOD_DOOR,
			Material.WOODEN_DOOR
		);
		
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		if (action == Action.RIGHT_CLICK_BLOCK && unsafeBlocks.contains(block.getType())) {
			event.setCancelled(true);
		}
	}

}
