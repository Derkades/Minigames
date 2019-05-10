package xyz.derkades.minigames;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;

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
import org.bukkit.event.EventPriority;
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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.bukkit.MaterialLists;
import xyz.derkades.minigames.menu.MainMenu;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class GlobalListeners implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Utils.teleportToLobby(player); //Teleport the player out of an arena they may be in
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false); //Just in case the player was spectator
		Utils.clearInventory(player);
		Minigames.setCanTakeDamage(player, false);
		
		player.setExp(0.0f);
		player.setLevel(0);
		
		//Console.sendCommand("scoreboard teams join all " + player.getName());
		
		String minMax = ChatColor.GRAY + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")";
		
		event.setJoinMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " has joined! " + minMax);
		
		Scheduler.delay(1, () -> {
			player.sendMessage(ChatColor.GRAY + "Welcome to the arcade server!");
			player.spigot().sendMessage(
					new ComponentBuilder("For feature requests and bug reports, click here.")
					.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Derkades/Minigames/issues"))
					.create());
			player.spigot().sendMessage(
					new ComponentBuilder("To join our discord server, click here.")
					.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/pfbV4GA"))
					.create());
		});
		
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
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent event) {
		event.setFormat(DARK_GRAY + "[" + AQUA + Points.getPoints(event.getPlayer()) + DARK_GRAY + "] " + event.getFormat());
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if (!Minigames.IS_IN_GAME){
			Player player = event.getPlayer();
			
			Material type = event.getTo().getBlock().getType();
			Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
			
			if (type == Material.WATER && player.getGameMode() == GameMode.ADVENTURE){
				player.teleport(new Location(Var.WORLD, 217.0, 67, 258.0, 90, 0));
			} else if (below == Material.SLIME_BLOCK) {
				PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
				player.addPotionEffect(jump);
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
		
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		if (action == Action.RIGHT_CLICK_BLOCK && MaterialLists.INTERACTABLE_BLOCKS.contains(block.getType())) {
			event.setCancelled(true);
		}
	}

}
