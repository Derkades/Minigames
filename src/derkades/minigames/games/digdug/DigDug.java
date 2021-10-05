package derkades.minigames.games.digdug;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.Leaderboard;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.derkades.derkutils.bukkit.BlockUtils;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

public class DigDug extends Game<DigDugMap> {

	private static final int COAL_AMOUNT = 100;
	private static final int IRON_AMOUNT = 90;
	private static final int GOLD_AMOUNT = 75;
	private static final int EMERALD_AMOUNT = 50;
	private static final int NETHERRACK_AMOUNT = 30;
	private static final int QUARTZ_AMOUNT = 40;

	private static final int COAL_POINTS = 1;
	private static final int IRON_POINTS = 2;
	private static final int GOLD_POINTS = 5;
	private static final int EMERALD_POINTS = 10;
	private static final int NETHERRACK_EFFECT_TIME = 5*20;
	private static final int QUARTZ_EFFECT_TIME = 10*20;

	public DigDug() {
		super(
				"dig_dug",
				"Dig Dug",
				new String[] {
						"Dig dirt with your shovel, and find",
						"ores. Left click on ores to collect.",
						ChatColor.GRAY + "Coal: " + COAL_POINTS + " points",
						ChatColor.GRAY + "Iron: " + IRON_POINTS + " points",
						ChatColor.GRAY + "Gold: " + GOLD_POINTS + " points",
						ChatColor.GRAY + "Emerald: " + EMERALD_POINTS + " points",
						ChatColor.GRAY + "Netherrack: give others blindness",
						ChatColor.GRAY + "Quartz: speed"
				},
				Material.IRON_SHOVEL,
				new DigDugMap[] {
						new Prototype(),
				},
				2,
				40,
				EnumSet.of(GameLabel.BLOCKS, GameLabel.SINGLEPLAYER, GameLabel.NO_TEAMS)
		);
	}

	private Leaderboard leaderboard;

	@Override
	public void onPreStart() {
		fillArena();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getSpawnLocation());
		}
	}

	@Override
	public void onStart() {
		this.leaderboard = new Leaderboard();

		final ItemStack shovel = new PaperItemBuilder(Material.DIAMOND_SHOVEL)
				.name(ChatColor.GREEN + "The Dig Dug Digger")
				.unbreakable()
				.canDestroyMinecraft(
						"dirt", 
						"grass_block", 
						"coal_ore",
						"iron_ore", 
						"gold_ore",
						"emerald_ore",
						"netherrack", 
						"quartz_block")
				.create();

		shovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);

		Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().addItem(shovel));
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		this.leaderboard.update(secondsLeft);
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false;
	}

	@Override
	public void onEnd() {
		DigDug.this.endGame(this.leaderboard.getWinnersAndUnregister());
		this.leaderboard = null;
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if ((event.getAction() != Action.LEFT_CLICK_BLOCK) || !event.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		final MPlayer player = new MPlayer(event);
		final Block block = event.getClickedBlock();
		switch(block.getType()) {
			case COAL_ORE -> {
				addPoints(player, COAL_POINTS);
				block.setType(Material.AIR);
				player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f);
			}
			case IRON_ORE -> {
				addPoints(player, IRON_POINTS);
				block.setType(Material.AIR);
				player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.2f);
			}
			case GOLD_ORE -> {
				addPoints(player, GOLD_POINTS);
				block.setType(Material.AIR);
				player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.35f);
			}
			case EMERALD_ORE -> {
				addPoints(player, EMERALD_POINTS);
				block.setType(Material.AIR);
				player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.5f);
			}
			case NETHERRACK -> {
				player.sendFormattedPlainActionBar("Everyone is now blinded for %s seconds.", NETHERRACK_EFFECT_TIME / 20);
				final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, NETHERRACK_EFFECT_TIME, 0, true, false); // TODO global field
				final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, NETHERRACK_EFFECT_TIME, 1, true, false);
				for (final Player online : Bukkit.getOnlinePlayers()) {
					if (player.getUniqueId() != online.getUniqueId()) {
						online.addPotionEffects(Arrays.asList(blind, slow));
					}
				}
				block.setType(Material.AIR);
				player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 0.5f);
			}
			case QUARTZ_BLOCK -> {
				player.sendFormattedPlainActionBar("Your walking speed and vision has been boosted for %s seconds.", QUARTZ_EFFECT_TIME / 20);
				final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, QUARTZ_EFFECT_TIME, 2, true, false); // TODO global field
				final PotionEffect vision = new PotionEffect(PotionEffectType.NIGHT_VISION, QUARTZ_EFFECT_TIME, 0, true, false);
				player.bukkit().addPotionEffect(speed);
				player.bukkit().addPotionEffect(vision);
				block.setType(Material.AIR);
				player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f);
			}
			default -> {}
		}
	}

	private void addPoints(final MPlayer player, final int pointsToAdd) {
		player.sendActionBar(Component.text("+ " + pointsToAdd + " points", NamedTextColor.GREEN));
		this.leaderboard.setScore(player, this.leaderboard.getScore(player) + pointsToAdd);
	}

	private void fillArena() {
		final int minX = this.map.getBlocksMinLocation().getBlockX();
		final int minY = this.map.getBlocksMinLocation().getBlockY();
		final int minZ = this.map.getBlocksMinLocation().getBlockZ();
		final int maxX = this.map.getBlocksMaxLocation().getBlockX();
		final int maxY = this.map.getBlocksMaxLocation().getBlockY();
		final int maxZ = this.map.getBlocksMaxLocation().getBlockZ();

		// Place dirt first, then fill with ores

		BlockUtils.fillArea(this.map.getWorld(), minX, minY, minZ, maxX, maxY - 1, maxZ, Material.DIRT);
		BlockUtils.fillArea(this.map.getWorld(), minX, maxY, minZ, maxX, maxY, maxZ, Material.GRASS_BLOCK); // Top grass layer

		placeOre(Material.COAL_ORE, COAL_AMOUNT, minX, maxX, minY, maxY, minZ, maxZ);
		placeOre(Material.IRON_ORE, IRON_AMOUNT, minX, maxX, minY, maxY, minZ, maxZ);
		placeOre(Material.GOLD_ORE, GOLD_AMOUNT, minX, maxX, minY, maxY, minZ, maxZ);
		placeOre(Material.EMERALD_ORE, EMERALD_AMOUNT, minX, maxX, minY, maxY, minZ, maxZ);
		placeOre(Material.NETHERRACK, NETHERRACK_AMOUNT, minX, maxX, minY, maxY, minZ, maxZ);
		placeOre(Material.QUARTZ_BLOCK, QUARTZ_AMOUNT, minX, maxX, minY, maxY, minZ, maxZ);
	}

	private void placeOre(final Material oreType, final int amount, final int minX, final int maxX, final int minY, final int maxY, final int minZ, final int maxZ) {
		for (int i = 0; i <= amount; i++) {
			final int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
			final int y = ThreadLocalRandom.current().nextInt(minY, maxY);
			final int z = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);
			new Location(this.map.getWorld(), x, y, z).getBlock().setType(oreType);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
