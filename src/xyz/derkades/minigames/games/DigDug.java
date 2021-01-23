package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Random;
import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.digdug.DigDugMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.utils.Winners;

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

	private Map<UUID, Integer> points;
	private Sidebar sidebar;

	@Override
	public void onPreStart() {
		this.points = new HashMap<>();
		this.sidebar = new Sidebar(ChatColor.DARK_AQUA + "" + ChatColor.DARK_AQUA + "Score",
				Minigames.getInstance(), Integer.MAX_VALUE, new SidebarString("Loading..."));

		fillArena();

		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			this.points.put(player.getUniqueId(), 0);
			this.sidebar.showTo(player.bukkit());
			player.queueTeleport(this.map.getSpawnLocation());
		}
	}

	@Override
	public void onStart() {
		final ItemStack shovel = new ItemBuilder(Material.DIAMOND_SHOVEL)
				.name(ChatColor.GREEN + "The Dig Dug Digger")
				.unbreakable()
				.canDestroy(Material.DIRT, Material.GRASS_BLOCK,
						Material.COAL_ORE, Material.IRON_ORE,
						Material.IRON_ORE, Material.GOLD_ORE,
						Material.EMERALD_ORE, Material.NETHERRACK, Material.QUARTZ_BLOCK)
				.create();

		shovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);

		Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().addItem(shovel));
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		DigDug.this.updateSidebar(secondsLeft);
		return secondsLeft;
	}

	@Override
	public void onEnd() {
		final Map<UUID, Integer> sorted = Utils.sortByValue(this.points);
		final AtomicInteger i = new AtomicInteger();
		sorted.forEach((uuid, points) -> {
			if (i.getAndIncrement() > 2) {
				return;
			}
			
			final Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				return;
			}
			
			sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points);
		});
		
		DigDug.this.endGame(Winners.fromPointsMap(DigDug.this.points));
		DigDug.this.points = null;
		Bukkit.getOnlinePlayers().forEach(DigDug.this.sidebar::hideFrom);
		this.sidebar = null;
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
			return;
		}

		if (!event.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		final MPlayer player = new MPlayer(event);
		final Block block = event.getClickedBlock();
		if (block.getType() == Material.COAL_ORE) {
			addPoints(player, COAL_POINTS);
			block.setType(Material.AIR);
			player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f);
		} else if (block.getType() == Material.IRON_ORE) {
			addPoints(player, IRON_POINTS);
			block.setType(Material.AIR);
			player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.2f);
		} else if (block.getType() == Material.GOLD_ORE) {
			addPoints(player, GOLD_POINTS);
			block.setType(Material.AIR);
			player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.35f);
		} else if (block.getType() == Material.EMERALD_ORE) {
			addPoints(player, EMERALD_POINTS);
			block.setType(Material.AIR);
			player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.5f);
		} else if (block.getType() == Material.NETHERRACK) {
			player.sendActionBar(ChatColor.RED + "Everyone is now blinded for " + NETHERRACK_EFFECT_TIME / 20 + " seconds.");
			final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, NETHERRACK_EFFECT_TIME, 0, true, false);
			final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, NETHERRACK_EFFECT_TIME, 1, true, false);
			for (final Player online : Bukkit.getOnlinePlayers()) {
				if (player.getUniqueId() != online.getUniqueId()) {
					online.addPotionEffects(Arrays.asList(blind, slow));
				}
			}
			block.setType(Material.AIR);
			player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 0.5f);
		} else if (block.getType() == Material.QUARTZ_BLOCK) {
			player.sendActionBar(ChatColor.AQUA + "Your walking speed and vision has been boosted for " + QUARTZ_EFFECT_TIME / 20 + " seconds.");
			final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, QUARTZ_EFFECT_TIME, 2, true, false);
			final PotionEffect vision = new PotionEffect(PotionEffectType.NIGHT_VISION, QUARTZ_EFFECT_TIME, 0, true, false);
			player.bukkit().addPotionEffect(speed);
			player.bukkit().addPotionEffect(vision);
			block.setType(Material.AIR);
			player.playSound(Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f);
		}
	}

	private void addPoints(final MPlayer player, final int pointsToAdd) {
		player.sendActionBar(ChatColor.GREEN + "+ " + pointsToAdd + " points");
		this.points.put(player.getUniqueId(), this.points.get(player.getUniqueId()) + pointsToAdd);
	}

	private void updateSidebar(final int secondsLeft) {
		final List<SidebarString> sidebarStrings = new ArrayList<>();

		final Map<UUID, Integer> sorted = Utils.sortByValue(this.points);
		
		sorted.forEach((uuid, points) -> {
			final Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				return;
			}
			sidebarStrings.add(new SidebarString(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points));
		});

		this.sidebar.setEntries(sidebarStrings);
		this.sidebar.addEmpty().addEntry(new SidebarString(ChatColor.GRAY + "Time left: " + secondsLeft + " seconds."));
		this.sidebar.update();
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
			final int x = Random.getRandomInteger(minX, maxX);
			final int y = Random.getRandomInteger(minY, maxY - 1);
			final int z = Random.getRandomInteger(minZ, maxZ);
			new Location(this.map.getWorld(), x, y, z).getBlock().setType(oreType);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		this.points.put(player.getUniqueId(), 0);
		player.teleport(this.map.getSpawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {

	}

}
