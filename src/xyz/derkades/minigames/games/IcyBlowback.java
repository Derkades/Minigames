package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.icyblowback.IcyBlowbackMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesJoinEvent;
import xyz.derkades.minigames.utils.Queue;
import xyz.derkades.minigames.utils.Utils;

public class IcyBlowback extends Game<IcyBlowbackMap> {

	private static final ItemStack SWORD = new ItemBuilder(Material.WOODEN_SWORD)
			.name(ChatColor.AQUA + "Knockback sword")
			.enchant(Enchantment.KNOCKBACK, 2)
			.create();

	private List<UUID> dead;
	private List<UUID> all;

	@Override
	public String getName() {
		return "Icy Blowback";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Try to knock others off the slippery ice."
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 3;
	}

	@Override
	public IcyBlowbackMap[] getGameMaps() {
		return IcyBlowbackMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	@Override
	public void onPreStart() {
		this.dead = new ArrayList<>();
		this.all = new ArrayList<>();

		final Location[] spawnLocations = this.map.getSpawnLocations();
		int index = 0;
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}

			final Location loc = spawnLocations[index];

			Queue.add(() -> {
				player.queueTeleport(loc);
				player.placeCage(true);
			});

			index--;
		}
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.setDisableDamage(false);
			player.giveItem(SWORD);
			player.giveInfiniteEffect(PotionEffectType.SPEED);
			player.giveInfiniteEffect(PotionEffectType.DAMAGE_RESISTANCE, 255);
			player.placeCage(false);
			IcyBlowback.this.all.add(player.getUniqueId());
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Utils.getAliveAcountFromDeadAndAllList(IcyBlowback.this.dead, IcyBlowback.this.all) < 2 && secondsLeft > 5) {
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

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final MPlayer player = new MPlayer(event);

		if (this.dead.contains(player.getUniqueId())) {
			return;
		}

		if (player.getY() < this.map.getBottomFloorLevel()) {
			//die
			this.dead.add(player.getUniqueId());
			this.map.getWorld().spigot().strikeLightningEffect(player.getLocation(), false);
			player.dieUp(10);
			this.sendMessage(player.getName() + " has died");
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		if (this.dead.contains(event.getDamager().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onJoin(final MinigamesJoinEvent event) {
		final MPlayer player = event.getPlayer();
		event.setTeleportPlayerToLobby(false);
		player.setGameMode(GameMode.SPECTATOR);
		final Location loc = this.map.getSpawnLocations()[0];
		loc.setY(loc.getY() + 10);
		player.teleport(loc);
	}

}
