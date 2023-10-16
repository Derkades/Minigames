package derkades.minigames.games.murderymister;

import derkades.minigames.GameState;
import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Var;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.queue.TaskQueue;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.Lightable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public class MurderyMister extends Game<MurderyMisterMap> {

	public MurderyMister() {
		super(
				"murdery_mister",
				"Murdery Mister",
				new String[] {
						"At game start, you are assigned one of three roles:",
						"  Murderer (trident): sneakily kill others",
						"  Detective (bow): kill murderer, but not players"
						"  Player: avoid being killed"
				},
				Material.TRIDENT,
				new MurderyMisterMap[] {
						new DeckedOutCastle(),
						new HauntedHouse(),
				},
				3,
				150,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT, GameLabel.NO_TEAMS)
		);
	}

	private UUID murderer;
	private Set<UUID> aliveInnocent; // All alive players, except murderer
	private BukkitTask arrowRemoveTask;

	private boolean murdererDead;

	@Override
	public void onPreStart() {
		this.murderer = null;
		this.aliveInnocent = Utils.getOnlinePlayersUuidSet();
		this.murdererDead = false;

		this.arrowRemoveTask = new ArrowRemoveTask().runTaskTimer(Minigames.getInstance(), 1, 1);

		final Location[] spawnLocations = this.map.getSpawnLocations();
		int index = 0;

		for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}

			final Location location = spawnLocations[index];
			TaskQueue.add(() -> {
				player.teleport(location);
				player.placeCage(true);
			});

			index--;
		}
	}

	@Override
	public void onStart() {
		if (Bukkit.getOnlinePlayers().size() < 1) {
			return; // Just in case everyone leaves, so the code below doesn't crash
		}

		final MPlayer murderer = Minigames.getOnlinePlayersInRandomOrder().get(0);
		murderer.sendTitle(Component.empty(), text("You are the murderer!", NamedTextColor.RED));
		murderer.giveItem(new ItemBuilder(Material.TRIDENT)
				.name(text("Trident", GOLD))
				.enchant(Enchantment.LOYALTY, 1)
				.create());
		murderer.getInventory().setHeldItemSlot(1);
		this.murderer = murderer.getUniqueId();

		this.aliveInnocent.remove(this.murderer);

		final List<MPlayer> all = Minigames.getOnlinePlayersInRandomOrder();
		all.removeIf((p) -> p.getUniqueId().equals(this.murderer));

		final MPlayer sheriff = all.remove(0);
		sheriff.giveItem(new ItemBuilder(Material.BOW).unbreakable().create());
		sheriff.giveItem(new ItemStack(Material.ARROW));
		sheriff.getInventory().setHeldItemSlot(2);

		this.map.getWorld().setTime(21000);

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.placeCage(false);
			player.enableSneakPrevention(p -> p.bukkit().damage(1000));
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (this.map.getCandles() != null) {
			for (final Location location : this.map.getCandles()) {
				location.setX(location.getX() + .5);
				location.setY(location.getY() + 1.15);
				location.setZ(location.getZ() + .5);
				location.getWorld().spawnParticle(Particle.FLAME, location, 0, 0, 0, 0.001, 2);
			}
		}

		if (this.map.getFlickeringRedstoneLamps() != null) {
			if (secondsLeft % 2 == 0) {
				final Lightable powerable = (Lightable) ListUtils.choice(this.map.getFlickeringRedstoneLamps()).getBlock().getBlockData();
				powerable.setLit(!powerable.isLit());
			}
		}

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.aliveInnocent.size() < 1 || this.murdererDead;
	}

	@Override
	public void onEnd() {
		if (this.murdererDead) {
			super.endGame(this.aliveInnocent);
		} else {
			super.endGame(this.murderer);
		}
		this.murderer = null;
		this.aliveInnocent = null;
		this.arrowRemoveTask.cancel();
		this.arrowRemoveTask = null;
	}

	@EventHandler
	public void onShootBow(final EntityShootBowEvent event) {
		if (event.getEntity().getType().equals(EntityType.PLAYER)) {
			Scheduler.delay(5*20, () -> ((Player) event.getEntity()).getInventory().addItem(new ItemStack(Material.ARROW)));
		}
	}

	@EventHandler
	public void onDamage(final MPlayerDamageEvent event) {
		final MPlayer damager = event.getDamagerPlayer();
		if (damager == null) {
			EntityDamageEvent cause = event.getBukkitEvent();
			// don't cancel plugin damage (void) or sneak cancel won't work
			if (cause.getCause() == EntityDamageEvent.DamageCause.VOID) {
				event.setCancelled(false);
			}
			return;
		}

		final Entity entity = event.getDirectDamagerEntity();
		if (entity == null) {
			throw new IllegalStateException("entity cannot be null if damager player is null");
		}
		switch(entity.getType()) {
			case ARROW, TRIDENT -> {
				event.setDamage(40);
				event.setCancelled(false);
			}
			default -> {}
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setCancelled(true);
		final MPlayer player = new MPlayer(event);

		Minigames.getOnlinePlayers().forEach((p) -> p.playSound(Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f));

		if (player.getUniqueId().equals(this.murderer)) {
			Logger.debug("Murderer was killed");
			// Murder is dead, all alive players win (except murderer)
			this.arrowRemoveTask.cancel();
			this.murdererDead = true;
			player.die();
			final MPlayer killer = Utils.getKiller(event);
			if (killer == null) {
				sendPlainMessage("The murderer has died!");
			} else {
				this.sendMessage(
						text("The murderer (", NamedTextColor.GRAY)
								.append(player.getDisplayName())
								.append(text(") has been killed by ", NamedTextColor.GRAY))
								.append(killer.getDisplayName())
								.append(text("!", NamedTextColor.GRAY))
				);
			}
			return;
		}

		// Innocent player (or detective)
		this.aliveInnocent.remove(player.getUniqueId());
		this.sendMessage(player.getDisplayName().append(text(" has been murdered!", NamedTextColor.GRAY)));
		player.die();

		if (player.getInventory().contains(Material.BOW)) {
			Logger.debug("Sheriff/detective died");
			// Sheriff is dead, give bow to random player
			if (this.aliveInnocent.size() > 0) {
				@SuppressWarnings("null") final Player target = Bukkit.getPlayer(ListUtils.choice(this.aliveInnocent));
				if (target != null) {
					target.getInventory().addItem(new ItemBuilder(Material.BOW).unbreakable().create());
					target.getInventory().addItem(new ItemBuilder(Material.ARROW).create());
				}
			}
		} else {
			// Innocent is dead
			Logger.debug("Innocent died");

			final MPlayer killer = Utils.getKiller(event);

			// If the innocent player was killed by a detective
			if (killer != null &&
					killer.getInventory().contains(Material.BOW)) {
				Logger.debug("killed by detective!");
				killer.bukkit().damage(40);
				killer.sendActionBar(text("You killed an innocent player!"));
			}
		}
		player.clearInventory();
	}

	@EventHandler
	public void chat(final AsyncChatEvent event) {
		if (GameState.getCurrentState().gameIsRunning() && event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
			new MPlayer(event).sendTitle(Component.empty(), text("Chat is disabled for spectators", NamedTextColor.RED));
			event.setCancelled(true);
		}
	}

	private class ArrowRemoveTask extends BukkitRunnable {

		@Override
		public void run() {
			for (final Arrow arrow : MurderyMister.this.map.getWorld().getEntitiesByClass(Arrow.class)) {
				if (arrow.isOnGround()) {
					arrow.remove();
				}
			}
		}

	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(Var.JAIL_LOCATION); // TODO proper join handling
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.aliveInnocent.remove(player.getUniqueId());
	}

}
