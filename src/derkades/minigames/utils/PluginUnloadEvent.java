package derkades.minigames.utils;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PluginUnloadEvent extends Event {

	@NotNull
	private static final HandlerList handlers = new HandlerList();

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	public static @NotNull HandlerList getHandlerList() {
	    return handlers;
	}

}
