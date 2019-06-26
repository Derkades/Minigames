package xyz.derkades.minigames.games;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Random;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.lootchests.LootChest;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.gladeroyale.GladeRoyaleItems;
import xyz.derkades.minigames.games.gladeroyale.GladeRoyaleMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesJoinEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.Queue;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class GladeRoyale extends Game<GladeRoyaleMap> {

	private static final int MIN_Y = 20;
	private static final int MAX_Y = 150;

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
	public int getRequiredPlayers() {
		return 6;
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

	private List<UUID> alive;
	private boolean gameEnded = false;

	@Override
	public void onPreStart() {
		this.currentBorderSize = this.map.getWorldborderSize();

		this.gameEnded = false;
		this.alive = Utils.getOnlinePlayersUuidList();

		final WorldBorder border = this.map.getWorld().getWorldBorder();
		border.setCenter(this.map.getMapCenter());
		border.setWarningDistance(30);
		border.setDamageAmount(1);
		border.setSize(20);



		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getMapCenter());
			player.setDisableItemMoving(false);
		}

		Queue.add(() -> {
			this.sendMessage("Starting world reset, you will experience lag.");

			final int minX = this.map.getMapCenter().getBlockX() - this.currentBorderSize / 2;
			final int maxX = this.map.getMapCenter().getBlockX() + this.currentBorderSize / 2;
			final int minZ = this.map.getMapCenter().getBlockZ() - this.currentBorderSize / 2;
			final int maxZ = this.map.getMapCenter().getBlockZ() + this.currentBorderSize / 2;

			int i = 0;
			int j = 0;

			for (int x = minX; x <= maxX; x++) {
				for (int y = MIN_Y; y <= MAX_Y; y++) {
					for (int z = minZ; z <= maxZ; z++) {
						i++;
						final Block block = new Location(this.map.getWorld(), x, y, z).getBlock();
						if (block.getType().equals(Material.TERRACOTTA) || block.getType().equals(Material.CHEST)) {
							j++;
							Bukkit.broadcastMessage(String.format("[debug] removed %s block at (%s, %s, %s). removed: %s. total: %s", block.getType(), x, y, z, j, i));
							block.setType(Material.AIR);
						}
					}
				}
			}
		});

		Queue.add(() ->{
			this.sendMessage("When the game starts, you will be teleported into the sky. Don't forget to activate your elytra!");
		});
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
			random.setY(random.getY() + 100);

			final Location finalRandom = random;

			Queue.add(() -> {
				player.sendTitle(ChatColor.RED + "You're falling", ChatColor.GRAY + "Activate your elytra!");
				player.teleport(finalRandom);
			});
		}
	}

	@Override
	public int gameTimer(int secondsLeft) {
		if (this.alive.size() < 2 && !this.gameEnded) {
			secondsLeft = 10;
			this.gameEnded = true;
			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				player.playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f);
			}
		}

		if (secondsLeft > 10 && secondsLeft % 10 == 0) {
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
	public void onEnd() {
		this.endGame(Utils.getWinnersFromAliveList(this.alive, false));
		this.alive.clear();
	}

	@EventHandler
	public void damage(final MinigamesPlayerDamageEvent event) {
		if (!this.started) {
			return;
		}

		if (event.willBeDead()) {
			event.setCancelled(true);
			event.getPlayer().dieUp(5);
			this.alive.remove(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void join(final MinigamesJoinEvent event) {
		event.getPlayer().dieTo(this.map.getMapCenter());
	}

	@EventHandler
	public void quit(final PlayerQuitEvent event) {
		this.alive.remove(event.getPlayer().getUniqueId());
	}

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

		final int x = Random.getRandomInteger(minX, maxX);
		final int z = Random.getRandomInteger(minZ, maxZ);

		Location location = null;

		for (int y = MAX_Y; y > MIN_Y; y--) {
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

}
