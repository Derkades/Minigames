package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import xyz.derkades.derkutils.Random;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class DigDug extends Game {

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
	
	private static final int ARENA_MIN_X = 130;
	private static final int ARENA_MIN_Y = 53;
	private static final int ARENA_MIN_Z = 67;
	
	private static final int ARENA_MAX_X = 169;
	private static final int ARENA_MAX_Y = 73;
	private static final int ARENA_MAX_Z = 103;
	
	private static final Location SPAWN_LOCATION = new Location(Var.WORLD, 149, 77.5, 86, 180, 90);
	
	private final Map<UUID, Integer> points = new HashMap<>();
	
	private int secondsLeft = 0;
	private Sidebar sidebar;
	
	@Override
	String[] getDescription() {
		return new String[] {
				"Dig away dirt with your shovel, and find",
				"ores. Right click on ores to collect.",
				ChatColor.GRAY + "Coal: " + COAL_POINTS + " points",
				ChatColor.GRAY + "Iron: " + IRON_POINTS + " points",
				ChatColor.GRAY + "Gold: " + GOLD_POINTS + " points",
				ChatColor.GRAY + "Emerald: " + EMERALD_POINTS + " points",
				ChatColor.GRAY + "Netherrack: give others blindness",
				ChatColor.GRAY + "Quartz: speed",
				};
	}

	@Override
	public String getName() {
		return "Dig Dug";
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public GamePoints getPoints() {
		return new GamePoints(3, 5);
	}

	@Override
	public void resetHashMaps(Player player) {
		points.put(player.getUniqueId(), 0);
	}

	@Override
	void begin() {
		secondsLeft = 65;
		
		fillArena();
		
		sidebar = new Sidebar(ChatColor.DARK_AQUA + "" + ChatColor.DARK_AQUA + "Score", Minigames.getInstance(), Integer.MAX_VALUE, new SidebarString[] {new SidebarString("test")});
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			sidebar.showTo(player);
			
			player.teleport(SPAWN_LOCATION);
			player.sendMessage(ChatColor.DARK_GREEN + "Digging will commence in 5 seconds!");
		}
		
		new BukkitRunnable() {
			public void run() {
				updateSidebar();
				
				secondsLeft--;
				
				if (secondsLeft == 0) {
					endGame();
					this.cancel();
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1*20);
		
		new BukkitRunnable() {
			public void run() {
				ItemStack shovel = new ItemBuilder(Material.DIAMOND_SPADE)
						.name(ChatColor.GREEN + "The Dig Dug Digger")
						.create();
				
				shovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
						
				net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(shovel);
				NBTTagCompound tag = nms.getTag();
				
				NBTTagList list = new NBTTagList();
				list.add(new NBTTagString("minecraft:dirt"));
				list.add(new NBTTagString("minecraft:grass"));
				
				tag.set("CanDestroy", list);
				tag.setBoolean("Unbreakable", true);
				
				shovel = CraftItemStack.asBukkitCopy(nms);
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.getInventory().addItem(shovel);
				}
			}
		}.runTaskLater(Minigames.getInstance(), 5*20);
		
		Utils.setGameRule("doTileDrops", false);
	}
	
	private void endGame() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			sidebar.hideFrom(player);
		}
		
		Utils.setGameRule("doTileDrops", true);
		
		super.startNextGame(Utils.getWinnersFromPointsHashmap(points));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!this.isRunning()) return;
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (block.getType() == Material.COAL_ORE) {
			addPoints(player, COAL_POINTS);
			block.setType(Material.AIR);
		} else if (block.getType() == Material.IRON_ORE) {
			addPoints(player, IRON_POINTS);
			block.setType(Material.AIR);
		} else if (block.getType() == Material.GOLD_ORE) {
			addPoints(player, GOLD_POINTS);
			block.setType(Material.AIR);
		} else if (block.getType() == Material.EMERALD_ORE) {
			addPoints(player, EMERALD_POINTS);
			block.setType(Material.AIR);
		} else if (block.getType() == Material.NETHERRACK) {
			player.sendMessage(ChatColor.RED + "Everyone is now blinded for " + NETHERRACK_EFFECT_TIME / 20 + " seconds.");
			PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, NETHERRACK_EFFECT_TIME, 0, true, false);
			PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, NETHERRACK_EFFECT_TIME, 1, true, false);
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (player.getUniqueId() != online.getUniqueId()) {
					online.addPotionEffects(Arrays.asList(blind, slow));
				}
			}
			block.setType(Material.AIR);
		} else if (block.getType() == Material.QUARTZ_BLOCK) {
			player.sendMessage(ChatColor.AQUA + "Your walking speed has been boosted for " + QUARTZ_EFFECT_TIME / 20 + " seconds.");
			PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, QUARTZ_EFFECT_TIME, 2, true, false);
			player.addPotionEffect(speed);
			block.setType(Material.AIR);
		}
	}
	
	private void addPoints(Player player, int pointsToAdd) {
		player.sendMessage(ChatColor.GREEN + "+ " + pointsToAdd + " points");
		points.put(player.getUniqueId(), points.get(player.getUniqueId()) + pointsToAdd);
	}
	
	private void updateSidebar() {
		List<SidebarString> sidebarStrings = new ArrayList<>();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			int points = this.points.get(player.getUniqueId());
			sidebarStrings.add(new SidebarString(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points));
		}
		
		sidebar.setEntries(sidebarStrings);
		sidebar.addEmpty().addEntry(new SidebarString(ChatColor.GRAY + "Time left: " + secondsLeft + " seconds."));
		sidebar.update();
	}
	
	private void fillArena() {
		Minigames.fillArea(ARENA_MIN_X, ARENA_MIN_Y, ARENA_MIN_Z, ARENA_MAX_X, ARENA_MAX_Y - 1, ARENA_MAX_Z, Material.DIRT);
		Minigames.fillArea(ARENA_MIN_X, ARENA_MAX_Y, ARENA_MIN_Z, ARENA_MAX_X, ARENA_MAX_Y, ARENA_MAX_Z, Material.GRASS); // Top grass layer

		for (int i = 0; i <= COAL_AMOUNT; i++) {
			int x = Random.getRandomInteger(ARENA_MIN_X, ARENA_MAX_X);
			int y = Random.getRandomInteger(ARENA_MIN_Y, ARENA_MAX_Y - 1);
			int z = Random.getRandomInteger(ARENA_MIN_Z, ARENA_MAX_Z);
			new Location(Var.WORLD, x, y, z).getBlock().setType(Material.COAL_ORE);
		}

		for (int i = 0; i <= IRON_AMOUNT; i++) {
			int x = Random.getRandomInteger(ARENA_MIN_X, ARENA_MAX_X);
			int y = Random.getRandomInteger(ARENA_MIN_Y, ARENA_MAX_Y - 1);
			int z = Random.getRandomInteger(ARENA_MIN_Z, ARENA_MAX_Z);
			new Location(Var.WORLD, x, y, z).getBlock().setType(Material.IRON_ORE);
		}

		for (int i = 0; i <= GOLD_AMOUNT; i++) {
			int x = Random.getRandomInteger(ARENA_MIN_X, ARENA_MAX_X);
			int y = Random.getRandomInteger(ARENA_MIN_Y, ARENA_MAX_Y - 1);
			int z = Random.getRandomInteger(ARENA_MIN_Z, ARENA_MAX_Z);
			new Location(Var.WORLD, x, y, z).getBlock().setType(Material.GOLD_ORE);
		}

		for (int i = 0; i <= EMERALD_AMOUNT; i++) {
			int x = Random.getRandomInteger(ARENA_MIN_X, ARENA_MAX_X);
			int y = Random.getRandomInteger(ARENA_MIN_Y, ARENA_MAX_Y - 1);
			int z = Random.getRandomInteger(ARENA_MIN_Z, ARENA_MAX_Z);
			new Location(Var.WORLD, x, y, z).getBlock().setType(Material.EMERALD_ORE);
		}

		for (int i = 0; i <= NETHERRACK_AMOUNT; i++) {
			int x = Random.getRandomInteger(ARENA_MIN_X, ARENA_MAX_X);
			int y = Random.getRandomInteger(ARENA_MIN_Y, ARENA_MAX_Y - 1);
			int z = Random.getRandomInteger(ARENA_MIN_Z, ARENA_MAX_Z);
			new Location(Var.WORLD, x, y, z).getBlock().setType(Material.NETHERRACK);
		}
		
		for (int i = 0; i <= QUARTZ_AMOUNT; i++) {
			int x = Random.getRandomInteger(ARENA_MIN_X, ARENA_MAX_X);
			int y = Random.getRandomInteger(ARENA_MIN_Y, ARENA_MAX_Y - 1);
			int z = Random.getRandomInteger(ARENA_MIN_Z, ARENA_MAX_Z);
			new Location(Var.WORLD, x, y, z).getBlock().setType(Material.QUARTZ_BLOCK);
		}
	}

}