package derkades.minigames.games.oitq;

import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import derkades.minigames.utils.Utils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class OneInTheQuiver extends Game<OITQMap> {

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

	@Override
	public String getIdentifier() {
		return "oitq";
	}

	@Override
	public String getName() {
		return "One in the Quiver";
	}

	@Override
	public String getAlias() {
		return "oitq";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Kill other players with a weak wooden sword.",
				"For every kill you'll get a single arrow. Arrows",
				"do enough damage to kill any player instantly."
		};
	}

	@Override
	public Material getMaterial() {
		return Material.ARROW;
	}

	@Override
	public int getRequiredPlayers() {
		return 3;
	}

	@Override
	public OITQMap[] getGameMaps() {
		return OITQMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 120;
	}

	private Set<UUID> alive;

	private BukkitTask arrowRemoveTask;

	@Override
	public void onPreStart() {
		this.alive = Utils.getOnlinePlayersUuidSet();
		this.arrowRemoveTask = new ArrowRemoveTask().runTaskTimer(Minigames.getInstance(), 1, 1);

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getSpawnLocation());
			player.giveEffect(PotionEffectType.INVISIBILITY, 5, 0);
		}
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.enableSneakPrevention(p -> p.bukkit().damage(1000));
			player.setDisableDamage(false);
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
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();

		if (event.getDamagerEntity() == null) {
			if (event.willBeDead()) {
				event.setCancelled(true);
				this.alive.remove(player.getUniqueId());
				player.clearInventory();
				final int playersLeft = this.alive.size();
				if (playersLeft > 1) {
					sendFormattedPlainMessage("%s has died. There are %s players left.", player.getName(), this.alive.size());
				} else {
					sendFormattedPlainMessage("%s has died.", player.getName());
				}
				player.dieUp(2);
			}
			return;
		}

		if (event.getDamagerEntity().getType().equals(EntityType.ARROW)){
			event.setDamage(20);
		} else if (event.getType().equals(DamageType.ENTITY)) {
			if (event.getDamagerEntity().getType().equals(EntityType.PLAYER)) {
				event.setDamage(3);
			}
		}

		if (event.willBeDead()) {
			event.setCancelled(true);

			this.alive.remove(player.getUniqueId());
			player.clearInventory();

			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();

				killer.getInventory().addItem(ARROW);

				final int playersLeft = this.alive.size();
				if (playersLeft > 1) {
					sendFormattedPlainMessage("%s has been killed by %s. There are %s players left.",
							player.getName(), killer.getName(), playersLeft);
				} else {
					sendFormattedPlainMessage("%s has been killed by %s.",
							player.getName(), killer.getName());
				}
			} else {
				final int playersLeft = this.alive.size();
				if (playersLeft > 1) {
					sendFormattedPlainMessage("%s has died. There are %s players left.",
							player.getName(), playersLeft);
				} else {
					sendFormattedPlainMessage("%s has died.", player.getName());
				}
			}

			player.dieUp(2);
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
		// TODO proper join handling
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
