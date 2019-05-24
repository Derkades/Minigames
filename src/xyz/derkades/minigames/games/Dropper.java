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
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Dropper extends Game {
	
	Dropper() {
		super("Dropper", new String[] {"Get down without dying"}, 1, 3, 4, DropperMap.DROPPER_MAPS);
	}

	private static final int WAIT_TIME = 5;
	private static final int GAME_DURATION = 45;
	
	private static final String FINISHED = "%s finished.";
	private static final String FINISHED_FIRST = "%s finished first and got 1 extra point!";
	private static final String SECONDS_LEFT = "%s seconds left.";

	private DropperMap map;
	private List<UUID> winners;
	
	@Override
	void begin(GameMap genericMap) {
		map = (DropperMap) genericMap;
		winners = new ArrayList<>();
		
		map.closeDoor();
		
		Utils.delayedTeleport(map.getLobbyLocation(), Bukkit.getOnlinePlayers());
		
		new BukkitRunnable() {
			
			int secondsLeft = WAIT_TIME;
			
			@Override
			public void run() {
				if (secondsLeft <= 0) {
					sendMessage("The game has started!");
					for (Player player : Bukkit.getOnlinePlayers()) {
						Minigames.setCanTakeDamage(player, true);
					}
					map.openDoor();
					this.cancel();
					return;
				}
				
				sendMessage("The game will start in " + secondsLeft + " seconds.");
				secondsLeft--;
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			
			int secondsLeft = GAME_DURATION;
			
			@Override
			public void run() {
				if (Utils.allPlayersWon(winners) && secondsLeft > 2) {
					secondsLeft = 2;
				}
				
				if (secondsLeft <= 0) {
					this.cancel();
					endGame();
					return;
				}
				
				if (secondsLeft == 30 || secondsLeft <= 5) {
					sendMessage(String.format(SECONDS_LEFT, secondsLeft));
				}
				
				secondsLeft--;
			}
			
		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}
	
	private void endGame() {
		super.startNextGame(Utils.getPlayerListFromUUIDList(winners));
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (winners.contains(event.getPlayer().getUniqueId())) {
			return; //Don't teleport players who have finished
		}
		
		if (event.getTo().getBlock().getType() == Material.WATER) {
			Player player = event.getPlayer();
			
			if (winners.isEmpty()) {
				//Player is first winner
				Points.addPoints(player, 1); //Add bonus point
				sendMessage(String.format(FINISHED_FIRST, player.getName()));
			} else {
				sendMessage(String.format(FINISHED, player.getName()));
			}
			
			winners.add(player.getUniqueId());
			Utils.giveInfiniteEffect(player, PotionEffectType.INVISIBILITY);
			Minigames.setCanTakeDamage(player, false);
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setAllowFlight(true);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Scheduler.delay(1, () -> {
			event.getEntity().spigot().respawn();
			event.getEntity().teleport(map.getLobbyLocation());
			
		});
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

}
