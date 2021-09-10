package derkades.minigames.games.breaktheblock;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class BreakTheBlock extends Game<BreakTheBlockMap> {

	private static final ItemStack PICKAXE = new ItemBuilder(Material.IRON_PICKAXE)
			.unbreakable()
			.name(ChatColor.GOLD + "Block breaker")
			.lore(ChatColor.YELLOW + "Use this gold pickaxe to break the ", ChatColor.YELLOW + "gold block at the end of the game.")
			.canDestroy(
					"minecraft:gold_block",
					"minecraft:red_glazed_terracotta" // cherry in cake map
					)
			.create();

	@Override
	public String getIdentifier() {
		return "break_the_block";
	}

	@Override
	public String getName() {
		return "Gold Rush";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Make it through an obstacle course to break the gold block.",
				};
	}

	@Override
	public Material getMaterial() {
		return Material.IRON_PICKAXE;
	}

	@Override
	public int getRequiredPlayers() {
		return 3;
	}

	@Override
	public BreakTheBlockMap[] getGameMaps() {
		return BreakTheBlockMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 200;
	}

	@Override
	public int getPreDuration() {
		return 2;
	}

	@Override
	public String getAlias() {
		return "btb";
	}

	private UUID blockBreaker;

	@Override
	public void onPreStart() {
		this.blockBreaker = null;

		Minigames.getOnlinePlayers().forEach((p) -> {
			p.giveItem(PICKAXE);
			p.queueTeleport(this.map.getStartLocation());
			p.giveInfiniteEffect(PotionEffectType.DAMAGE_RESISTANCE, 10);
		});
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach((player) -> {
			player.setDisableDamage(false);
//			player.enableSneakPrevention(p -> {
//				p.clearInventory();
//				p.teleport(this.map.getStartLocation());
//				p.giveItem(PICKAXE);
//			});
			player.giveInfiniteEffect(PotionEffectType.SLOW_DIGGING, 1);
		});

	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		if (BreakTheBlock.this.blockBreaker != null) {
			Minigames.getOnlinePlayers().forEach(MPlayer::disableSneakPrevention);
			return true;
		}
		return false;
	}

	@Override
	public void onEnd() {
		this.endGame(this.blockBreaker);
		this.blockBreaker = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getStartLocation());
		player.giveItem(PICKAXE);
		player.setDisableDamage(false);
//		player.enableSneakPrevention(p -> {
//			p.clearInventory();
//			p.teleport(this.map.getStartLocation());
//		});
		player.giveInfiniteEffect(PotionEffectType.SLOW_DIGGING, 1);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

	private static final PotionEffect SLIME_BOOST = new PotionEffect(PotionEffectType.JUMP, 20, 8);
	private static final ItemStack BOW = new ItemBuilder(Material.BOW).unbreakable().enchant(Enchantment.ARROW_INFINITE, 1).create();
	private static final ItemStack ARROW = new ItemStack(Material.ARROW);

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final MPlayer player = new MPlayer(event);

		if (player.getBlockIn().getType() == Material.WATER || player.getLocation().getY() < 60){
			player.teleport(this.map.getStartLocation());
			player.clearInventory();
			player.giveItem(PICKAXE);
			return;
		}

		final PlayerInventory inv = player.getInventory();

		final Block blockOn = player.getBlockOn();

		if (blockOn.getType().equals(Material.ORANGE_CONCRETE) &&
				!inv.contains(Material.FIREWORK_ROCKET)) {
			inv.addItem(new ItemBuilder(Material.FIREWORK_ROCKET).name(ChatColor.LIGHT_PURPLE + "Elytra fireworks").create());
		}

		if (blockOn.getType().equals(Material.PINK_CONCRETE) &&
				!inv.contains(Material.ELYTRA)) {
			final ItemStack elytra = new ItemBuilder(Material.ELYTRA)
					.unbreakable()
					.create();
			player.setArmor(null, elytra, null, null);
		}

		if (blockOn.getType() == Material.LIME_CONCRETE) {
			player.giveEffect(SLIME_BOOST);
		}

		if (blockOn.getType() == Material.MAGENTA_CONCRETE) {
			if (player.getInventory().getItem(1) == null ||
					player.getInventory().getItem(1).getType() == Material.AIR) {
				player.getInventory().setHeldItemSlot(1);
				player.getInventory().setItem(1, BOW);
				player.getInventory().setItem(9, ARROW);
			}
		} else {
			player.getInventory().setItem(1, null);
			player.getInventory().setItem(9, null);
		}
	}

	@EventHandler
	public void blockBreakEvent(final BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.GOLD_BLOCK) {
			return;
		}

		sendPlainMessage(event.getPlayer().getName() + " has broken the block!");

		this.blockBreaker = event.getPlayer().getUniqueId();

		this.map.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, event.getBlock().getLocation(), 1);

		for (final MPlayer all : Minigames.getOnlinePlayers()) {
			all.playSound(Sound.ENTITY_GENERIC_EXPLODE, 1.0f);
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER ||
				event.getDamager().getType() != EntityType.PLAYER) {
			return;
		}

//		final Block blockBelow = event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN);
		event.setCancelled(!this.map.canTakeDamage(new MPlayer(event)));
		event.setDamage(0);
	}

}
