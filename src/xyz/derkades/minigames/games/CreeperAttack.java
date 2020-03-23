package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.creeperattack.CreeperAttackMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.Winners;

public class CreeperAttack extends Game<CreeperAttackMap> {

	@Override
	public String getName() {
		return "Creeper Attack";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Avoid dying from creeper explosions. Use your",
				"knockback stick to defend yourself against creepers."
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public CreeperAttackMap[] getGameMaps() {
		return CreeperAttackMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 60;
	}

	private List<UUID> alive;

	private int numberOfCreepers = 1;

	@Override
	public void onPreStart() {
		this.alive = new ArrayList<>();
		this.numberOfCreepers = 1;

		final ItemStack knockbackStick = new ItemBuilder(Material.STICK)
				.name(ChatColor.GOLD + "" + ChatColor.BOLD + "Creeper Smasher")
				.create();

		knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);

		Minigames.getOnlinePlayers().forEach((player) -> {
			this.alive.add(player.getUniqueId());
			player.giveItem(knockbackStick);
			player.setDisableDamage(false);
			player.queueTeleport(this.map.getSpawnLocation());
		});
	}

	@Override
	public void onStart() {}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (this.alive.size() <= 1 && secondsLeft > 5) {
			return 5;
		}

		if (secondsLeft % 10 == 0){
			this.numberOfCreepers++;
		}

		for (int i = 0; i < this.numberOfCreepers; i++) {
			if (this.alive.size() > 1) {
				final Creeper creeper = CreeperAttack.this.map.getWorld().spawn(CreeperAttack.this.map.getCreeperLocation(), Creeper.class);
				creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);
				creeper.setTarget(Bukkit.getPlayer(ListUtils.getRandomValueFromList(CreeperAttack.this.alive)));
			}
		}

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		for (final Creeper creeper : CreeperAttack.this.map.getWorld().getEntitiesByClass(Creeper.class)) {
			creeper.remove();
		}

		this.endGame(Winners.fromAlive(this.alive, false));

		this.alive = null;
	}

	@EventHandler
	public void onAttack(final EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() != EntityType.CREEPER) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		if (event.willBeDead()) {
			event.setCancelled(true);
			final MPlayer player = event.getPlayer();
			this.alive.remove(player.getUniqueId());
			player.die();
			sendMessage(player.getName() + " has been blown up by a creeper");
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.die();
		player.teleport(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
