package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.creeperattack.CreeperAttackMap;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class CreeperAttack extends Game {

	CreeperAttack() {
		super("Creeper Attack", new String[] {
				"Avoid dying from creeper explosions. Use your",
				"knockback stick to defend yourself against creepers."
		}, 2, 6, 7, CreeperAttackMap.MAPS);
	}

	private List<UUID> alive;


	private CreeperAttackMap map;

	@Override
	void begin(final GameMap genericMap) {
		this.alive = new ArrayList<>();
		this.map = (CreeperAttackMap) genericMap;

		final ItemStack knockbackStick = new ItemBuilder(Material.STICK)
				.name(ChatColor.GOLD + "" + ChatColor.BOLD + "Creeper Smasher")
				.create();

		knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);

		Utils.setGameRule("doMobLoot", false);

		Utils.delayedTeleport(this.map.getSpawnLocation(), Bukkit.getOnlinePlayers());

		Bukkit.getOnlinePlayers().forEach((player) -> {
			this.alive.add(player.getUniqueId());
			player.getInventory().addItem(knockbackStick);
			Minigames.setCanTakeDamage(player, true);
		});

		new GameTimer(this, 60, 2) {

			private int numberOfCreepers = 1;

			@Override
			public void onStart() {

			}

			@Override
			public int gameTimer(final int secondsLeft) {
				final List<Player> alivePlayers = Utils.getPlayerListFromUUIDList(CreeperAttack.this.alive);
				if (alivePlayers.size() <= 1 && secondsLeft > 1) {
					return 1;
				}

				if (secondsLeft % 5 == 0){
					this.numberOfCreepers++;
				}

				for (int i = 0; i < this.numberOfCreepers; i++) {
					final Creeper creeper = Var.WORLD.spawn(CreeperAttack.this.map.getCreeperLocation(), Creeper.class);
					creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);
					creeper.setTarget(ListUtils.getRandomValueFromList(Utils.getPlayerListFromUUIDList(CreeperAttack.this.alive)));
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				for (final Creeper creeper : Var.WORLD.getEntitiesByClass(Creeper.class)) {
					creeper.remove();
				}

				if (CreeperAttack.this.alive.size() == 1) {
					CreeperAttack.this.endGame(Utils.getPlayerListFromUUIDList(CreeperAttack.this.alive));
				} else {
					CreeperAttack.this.endGame(Arrays.asList());
				}
			}

		};
	}

	@EventHandler
	public void onAttack(final EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() != EntityType.CREEPER) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setDeathMessage("");
		this.alive.remove(event.getEntity().getUniqueId());
		Scheduler.delay(1, () -> {
			event.getEntity().spigot().respawn();
			Utils.clearInventory(event.getEntity());
			if (this.map.getSpectatorLocation() != null)
				event.getEntity().teleport(this.map.getSpectatorLocation());
			Minigames.setCanTakeDamage(event.getEntity(), false);
		});

		this.sendMessage(event.getEntity().getName() + " has been blown up by a creeper");
	}

}
