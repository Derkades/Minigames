package derkades.minigames.games.elytra;

import derkades.minigames.GameState;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Elytra extends Game<ElytraMap> {

	private static final ElytraMap[] MAPS = {
			new Cave(),
	};

	public Elytra() {
		super(
				"elytra",
				"Elytra",
				new String[]{
						"Fly to the end of the cave without touching",
						"the ground or lava."
				},
				Material.ELYTRA,
				new ElytraMap[] {
						new Cave(),
				}

		);
	}

	@Override
	public int getRequiredPlayers() {
		return 1;
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
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return Utils.allPlayersFinished(this.finished);
	}

	@Override
	public void onEnd() {
		Elytra.super.endGame(Elytra.this.finished);
		this.finished = null;
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event);

		if (!GameState.getCurrentState().gameIsRunning() || this.finished.contains(player.getUniqueId())) {
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

			this.sendMessage(player.getDisplayName().append(Component.text(" has finished.", NamedTextColor.GRAY)));
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
