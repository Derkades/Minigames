package xyz.derkades.minigames.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Deprecated
public class MinigamesJoinEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private boolean teleportPlayer;

	public MinigamesJoinEvent(final Player player) {
		this.player = player;
		this.teleportPlayer = true;
	}

	public MPlayer getPlayer() {
		return new MPlayer(this.player);
	}

	public boolean getTeleportPlayerToLobby() {
		return this.teleportPlayer;
	}

	public void setTeleportPlayerToLobby(final boolean teleportPlayer) {
		this.teleportPlayer = teleportPlayer;
	}

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
