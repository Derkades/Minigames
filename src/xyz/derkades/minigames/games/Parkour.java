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
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Spectator;
import xyz.derkades.minigames.games.parkour.ParkourMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesJoinEvent;
import xyz.derkades.minigames.utils.Utils;

public class Parkour extends Game<ParkourMap> {

	@Override
	public String getName() {
		return "Parkour";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Jump to the finish without touching the ground",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 0;
	}

	@Override
	public ParkourMap[] getGameMaps() {
		return ParkourMap.MAPS;
	}

	@Override
	public int getDuration() {
		return this.map.getDuration();
	}

	private List<UUID> finished;
	private List<UUID> all;

	@Override
	public void onPreStart() {
		this.finished = new ArrayList<>();
		this.all = Utils.getOnlinePlayersUuidList();

		for (final Player player: Bukkit.getOnlinePlayers()){
			player.teleport(this.map.getStartLocation());
			Utils.hideForEveryoneElse(player);
		}

	}

	@Override
	public void onStart() {
		Bukkit.getOnlinePlayers().forEach(Utils::hideForEveryoneElse);
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Parkour.this.all.size() >= Parkour.this.finished.size() && secondsLeft > 5) {
			return 5;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		Parkour.this.endGame(Utils.getPlayerListFromUUIDList(Parkour.this.finished));
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

			Utils.playSoundForAllPlayers(Sound.ENTITY_PLAYER_LEVELUP, 1);

			// Show all players to the spectator (the spectator is still invisible to others)
			Bukkit.getOnlinePlayers().forEach((player2) -> player.showPlayer(Minigames.getInstance(), player2));

			Spectator.finish(player);
		}

	}

	@EventHandler
	public void onJoin(final MinigamesJoinEvent event) {
		final MPlayer player = event.getPlayer();
		event.setTeleportPlayerToLobby(false);

		this.all.add(player.getUniqueId());

		player.hideForEveryoneElse();
		player.teleport(this.map.getStartLocation());
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		this.all.remove(event.getPlayer().getUniqueId());
	}

}
