package derkades.minigames.games.parkour;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class Parkour extends Game<ParkourMap> {

	@Override
	public @NotNull String getIdentifier() {
		return "parkour";
	}

	@Override
	public @NotNull String getName() {
		return "Parkour";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Jump to the finish without touching the ground",
		};
	}

	@Override
	public @NotNull Material getMaterial() {
		return Material.GOLDEN_BOOTS;
	}

	@Override
	public int getRequiredPlayers() {
		return 1;
	}

	@Override
	public ParkourMap[] getGameMaps() {
		return ParkourMap.MAPS;
	}

	@Override
	public int getDuration() {
		return this.map.getDuration();
	}

	@Override
	public int getPreDuration() {
		return 0;
	}

	private Set<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new HashSet<>();

		for (final MPlayer player: Minigames.getOnlinePlayers()){
			player.queueTeleport(this.map.getStartLocation());
			player.hideForEveryoneElse();
		}

	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach(MPlayer::hideForEveryoneElse);
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return Utils.allPlayersFinished(this.finished);
	}

	@Override
	public void onEnd() {
		Parkour.this.endGame(Parkour.this.finished);
		this.finished = null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event);

		if (this.finished.contains(player.getUniqueId())) {
			return;
		}

		final Material blockType = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

		if (this.map.hasDied(player, blockType)) {
			player.teleport(this.map.getStartLocation());
		}

		if (this.map.hasFinished(player, blockType)) {
			if (this.finished.isEmpty()) {
				super.sendFormattedPlainMessage("%s finished first and got an extra point!", player.getName());
				player.addPoints(1);
			} else {
				sendFormattedPlainMessage("%s has made it to the finish!", player.getName());
			}

			this.finished.add(player.getUniqueId());

			Minigames.getOnlinePlayers().forEach((player2) -> {
				// Show all players to the spectator (the spectator is still invisible to others)
				player.showPlayer(player2);
				player2.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1);
			});

			player.finishTo(this.map.getStartLocation());
		}

	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (this.finished.contains(player.getUniqueId())) {
			player.finishTo(this.map.getStartLocation());
			player.setGameMode(GameMode.SPECTATOR);
		} else {
			player.hideForEveryoneElse();
			player.teleport(this.map.getStartLocation());
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {}

}
