package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Cooldown;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Utils;

public class TntTag extends Game {

	TntTag() {
		super("Tnt Tag", new String[] {
				"Run away from players who have tnt on them.",
				"If you have tnt on you, pass it on to another",
				"player by punching them."
		}, 7, null);
	}

	private BukkitTask tntCountdownTask;
	private List<UUID> dead;
	private float time;

	@Override
	void begin(final GameMap genericMap) {
		this.dead = new ArrayList<>();
		this.time = 1.1f;

		this.sendMessage("You have 5 seconds to spread.");
		for (final Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(new Location(Var.WORLD, 347, 44, 365, 0, 90));
			Utils.giveInvisibility(player);
		}

		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
			for (final Player player : Bukkit.getOnlinePlayers()) {
				Minigames.setCanTakeDamage(player, true);
				Utils.clearPotionEffects(player);
			}

			final Player randomPlayer = this.getRandomPlayer();
			this.tag(randomPlayer);
			randomPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 2, true, false));
		}, 10*20);
	}

	@EventHandler
	public void onDamageEvent(final EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER) {
			return;
		}

		event.setCancelled(true);

		final Player target = (Player) event.getEntity();
		final Player attacker = (Player) event.getDamager();

		final long tagProtection = Cooldown.getCooldown("tnttag-anti-tag-back-protection" + target.getUniqueId());
		if (tagProtection != 0) {
			attacker.sendMessage(ChatColor.RED + "You can't tag this player yet.");
			event.setCancelled(true);
			return;
		}

		if (this.dead.contains(attacker.getUniqueId())) {
			event.setCancelled(true);
			return;
		}

		if (!attacker.getInventory().contains(Material.TNT)) {
			attacker.sendMessage(ChatColor.RED + "You don't have tnt on you, you don't need to hit people.");
			event.setCancelled(true);
			return;
		}

		attacker.getInventory().clear();
		attacker.getInventory().setHelmet(new ItemStack(Material.AIR));

		attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 2, true, false));

		Cooldown.addCooldown("tnttag-anti-tag-back-protection" + attacker.getUniqueId(), 5000);

		this.tag(target);
	}

	@EventHandler
	public void onDrop(final PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (this.dead.contains(event.getPlayer().getUniqueId())) {
			return;
		}

		if (event.getPlayer().isSneaking()) {
			event.getPlayer().sendMessage(ChatColor.RED + "You cannot hide from players by sneaking.");
			event.setCancelled(true);
		}
	}

	private void tag(final Player player) {
		if (this.tntCountdownTask != null) {
			this.tntCountdownTask.cancel();
		}

		this.tntCountdownTask = new TntCountdown(player).runTaskTimer(Minigames.getInstance(), 20, 20);

		this.sendMessage(String.format("%s has tnt on them, run!", player.getName()));
		player.getInventory().setHelmet(new ItemStack(Material.TNT));
		for (int i = 0; i < 9; i++) {
			player.getInventory().setItem(i, new ItemStack(Material.TNT));
		}
		player.sendMessage(ChatColor.GOLD + "You have been tagged! Punch someone to get rid of your tnt.");

		this.time -= 0.05f;
	}

	private Player getRandomPlayer(){
		return ListUtils.getRandomValueFromList(this.getAlivePlayers());
	}

	private List<Player> getAlivePlayers() {
		final Collection<? extends Player> list = Bukkit.getOnlinePlayers();
		final List<Player> alive = new ArrayList<>();
		list.forEach((player) -> {
			if (!this.dead.contains(player.getUniqueId())) alive.add(player);
		});
		return alive;
	}

	public class TntCountdown extends BukkitRunnable {

		private final Player player;
		private float progress;

		TntCountdown(final Player player){
			this.player = player;

			this.progress = TntTag.this.time;
		}

		@Override
		public void run() {
			this.progress -= 0.1f;
			this.player.setExp(this.progress);

			if (this.progress <= 0.0f) {
				this.cancel();

				TntTag.this.dead.add(this.player.getUniqueId());

				this.player.getInventory().clear();
				this.player.getInventory().setHelmet(new ItemStack(Material.AIR));
				Bukkit.getOnlinePlayers().forEach((online) -> online.hidePlayer(Minigames.getInstance(), this.player));
				this.player.setAllowFlight(true);

				for (final Player player : Bukkit.getOnlinePlayers()) {
					player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
				}

				final int alive = TntTag.this.getAlivePlayers().size();
				TntTag.this.sendMessage(this.player.getName() + " exploded into a million pieces! " + alive + " players left.");
				if (alive == 1) {
					TntTag.this.endGame(Utils.getWinnersFromDeadList(TntTag.this.dead));
					return;
				}

				final Player randomPlayer = TntTag.this.getRandomPlayer();
				TntTag.this.tag(randomPlayer);
				randomPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 2, true, false));
			}
		}

	}

}
