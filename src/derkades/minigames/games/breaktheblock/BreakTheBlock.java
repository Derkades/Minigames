package derkades.minigames.games.breaktheblock;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MPlayerDamageEvent;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.EnumSet;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class BreakTheBlock extends Game<BreakTheBlockMap> {

	private static final ItemStack PICKAXE = new ItemBuilder(Material.IRON_PICKAXE)
			.unbreakable()
			.name(text("Block breaker", GOLD))
			.lore(text("Use this gold pickaxe to break the ", YELLOW), text("gold block at the end of the game.", YELLOW))
			.canDestroyMinecraft(
					"gold_block",
					"red_glazed_terracotta" // cherry in cake map
					)
			.create();

	private static final int MINIMUM_PLAYERS_FOR_MULTIPLE_SPAWN_LOCATIONS = 4;

	public BreakTheBlock() {
		super(
				"break_the_block",
				"Gold Rush",
				new String[]{
						"Make it through an obstacle course to break the gold block.",
				},
				Material.GOLD_BLOCK,
				new BreakTheBlockMap[]{
						new Cake(),
						new HollowHills(),
						new Prototype(),
				},
				3,
				200,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT, GameLabel.PARKOUR, GameLabel.NO_TEAMS)
		);
	}

	private UUID blockBreaker;

	@Override
	public void onPreStart() {
		this.blockBreaker = null;

		if (Minigames.getOnlinePlayerCount() >= MINIMUM_PLAYERS_FOR_MULTIPLE_SPAWN_LOCATIONS) {
			int i = 0;
			for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
				player.queueTeleport(this.map.getStartLocations()[i]);

				i++;
				if (i >= this.map.getStartLocations().length) {
					i = 0;
				}
			}
		} else {
			Minigames.getOnlinePlayers().forEach(p -> p.queueTeleport(this.map.getStartLocations()[0]));
		}
	}

	private static final PotionEffect INFINITE_SLOW_DIGGING = new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 1, true, false);

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach((player) -> {
//			player.enableSneakPrevention(p -> {
//				p.clearInventory();
//				p.teleport(this.map.getStartLocation());
//				p.giveItem(PICKAXE);
//			});
			player.giveItem(PICKAXE);
			player.giveEffect(INFINITE_SLOW_DIGGING);
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
		if (Minigames.getOnlinePlayerCount() > MINIMUM_PLAYERS_FOR_MULTIPLE_SPAWN_LOCATIONS) {
			player.teleport(ListUtils.choice(this.map.getStartLocations()));
		} else {
			player.teleport(this.map.getStartLocations()[0]);
		}
		player.giveItem(PICKAXE);
//		player.enableSneakPrevention(p -> {
//			p.clearInventory();
//			p.teleport(this.map.getStartLocation());
//		});
		player.giveEffect(INFINITE_SLOW_DIGGING);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

	private static final PotionEffect SLIME_BOOST = new PotionEffect(PotionEffectType.JUMP, 20, 6, true, false);
	private static final ItemStack BOW = new ItemBuilder(Material.BOW)
			.unbreakable()
			.enchant(Enchantment.ARROW_INFINITE)
			.create();
	private static final ItemStack ARROW = new ItemStack(Material.ARROW);

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final MPlayer player = new MPlayer(event);

		if (player.getLocation().getY() < this.map.getMinimumY()){
			if (Minigames.getOnlinePlayerCount() > MINIMUM_PLAYERS_FOR_MULTIPLE_SPAWN_LOCATIONS) {
				player.teleport(ListUtils.choice(this.map.getStartLocations()));
			} else {
				player.teleport(this.map.getStartLocations()[0]);
			}
			player.clearInventory();
			player.giveItem(PICKAXE);
			return;
		}

		final PlayerInventory inv = player.getInventory();

		final Block blockOn = player.getBlockOn();

		if (blockOn.getType().equals(Material.ORANGE_CONCRETE) &&
				!inv.contains(Material.FIREWORK_ROCKET)) {
			inv.addItem(new ItemBuilder(Material.FIREWORK_ROCKET).name(text("Elytra fireworks", LIGHT_PURPLE)).create());
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

		ItemStack secondSlot = player.getInventory().getItem(1);

		if (blockOn.getType() == Material.MAGENTA_CONCRETE) {
			if (secondSlot == null ||
					secondSlot.getType() == Material.AIR) {
				player.getInventory().setHeldItemSlot(1);
				player.getInventory().setItem(1, BOW);
				player.getInventory().setItem(9, ARROW);
			}
		} else if (secondSlot != null &&
				secondSlot.getType() == Material.BOW) {
			player.getInventory().setItem(1, null);
			player.getInventory().setItem(9, null);
		}

		if (player.bukkit().hasPotionEffect(PotionEffectType.JUMP) && player.getBlockOn().getType().isAir()) {
			player.bukkit().removePotionEffect(PotionEffectType.JUMP);
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
	public void onDamage(final MPlayerDamageEvent event) {
		MPlayer player = event.getPlayer();
		event.setCancelled(!this.map.canTakeDamage(player));
		event.setDamage(0);
	}

}
