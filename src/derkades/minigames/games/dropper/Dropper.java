package derkades.minigames.games.dropper;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Dropper extends Game<DropperMap> {

	public Dropper() {
		super(
				"dropper",
				"Dropper",
				new String[] {
						"Get down without dying"
				},
				Material.DIAMOND_BOOTS,
				new DropperMap[] {
						new BlackWhite(),
						new Rainbow(),
						new Redstone(),
						new Trees(),
				},
				1,
				45,
				EnumSet.of(GameLabel.DROPPER, GameLabel.SINGLEPLAYER, GameLabel.NO_TEAMS)
		);
	}

	private Set<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new HashSet<>();

		this.map.closeDoor();

		Minigames.getOnlinePlayers().forEach((p) -> p.queueTeleport(this.map.getLobbyLocation()));
	}

	@Override
	public void onStart() {
		Dropper.this.map.openDoor();
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return Utils.allPlayersIn(this.finished);
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
				// Player is first winner, add bonus point
				player.addPoints(1);
				this.sendMessage(player.getDisplayName().append(Component.text(" finished first and got an extra point!", NamedTextColor.GRAY)));
			} else {
				this.sendMessage(player.getDisplayName().append(Component.text(" finished.", NamedTextColor.GRAY)));
			}

			this.finished.add(player.getUniqueId());
			player.heal();
			player.removeFire();
			player.finishTo(this.map.getLobbyLocation());
		}
	}

	@EventHandler
	public void onDamage(MPlayerDamageEvent event) {
		// disable pvp, allow other damage
		event.setCancelled(event.getDamagerPlayer() != null);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setCancelled(true);
		MPlayer player = new MPlayer(event);
		player.queueTeleport(this.map.getLobbyLocation());
		player.heal();
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getLobbyLocation());

		if (this.finished.contains(player.getUniqueId())) {
			player.setGameMode(GameMode.SPECTATOR);
		} else {
			player.hideForEveryoneElse();
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
