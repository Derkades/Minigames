package xyz.derkades.minigames.utils;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

public class MinigamesPlayerDamageEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final Entity damager;
	private double damage;
	private final DamageCause cause;
	private final DamageType type;

	private boolean cancelled;

	public MinigamesPlayerDamageEvent(final Player player, final Entity damager, final DamageCause cause, final double damage) {
		this.player = player;
		this.damager = damager;
		this.damage = damage;
		this.cause = cause;
		this.type = DamageType.ENTITY;
	}


	public MinigamesPlayerDamageEvent(final Player player, final DamageCause cause, final double damage) {
		this.player = player;
		this.damager = null;
		this.damage = damage;
		this.cause = cause;
		this.type = DamageType.SELF;
	}

	public MPlayer getPlayer() {
		return new MPlayer(this.player);
	}

	public Entity getDamagerEntity() {
		return this.damager;
	}

	public MPlayer getDamagerPlayer() {
		if (this.damager.getType().equals(EntityType.PLAYER))
			return new MPlayer((Player) this.damager);
		else if (this.damager.getType().equals(EntityType.ARROW)) {
			final ProjectileSource shooter = ((Arrow) this.damager).getShooter();
			if (shooter instanceof Player)
				return new MPlayer((Player) shooter);
			else
				return null;
		} else
			return null;

	}

	public double getDamage() {
		return this.damage;
	}

	public void setDamage(final double damage) {
		this.damage = damage;
	}

	public boolean willBeDead() {
		return (this.player.getHealth() - this.damage) < 1;
	}

	public DamageType getType() {
		return this.type;
	}

	public DamageCause getCause() {
		return this.cause;
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
		return this.cancelled;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	public static enum DamageType {

		SELF, ENTITY;

	}

}
