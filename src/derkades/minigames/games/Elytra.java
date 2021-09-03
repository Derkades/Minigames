package derkades.minigames.games;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import derkades.minigames.Minigames;
import derkades.minigames.games.elytra.ElytraMap;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;

public class Elytra extends Game<ElytraMap> {

	@Override
	public String getIdentifier() {
		return "elytra";
	}

	@Override
	public String getName() {
		return "Elytra";
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Fly to the end of the cave without touching",
				"the ground or lava."
		};
	}

	@Override
	public Material getMaterial() {
		return Material.ELYTRA;
	}

	@Override
	public int getRequiredPlayers() {
		return 1;
	}

	@Override
	public ElytraMap[] getGameMaps() {
		return ElytraMap.MAPS;
	}

	@Override
	public int getPreDuration() {
		return 2;
	}

	@Override
	public int getDuration() {
		return 40;
	}

	private Set<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new HashSet<>();
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()){
			player.setArmor(null, Material.ELYTRA, null, null);
			player.queueTeleport(this.map.getStartLocation());
		}
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Utils.allPlayersFinished(this.finished) && secondsLeft > 5) {
			return 5;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		Elytra.super.endGame(Elytra.this.finished);
		this.finished = null;
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event);

		if (!this.hasStarted() || this.finished.contains(player.getUniqueId())) {
			return;
		}

		if (this.map.isDead(player)) {
			player.removeFire();
			player.teleport(this.map.getStartLocation());
		}

		if (this.map.hasFinished(player)) {
			player.clearInventory();
			player.finishTo(this.map.getStartLocation());

			this.finished.add(player.getUniqueId());

			sendMessage(player.getName() + " has finished");
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getStartLocation());

		if (this.finished.contains(player.getUniqueId())) {
			player.setGameMode(GameMode.SPECTATOR);
		} else {
			player.setGameMode(GameMode.ADVENTURE);
			player.setArmor(null, Material.ELYTRA, null, null);
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {}

}
