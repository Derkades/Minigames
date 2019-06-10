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
	private List<UUID> finished;
	private List<UUID> all;

	@Override
	void begin(final GameMap genericMap) {
		this.map = (DropperMap) genericMap;

		this.finished = new ArrayList<>();
		this.all = Utils.getOnlinePlayersUuidList();

		this.map.closeDoor();

		Utils.delayedTeleport(this.map.getLobbyLocation(), Bukkit.getOnlinePlayers());
		
		new GameTimer(this, GAME_DURATION, WAIT_TIME) {

			@Override
			public void onStart() {
				Bukkit.getOnlinePlayers().forEach(p -> Minigames.setCanTakeDamage(p, true));
				Dropper.this.map.openDoor();
			}

			@Override
			public int gameTimer(int secondsLeft) {
				if (Utils.getWinnersFromFinished(finished, all).size() >= all.size() && secondsLeft > 2) {
					secondsLeft = 2;
				}
				
				return secondsLeft;
			}

			@Override
			public void onEnd() {
				endGame(Utils.getWinnersFromFinished(finished, all));
			}
			
		};
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (this.finished.contains(event.getPlayer().getUniqueId())) {
			return; //Don't teleport players who have finished
		}

		if (event.getTo().getBlock().getType() == Material.WATER) {
			final Player player = event.getPlayer();

			if (this.finished.isEmpty()) {
				//Player is first winner
				Points.addPoints(player, 1); //Add bonus point
				this.sendMessage(String.format(FINISHED_FIRST, player.getName()));
			} else {
				this.sendMessage(String.format(FINISHED, player.getName()));
			}

			this.finished.add(player.getUniqueId());
			Utils.giveInfiniteEffect(player, PotionEffectType.INVISIBILITY);
			Minigames.setCanTakeDamage(player, false);
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setAllowFlight(true);
			player.teleport(this.map.getLobbyLocation());
			player.setFireTicks(0);
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
