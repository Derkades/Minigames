package xyz.derkades.minigames;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.utils.MPlayer;

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
	}

	public static class BowAndTargetTask extends BukkitRunnable {

		@Override
		public void run() {
			if (Minigames.CURRENT_GAME != null) {
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

				if (player.isIn2dBounds(Var.LOBBY_WORLD, 221, 279, 217, 283)) {
					if (!player.getInventory().contains(BOW)) {
						player.giveItem(BOW, ARROW);
					}
				} else {
					player.getInventory().removeItem(BOW, ARROW);
				}
			}
		}

	}

	public static class SpawnTask extends BukkitRunnable {

		@Override
		public void run() {
			// No need to spawn zombies when a game is running
			if (Minigames.CURRENT_GAME != null) {
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
