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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.sniper.SniperMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class OneInTheQuiver extends Game {

	private static final int MAX_GAME_DURATION = 120;
	private static final int SPREAD_TIME = 5;
	
	private static final ItemStack SWORD = new ItemBuilder(Material.WOODEN_SWORD)
			.unbreakable()
			.create();
	
	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.unbreakable()
			.name("Insta-kill bow")
			.lore("Get arrows by killing other players")
			.create();
	
	private static final ItemStack ARROW = new ItemBuilder(Material.ARROW)
			.create();
	
	OneInTheQuiver() {
		super("One in the Quiver", new String[] {
				"Kill other players with a weak wooden sword.",
				"For every kill you'll get a single arrow. Arrows",
				"do enough damage to kill any player instantly."
		}, 3, 3, 5, SniperMap.MAPS);
	}

	private List<UUID> dead;
	
	private SniperMap map;
	
	@Override
	void begin(GameMap genericMap) {
		dead = new ArrayList<>();
		map = (SniperMap) genericMap;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(map.getSpawnLocation());
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5*20, 0, true, false));
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
		List<Player> winners = Utils.getWinnersFromDeadList(dead);
		if (winners.size() > 1) {
			winners.clear();
		}
		super.endGame(winners);
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
		
		ItemStack weapon = ((Player) attackingEntity).getInventory().getItemInMainHand();
		
		if (weapon.isSimilar(SWORD)) {
			event.setDamage(2);
		}
	}

}
