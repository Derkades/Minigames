package derkades.minigames.games.icyblowback;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.queue.TaskQueue;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public class IcyBlowback extends Game<IcyBlowbackMap> {

	private static final ItemStack SWORD = new ItemBuilder(Material.WOODEN_SWORD)
			.name(text("Knockback sword", GOLD))
			.enchant(Enchantment.KNOCKBACK, 2)
			.create();

	private static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false);

	public IcyBlowback() {
		super(
				"icy_blowback",
				"Icy Blowback",
				new String[] {
						"Try to knock others off the slippery ice."
				},
				Material.ICE,
				new IcyBlowbackMap[] {
						new IcyBlowbackMapImpl(),
				},
				3,
				100,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT, GameLabel.NO_TEAMS)
		);
	}

	private Set<UUID> alive;

	@Override
	public void onPreStart() {
		this.alive = new HashSet<>();

		final Location[] spawnLocations = this.map.getSpawnLocations();
		int index = 0;
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (index >= spawnLocations.length) {
				index = 0;
			}

			final Location loc = spawnLocations[index];

			TaskQueue.add(() -> {
				player.teleport(loc);
				player.placeCage(true);
			});

			index++;
		}
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.giveItem(SWORD);
			player.giveEffect(SPEED);
			player.placeCage(false);
			IcyBlowback.this.alive.add(player.getUniqueId());
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.alive.size() < 2;
	}

	@Override
	public void onEnd() {
		IcyBlowback.this.endGame(this.alive, false);
		this.alive = null;
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final MPlayer player = new MPlayer(event);

		if (!this.alive.contains(player.getUniqueId())) {
			return;
		}

		if (player.getY() < this.map.getBottomFloorLevel()) {
			// die
			this.alive.remove(player.getUniqueId());
			this.map.getWorld().spigot().strikeLightningEffect(player.getLocation(), false);
			player.dieUp(10);
			this.sendMessage(player.getDisplayName().append(text(" has died.", NamedTextColor.GRAY)));
		}
	}

	@EventHandler
	public void onDamage(final MPlayerDamageEvent event) {
		if (event.getDamagerPlayer() == null) {
			event.setCancelled(false);
			event.setDamage(0);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.setGameMode(GameMode.SPECTATOR);
		final Location loc = this.map.getSpawnLocations()[0];
		loc.setY(loc.getY() + 10);
		player.teleport(loc);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
