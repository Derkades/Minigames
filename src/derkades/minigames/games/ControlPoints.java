package derkades.minigames.games;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import derkades.minigames.Minigames;
import derkades.minigames.games.pointcontrol.ControlPointsMap;
import derkades.minigames.games.pointcontrol.ControlStatus;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class ControlPoints extends Game<ControlPointsMap> {

	private static final int CONTROL_THRESHOLD = 5;
	private static final int RESPAWN_DELAY = 5*20;
	
	@Override
	public String getIdentifier() {
		return "control_points";
	}
	
	@Override
	public String getName() {
		return "Control Points";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Points. Take control."
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 6;
	}

	@Override
	public ControlPointsMap[] getGameMaps() {
		return ControlPointsMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 200;
	}
	
	private Set<UUID> teamRed;
	private Set<UUID> teamBlue;
	private Set<UUID> winners;
	private Map<Integer, Integer> status; // more negative = blue, more positive = red
	private BossBar barRed;
	private BossBar barBlue;
	
	@Override
	public void onPreStart() {
		this.teamRed = new HashSet<>();
		this.teamBlue = new HashSet<>();
		this.status = new HashMap<>();
		
		this.barRed = Bukkit.createBossBar("Red", BarColor.RED, BarStyle.SOLID);
		this.barBlue = Bukkit.createBossBar("Blue", BarColor.BLUE, BarStyle.SOLID);
		
		for (int i = 0; i < this.map.getControlPointLocations().length; i++) {
			this.status.put(i, 0);
		}
		
		boolean team = false;
		for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			if (team) {
				player.sendTitle("", String.format("%sYou are in the %s%sRED%s team", ChatColor.GRAY, ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY));
				this.teamRed.add(player.getUniqueId());
				player.queueTeleport(this.map.getRedSpawnLocation());
			} else {
				player.sendTitle("", String.format("%sYou are in the %s%sBLUE%s team", ChatColor.GRAY, ChatColor.BLUE, ChatColor.BOLD, ChatColor.GRAY));
				this.teamBlue.add(player.getUniqueId());
				player.queueTeleport(this.map.getBlueSpawnLocation());
			}

			team = !team;
		}
		
		for (final Location point : this.map.getControlPointLocations()) {
			this.map.setControlPointStatus(point, ControlStatus.NEUTRAL);
		}
	}
	
	public void giveGear(final MPlayer player) {
		player.giveItem(
				new ItemBuilder(Material.STONE_SWORD).name("a weapon").unbreakable().create(),
				new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_INFINITE, 1).unbreakable().create(),
				new ItemBuilder(Material.ARROW).create()
				);
		player.giveInfiniteEffect(PotionEffectType.SPEED);
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach(p -> {
			this.barBlue.addPlayer(p.bukkit());
			this.barRed.addPlayer(p.bukkit());
			p.setDisableDamage(false);
			giveGear(p);
		});
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		int bluePoints = 0;
		int redPoints = 0;
		for (int i = 0; i < this.map.getControlPointLocations().length; i++) {
			final Location controlPoint = this.map.getControlPointLocations()[i];
			int red = 0;
			int blue = 0;
			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				if (player.isSpectator()) {
					continue;
				}
				
				if (this.map.isOnControlPoint(controlPoint, player)) {
					if (this.teamRed.contains(player.getUniqueId())) {
						player.sendActionBar("Claiming control point! " + this.status.get(i));
						red++;
					} else if (this.teamBlue.contains(player.getUniqueId())) {
						player.sendActionBar("Claiming control point! " + -this.status.get(i));
						blue++;
					}
				}
			}
			
			final int current = this.status.get(i);
			
			if (blue > red && current > -CONTROL_THRESHOLD) {
				this.status.put(i, current - 1);
			} else if (red > blue && current < CONTROL_THRESHOLD) {
				this.status.put(i, current + 1);
			}
			
			ControlStatus controlStatus;
			switch(this.status.get(i)) {
			case CONTROL_THRESHOLD:
				controlStatus = ControlStatus.RED;
				redPoints++;
				break;
			case -CONTROL_THRESHOLD:
				controlStatus = ControlStatus.BLUE;
				bluePoints++;
				break;
			default:
				controlStatus = ControlStatus.NEUTRAL;
			}
			
			this.map.setControlPointStatus(controlPoint, controlStatus);
		}
		
		final double max = this.map.getControlPointLocations().length;
		this.barRed.setProgress(redPoints / max);
		this.barBlue.setProgress(bluePoints / max);
		
		if (secondsLeft > 6) {
			if (redPoints == this.map.getControlPointLocations().length) {
				end("red");
				return 6;
			} else if (bluePoints == this.map.getControlPointLocations().length) {
				end("blue");
				return 6;
			}
		}
		
		return secondsLeft;
	}
	
	public void end(final String winningTeam) {
		if (winningTeam.equals("red")) {
			this.winners = this.teamRed;
		} else {
			this.winners = this.teamBlue;
		}
		
		sendMessage("The game has ended, team " + winningTeam + " is in control of all control points!");
		Minigames.getOnlinePlayers().forEach(p -> {
			p.spectator();
		});
	}

	@Override
	public void onEnd() {
		if (this.winners != null) {
			endGame(this.winners, false);
		} else {
			// Team with most control points wins
			int blue = 0;
			int red = 0;
			for (int i = 0; i < this.map.getControlPointLocations().length; i++) {
				if (this.status.get(i) == CONTROL_THRESHOLD) {
					red++;
				} else if (this.status.get(i) == -CONTROL_THRESHOLD) {
					blue++;
				}
			}
			
			if (blue > red) {
				endGame(this.teamBlue);
			} else if (red > blue) {
				endGame(this.teamRed);
			} else {
				endGame();
			}
		}
		
		this.teamRed = null;
		this.teamBlue = null;
		this.winners = null;
		this.status = null;
		this.barBlue.removeAll();
		this.barBlue = null;
		this.barRed.removeAll();
		this.barRed = null;
	}
	
	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();
		
		if (event.getType() == DamageType.ENTITY) {
			final MPlayer damager = event.getDamagerPlayer();
			// Disable damage to team mates
			if (this.teamRed.contains(player.getUniqueId()) && this.teamRed.contains(damager.getUniqueId()) ||
					this.teamBlue.contains(player.getUniqueId()) && this.teamBlue.contains(damager.getUniqueId())) {
				event.setCancelled(true);
				return;
			}
		}
		
		if (event.willBeDead()) {
			event.setCancelled(true);
			
			if (event.getType() == DamageType.ENTITY) {
				sendMessage(player.getName() + " was killed by " + event.getDamagerPlayer().getName());
			} else {
				sendMessage(player.getName() + " has died");
			}
			
			player.die();
			final UUID uuid = player.getUniqueId();
			
			Scheduler.delay(RESPAWN_DELAY, () -> {
				final MPlayer player2 = Minigames.getPlayer(uuid);
				if (player2 == null) {
					return;
				}
				
				player.setGameMode(GameMode.ADVENTURE);
				player2.heal();
				
				if (this.teamBlue.contains(player2.getUniqueId())) {
					player2.teleport(this.map.getBlueSpawnLocation());
				} else if (this.teamRed.contains(player2.getUniqueId())) {
					player2.teleport(this.map.getRedSpawnLocation());
				}
			});
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (this.teamRed.contains(player.getUniqueId())) {
			player.setDisableDamage(false);
			player.teleport(this.map.getRedSpawnLocation());
			giveGear(player);
		} else if (this.teamBlue.contains(player.getUniqueId())) {
			player.setDisableDamage(false);
			player.teleport(this.map.getBlueSpawnLocation());
			giveGear(player);
		} else {
//			player.teleport(this.map.getWorld().getSpawnLocation()); TODO Use this when control points map has been moved to a dedicated world
			player.teleport(this.map.getBlueSpawnLocation());
			player.spectator();
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		
	}

}