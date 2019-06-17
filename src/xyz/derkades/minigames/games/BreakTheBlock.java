package xyz.derkades.minigames.games;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Utils;

public class BreakTheBlock extends Game {

	BreakTheBlock() {
		super("Break the Block", new String[] {
				"Be the first player to break the gold block.",
				"You can hit players who are standing on red concrete.",
		}, 4, BreakTheBlockMap.MAPS);
	}

	private static final int DURATION = 200;
	private static final int PRE_START = 10;

	private BreakTheBlockMap map;

	private UUID blockBreaker;

	@Override
	void begin(final GameMap genericMap) {
		this.map = (BreakTheBlockMap) genericMap;
		this.blockBreaker = null;

		this.map.onPreStart();

		final ItemStack pickaxe = new ItemBuilder(Material.IRON_PICKAXE)
				.unbreakable()
				.name(ChatColor.GOLD + "Block breaker")
				.lore(ChatColor.YELLOW + "Use this gold pickaxe to break the ", ChatColor.YELLOW + "gold block at the end of the game.")
				.canDestroy("gold_block")
				.create();



		Bukkit.getOnlinePlayers().forEach((player) -> {
			player.getInventory().addItem(pickaxe);

			player.teleport(this.map.getStartLocation());

			Utils.giveInfiniteEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 10);
		});

		new GameTimer(this, DURATION, PRE_START) {

			@Override
			public void onStart() {
				BreakTheBlock.this.map.onStart();
				Bukkit.getOnlinePlayers().forEach((player) -> {
					Minigames.setCanTakeDamage(player, true);
					Utils.giveInfiniteEffect(player, PotionEffectType.SLOW_DIGGING, 1);
				});

			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (BreakTheBlock.this.blockBreaker != null && secondsLeft > 3) {
					return 3;
				}

				BreakTheBlock.this.map.timer();

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				final Player winner = Bukkit.getPlayer(BreakTheBlock.this.blockBreaker);
				if (winner != null) {
					BreakTheBlock.this.endGame(winner);
				} else {
					BreakTheBlock.this.endGame();
				}
			}

		};

	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();

		if (player.getLocation().getBlock().getType() == Material.WATER){
			player.teleport(this.map.getStartLocation());
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
			Utils.setArmor(player, null, elytra, null, null);
		}
	}

	@EventHandler
	public void blockBreakEvent(final BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.GOLD_BLOCK) {
			return;
		}

		this.sendMessage(event.getPlayer().getName() + " has broken the block!");

		this.blockBreaker = event.getPlayer().getUniqueId();

		Utils.particle(Particle.EXPLOSION_HUGE, event.getBlock().getLocation(), 1);
		Utils.playSoundForAllPlayers(Sound.ENTITY_GENERIC_EXPLODE, 1.0f);
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
