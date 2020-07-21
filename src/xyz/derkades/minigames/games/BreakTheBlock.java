package xyz.derkades.minigames.games;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.breaktheblock.BreakTheBlockMap;
import xyz.derkades.minigames.utils.MPlayer;

public class BreakTheBlock extends Game<BreakTheBlockMap> {

	private static final ItemStack PICKAXE = new ItemBuilder(Material.IRON_PICKAXE)
			.unbreakable()
			.name(ChatColor.GOLD + "Block breaker")
			.lore(ChatColor.YELLOW + "Use this gold pickaxe to break the ", ChatColor.YELLOW + "gold block at the end of the game.")
//			.canDestroy("gold_block")
			.canDestory(Material.GOLD_BLOCK)
			.create();

	@Override
	public String getName() {
		return "Break the Block";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Be the first player to break the gold block.",
				"You can hit players who are standing on red concrete."
				};
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
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
			player.enableSneakPrevention(p -> {
				p.clearInventory();
				p.teleport(this.map.getStartLocation());
			});
			player.giveInfiniteEffect(PotionEffectType.SLOW_DIGGING, 1);
		});

	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (BreakTheBlock.this.blockBreaker != null && secondsLeft > 5) {
			Minigames.getOnlinePlayers().forEach(MPlayer::disableSneakPrevention);

			return 5;
		}

		return secondsLeft;
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
		player.enableSneakPrevention(p -> {
			p.clearInventory();
			p.teleport(this.map.getStartLocation());
		});
		player.giveInfiniteEffect(PotionEffectType.SLOW_DIGGING, 1);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final MPlayer player = new MPlayer(event);

		if (player.getBlockIn().getType() == Material.WATER){
			player.teleport(this.map.getStartLocation());
			player.clearInventory();
			return;
		}

		final PlayerInventory inv = player.getInventory();
		
		if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.ORANGE_CONCRETE) &&
				!inv.contains(Material.FIREWORK_ROCKET)) {
			inv.addItem(new ItemBuilder(Material.FIREWORK_ROCKET).name(ChatColor.LIGHT_PURPLE + "Elytra fireworks").create());
		}

		if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.PINK_CONCRETE) &&
				!inv.contains(Material.ELYTRA)) {
			final ItemStack elytra = new ItemBuilder(Material.ELYTRA)
					.unbreakable()
					.create();
			player.setArmor(null, elytra, null, null);
		}
	}

	@EventHandler
	public void blockBreakEvent(final BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.GOLD_BLOCK) {
			return;
		}

		sendMessage(event.getPlayer().getName() + " has broken the block!");

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

		final Block blockBelow = event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN);
		event.setCancelled(!(blockBelow.getType() == Material.RED_CONCRETE || blockBelow.getRelative(BlockFace.DOWN).getType() == Material.RED_CONCRETE));
	}

}
