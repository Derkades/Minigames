package derkades.minigames.games.dropper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import derkades.minigames.utils.Utils;

public class Dropper extends Game<DropperMap> {

	@Override
	public String getIdentifier() {
		return "dropper";
	}

	@Override
	public String getName() {
		return "Dropper";
	}

	@Override
	public String[] getDescription() {
		return new String[] {"Get down without dying"};
	}

	@Override
	public Material getMaterial() {
		return Material.DIAMOND_BOOTS;
	}

	@Override
	public int getRequiredPlayers() {
		return 1;
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

	private Set<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new HashSet<>();

		this.map.closeDoor();

		Minigames.getOnlinePlayers().forEach((p) -> p.queueTeleport(this.map.getLobbyLocation()));
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach((p) -> p.setDisableDamage(false));
		Dropper.this.map.openDoor();
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
		Dropper.this.endGame(Dropper.this.finished);
		this.finished = null;
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
				sendPlainMessage(String.format(FINISHED_FIRST, player.getName()));
			} else {
				sendPlainMessage(String.format(FINISHED, player.getName()));
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
			event.getPlayer().heal();
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getLobbyLocation());

		if (this.finished.contains(player.getUniqueId())) {
			player.setGameMode(GameMode.SPECTATOR);

		} else {
			player.hideForEveryoneElse();
			player.setDisableDamage(false);
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
