package xyz.derkades.minigames.games;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.missile_wars.MissileWarsMap;
import xyz.derkades.minigames.games.teams.GameTeam;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.queue.TaskQueue;

public class MissileWars extends Game<MissileWarsMap> {

	@Override
	public String getIdentifier() {
		return "missile_wars";
	}

	@Override
	public String getName() {
		return "Missile Wars";
	}

	@Override
	public String[] getDescription() {
		return new String[] {};
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public MissileWarsMap[] getGameMaps() {
		return MissileWarsMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 300;
	}
	
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
						if (block.getType() != Material.BARRIER) {
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