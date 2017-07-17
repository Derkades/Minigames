package xyz.derkades.minigames;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Utils;

public class GlobalListeners implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Utils.teleportToLobby(player); //Teleport the player out of an arena they may be in
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false); //Just in case the player was spectator
		
		for (Game game : Game.getAllGames()){
			Utils.setCanTakeDamage(player, false);
			game.resetHashMaps(event.getPlayer());
		}
		
		Console.sendCommand("scoreboard teams join all " + player.getName());
		
		event.setJoinMessage(ChatColor.AQUA + "" + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " has joined!");
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		event.setQuitMessage(ChatColor.AQUA + "" + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " has left.");
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
		if (!Utils.canTakeDamage(player)){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if (player.getName().equals("RobinMC")){
			event.setFormat(DARK_GRAY + "[" + AQUA + Points.getPoints(player) + DARK_GRAY + "] " + 
					DARK_GRAY + "[" + ChatColor.GREEN + "Owner" + DARK_GRAY + "] " +
					NameColor.getNameColor(player) + player.getName() + DARK_GRAY + 
					": " + GRAY + event.getMessage() + "");
		} else if (VIP.isStaff(player)){
			event.setFormat(DARK_GRAY + "[" + AQUA + Points.getPoints(player) + DARK_GRAY + "] " + 
					DARK_GRAY + "[" + ChatColor.AQUA + "VIP" + DARK_GRAY + "] " +
					NameColor.getNameColor(player) + player.getName() + DARK_GRAY + 
					": " + GRAY + event.getMessage() + "");
		} else {
			event.setFormat(DARK_GRAY + "[" + AQUA + Points.getPoints(player) + DARK_GRAY + "] " + 
					NameColor.getNameColor(player) + player.getName() + DARK_GRAY + 
					": " + GRAY + event.getMessage() + "");
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if (!Main.IS_IN_GAME){
			Material type = event.getTo().getBlock().getType();
			if ((type == Material.WATER || type == Material.STATIONARY_WATER) &&
					event.getPlayer().getGameMode() == GameMode.ADVENTURE){
				event.getPlayer().teleport(new Location(Var.WORLD, 217.0, 67, 258.0, 90, 0));
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (entity instanceof Villager){
			event.setCancelled(true);
			Menu.open(player);
		}
	}

}
