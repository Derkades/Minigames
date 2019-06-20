package xyz.derkades.minigames.utils;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.projectiles.ProjectileSource;

public class MinigamesPlayerDamageEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final Entity damager;
	private double damage;
	private final DamageType type;

	private boolean cancelled;

	public MinigamesPlayerDamageEvent(final Player player, final Entity damager, final double damage) {
		this.player = player;
		this.damager = damager;
		this.damage = damage;
		this.type = DamageType.ENTITY;
	}


	public MinigamesPlayerDamageEvent(final Player player, final double damage) {
		this.player = player;
		this.damager = null;
		this.damage = damage;
		this.type = DamageType.SELF;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Entity getDamagerEntity() {
		return this.damager;
	}

	public Player getDamagerPlayer() {
		if (this.damager.getType().equals(EntityType.PLAYER)) {
			return (Player) this.damager;
		} else if (this.damager.getType().equals(EntityType.ARROW)) {
			final ProjectileSource shooter = ((Arrow) this.damager).getShooter();
			if (shooter instanceof Player) {
				return (Player) shooter;
			} else {
				return null;
			}
		} else {
			return null;
		}

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
