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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.Random;
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
	private BukkitTask task;
	private float chance;
	
	private CreeperAttackMap map;
	
	@Override
	void begin(final GameMap genericMap) {
		alive = new ArrayList<>();
		chance = 0.35f;
		this.map = (CreeperAttackMap) genericMap;
		
		ItemStack knockbackStick = new ItemBuilder(Material.STICK)
				.name(ChatColor.GOLD + "" + ChatColor.BOLD + "Creeper Smasher")
				.create();
		
		knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
		
		Utils.setGameRule("doMobLoot", false);
		
		Utils.delayedTeleport(map.getSpawnLocation(), Bukkit.getOnlinePlayers());
		
		Bukkit.getOnlinePlayers().forEach((player) -> {
			alive.add(player.getUniqueId());
			player.getInventory().addItem(knockbackStick);
			Minigames.setCanTakeDamage(player, true);
		});
		
		task = Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), () -> {
			List<Player> alivePlayers = Utils.getPlayerListFromUUIDList(alive);
			if (alivePlayers.size() <= 1) {
				finish();
			}
			
			if (Random.getRandomFloat() < chance) {
				chance += 0.02f;
				Creeper creeper = Var.WORLD.spawn(map.getCreeperLocation(), Creeper.class);
				//creeper.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000000, 50, true, false));
				//creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 1, true, false));
				creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);
				creeper.setTarget(ListUtils.getRandomValueFromList(Utils.getPlayerListFromUUIDList(alive)));
			}
		}, 20, 10);
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() != EntityType.CREEPER) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");
		alive.remove(event.getEntity().getUniqueId());
		Scheduler.delay(1, () -> {
			event.getEntity().spigot().respawn();
			Utils.clearInventory(event.getEntity());
			if (map.getSpectatorLocation() != null)
				event.getEntity().teleport(map.getSpectatorLocation());
			Minigames.setCanTakeDamage(event.getEntity(), false);
		});
		
		sendMessage(event.getEntity().getName() + " has been blown up by a creeper");
	}
	
	private void finish() {
		task.cancel();
		for (Creeper creeper : Var.WORLD.getEntitiesByClass(Creeper.class)) {
			creeper.remove();
		}
		super.endGame(Utils.getPlayerListFromUUIDList(alive));
	}

}
