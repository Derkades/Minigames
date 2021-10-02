package derkades.minigames.games.parkour;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Parkour extends Game<ParkourMap> {

	public Parkour() {
		super(
				"parkour",
				"Parkour",
				new String[] {
						"Jump to the finish without touching the ground",
				},
				Material.GOLDEN_BOOTS,
				new ParkourMap[] {
						new JungleRun(),
						//new Plains(),
						new RedstoneCave(),
						new Snow(),
						new TNT(),
				},
				1,
				120,
				EnumSet.of(GameLabel.SINGLEPLAYER, GameLabel.PARKOUR)
		);
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
				player.addPoints(1);
				this.sendMessage(player.getDisplayName().append(Component.text(" finished first and got an extra point!")));
			} else {
				this.sendMessage(player.getDisplayName().append(Component.text(" finished.")));
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
