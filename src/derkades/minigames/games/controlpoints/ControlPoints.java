package derkades.minigames.games.controlpoints;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.GameTeam;
import derkades.minigames.games.RedBlueTeamGame;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControlPoints extends RedBlueTeamGame<ControlPointsMap> {

	private static final int CONTROL_THRESHOLD = 5;
	private static final int RESPAWN_DELAY = 5*20;
	private static final PotionEffect INFINITE_SPEED = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true);

	public ControlPoints() {
		super(
				"control_points",
				"Control Points",
				new String[] {
						"Points. Take control."
				},
				Material.CLOCK,
				new ControlPointsMap[] {
						new Prototype(),
				},
				6
		);
	}

	@Override
	public int getDuration() {
		return 200;
	}

	private GameTeam forceWinTeam;
	private Map<Integer, Integer> status; // more negative = blue, more positive = red
	private BossBar barRed;
	private BossBar barBlue;

	@Override
	public void onPreStart() {
		this.forceWinTeam = null;
		this.status = new HashMap<>();

		this.barRed = Bukkit.createBossBar("Red", BarColor.RED, BarStyle.SOLID);
		this.barBlue = Bukkit.createBossBar("Blue", BarColor.BLUE, BarStyle.SOLID);

		for (int i = 0; i < this.map.getControlPointLocations().length; i++) {
			this.status.put(i, 0);
		}

		super.splitPlayers((player, team) -> {
			player.queueTeleport(team == GameTeam.RED ? this.map.getRedSpawnLocation() : this.map.getBlueSpawnLocation());
		});

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
		player.giveEffect(INFINITE_SPEED);
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
					final GameTeam team = this.getTeams().getTeam(player);
					if (team == GameTeam.RED) {
						player.sendFormattedPlainActionBar("Claiming control point! %s", this.status.get(i));
						red++;

					} else if (team == GameTeam.BLUE) {
						player.sendFormattedPlainActionBar("Claiming control point! %s", -this.status.get(i));
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
			switch (this.status.get(i)) {
				case CONTROL_THRESHOLD -> {
					controlStatus = ControlStatus.RED;
					redPoints++;
				}
				case -CONTROL_THRESHOLD -> {
					controlStatus = ControlStatus.BLUE;
					bluePoints++;
				}
				default -> controlStatus = ControlStatus.NEUTRAL;
			}

			this.map.setControlPointStatus(controlPoint, controlStatus);
		}

		final double max = this.map.getControlPointLocations().length;
		this.barRed.setProgress(redPoints / max);
		this.barBlue.setProgress(bluePoints / max);

		if (secondsLeft > 5) {
			if (redPoints == this.map.getControlPointLocations().length) {
				end("red");
				return 5;
			} else if (bluePoints == this.map.getControlPointLocations().length) {
				end("blue");
				return 5;
			}
		}

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false; // more convenient to handle manually in gameTimer for now
		// TODO use this method instead
	}

	public void end(final String winningTeam) {
		GameState.setState(GameState.RUNNING_ENDED_EARLY, this); // TODO remove when endEarly is used
		if (winningTeam.equals("red")) {
			this.forceWinTeam = GameTeam.RED;
		} else {
			this.forceWinTeam = GameTeam.BLUE;
		}

		sendPlainMessage("The game has ended, team " + winningTeam + " is in control of all control points!");
		Minigames.getOnlinePlayers().forEach(MPlayer::spectator);
	}

	@Override
	public void onEnd() {
		if (this.forceWinTeam != null) {
			endGame(this.getTeams().getMembers(this.forceWinTeam, false), false);
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
				endGame(this.getTeams().getMembers(GameTeam.BLUE, false));
			} else if (red > blue) {
				endGame(this.getTeams().getMembers(GameTeam.RED, false));
			} else {
				endGame();
			}
		}

		this.forceWinTeam = null;
		this.status = null;
		this.barBlue.removeAll();
		this.barBlue = null;
		this.barRed.removeAll();
		this.barRed = null;
	}

	@EventHandler
	public void onDamage(MPlayerDamageEvent event) {
		// Disable damage to teammates
		MPlayer damager = event.getDamagerPlayer();
		event.setCancelled(damager != null && this.getTeams().isInSameTeam(event.getPlayer(), damager));
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		final MPlayer player = new MPlayer(event);
		event.setCancelled(true);

		this.sendMessage(Utils.getDeathMessage(event));

		player.die();
		final UUID uuid = player.getUniqueId();

		Scheduler.delay(RESPAWN_DELAY, () -> {
			final MPlayer player2 = Minigames.getPlayer(uuid);
			if (player2 == null) {
				return;
			}

			player.setGameMode(GameMode.ADVENTURE);
			player2.heal();

			player.teleport(this.getTeams().isTeamMember(player, GameTeam.RED) ? this.map.getRedSpawnLocation() : this.map.getBlueSpawnLocation());
		});
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		final GameTeam team = this.getTeams().getTeam(player);
		if (team == null) {
			player.teleport(this.map.getWorld().getSpawnLocation());
			player.spectator();
		} else {
			player.setDisableDamage(false);
			giveGear(player);
			player.teleport(team == GameTeam.RED ? this.map.getRedSpawnLocation() : this.map.getBlueSpawnLocation());
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
