package derkades.minigames.games.meteorshower;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MeteorShower extends Game<MeteorShowerMap> {

	public MeteorShower() {
		super("meteorshower",
				"Meteor Shower",
				new String[]{
						"Avoid getting blown up."
				},
				Material.FIRE_CHARGE,
				new MeteorShowerMap[]{
						new Prototype(),
				},
				2,
				100,
				EnumSet.of(GameLabel.SINGLEPLAYER, GameLabel.NO_TEAMS)
		);
	}

	private List<BukkitTask> meteorTasks;

	@Override
	protected void onPreStart() {
		this.meteorTasks = new LinkedList<>();

		for (MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(map.getSpawnLocation());
		}
	}

	@Override
	protected void onStart() {
		spawnMeteor();
	}

	private static void createFire(Location center) {
		int min = 0;
		int max = 15;
		int fireCount = ThreadLocalRandom.current().nextInt(min, max);
		for (int i = 0; i < fireCount; i++) {
			int x = center.getBlockX() + ThreadLocalRandom.current().nextInt(-4, 5);
			int z = center.getBlockZ() + ThreadLocalRandom.current().nextInt(-4, 5);
			for (int y = center.getBlockY() - 2; y < center.getBlockY() + 4; y++) {
				Block block = center.getWorld().getBlockAt(x, y, z);
				if (block.getType() == Material.AIR) {
					block.setType(Material.FIRE);
				}
			}
		}
	}

	private void spawnMeteor() {
		MeteorBounds bounds = this.map.getMeteorBounds();
		float x = ThreadLocalRandom.current().nextFloat(bounds.minX(), bounds.maxX());
		float y = ThreadLocalRandom.current().nextFloat(bounds.y()-10, bounds.y()+10);
		float z = ThreadLocalRandom.current().nextFloat(bounds.minZ(), bounds.maxZ());

		Location bulletLoc = new Location(this.map.getWorld(), x, y, z);
		UUID uuid = this.map.getWorld().spawn(bulletLoc, ShulkerBullet.class).getUniqueId();

		meteorTasks.add(new BukkitRunnable() {
			Location loc = null;

			public void run() {
				Entity entity = map.getWorld().getEntity(uuid);
				if (entity == null) {
					if (loc != null) {
						loc.getWorld().createExplosion(loc, 2.5f);
						loc.getWorld().spawnParticle(Particle.LAVA, loc, 30, 1, 0, 1, 0);

						if (ThreadLocalRandom.current().nextFloat() > 0.6) {
							while (loc.getBlock().getType() == Material.AIR) {
								loc.add(0, -1, 0);
								if (y < 50) {
									return;
								}
							}

							createFire(loc);

							loc.getBlock().setType(Material.LAVA);
						}
					}
					this.cancel();
					return;
				}
				ShulkerBullet bullet = (ShulkerBullet) entity;
				loc = bullet.getLocation();
				loc.getWorld().spawnParticle(Particle.FLAME, loc, 5, 0, 0.5, 0, 0);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 2, 0, .5, 0, 0);
			}
		}.runTaskTimer(Minigames.getInstance(), 1, 1));

		long delay = 15 - Math.max(this.getSecondsLeft() / 10, 10);
		this.meteorTasks.add(Scheduler.delay(10, this::spawnMeteor));
	}

	@Override
	public int gameTimer(int secondsLeft) {
		this.meteorTasks.removeIf(BukkitTask::isCancelled);

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return Bukkit.getOnlinePlayers().stream().filter(p -> p.getGameMode() == GameMode.ADVENTURE).count() < 2;
	}

	@Override
	protected void onEnd() {
		endGame(Bukkit.getOnlinePlayers().stream().filter(p -> p.getGameMode() == GameMode.ADVENTURE).map(Player::getUniqueId).collect(Collectors.toUnmodifiableSet()));

		for (BukkitTask task : this.meteorTasks) {
			task.cancel();
		}
		this.meteorTasks = null;
	}

	@Override
	public void onPlayerJoin(MPlayer player) {
		if (GameState.currentGameIsRunning()) {
			player.queueTeleport(map.getSpawnLocation(), MPlayer::spectator);
		} else {
			player.queueTeleport(map.getSpawnLocation());
		}
	}

	@Override
	public void onPlayerQuit(MPlayer player) {
	}

	@EventHandler
	public void onDamage(MPlayerDamageEvent event) {
		event.setCancelled(event.getDamagerPlayer() != null);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setCancelled(true);
		MPlayer player = new MPlayer(event);
		player.dieTo(map.getSpawnLocation());
	}

}
