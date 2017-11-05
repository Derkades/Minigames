package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Random;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class CreeperAttack extends Game {

	CreeperAttack() {
		super("Creeper Attack", new String[] {"Avoid dying from creeper explosions"}, 2, 6, 7);
	}

	private List<UUID> alive;
	private BukkitTask task;
	private float chance;
	
	@Override
	void begin() {
		alive = new ArrayList<>();
		chance = 0.1f;
		
		ItemStack knockbackStick = new ItemBuilder(Material.STICK)
				.name(ChatColor.GOLD + "" + ChatColor.BOLD + "Creeper Smasher")
				.unsafeEnchant(Enchantment.KNOCKBACK, 3)
				.create();
		
		Bukkit.getOnlinePlayers().forEach((player) -> {
			alive.add(player.getUniqueId());
			player.teleport(new Location(Var.WORLD, 120.5, 105, 317.5));
			player.getInventory().addItem(knockbackStick);
			Minigames.setCanTakeDamage(player, true);
		});
		
		task = Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), () -> {
			List<Player> alivePlayers = Utils.getPlayerListFromUUIDList(alive);
			if (alivePlayers.size() <= 1) {
				finish();
			}
			
			if (Random.getRandomFloat() < chance) {
				chance += 0.02f;
				Creeper creeper = Var.WORLD.spawn(new Location(Var.WORLD, 120.5, 105, 317.5), Creeper.class);
				creeper.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000000, 50, true, false));
				if (chance > 0.5f) {
					creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 1, true, false));
				}
			}
		}, 20, 10);
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() != EntityType.CREEPER) {
			event.setCancelled(true);
		}
	}
	
	private void finish() {
		task.cancel();
		super.startNextGame(Utils.getPlayerListFromUUIDList(alive));
	}

}
