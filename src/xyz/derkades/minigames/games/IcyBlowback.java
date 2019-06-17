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

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.icyblowback.IcyBlowbackMap;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Utils;

public class IcyBlowback extends Game {

	private static final int GAME_DURATION = 100;
	private static final int SPREAD_TIME = 5;
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

	private List<UUID> dead;
	private List<UUID> all;

	private IcyBlowbackMap map;

	@Override
	void begin(final GameMap genericMap) {
		this.dead = new ArrayList<>();
		this.all = new ArrayList<>();

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

		new GameTimer(this, GAME_DURATION, SPREAD_TIME) {

			@Override
			public void onStart() {
				for (final Player player : Bukkit.getOnlinePlayers()) {
					Minigames.setCanTakeDamage(player, true);
					player.getInventory().addItem(SWORD);
					Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 0);
					Utils.giveInfiniteEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 255);
					IcyBlowback.this.all.add(player.getUniqueId());
				}
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (Utils.getAliveAcountFromDeadAndAllList(IcyBlowback.this.dead, IcyBlowback.this.all) < 2 && secondsLeft > 2) {
					return 5;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				IcyBlowback.this.endGame(Utils.getWinnersFromDeadAndAllList(IcyBlowback.this.dead, IcyBlowback.this.all, false));
				IcyBlowback.this.dead.clear();
				IcyBlowback.this.all.clear();
			}

		};
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
		this.sendMessage(player.getName() + " has died");
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
