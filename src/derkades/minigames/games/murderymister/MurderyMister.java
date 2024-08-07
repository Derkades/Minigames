package derkades.minigames.games.murderymister;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.Lightable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import derkades.minigames.GameState;
import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Var;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.queue.TaskQueue;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class MurderyMister extends Game<MurderyMisterMap> {

	@Override
	public String getIdentifier() {
		return "murdery_mister";
	}

	@Override
	public String getName() {
		return "Murdery Mister";
	}

	@Override
	public String getAlias() {
		return "mm";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Description",
		}; // TODO Description
	}

	@Override
	public Material getMaterial() {
		return Material.TRIDENT;
	}

	@Override
	public int getRequiredPlayers() {
		return 3;
	}

	@Override
	public MurderyMisterMap[] getGameMaps() {
		return MurderyMisterMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 150;
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
		murderer.sendTitle(Component.empty(), Component.text("You are the murderer!", NamedTextColor.RED));
		murderer.giveItem(new ItemBuilder(Material.TRIDENT)
				.name(ChatColor.GOLD + "Knife")
				.lore(ChatColor.YELLOW + "Stab people to kill them, ",
						ChatColor.YELLOW + "right click to throw")
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
	public void onDamage(final EntityShootBowEvent event) {
		if (event.getEntity().getType().equals(EntityType.PLAYER)) {
			Scheduler.delay(5*20, () -> {
				((Player) event.getEntity()).getInventory().addItem(new ItemStack(Material.ARROW));
			});
		}
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		if (event.getType().equals(DamageType.SELF)) {
			event.setDamage(0);
			event.setCancelled(true);
			return;
		} else if (event.getDamagerEntity().getType() == EntityType.ARROW ||
				event.getDamagerEntity().getType() == EntityType.TRIDENT) {
			event.setDamage(40);
		} else {
			event.setDamage(0);
			event.setCancelled(true);
			return;
		}

		if (event.willBeDead()) {
			event.setCancelled(true);

			final MPlayer player = event.getPlayer();

			sendFormattedPlainMessage("%s has been killed", player.getName());
			Minigames.getOnlinePlayers().forEach((p) -> p.playSound(Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f));
			this.aliveInnocent.remove(player.getUniqueId());

			if (player.getUniqueId().equals(this.murderer)) {
				// Murder is dead, all alive players win (except murderer)
				this.arrowRemoveTask.cancel();
				this.murdererDead = true;
				player.die();
				sendFormattedPlainMessage("The murderer has been killed by %s!", event.getDamagerPlayer().getName());
			} else if (player.getInventory().contains(Material.BOW)) {
				// Sheriff is dead, give bow to random player
				if (this.aliveInnocent.size() > 0) {
					final Player target = Bukkit.getPlayer(ListUtils.choice(this.aliveInnocent));
					if (target != null) {
						target.getInventory().addItem(new ItemBuilder(Material.BOW).unbreakable().create());
						target.getInventory().addItem(new ItemBuilder(Material.ARROW).create());
					}
				}
				player.die();
				Logger.debug("Sheriff died");
			} else {
				// Innocent is dead
				player.die();
				Logger.debug("Innocent died");
				final MPlayer killer = event.getDamagerPlayer();
				killer.bukkit().damage(40);
				if (killer != null) {
					killer.sendActionBar(Component.text("You killed an innocent player!"));
					killer.die();
					this.aliveInnocent.remove(killer.getUniqueId());
				}
			}
			player.clearInventory();
		}
	}

	@EventHandler
	public void chat(final AsyncChatEvent event) {
		if (GameState.getCurrentState().gameIsRunning() && event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
			new MPlayer(event).sendTitle(Component.empty(), Component.text("Chat is disabled for spectators", NamedTextColor.RED));
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
		player.teleport(Var.LOBBY_LOCATION); // TODO proper join handling
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.aliveInnocent.remove(player.getUniqueId());
	}

}
