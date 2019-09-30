package xyz.derkades.minigames.games;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.murderymister.MurderyMisterMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Queue;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class MurderyMister extends Game<MurderyMisterMap> {

	@Override
	public String getName() {
		return "Murdery Mister";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Description",
		}; // TODO Description
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public MurderyMisterMap[] getGameMaps() {
		return MurderyMisterMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 200;
	}

	private UUID murderer;
	private List<UUID> alive; // All alive players, except murderer
	private BukkitTask arrowRemoveTask;

	private boolean murdererDead;

	@Override
	public void onPreStart() {
		this.murderer = null;
		this.alive = Utils.getOnlinePlayersUuidList();
		this.murdererDead = false;

		this.arrowRemoveTask = new ArrowRemoveTask().runTaskTimer(Minigames.getInstance(), 1, 1);

		this.map.getWorld().getEntitiesByClass(Trident.class).forEach(Entity::remove);

		final Location[] spawnLocations = this.map.getSpawnLocations();
		int index = 0;

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}

			final Location location = spawnLocations[index];

			Queue.add(() -> {
				player.teleport(location);
				player.placeCage(true);
			});
			index--;
		}
	}

	@Override
	public void onStart() {
		if (Bukkit.getOnlinePlayers().size() < 1)
			return; // Just in case everyone leaves, so the code below doesn't crash

		final MPlayer murderer = Minigames.getOnlinePlayersInRandomOrder().get(0);
		murderer.sendTitle("", ChatColor.RED + "You are the murderer!");
		murderer.giveItem(new ItemBuilder(Material.TRIDENT)
				.name(ChatColor.GOLD + "Knife")
				.lore(ChatColor.YELLOW + "Stab people to kill them, ",
						ChatColor.YELLOW + "right click to throw")
				.enchant(Enchantment.LOYALTY, 1)
				.create());
		murderer.getInventory().setHeldItemSlot(6);
		this.murderer = murderer.getUniqueId();

		this.alive.remove(this.murderer);

		final List<MPlayer> all = Minigames.getOnlinePlayersInRandomOrder();
		all.removeIf((p) -> p.getUniqueId().equals(this.murderer));

		final MPlayer sheriff = all.remove(0);
		sheriff.giveItem(new ItemBuilder(Material.BOW).unbreakable().create());
		sheriff.giveItem(new ItemStack(Material.ARROW));

		this.map.getWorld().setTime(21000);

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.placeCage(false);
			player.setDisableSneaking(true);
		}
	}

	@Override
	public int gameTimer(int secondsLeft) {
		if (Utils.getWinnersFromAliveList(this.alive, true).size() < 1 && secondsLeft > 5) {
			secondsLeft = 5;
		}

		if (this.murdererDead && secondsLeft > 5) {
			secondsLeft = 5;
		}

		for (final Location location : this.map.getCandles()) {
			location.setX(location.getX() + .5);
			location.setY(location.getY() + 1.15);
			location.setZ(location.getZ() + .5);
			location.getWorld().spawnParticle(Particle.FLAME, location, 0, 0, 0, 0.001, 2);
		}

		if (secondsLeft % 2 == 0) {
			final Lightable powerable = (Lightable) ListUtils.getRandomValueFromArray(this.map.getFlickeringRedstomeLamps()).getBlock().getBlockData();
			powerable.setLit(!powerable.isLit());
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		this.arrowRemoveTask.cancel();
		super.endGame(Utils.getWinnersFromAliveList(this.alive, true));
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
		} else {
			if (event.getDamagerEntity().getType().equals(EntityType.PLAYER)) {
				final Player attacker = (Player) event.getDamagerEntity();
				if (attacker.getInventory().getItemInMainHand().getType().equals(Material.TRIDENT)) {
					event.setDamage(40);
				} else {
					event.setDamage(0);
					event.setCancelled(true);
				}
			} else {
				event.setDamage(40);
			}
		}

		if (event.willBeDead()) {
			event.setCancelled(true);

			final MPlayer player = event.getPlayer();

			this.sendMessage(player.getName() + " has been killed");
			Minigames.getOnlinePlayers().forEach((p) -> p.playSound(Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f));
			this.alive.remove(player.getUniqueId());

			if (player.getUniqueId().equals(this.murderer)) {
				// Murder is dead, all alive players win (except murderer)
				this.arrowRemoveTask.cancel();
				this.murdererDead = true;
				player.die();
				this.sendMessage("The murderer has been killed by " + event.getDamagerPlayer().getName() + "!");
			} else if (player.getInventory().contains(Material.BOW)) {
				// Sheriff is dead, give bow to random player
				if (this.alive.size() > 0) {
					final Player target = Bukkit.getPlayer(ListUtils.getRandomValueFromList(this.alive));
					if (target != null) {
						target.getInventory().addItem(new ItemBuilder(Material.BOW).unbreakable().create());
						target.getInventory().addItem(new ItemBuilder(Material.ARROW).create());
					}
				}
				player.die();
				System.out.println("Sheriff died");
			} else {
				// Innocent is dead
				player.die();
				System.out.println("Innocent died");
				if (event.getType().equals(DamageType.ENTITY) && !event.getDamagerPlayer().getUniqueId().equals(this.murderer)) {
					final MPlayer killer = event.getDamagerPlayer();
					killer.bukkit().damage(40);
					killer.sendActionBar(ChatColor.RED + "You killed an innocent player!");

				}
			}
			player.clearInventory();
		}
	}

	@EventHandler
	public void chat(final AsyncPlayerChatEvent event) {
		if (this.started) {
			new MPlayer(event).sendTitle("", ChatColor.RED + "Chat is disabled");
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

}
