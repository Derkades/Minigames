package xyz.derkades.minigames.games;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.freefall.FreeFallMap;
import xyz.derkades.minigames.games.freefall.Hole;
import xyz.derkades.minigames.games.freefall.Layer;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesJoinEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Utils;

public class FreeFall extends Game<FreeFallMap> {

	@Override
	public String getName() {
		return "Free Fall";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"On every layer, choose the correct platform",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public FreeFallMap[] getGameMaps() {
		return FreeFallMap.MAPS;
	}

	@Override
	public int getDuration() {
		return this.map.getLayers().length * 10 + 10;
	}

	private int layerHeight;
	private List<UUID> winners;

	@Override
	public void onPreStart() {
		this.layerHeight = this.map.getLayers().length - 1;

		for (final Layer layer : this.map.getLayers()) {
			layer.placeBlocks();
		}

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(new Location(this.map.getWorld(), 0, 242, 0));
			player.setDisableDamage(false);
		}
	}

	@Override
	public void onStart() {

	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (this.layerHeight == 0) {
			if (secondsLeft > 2)
				return 2;
			else
				return secondsLeft;
		}

		if (secondsLeft % 10 == 0) {
			final Layer layer = this.map.getLayers()[this.layerHeight];
			final Hole correctHole = layer.setRandomCorrectHole();
			layer.placeFluid();
			layer.removeBlocks();

			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				if (correctHole.isInHole(player)) {
					player.sendActionBar(new ComponentBuilder("You chose the correct hole").color(ChatColor.GREEN).create());
				} else {
					player.sendActionBar(new ComponentBuilder("You chose the wrong hole").color(ChatColor.RED).create());
				}
			}
			return secondsLeft;
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		this.endGame(Utils.getPlayerListFromUUIDList(this.winners));
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		if (event.getType() == DamageType.ENTITY) {
			event.setCancelled(true);
			return;
		}

		final MPlayer player = event.getPlayer();
		player.removeFire();

		if (event.getCause() == DamageCause.FALL){
			event.setCancelled(true);
			return;
		}

		if (event.willBeDead()) {
			event.setCancelled(true);
			this.winners.remove(player.getUniqueId());
			player.die();
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		this.winners.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onJoin(final MinigamesJoinEvent event) {
		event.setTeleportPlayerToLobby(false);

		final MPlayer player = event.getPlayer();
		player.teleport(this.map.getSpawnLocation());
		player.spectator();
	}

}