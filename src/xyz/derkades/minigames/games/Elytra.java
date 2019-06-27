package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.elytra.ElytraMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesJoinEvent;
import xyz.derkades.minigames.utils.Utils;

public class Elytra extends Game<ElytraMap> {

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
	public int getRequiredPlayers() {
		return 1;
	}

	@Override
	public ElytraMap[] getGameMaps() {
		return ElytraMap.MAPS;
	}

	@Override
	public int getPreDuration() {
		return 0;
	}

	@Override
	public int getDuration() {
		return 40;
	}

	private List<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new ArrayList<>();;

		for (final MPlayer player : Minigames.getOnlinePlayers()){
			player.setArmor(null, Material.ELYTRA, null, null);
			player.giveInfiniteEffect(PotionEffectType.SLOW, 5);
			player.giveInfiniteEffect(PotionEffectType.INVISIBILITY, 2);
			player.queueTeleport(this.map.getStartLocation());
		}
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach(MPlayer::clearPotionEffects);
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
		Elytra.super.endGame(Utils.getPlayerListFromUUIDList(Elytra.this.finished));
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event);

		if (!this.started && !this.map.isSafeOnSpawnPlatform(player))
			player.teleport(this.map.getStartLocation());

		if (this.finished.contains(player.getUniqueId()))
			return;

		if (this.map.isDead(player))
			player.removeFire();
			player.teleport(this.map.getStartLocation());

		if (this.map.hasFinished(player)) {
			player.clearInventory();
			player.finishTo(this.map.getSpectatorLocation());

			this.finished.add(player.getUniqueId());

			this.sendMessage(player.getName() + " has finished");
		}
	}

	@EventHandler
	public void join(final MinigamesJoinEvent event) {
		event.setTeleportPlayerToLobby(false);

		final MPlayer player = event.getPlayer();

		if (this.finished.contains(player.getUniqueId())) {
			player.setGameMode(GameMode.SPECTATOR);
			player.teleport(this.map.getSpectatorLocation());
		} else {
			player.setGameMode(GameMode.ADVENTURE);
			player.setArmor(null, Material.ELYTRA, null, null);
			player.teleport(this.map.getStartLocation());
		}
	}

}
