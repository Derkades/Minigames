package derkades.minigames.modules;

import derkades.minigames.GameState;
import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.UpdateSigns;
import derkades.minigames.Var;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class SpawnZombieShooter extends Module {

	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.name(text("Zombie Shooter", YELLOW, BOLD))
			.enchant(Enchantment.ARROW_DAMAGE, 5)
			.enchant(Enchantment.ARROW_INFINITE, 1)
			.unbreakable()
			.create();

	private static final ItemStack ARROW = new ItemBuilder(Material.ARROW)
			.name(text("Zombie Shooter Arrow", YELLOW, BOLD))
			.create();

	private static final float HUSK_CHANCE = 0.15f;
	private static final int MAX_LIVED_TICKS = 130;
	private static final UUID VILLAGER_UUID = UUID.fromString("5c70b3ae-aadf-4f76-81db-324cda76756e");

	public SpawnZombieShooter() {
		Scheduler.repeat(1, this::tickBowTarget);
		Scheduler.repeat(50, this::tickSpawn);
	}

	public void tickBowTarget() {
		if (GameState.isCurrentlyInGame() || Var.LOBBY_WORLD.getPlayers().isEmpty()) {
			return;
		}

		Entity entity = Var.LOBBY_WORLD.getEntity(VILLAGER_UUID);
		if (entity instanceof Villager bait) {
			for (final Zombie zombie : Var.LOBBY_WORLD.getEntitiesByClass(Zombie.class)) {
				zombie.setTarget(bait);
				if (zombie.getTicksLived() > MAX_LIVED_TICKS) {
					zombie.damage(100);
				}
			}
		} else {
			Logger.warning("Bait villager does not exist");
		}

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (!player.getGameMode().equals(GameMode.ADVENTURE) || !player.getLocation().getWorld().equals(Var.LOBBY_WORLD)) {
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

	public void tickSpawn() {
		if (GameState.isCurrentlyInGame() || Var.LOBBY_WORLD.getPlayers().isEmpty()) {
			return;
		}

		Class<? extends Zombie> type = ThreadLocalRandom.current().nextFloat() > (1.0f - HUSK_CHANCE) ? Husk.class : Zombie.class;
		final Zombie zombie = Var.LOBBY_WORLD.spawn(new Location(Var.LOBBY_WORLD, 224.5, 64, 289.5), type);
		zombie.setAdult();
		zombie.setSilent(true);
	}

	@EventHandler
	public void onDeath(final EntityDeathEvent event) {
		if (!(
				event.getEntityType() == EntityType.ZOMBIE ||
				event.getEntityType() == EntityType.HUSK)) {
			return;
		}

		final Zombie zombie = (Zombie) event.getEntity();

		if (!zombie.getLocation().getWorld().equals(Var.LOBBY_WORLD) ||
				zombie.getLastDamageCause() == null ||
				zombie.getLastDamageCause().getCause() != DamageCause.PROJECTILE) {
			return;
		}

		final Player killer = zombie.getKiller();

		if (killer == null) {
			return;
		}

		if (event.getEntityType() == EntityType.HUSK) {
			MPlayer m = new MPlayer(killer);
			m.sendActionBar(text("Bonus point!", NamedTextColor.GOLD));
			m.addPoints(1);
			UpdateSigns.updateLeaderboard();
		}

		killer.playSound(killer.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
		int killCount = Minigames.getInstance().getConfig().getInt("zombie-kill-count", 0);
		Minigames.getInstance().getConfig().set("zombie-kill-count", ++killCount);
		Minigames.getInstance().queueConfigSave();
		UpdateSigns.updateGlobalStats();
	}

}
