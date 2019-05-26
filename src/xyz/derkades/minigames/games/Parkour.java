package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.parkour.ParkourMap;
import xyz.derkades.minigames.utils.Utils;

public class Parkour extends Game {

	Parkour() {
		super("Parkour", new String[] {
				"Jump to the finish without touching the ground",
		}, 1, 3, 5, ParkourMap.MAPS);
	}

	private ParkourMap map;
	private List<UUID> finished;

	@Override
	void begin(final GameMap genericMap) {
		this.map = (ParkourMap) genericMap;
		this.finished = new ArrayList<>();

		for (final Player player: Bukkit.getOnlinePlayers()){
			player.teleport(this.map.getStartLocation());
			Utils.hideForEveryoneElse(player);
		}

		new GameTimer(this, this.map.getDuration(), 2) {

			@Override
			public void onStart() {
				Bukkit.getOnlinePlayers().forEach(Utils::hideForEveryoneElse);
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (Bukkit.getOnlinePlayers().size() == Parkour.this.finished.size() && secondsLeft > 1) {
					return 1;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				Parkour.this.endGame(Utils.getPlayerListFromUUIDList(Parkour.this.finished));
			}

		};
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final Player player = event.getPlayer();

		if (this.finished.contains(player.getUniqueId())) {
			return;
		}

		final Material blockType = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();

		if (this.map.hasDied(player, blockType)) {
			player.teleport(this.map.getStartLocation());
		}

		if (this.map.hasFinished(player, blockType)) {
			if (this.finished.isEmpty()) {
				super.sendMessage(player.getName() + " finished first and got an extra point!");
				Points.addPoints(player, 1);
			} else {
				this.sendMessage(player.getName() + " has made it to the finish!");
			}

			this.finished.add(player.getUniqueId());

			if (this.map.getSpectatorLocation() != null) player.teleport(this.map.getSpectatorLocation());

			Utils.playSoundForAllPlayers(Sound.ENTITY_PLAYER_LEVELUP, 1);

			// Show all players to the spectator (the spectator is still invisible to others)
			Bukkit.getOnlinePlayers().forEach((player2) -> player.showPlayer(Minigames.getInstance(), player2));

			if (this.map.spectatorFreeFlight()) {
				player.setAllowFlight(true);
				player.setFlying(true);
				Utils.giveInvisibility(player);
			}
		}

	}

}
