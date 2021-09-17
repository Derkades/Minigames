package derkades.minigames.games.digdug;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

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

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.Leaderboard;
import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

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

	@Override
	public String getIdentifier() {
		return "dig_dug";
	}

	@Override
	public String getName() {
		return "Dig Dug";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Dig dirt with your shovel, and find",
				"ores. Left click on ores to collect.",
				ChatColor.GRAY + "Coal: " + COAL_POINTS + " points",
				ChatColor.GRAY + "Iron: " + IRON_POINTS + " points",
				ChatColor.GRAY + "Gold: " + GOLD_POINTS + " points",
				ChatColor.GRAY + "Emerald: " + EMERALD_POINTS + " points",
				ChatColor.GRAY + "Netherrack: give others blindness",
				ChatColor.GRAY + "Quartz: speed"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.IRON_SHOVEL;
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public DigDugMap[] getGameMaps() {
		return DigDugMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 40;
	}

	private Leaderboard leaderboard;

	@Override
	public void onPreStart() {
		this.leaderboard = new Leaderboard();

		fillArena();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.queueTeleport(this.map.getSpawnLocation());
		}
	}

	@Override
	public void onStart() {
		this.leaderboard.show();

		final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.name(ChatColor.GREEN + "The Dig Dug Digger")
				.unbreakable()
//				.canDestroy(Material.DIRT, Material.GRASS_BLOCK,
//						Material.COAL_ORE, Material.IRON_ORE,
//						Material.IRON_ORE, Material.GOLD_ORE,
//						Material.EMERALD_ORE, Material.NETHERRACK, Material.QUARTZ_BLOCK)
				.canDestroy("minecraft:dirt", "minecraft:grass_block", "minecraft:coal_ore",
						"minecraft:iron_ore", "minecraft:gold_ore", "minecraft:emerald_ore",
						"minecraft:netherrack", "minecraft:quartz_block")
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
		DigDug.this.endGame(this.leaderboard.getWinnersPrintHide());
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
				final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, NETHERRACK_EFFECT_TIME, 0, true, false);
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
				final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, QUARTZ_EFFECT_TIME, 2, true, false);
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
