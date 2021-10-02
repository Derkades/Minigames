package derkades.minigames.games.missile.wars;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameTeam;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.queue.TaskQueue;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MissileWars extends Game<MissileWarsMap> {

	public MissileWars() {
		super(
				"missile_wars",
				"Missile Wars",
				new String[]{
						"description" // TODO Description
				},
				Material.SLIME_BLOCK,
				new MissileWarsMap[]{
						new MissileWarsMapImpl(),
				},
				6,
				300
		);
	}

	private static final Set<Material> DONT_REPLACE = Set.of(
			Material.BARRIER,
			Material.BEDROCK
	);

	private Map<UUID, GameTeam> teams = null;

	@Override
	public void onPreStart() {
		this.teams = new HashMap<>();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			TaskQueue.add(() -> {
				player.teleport(this.map.getWorld().getSpawnLocation());
				player.setGameMode(GameMode.SPECTATOR);
			});
		}

		final Location min = this.map.getArenaBorderMin();
		final Location max = this.map.getArenaBorderMax();
		for (int y = max.getBlockY() - 1; y >= 0; y--) {
			final int finalY = y;
			TaskQueue.add(() -> {
				for (int x = min.getBlockX() + 1; x < max.getBlockX(); x++) {
					for (int z = min.getBlockZ() + 1; z < max.getBlockZ(); z++) {
						final Block block = this.map.getWorld().getBlockAt(x, finalY, z);
						if (!DONT_REPLACE.contains(block.getType())) {
							block.setType(Material.AIR);
						}
					}
				}
			});
		}
		this.map.buildArena();
	}

	@Override
	public void onStart() {
		boolean team = false;
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			final Location teamSpawnLocation = team ? this.map.getTeamRedSpawnLocation() : this.map.getTeamBlueSpawnLocation();
			this.teams.put(player.getUniqueId(), team ? GameTeam.RED : GameTeam.BLUE);
			TaskQueue.add(() -> {
				player.teleport(teamSpawnLocation);
				player.setGameMode(GameMode.ADVENTURE);
			});
			team = !team;
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false;
	}

	@Override
	public void onEnd() {
		endGame();
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.spectator();
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
