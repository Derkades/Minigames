package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.games.dropper.DropperMap;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Dropper extends Game {

	Dropper() {
		super("Dropper", new String[] {"Get down without dying"}, 1, DropperMap.DROPPER_MAPS);
	}

	private static final int WAIT_TIME = 5;
	private static final int GAME_DURATION = 45;

	private static final String FINISHED = "%s finished.";
	private static final String FINISHED_FIRST = "%s finished first and got 1 extra point!";
	private static final String SECONDS_LEFT = "%s seconds left.";

	private DropperMap map;
	private List<UUID> winners;

	@Override
	void begin(final GameMap genericMap) {
		this.map = (DropperMap) genericMap;
		this.winners = new ArrayList<>();

		this.map.closeDoor();

		Utils.delayedTeleport(this.map.getLobbyLocation(), Bukkit.getOnlinePlayers());

		new BukkitRunnable() {

			int secondsLeft = WAIT_TIME;

			@Override
			public void run() {
				if (this.secondsLeft <= 0) {
					Dropper.this.sendMessage("The game has started!");
					for (final Player player : Bukkit.getOnlinePlayers()) {
						Minigames.setCanTakeDamage(player, true);
					}
					Dropper.this.map.openDoor();
					this.cancel();
					return;
				}

				Dropper.this.sendMessage("The game will start in " + this.secondsLeft + " seconds.");
				this.secondsLeft--;
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 20);

		new BukkitRunnable() {

			int secondsLeft = GAME_DURATION;

			@Override
			public void run() {
				if (Utils.allPlayersWon(Dropper.this.winners) && this.secondsLeft > 2) {
					this.secondsLeft = 2;
				}

				if (this.secondsLeft <= 0) {
					this.cancel();
					Dropper.this.endGame(Utils.getPlayerListFromUUIDList(Dropper.this.winners));
					return;
				}

				if (this.secondsLeft == 30 || this.secondsLeft <= 5) {
					Dropper.this.sendMessage(String.format(SECONDS_LEFT, this.secondsLeft));
				}

				this.secondsLeft--;
			}

		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (this.winners.contains(event.getPlayer().getUniqueId())) {
			return; //Don't teleport players who have finished
		}

		if (event.getTo().getBlock().getType() == Material.WATER) {
			final Player player = event.getPlayer();

			if (this.winners.isEmpty()) {
				//Player is first winner
				Points.addPoints(player, 1); //Add bonus point
				this.sendMessage(String.format(FINISHED_FIRST, player.getName()));
			} else {
				this.sendMessage(String.format(FINISHED, player.getName()));
			}

			this.winners.add(player.getUniqueId());
			Utils.giveInfiniteEffect(player, PotionEffectType.INVISIBILITY);
			Minigames.setCanTakeDamage(player, false);
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setAllowFlight(true);
			player.teleport(this.map.getLobbyLocation());
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setDeathMessage("");
		Scheduler.delay(1, () -> {
			event.getEntity().spigot().respawn();
			event.getEntity().teleport(this.map.getLobbyLocation());
		});
	}

	@EventHandler
	public void onDamageByEntity(final EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

}
