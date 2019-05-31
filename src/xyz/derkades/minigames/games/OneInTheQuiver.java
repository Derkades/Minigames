package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
	void begin(final GameMap genericMap) {
		this.dead = new ArrayList<>();
		this.map = (SniperMap) genericMap;

		for (final Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(this.map.getSpawnLocation());
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5*20, 0, true, false));
		}

		new BukkitRunnable() {

			int timeLeft = MAX_GAME_DURATION + SPREAD_TIME;

			@Override
			public void run() {
				if (this.timeLeft > MAX_GAME_DURATION) {
					OneInTheQuiver.this.sendMessage("The game will start in " + (this.timeLeft - MAX_GAME_DURATION) + " seconds");
				}

				if (this.timeLeft == MAX_GAME_DURATION) {
					OneInTheQuiver.this.start();
				}

				if (this.timeLeft == 60 || this.timeLeft == 30 || this.timeLeft == 10 || this.timeLeft < 5) {
					OneInTheQuiver.this.sendMessage("The game will end in " + this.timeLeft + " seconds");
				}

				if (this.timeLeft <= 0) {
					OneInTheQuiver.this.end();
					this.cancel();
				}

				if (Utils.getAliveCountFromDeadList(OneInTheQuiver.this.dead) < 2) {
					OneInTheQuiver.this.end();
					this.cancel();
				}

				this.timeLeft--;
			}

		}.runTaskTimer(Minigames.getInstance(), 20, 20);
	}

	private void start() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			Minigames.setCanTakeDamage(player, true);
			final PlayerInventory inv = player.getInventory();
			inv.addItem(SWORD, BOW, ARROW);
		}
	}

	private void end() {
		final List<Player> winners = Utils.getWinnersFromDeadList(this.dead);
		if (winners.size() > 1) {
			winners.clear();
		}
		super.endGame(winners);
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		final Player killer = player.getKiller();
		killer.getInventory().addItem(ARROW);
		this.dead.add(player.getUniqueId());

		Utils.hideForEveryoneElse(player);

		Scheduler.delay(1, () -> {
			player.spigot().respawn();
			if (this.map.getSpectatorLocation() != null)
				player.teleport(this.map.getSpectatorLocation());
			player.setAllowFlight(true);
			Utils.giveInvisibility(player);
			player.getInventory().clear();
			Minigames.setCanTakeDamage(player, false);
		});
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
			final Player player = event.getPlayer();

			final Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

			if (below == Material.HAY_BLOCK) {
				final PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
				player.addPotionEffect(jump);
			}
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		final Entity damagedEntity = event.getEntity();
		final Entity attackingEntity = event.getDamager();

		if (damagedEntity.getType() != EntityType.PLAYER) {
			return;
		}

		if (this.dead.contains(attackingEntity.getUniqueId()) || this.dead.contains(damagedEntity.getUniqueId())) {
			event.setCancelled(true);
			return;
		}

		if (attackingEntity.getType() == EntityType.ARROW) {
			event.setDamage(20);
			return;
		}

		if (attackingEntity.getType() != EntityType.PLAYER) {
			return;
		}

		final ItemStack weapon = ((Player) attackingEntity).getInventory().getItemInMainHand();

		if (weapon.isSimilar(SWORD)) {
			event.setDamage(2);
		}
	}

}
