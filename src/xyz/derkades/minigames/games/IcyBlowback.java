package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.icyblowback.IcyBlowbackMap;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Utils;

public class IcyBlowback extends Game {

	private static final int GAME_DURATION = 100;
	private static final int SPREAD_TIME = 2;
	private static final ItemStack SWORD = new ItemBuilder(Material.WOODEN_SWORD)
			.name(ChatColor.AQUA + "Knockback sword")
			.enchant(Enchantment.KNOCKBACK, 2)
			.create();
	
	IcyBlowback() {
		super("Icy Blowback", 
				new String[] {
						"Try to knock others off the slippery ice."
				}, 3, 3, 6, IcyBlowbackMap.MAPS);
	}

	List<UUID> dead;
	IcyBlowbackMap map;
	
	@Override
	void begin(GameMap genericMap) {
		dead = new ArrayList<>();
		map = (IcyBlowbackMap) genericMap;
		
		
		Location[] spawnLocations = map.getSpawnLocations();
		int index = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}
			
			player.teleport(spawnLocations[index]);
			index--;
		}
		
		new BukkitRunnable() {
			
			int timeLeft = GAME_DURATION + SPREAD_TIME;
			
			public void run() {
				if (timeLeft > GAME_DURATION) {
					sendMessage("The game will start in " + (timeLeft - GAME_DURATION) + " seconds");
				}
				
				if (timeLeft == GAME_DURATION) {
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
			player.getInventory().addItem(SWORD);
			Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 0);
			Utils.giveInfiniteEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 255);
		}
		
		//Var.WORLD.setTime(14000);
	}
	
	private void end() {
		//Var.WORLD.setTime(6000);
		super.startNextGame(Arrays.asList(ListUtils.getRandomValueFromList(Utils.getWinnersFromDeadList(dead))));
	}
	
	private void die(Player player) {
		dead.add(player.getUniqueId());
		player.setAllowFlight(true);
		Utils.giveInvisibility(player);
		Var.WORLD.spigot().strikeLightningEffect(player.getLocation(), false);
		//player.teleport(map.getSpectatorLocation());
		player.getInventory().clear();
		Utils.hideForEveryoneElse(player);
		player.setFlying(true);
		Location loc = player.getLocation();
		loc.setY(loc.getY() + 10);
		player.teleport(loc);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getLocation().getY() < 87) {
			die(player);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (dead.contains(event.getDamager().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
}
