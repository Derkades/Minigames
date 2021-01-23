package xyz.derkades.minigames.games;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.derkutils.Cooldown;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.missile_racer.MissileRacerMap;
import xyz.derkades.minigames.games.missiles.Missile;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;

public class MissileRacer extends Game<MissileRacerMap> {

	private static final ItemStack PLACEABLE_TNT = new ItemBuilder(Material.TNT)
			.amount(20)
			.canPlaceOn(
					Material.OBSIDIAN,
					Material.SLIME_BLOCK,
					Material.REDSTONE_BLOCK,
					Material.GLASS,
					Material.HONEY_BLOCK,
					Material.OBSERVER,
					Material.PISTON,
					Material.STICKY_PISTON
					)
			.create();
	
	private static final ItemStack IGNITER = new ItemBuilder(Material.FLINT_AND_STEEL)
			.create();
	
	private static final ItemStack MISSILE_SPAWNER = new ItemBuilder(Material.PISTON)
			.name("Missile spawner")
			.create();
	
	private static final int MISSILE_PLACE_COOLDOWN = 7_000;
	
	private static final Missile[] MISSILE_CHOICES = {
			Missile.MATIGE_MISSILE_1,
			Missile.MATIGE_MISSILE_2,
			Missile.MATIGE_MISSILE_3,
			Missile.MATIGE_MISSILE_4,
//			Missile.BEE,
	};
	
	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.enchant(Enchantment.ARROW_INFINITE, 1)
			.enchant(Enchantment.ARROW_FIRE, 1)
			.unbreakable()
			.create();
	
	private static final ItemStack ARROW = new ItemBuilder(Material.ARROW)
			.create();
	
	@Override
	public String getIdentifier() {
		return "missile_racer";
	}
	
	@Override
	public String getName() {
		return "Missile Racer";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Parkour + missiles",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public MissileRacerMap[] getGameMaps() {
		return MissileRacerMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 200;
	}
	
	private UUID winner;

	@Override
	public void onPreStart() {
		this.winner = null;
		
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getSpawnLocation());
			player.getInventory().addItem(MISSILE_SPAWNER);
			player.getInventory().addItem(BOW);
			player.getInventory().addItem(ARROW);
			player.giveInfiniteEffect(PotionEffectType.HEALTH_BOOST, 4);
			player.giveEffect(PotionEffectType.HEAL, 1, 100);
		}
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.setDisableDamage(false);
		}
	}

	@Override
	public int gameTimer(int secondsLeft) {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (!player.getInventory().contains(Material.TNT, 32)) {
				player.getInventory().addItem(PLACEABLE_TNT);
			}
			if (!player.getInventory().contains(Material.FLINT_AND_STEEL)) {
				player.getInventory().addItem(IGNITER);
			}
		}
		
		if (this.winner == null) {
			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				if (this.map.isInFinishBounds(player)) {
					sendMessage(player.getName() + " made it to the finish line!");
					this.winner = player.getUniqueId();
					if (secondsLeft > 5) {
						secondsLeft = 5;
					}
					break;
				}
			}
		}
		
		return secondsLeft;
	}

	@Override
	public void onEnd() {
		this.endGame(this.winner);
		this.winner = null;
	}
	
	@EventHandler
	public void onClick(final PlayerInteractEvent event) {
		if (!this.started) {
			return;
		}
		
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		
		final MPlayer player = new MPlayer(event);
		
		if (player.getGameMode() != GameMode.ADVENTURE) {
			return;
		}
		
		if (player.getInventory().getItemInMainHand().getType() != Material.PISTON) {
			return;
		}
		
		final String cooldownId = "missileracer" + player.getName();
		
		if (Cooldown.getCooldown(cooldownId) > 0) {
			player.sendActionBar("You cannot use this right now.");
			return;
		}
		
		Cooldown.addCooldown(cooldownId, MISSILE_PLACE_COOLDOWN);
		
		final Missile random = ListUtils.getRandomValueFromArray(MISSILE_CHOICES);
		
		random.build(player.getLocation().add(0, -3, 0), player.getFacingAsBlockFace());
	}
	
	@EventHandler
	public void onDeath(final MinigamesPlayerDamageEvent event) {
		if (event.willBeDead()) {
			event.setCancelled(true);
			final MPlayer player = event.getPlayer();
			player.teleport(this.map.getSpawnLocation());
			player.giveEffect(PotionEffectType.HEAL, 1, 100);
		}
	}
	
	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (event.getTo().getY() < this.map.getMinimumY()) {
			final MPlayer player = new MPlayer(event);
			player.teleport(this.map.getSpawnLocation());
			player.giveEffect(PotionEffectType.HEAL, 1, 100);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getSpawnLocation());
		player.getInventory().addItem(MISSILE_SPAWNER);
		player.giveInfiniteEffect(PotionEffectType.HEALTH_BOOST, 4);
		player.giveEffect(PotionEffectType.HEAL, 1, 100);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		
	}

}