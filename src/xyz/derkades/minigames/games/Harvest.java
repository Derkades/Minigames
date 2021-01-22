package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.harvest.HarvestMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class Harvest extends Game<HarvestMap> {

	private static final int CROPS_PER_SECOND = 30;
	private static final int RESPAWN_DELAY = 3*20;
	
	private static final ItemStack[] ITEMS = {
			new ItemBuilder(Material.IRON_HOE)
					.unbreakable()
//					.canDestroy("minecraft:wheat")
					.canDestroy(Material.WHEAT)
					.create(),
			new ItemBuilder(Material.WOODEN_SWORD)
					.unbreakable()
					.create()
	};
	
	@Override
	public String getIdentifier() {
		return "harvest";
	}
	
	@Override
	public String getName() {
		return "Harvest";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Harvest crops to get as much wheat as possible. Make",
				"sure to not break any crops that are not fully grown!"
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public HarvestMap[] getGameMaps() {
		return HarvestMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 50;
	}
	
	private List<Location> blocks;
	private Sidebar sidebar;

	@Override
	public void onPreStart() {
		this.blocks = this.map.getCropLocations();
		
		this.sidebar = new Sidebar(ChatColor.DARK_AQUA + "" + ChatColor.DARK_AQUA + "Score",
				Minigames.getInstance(), Integer.MAX_VALUE, new SidebarString(".."));
		
		this.map.getWorld().setGameRule(GameRule.DO_TILE_DROPS, true);
		this.map.getWorld().getEntitiesByClass(Item.class).forEach(Item::remove);
		
		Minigames.getOnlinePlayers().forEach(p -> p.queueTeleport(this.map.getSpawnLocation()));
	}
	
	private void giveItems(final MPlayer player) {
		player.clearInventory();
		player.giveItem(ITEMS);
	}
	
	private Map<MPlayer, Integer> getSortedPointsMap() {
		final Map<MPlayer, Integer> pointsMap = new HashMap<>();
		
		Minigames.getOnlinePlayers().forEach(p -> {
			final int amount = Arrays.stream(p.getInventory().getContents()).filter(i -> i != null).filter(i -> i.getType() == Material.WHEAT).mapToInt(ItemStack::getAmount).sum();
			pointsMap.put(p, amount);
		});
		
		return Utils.sortByValue(pointsMap);
	}
	
	private void updateSidebar(final int secondsLeft) {
		final List<SidebarString> sidebarStrings = new ArrayList<>();
		
		getSortedPointsMap().forEach((player, points) -> {
			sidebarStrings.add(new SidebarString(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points));
		});

		this.sidebar.setEntries(sidebarStrings);
		this.sidebar.addEmpty().addEntry(new SidebarString(ChatColor.GRAY + "Time left: " + secondsLeft + " seconds."));
		this.sidebar.update();
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach(p -> {
			giveItems(p);
//			p.setDisableDamage(false);
			this.sidebar.showTo(p.bukkit());
		});
	}

	private void tick(final Location loc) {
		final Block block = loc.getBlock();
		
		if (block.getType() == Material.AIR) {
			block.setType(Material.WHEAT);
		} else if (block.getType() == Material.WHEAT) {
			final Ageable ageable = (Ageable) block.getBlockData();
			final int age = ageable.getAge();
			if (age < ageable.getMaximumAge()) {
				ageable.setAge(age + 1);
				block.setBlockData(ageable);
			}
		} else {
			Logger.warning("Invalid block at (%s, %s, %s)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		}
	}
	
	@Override
	public int gameTimer(final int secondsLeft) {
		Minigames.getOnlinePlayers().forEach(p -> {
			p.getInventory().remove(Material.WHEAT_SEEDS);
		});
		
		for (int i = 0; i < CROPS_PER_SECOND; i++) {
			tick(ListUtils.getRandomValueFromList(this.blocks));
		}
		
		updateSidebar(secondsLeft);
		
		return secondsLeft;
	}

	@Override
	public void onEnd() {
		final Map<MPlayer, Integer> sorted = getSortedPointsMap();
		final AtomicInteger i = new AtomicInteger();
		sorted.forEach((player, points) -> {
			if (i.getAndIncrement() > 2) {
				return;
			}
			
			sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + points);
		});
		
		final List<UUID> winners = Utils.getHighestValuesFromHashMap(getSortedPointsMap()).stream()
				.map(MPlayer::getUniqueId).collect(Collectors.toList());
		endGame(winners);
		Bukkit.getOnlinePlayers().forEach(this.sidebar::hideFrom);
		this.blocks = null;
		this.sidebar = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getSpawnLocation());
		giveItems(player);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {}
	
	@EventHandler
	public void damage(final MinigamesPlayerDamageEvent event) {
		if (event.willBeDead()) {
			event.setCancelled(true);
			final MPlayer player = event.getPlayer();
			
			for (final ItemStack item : player.getInventory()) {
				if (item != null && item.getType() == Material.WHEAT) {
					this.map.getSpawnLocation().getWorld().dropItemNaturally(player.getLocation(), item);
				}
			}
			
			player.spectator();
			final UUID uuid = player.getUniqueId();
			Scheduler.delay(RESPAWN_DELAY, () -> {
				final MPlayer player2 = Minigames.getPlayer(uuid);
				if (player2 == null) {
					return;
				}
				
				player2.heal();
				player2.setGameMode(GameMode.ADVENTURE);
				giveItems(player2);
				player2.teleport(this.map.getSpawnLocation());
			});
		}
	}
	
	@EventHandler
	public void onBreak(final BlockBreakEvent event) {
		final Block block = event.getBlock();
		if (block.getType() != Material.WHEAT) {
			return;
		}
		
		final Ageable ageable = (Ageable) block.getBlockData();
		if (ageable.getAge() == ageable.getMaximumAge()) {
			return;
		}
		
		final MPlayer player = new MPlayer(event.getPlayer());
		player.sendChat(ChatColor.RED + "This crop was not fully grown yet! You lose one wheat.");
		player.sendTitle("", ChatColor.RED + "-1");
		player.getInventory().removeItem(new ItemStack(Material.WHEAT, 1));
	}

}
