package derkades.minigames.games.pyramid_duels;

import derkades.minigames.GameState;
import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PyramidDuels extends Game<PyramidDuelsMap> {

	public PyramidDuels() {
		super("pyramid_duels",
				"Pyramid Duels",
				new String[] {
						"Battle to the top of the pyramid",
				},
				Material.SANDSTONE,
				new PyramidDuelsMap[] {
						new Prototype(),
				},
				2,
				12,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT)
		);
	}

	private BukkitTask gateUpdateTask =  null;

	@Override
	protected void onPreStart() {
		Location spectatorLocation = this.map.getSpectatorLocation();
		for (MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(spectatorLocation, p2 -> {
				p2.setGameMode(GameMode.SPECTATOR);
			});
		}
	}

	@Override
	protected void onStart() {
		int level = (Bukkit.getOnlinePlayers().size() - 1) / 2;
		Logger.debug("With %d online players, start at level %d", Bukkit.getOnlinePlayers(), level);
		List<PyramidNode> nodes = new ArrayList<>((level * 2) + 1);
		addNodesToList(this.map.getRootNode(), 0, level, nodes);
		Logger.debug("Final nodes list size is %d, expected %d", nodes.size(), (level * 2) + 1);

		int i = 0;
		for (MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			player.queueTeleport(nodes.get(i).getCenterLocation(), p2 -> {
				p2.setGameMode(GameMode.ADVENTURE);
			});
			i++;
		}

		gateUpdateTask = Scheduler.repeat(5, 5, () -> updatePyramidGates(this.map.getRootNode()));
	}

	private void addNodesToList(PyramidNode node, int currentLevel, int desiredLevel, List<PyramidNode> nodesAtLevel) {
		if (currentLevel == desiredLevel) {
			nodesAtLevel.add(node);
		} else if (node.getChildren() != null) {
			for (PyramidNode child : node.getChildren()) {
				addNodesToList(child, currentLevel + 1, desiredLevel, nodesAtLevel);
			}
		} else {
			Logger.warning("Pyramid node at level %d does not have children", currentLevel);
		}
	}

	private void updatePyramidGates(PyramidNode node) {
		int playersInNode = (int) Bukkit.getOnlinePlayers().stream()
				.map(Entity::getLocation)
				.filter(node::isInNode)
				.count();
		if (playersInNode >= 2) {
			node.closeGates();
		} else {
			node.openGates();
		}

		if (node.getChildren() != null) {
			for (PyramidNode child : node.getChildren()) {
				updatePyramidGates(child);
			}
		}
	}

	@Override
	public int gameTimer(int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return Minigames.getOnlinePlayers().stream().filter(p -> p.getGameMode() == GameMode.ADVENTURE).count() < 2;
	}

	@Override
	protected void onEnd() {
		gateUpdateTask.cancel();
		gateUpdateTask = null;
	}

	@Override
	public void onPlayerJoin(MPlayer player) {
		if (GameState.isCurrentlyInGame()) {
			player.queueTeleport(map.getSpectatorLocation(), MPlayer::spectator);
		}
	}

	@Override
	public void onPlayerQuit(MPlayer player) {

	}

	@Override
	public boolean isDisabled() {
		return true;
	}

}
