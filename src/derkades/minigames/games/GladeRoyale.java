package derkades.minigames.games;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.gladeroyale.GladeRoyaleItems;
import derkades.minigames.games.gladeroyale.GladeRoyaleMap;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.queue.TaskQueue;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.lootchests.LootChest;

public class GladeRoyale extends Game<GladeRoyaleMap> {

	@Override
	public String getIdentifier() {
		return "glade_royale";
	}

	@Override
	public String getName() {
		return "Glade Royale";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"totally not battle royale"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.CHEST;
	}

	@Override
	public int getRequiredPlayers() {
		return 8;
	}

	@Override
	public GladeRoyaleMap[] getGameMaps() {
		return GladeRoyaleMap.MAPS;
	}

	@Override
	public int getPreDuration() {
		return 15;
	}

	@Override
	public int getDuration() {
		return 400; // Do not change without changing the game timer code
	}

	private int currentBorderSize = 0;

	private Set<UUID> alive;

	@Override
	public void onPreStart() {
		this.currentBorderSize = this.map.getWorldborderSize();
		this.alive = Utils.getOnlinePlayersUuidSet();

		final WorldBorder border = this.map.getWorld().getWorldBorder();
		border.setCenter(this.map.getMapCenter());
		border.setWarningDistance(30);
		border.setDamageAmount(1);
		border.setSize(20);

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getMapCenter());
			player.setDisableItemMoving(false);
		}

		this.sendMessage("Starting world reset, you will experience lag.");

		final int minX = this.map.getMapCenter().getBlockX() - this.currentBorderSize / 2;
		final int maxX = this.map.getMapCenter().getBlockX() + this.currentBorderSize / 2;
		final int minZ = this.map.getMapCenter().getBlockZ() - this.currentBorderSize / 2;
		final int maxZ = this.map.getMapCenter().getBlockZ() + this.currentBorderSize / 2;

		final AtomicInteger counter = new AtomicInteger();

		for (int y = this.map.getMinY(); y <= this.map.getMaxY(); y++) {
			final int fY = y;
			TaskQueue.add(() -> {
				for (int x = minX; x <= maxX; x++) {
					for (int z = minZ; z <= maxZ; z++) {
						final Block block = new Location(this.map.getWorld(), x, fY, z).getBlock();
						if (block.getType().equals(Material.TERRACOTTA) || block.getType().equals(Material.CHEST)) {
							Logger.debug("removed %s (%s, %s, %s). %s", block.getType(), x, fY, z, counter.incrementAndGet());
							block.setType(Material.AIR);
						}
					}
				}
			});
		}

		Logger.info("Removed %s blocks", counter.get());

		this.sendMessage("When the game starts, you will be teleported into the sky. Don't forget to activate your elytra!");
	}

	@Override
	public void onStart() {
		this.map.getWorld().getWorldBorder().setSize(this.currentBorderSize);

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.setDisableDamage(false);

			player.setArmor(null, new ItemBuilder(Material.ELYTRA).name(ChatColor.YELLOW + "\"Parachute\"").create(), null, null);
			player.giveItem(GladeRoyaleItems.TOOL);
			player.giveItem(new ItemBuilder(GladeRoyaleItems.BLOCK).amount(32).create());

			Location random = this.getRandomLocationWithinBorder();
			if (random == null) { // This shouldn't happen, but just in case
				random = this.map.getMapCenter();
			}
			random.setY(this.map.getSpawnY());

			player.sendTitle(ChatColor.RED + "You're falling", ChatColor.GRAY + "Activate your elytra!");
			player.queueTeleport(random);
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (secondsLeft > 10 && ((secondsLeft > 360 && secondsLeft % 5 == 0) || secondsLeft % 20 == 0)) {
			this.spawnSupplyDrop();
		}

		if (secondsLeft == 360) {
			final int newSize = this.currentBorderSize / 2; // e.g. 512 -> 256
			this.sendMessage(String.format("Shrinking border from %sx%s to %sx%s in 1 minute",
					this.currentBorderSize, this.currentBorderSize, newSize, newSize));
		}

		if (secondsLeft == 310) {
			final int newSize = this.currentBorderSize / 2; // e.g. 512 -> 256
			this.sendMessage(String.format("Shrinking border from %sx%s to %sx%s in 10 seconds",
					this.currentBorderSize, this.currentBorderSize, newSize, newSize));
		}

		if (secondsLeft == 300) {
			final int newSize = this.currentBorderSize / 2; // e.g. 512 -> 256
			this.sendMessage(String.format("Shrinking border from %sx%s to %sx%s",
					this.currentBorderSize, this.currentBorderSize, newSize, newSize));
			this.map.getWorld().getWorldBorder().setSize(newSize, 20);
			this.currentBorderSize = newSize;
		}

		if (secondsLeft == 260) {
			final int newSize = this.currentBorderSize / 2; // e.g. 256 -> 128
			this.sendMessage(String.format("Shrinking border from %sx%s to %sx%s in 1 minute",
					this.currentBorderSize, this.currentBorderSize, newSize, newSize));
		}

		if (secondsLeft == 210) {
			final int newSize = this.currentBorderSize / 2; // e.g. 256 -> 128
			this.sendMessage(String.format("Shrinking border from %sx%s to %sx%s in 10 seconds",
					this.currentBorderSize, this.currentBorderSize, newSize, newSize));
		}

		if (secondsLeft == 200) {
			final int newSize = this.currentBorderSize / 2; // e.g. 256 -> 128
			this.sendMessage(String.format("Shrinking border from %sx%s to %sx%s",
					this.currentBorderSize, this.currentBorderSize, newSize, newSize));
			this.map.getWorld().getWorldBorder().setSize(newSize, 20);
			this.currentBorderSize = newSize;
		}

		if (secondsLeft == 150) {
			this.sendMessage("Slowly shrinking border to 20x20");
			this.map.getWorld().getWorldBorder().setSize(20, 100);
			this.currentBorderSize = 20;
		}

		if (secondsLeft == 40) {
			this.sendMessage("Camping doesn't work, if more than one player is alive at the end, no one will win.");
		}

		if (secondsLeft == 30 || secondsLeft == 22 || secondsLeft == 14) {
			this.sendMessage("Country roads, take me home");
		}
		if (secondsLeft == 29 || secondsLeft == 21 || secondsLeft == 13) {
			this.sendMessage("To the place I belong");
		}
		if (secondsLeft == 28 || secondsLeft == 20 || secondsLeft == 12) {
			this.sendMessage("West Virginia, mountain mama");
		}
		if (secondsLeft == 27 || secondsLeft == 19 || secondsLeft == 11) {
			this.sendMessage("Take me home, country roads");
		}

		if (secondsLeft == 26) {
			this.sendMessage("All my memories gather round her");
		}
		if (secondsLeft == 25) {
			this.sendMessage("Miner's lady, stranger to blue water");
		}
		if (secondsLeft == 24) {
			this.sendMessage("Dark and dusty, painted on the sky");
		}
		if (secondsLeft == 23) {
			this.sendMessage("Misty taste of moonshine, teardrop in my eye");
		}

		if (secondsLeft == 18) {
			this.sendMessage("I hear her voice, in the morning hour she calls me");
		}
		if (secondsLeft == 17) {
			this.sendMessage("The radio reminds me of my home far away");
		}
		if (secondsLeft == 16) {
			this.sendMessage("And driving down the road I get a feeling");
		}
		if (secondsLeft == 15) {
			this.sendMessage("That I should have been home yesterday, yesterday");
		}

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		if (this.alive.size() < 2) {
			this.map.getWorld().getWorldBorder().setSize(this.map.getWorldborderSize());
			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				player.playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEnd() {
		this.endGame(this.alive, true);
		this.alive = null;
	}

	@EventHandler
	public void damage(final MinigamesPlayerDamageEvent event) {
		if (!this.hasStarted()) {
			return;
		}

		if (event.willBeDead()) {
			event.setCancelled(true);
			event.getPlayer().dieUp(5);
			this.alive.remove(event.getPlayer().getUniqueId());
			event.getPlayer().dropItems();

			if (event.getType() == DamageType.ENTITY) {
				this.sendMessage(String.format("%s has been killed by %s", event.getPlayer().getName(), event.getDamagerPlayer().getName()));
			} else {
				this.sendMessage(String.format("%s has died", event.getPlayer().getName()));
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void blockPlace(final BlockPlaceEvent event) {
		if (event.getBlock().getY() >= this.map.getMaxY()) {
			new MPlayer(event.getPlayer()).sendChat("You cannot build any higher");
			event.setCancelled(true);
		}
	}

//	@EventHandler
//	public void join(final MinigamesJoinEvent event) {
//		event.getPlayer().dieTo(this.map.getMapCenter());
//	}
//
//	@EventHandler
//	public void quit(final PlayerQuitEvent event) {
//		this.alive.remove(event.getPlayer().getUniqueId());
//	}

	private void spawnSupplyDrop() {
		final Location loc = this.getRandomLocationWithinBorder();
		if (loc == null) {
			return;
		}

		this.sendMessage(String.format("Supply drop at (%s, %s)", loc.getBlockX(), loc.getBlockZ()));

		final Location shulkerBullet = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY() + 100, loc.getZ() + 0.5);
		loc.getWorld().spawnEntity(shulkerBullet, EntityType.SHULKER_BULLET);
		Scheduler.delay(70, () -> {
			final Block block = loc.getBlock();
			block.setType(Material.CHEST);

			final Chest chest = (Chest) block.getState();

			chest.setCustomName("Supply drop");

			chest.update();

			final LootChest loot = new LootChest(loc, GladeRoyaleItems.SUPPLY_DROP);
			loot.fill();
			loc.getWorld().spigot().strikeLightning(loc, false);
		});
	}

	private Location getRandomLocationWithinBorder() {
		final int minX = this.map.getMapCenter().getBlockX() - this.currentBorderSize / 2;
		final int maxX = this.map.getMapCenter().getBlockX() + this.currentBorderSize / 2;
		final int minZ = this.map.getMapCenter().getBlockZ() - this.currentBorderSize / 2;
		final int maxZ = this.map.getMapCenter().getBlockZ() + this.currentBorderSize / 2;

		final int x = ThreadLocalRandom.current().nextInt(minX + 1, maxX);
		final int z = ThreadLocalRandom.current().nextInt(minZ + 1, maxZ);

		Location location = null;

		for (int y = this.map.getMaxY(); y > this.map.getMinY(); y--) {
			final Location locTemp = new Location(this.map.getWorld(), x, y, z);

			if (locTemp.getBlock().getType() != Material.AIR) {
				location = locTemp;
				break;
			}
		}

		if (location == null) {
			this.sendMessage(String.format("[debug warning] Unsuitable loot location at (%s, %s)", x, z));
		}

		if (location != null) {
			location.setY(location.getY() + 1);
		}

		return location;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.dieTo(this.map.getMapCenter());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
