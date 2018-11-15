package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.sniper.SniperMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Sniper extends Game {

	private static final int MAX_GAME_DURATION = 120;
	private static final int SPREAD_TIME = 10;
	
	private static final ItemStack SWORD = new ItemBuilder(Material.WOOD_SWORD)
			.create();
	
	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.name("Insta-kill bow")
			.lore("Get arrows by killing other players")
			.create();
	
	private static final ItemStack ARROW = new ItemBuilder(Material.ARROW)
			.create();
	
	Sniper() {
		super("Sniper", new String[] {
				"Shoot other players with your instant kill bow.",
		}, 3, 3, 5);
	}

	private List<UUID> dead;
	private SniperMap map;
	
	@Override
	void begin() {
		dead = new ArrayList<>();
		map = ListUtils.getRandomValueFromArray(SniperMap.MAPS);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(map.getSpawnLocation());
		}
		
		new BukkitRunnable() {
			
			int timeLeft = MAX_GAME_DURATION + SPREAD_TIME;
			
			@Override
			public void run() {
				if (timeLeft > MAX_GAME_DURATION) {
					sendMessage("The game will start in " + (timeLeft - MAX_GAME_DURATION) + " seconds");
				}
				
				if (timeLeft == MAX_GAME_DURATION) {
					start();
				}
				
				if (timeLeft == 60 || timeLeft == 30 || timeLeft == 10 || timeLeft < 5) {
					sendMessage("The game will end in " + timeLeft + " seconds");
				}
				
				if (timeLeft <= 0) {
					end();
					this.cancel();
				}
				
				if (Utils.getAliveCountFromDeadList(dead) < 2) {
					end();
					this.cancel();
				}
				
				timeLeft--;
			}
			
		}.runTaskTimer(Minigames.getInstance(), 20, 20);
	}
	
	private void start() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Minigames.setCanTakeDamage(player, true);
			PlayerInventory inv = player.getInventory();
			inv.addItem(SWORD, BOW, ARROW);
		}
	}
	
	private void end() {
		super.startNextGame(Utils.getWinnersFromDeadList(dead));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();
		killer.getInventory().addItem(ARROW);
		dead.add(player.getUniqueId());
		
		Scheduler.delay(1, () -> {
			player.spigot().respawn();
			if (map.getSpectatorLocation() != null) 
				player.teleport(map.getSpectatorLocation());
			player.setAllowFlight(true);
			Utils.giveInvisibility(player);
			player.getInventory().clear();
			Minigames.setCanTakeDamage(player, false);
		});
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		Entity damagedEntity = event.getEntity();
		Entity attackingEntity = event.getDamager();
		
		if (damagedEntity.getType() != EntityType.PLAYER) {
			return;
		}
		
		if (dead.contains(attackingEntity.getUniqueId()) || dead.contains(damagedEntity.getUniqueId())) {
			return;
		}
		
		if (attackingEntity.getType() == EntityType.ARROW) {
			event.setDamage(20);
			return;
		}
		
		if (attackingEntity.getType() != EntityType.PLAYER) {
			return;
		}
		
		ItemStack weapon = ((Player) attackingEntity).getItemInHand();
		
		if (weapon.isSimilar(SWORD)) {
			event.setDamage(2);
		}
	}

}
