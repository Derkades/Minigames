package derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import derkades.minigames.utils.MPlayer;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class SpawnZombieShooter {

	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.name(ChatColor.YELLOW + "" + ChatColor.BOLD + "Zombie Shooter")
			.enchant(Enchantment.ARROW_DAMAGE, 5)
			.enchant(Enchantment.ARROW_INFINITE, 1)
			.unbreakable()
			.create();

	private static final ItemStack ARROW = new ItemBuilder(Material.ARROW)
			.name(ChatColor.YELLOW + "" + ChatColor.BOLD + "Zombie Shooter Arrow")
			.create();

	public static void init() {
		new BowAndTargetTask().runTaskTimer(Minigames.getInstance(), 40, 1);
		new SpawnTask().runTaskTimer(Minigames.getInstance(), 40, 50);
		Bukkit.getPluginManager().registerEvents(new ZombieDieListener(), Minigames.getInstance());
	}
	
	private static class ZombieDieListener implements Listener {
		
		@EventHandler
		public void onDeath(final EntityDeathEvent event) {
			if (Minigames.CURRENT_GAME != null || event.getEntityType() != EntityType.ZOMBIE) {
				return;
			}
			
			final Zombie zombie = (Zombie) event.getEntity();
			
			if (zombie.getLastDamageCause().getCause() != DamageCause.PROJECTILE) {
				return;
			}
			
			final Player killer = zombie.getKiller();
			
			if (killer == null) {
				return;
			}
			
			killer.playSound(killer.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
			int killCount = Minigames.getInstance().getConfig().getInt("zombie-kill-count", 0);
			Minigames.getInstance().getConfig().set("zombie-kill-count", ++killCount);
			Minigames.getInstance().saveConfig();
			UpdateSigns.updateGlobalStats();
		}
		
	}

	private static class BowAndTargetTask extends BukkitRunnable {

		@Override
		public void run() {
			if (Minigames.CURRENT_GAME != null) {
				return;
			}
			
			if (Var.LOBBY_WORLD.getPlayers().isEmpty()) {
				return;
			}

			for (final Zombie zombie : Var.LOBBY_WORLD.getEntitiesByClass(Zombie.class)) {
				final Villager bait = getBaitVillager();
				if (bait == null) {
					System.out.println("Bait villager does not exist");
					return;
				}

				zombie.setTarget(bait);
			}

			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				if (!player.getGameMode().equals(GameMode.ADVENTURE)) {
					continue;
				}

				if (!player.getLocation().getWorld().equals(Var.LOBBY_WORLD)) {
					continue;
				}

				if (player.isIn3dBounds(Var.LOBBY_WORLD, 222, 63, 278, 217, 69, 283) &&
						(player.yawInBounds(-70, 70))
						) {
					if (!player.getInventory().contains(BOW)) {
						player.giveItem(BOW);
						player.getInventory().setItem(9, ARROW);
					}
					player.getInventory().setHeldItemSlot(0);
				} else {
					player.getInventory().removeItem(BOW, ARROW);
				}
			}
		}

	}

	private static class SpawnTask extends BukkitRunnable {

		@Override
		public void run() {
			// No need to spawn zombies when a game is running
			if (Minigames.CURRENT_GAME != null) {
				return;
			}
			
			if (Var.LOBBY_WORLD.getPlayers().isEmpty()) {
				return;
			}

			final Zombie zombie = Var.LOBBY_WORLD.spawn(new Location(Var.LOBBY_WORLD, 224.5, 64, 289.5), Zombie.class);
			zombie.setAdult();
		}

	}

	private static Villager getBaitVillager() {
		for (final Villager villager : Var.LOBBY_WORLD.getEntitiesByClass(Villager.class)) {
			if (villager.getCustomName() != null && villager.getCustomName().equals("Bait")) {
				return villager;
			}
		}
		return null;
	}

}
