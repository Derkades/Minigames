package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class TntTag extends Game {

	TntTag() {
		super("Tnt Tag", new String[] {
				"Pass TNT onto other players",
				"The game is finished when only",
				"one player has not exploded."
		}, 2, 2, 5);
	}
	
	private BukkitTask tntCountdownTask;
	private List<UUID> dead;

	@Override
	void begin() {
		dead = new ArrayList<>();
		
		sendMessage("You have 5 seconds to spread.");
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(new Location(Var.WORLD, 347, 44, 365, 0, 90));
			Utils.giveInvisibility(player);
		}
		
		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				Minigames.setCanTakeDamage(player, true);
				Utils.clearPotionEffects(player);
			}
			
			Player randomPlayer = getRandomPlayer();
			tag(randomPlayer);
			randomPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 2, true, false));
		}, 10*20);		
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) {
			return;
		}
		
		event.setCancelled(true);
		
		Player target = (Player) event.getEntity();
		Player attacker = (Player) event.getDamager();
		
		if (dead.contains(attacker.getUniqueId())) {
			event.setCancelled(true);
			return;
		}
		
		if (!attacker.getInventory().contains(Material.TNT)) {
			attacker.sendMessage(ChatColor.RED + "You don't have tnt on you, you don't need to hit people.");
			event.setCancelled(true);
			return;
		}
		
		attacker.getInventory().clear();
		attacker.getInventory().setHelmet(new ItemStack(Material.AIR));
		
		attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 2, true, false));
		
		tag(target);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (event.getPlayer().isSneaking()) {
			event.getPlayer().sendMessage(ChatColor.RED + "You cannot hide from players by sneaking.");
		}
	}
	
	private void tag(Player player) {
		if (tntCountdownTask != null) {
			tntCountdownTask.cancel();
		}
		
		tntCountdownTask = new TntCountdown(player).runTaskTimer(Minigames.getInstance(), 20, 20);
		
		sendMessage(String.format("%s has tnt on them, run!", player.getName()));
		player.getInventory().setHelmet(new ItemStack(Material.TNT));
		for (int i = 0; i < 9; i++) {
			player.getInventory().setItem(i, new ItemStack(Material.TNT));
		}
		player.sendMessage("You have been tagged! Punch someone to get rid of your tnt.");
	}
	
	private Player getRandomPlayer(){
		//Collection<? extends Player> list = getAlivePlayers();
		//int size = list.size();
		//int index = Random.getRandomInteger(0, size - 1); //Size -1 because if the list has 1 entry (entry 0) the length is 1.
		//return (Player) list.toArray()[index];
		return ListUtils.getRandomValueFromList(getAlivePlayers());
	}
	
	private List<Player> getAlivePlayers() {
		Collection<? extends Player> list = Bukkit.getOnlinePlayers();
		List<Player> alive = new ArrayList<>();
		list.forEach((player) -> {
			if (!dead.contains(player.getUniqueId())) alive.add(player);
		});
		return alive;
	}
	
	public class TntCountdown extends BukkitRunnable {
		
		private Player player;
		private float progress;
		
		TntCountdown(Player player){
			this.player = player;
			
			progress = 1.1f;
		}

		@Override
		public void run() {
			progress -= 0.1f;
			player.setExp(progress);
			
			if (progress <= 0.0f) {
				this.cancel();
			
				dead.add(player.getUniqueId());
				
				player.getInventory().clear();
				player.getInventory().setHelmet(new ItemStack(Material.AIR));
				Bukkit.getOnlinePlayers().forEach((online) -> online.hidePlayer(player));
				player.setAllowFlight(true);
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
				}
				
				int alive = getAlivePlayers().size();
				sendMessage(player.getName() + " exploded into a million pieces! " + alive + " players left.");
				if (alive == 1) {
					startNextGame(Utils.getWinnersFromDeadList(dead));
					return;
				}
				
				Player randomPlayer = getRandomPlayer();
				tag(randomPlayer);
				randomPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 2, true, false));
			}
		}
		
	}

}
