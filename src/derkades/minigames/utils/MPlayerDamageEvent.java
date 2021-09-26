package derkades.minigames.utils;

import java.util.Objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MPlayerDamageEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	@NotNull
	private final EntityDamageEvent event;
	@NotNull
	private final MPlayer player;

	public MPlayerDamageEvent(@NotNull final EntityDamageEvent event) {
		this.event = Objects.requireNonNull(event);

		final Entity damagee = event.getEntity();

		if (damagee.getType() != EntityType.PLAYER) {
			throw new IllegalStateException("Damaged entity must be a player");
		}

		this.player = new MPlayer((Player) damagee);
	}

	public MPlayer getPlayer() {
		return this.player;
	}

	@Nullable
	public Entity getDirectDamagerEntity() {
		if (this.event instanceof EntityDamageByEntityEvent) {
			return ((EntityDamageByEntityEvent) this.event).getDamager();
		} else {
			return null;
		}
	}

	public MPlayer getDamagerPlayer() {
		return Utils.getDamagerPlayer(this.event);
	}

	public double getDamage() {
		return this.event.getDamage();
	}

	public void setDamage(final double damage) {
		this.event.setDamage(damage);
	}

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.event.isCancelled();
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.event.setCancelled(cancelled);
	}

}
