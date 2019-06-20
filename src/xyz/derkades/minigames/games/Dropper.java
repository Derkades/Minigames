package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.dropper.DropperMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesJoinEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Utils;

public class Dropper extends Game<DropperMap> {

	@Override
	public String getName() {
		return "Dropper";
	}

	@Override
	public String[] getDescription() {
		return new String[] {"Get down without dying"};
	}

	@Override
	public int getRequiredPlayers() {
		return 0;
	}

	@Override
	public DropperMap[] getGameMaps() {
		return DropperMap.DROPPER_MAPS;
	}

	@Override
	public int getDuration() {
		return 45;
	}

	private static final String FINISHED = "%s finished.";
	private static final String FINISHED_FIRST = "%s finished first and got 1 extra point!";

	private DropperMap map;
	private List<UUID> finished;
	private List<UUID> all;

	@Override
	public void onPreStart() {
		this.finished = new ArrayList<>();
		this.all = Utils.getOnlinePlayersUuidList();

		this.map.closeDoor();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getLobbyLocation());
		}
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach((p) -> p.setDisableDamage(false));
		Dropper.this.map.openDoor();
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Utils.getWinnersFromFinished(Dropper.this.finished, Dropper.this.all).size() >= Dropper.this.all.size() && secondsLeft > 5) {
			return 5;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		Dropper.this.endGame(Utils.getWinnersFromFinished(Dropper.this.finished, Dropper.this.all));
		this.finished.clear();
		this.all.clear();
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (this.finished.contains(event.getPlayer().getUniqueId())) {
			return; //Don't teleport players who have finished
		}

		if (event.getTo().getBlock().getType() == Material.WATER) {
			final MPlayer player = new MPlayer(event);

			if (this.finished.isEmpty()) {
				//Player is first winner
				player.addPoints(1); //Add bonus point
				this.sendMessage(String.format(FINISHED_FIRST, player.getName()));
			} else {
				this.sendMessage(String.format(FINISHED, player.getName()));
			}

			this.finished.add(player.getUniqueId());
			player.heal();
			player.removeFire();
			player.finishTo(this.map.getLobbyLocation());
		}
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event){
		if (event.getType().equals(DamageType.ENTITY)) {
			event.setCancelled(true);
			return;
		}

		if (event.willBeDead()) {
			event.setCancelled(true);
			event.getPlayer().queueTeleport(this.map.getLobbyLocation());
		}
	}

	@EventHandler
	public void onJoin(final MinigamesJoinEvent event) {
		final MPlayer player = event.getPlayer();
		event.setTeleportPlayerToLobby(false);
		player.hideForEveryoneElse();
		player.teleport(this.map.getLobbyLocation());
		player.setDisableDamage(false);
		this.all.add(player.getUniqueId());
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		this.all.remove(event.getPlayer().getUniqueId());
	}

}
