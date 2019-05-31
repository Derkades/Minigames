package xyz.derkades.minigames.games;

import java.util.ArrayList;
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
				}, 3, IcyBlowbackMap.MAPS);
	}

	List<UUID> dead;
	IcyBlowbackMap map;

	@Override
	void begin(final GameMap genericMap) {
		this.dead = new ArrayList<>();
		this.map = (IcyBlowbackMap) genericMap;


		final Location[] spawnLocations = this.map.getSpawnLocations();
		int index = 0;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}

			player.teleport(spawnLocations[index]);
			index--;
		}

		new BukkitRunnable() {

			int timeLeft = GAME_DURATION + SPREAD_TIME;

			@Override
			public void run() {
				if (this.timeLeft > GAME_DURATION) {
					IcyBlowback.this.sendMessage("The game will start in " + (this.timeLeft - GAME_DURATION) + " seconds");
				}

				if (this.timeLeft == GAME_DURATION) {
					IcyBlowback.this.start();
				}

				if (this.timeLeft == 60 || this.timeLeft == 30 || this.timeLeft == 10 || this.timeLeft < 5) {
					IcyBlowback.this.sendMessage("The game will end in " + this.timeLeft + " seconds");
				}

				if (this.timeLeft <= 0) {
					IcyBlowback.this.end();
					this.cancel();
				}

				if (Utils.getAliveCountFromDeadList(IcyBlowback.this.dead) < 2) {
					IcyBlowback.this.end();
					this.cancel();
				}

				this.timeLeft--;
			}
		}.runTaskTimer(Minigames.getInstance(), 20, 20);
	}

	private void start() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			Minigames.setCanTakeDamage(player, true);
			player.getInventory().addItem(SWORD);
			Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 0);
			Utils.giveInfiniteEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 255);
		}
	}

	private void end() {
		final List<Player> winners = Utils.getWinnersFromDeadList(this.dead);
		if (winners.size() == 1) {
			super.endGame(winners);
		} else {
			super.endGame(new ArrayList<>());
		}

	}

	private void die(final Player player) {
		this.dead.add(player.getUniqueId());
		player.setAllowFlight(true);
		Utils.giveInvisibility(player);
		Var.WORLD.spigot().strikeLightningEffect(player.getLocation(), false);
		player.getInventory().clear();
		Utils.hideForEveryoneElse(player);
		player.setFlying(true);
		final Location loc = player.getLocation();
		loc.setY(loc.getY() + 10);
		player.teleport(loc);
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();

		if (this.dead.contains(player.getUniqueId())) {
			return;
		}

		if (player.getLocation().getY() < 87) {
			this.die(player);
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		if (this.dead.contains(event.getDamager().getUniqueId())) {
			event.setCancelled(true);
		}
	}

}
