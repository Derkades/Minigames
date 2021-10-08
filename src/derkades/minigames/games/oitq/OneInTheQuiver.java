package derkades.minigames.games.oitq;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class OneInTheQuiver extends Game<OITQMap> {

	private static final ItemStack SWORD = new ItemBuilder(Material.WOODEN_SWORD)
			.unbreakable()
			.create();

	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.unbreakable()
			.name(text("Insta-kill bow", GOLD))
			.lore(text("Get arrows by killing other players", GRAY))
			.create();

	private static final ItemStack ARROW = new ItemBuilder(Material.ARROW)
			.create();

	private static final PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 5*20, 0, true, false);

	public OneInTheQuiver() {
		super(
				"oitq",
				"One in the Quiver",
				new String[]{
						"Kill other players with a weak wooden sword.",
						"For every kill you'll get a single arrow. Arrows",
						"do enough damage to kill any player instantly."
				},
				Material.ARROW,
				new OITQMap[]{
						new Barn(),
						new Castle(),
						new Desert(),
						new HouseWithFarm(),
						new Snow(),
				},
				3,
				120,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT, GameLabel.NO_TEAMS)
		);
	}

	private Set<UUID> alive;

	private BukkitTask arrowRemoveTask;

	@Override
	public void onPreStart() {
		this.alive = Utils.getOnlinePlayersUuidSet();
		this.arrowRemoveTask = new ArrowRemoveTask().runTaskTimer(Minigames.getInstance(), 1, 1);

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getSpawnLocation());
			player.giveEffect(INVISIBILITY);
		}
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.enableSneakPrevention(p -> p.bukkit().damage(1000));
			player.giveItem(SWORD, BOW, ARROW);
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
		OneInTheQuiver.super.endGame(this.alive, true);
		this.alive = null;
		this.arrowRemoveTask.cancel();
		this.arrowRemoveTask = null;
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
		final Player player = event.getPlayer();

		final Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

		if (below == Material.HAY_BLOCK) {
			final PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
			player.addPotionEffect(jump);
		}

		if (player.getGameMode().equals(GameMode.SPECTATOR) &&
				(player.getLocation().getX() > 500 || player.getLocation().getX() < -500 ||
						player.getLocation().getZ() > 500 || player.getLocation().getZ() < -500)) {
			player.teleport(this.map.getSpawnLocation());
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		MPlayer player = new MPlayer(event);
		event.setCancelled(true);

		this.alive.remove(player.getUniqueId());
		player.dieUp(2);

		this.sendMessage(Utils.getDeathMessage(event, this.alive.size()));

		MPlayer killer = Utils.getKiller(event);
		if (killer != null) {
			killer.getInventory().addItem(ARROW);
		}
	}

	@EventHandler
	public void onDamage(final MPlayerDamageEvent event) {
		event.setCancelled(false);
		MPlayer damager = event.getDamagerPlayer();

		if (damager != null) {
			if (event.getDirectDamagerEntity() instanceof AbstractArrow) {
				event.setDamage(20);
			} else {
				event.setDamage(3);
			}
		}
	}

	private class ArrowRemoveTask extends BukkitRunnable {

		@Override
		public void run() {
			for (final Arrow arrow : OneInTheQuiver.this.map.getWorld().getEntitiesByClass(Arrow.class)) {
				if (arrow.isOnGround()) {
					arrow.remove();
				}
			}
		}

	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (GameState.currentGameIsRunning()) {
			player.queueTeleport(map.getSpawnLocation(), player::spectator);
		} else {
			player.giveEffect(INVISIBILITY);
			player.queueTeleport(this.map.getSpawnLocation());
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
