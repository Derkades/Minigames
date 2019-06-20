package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.lootchests.LootChest;
import xyz.derkades.derkutils.bukkit.lootchests.LootItem;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.SneakPrevention;
import xyz.derkades.minigames.Spectator;
import xyz.derkades.minigames.games.hungergames.HungerGamesMap;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Utils;

public class HungerGames extends Game {

	private static final int DURATION = 200;
	private static final int PRE_START = 10;

	private static final LootItem[] LOOT_1 = {
			new LootItem(Material.COOKED_CHICKEN, 1, 5, 0.3f),
			new LootItem(Material.STONE_SWORD, 0.5f),
			new LootItem(Material.GOLDEN_SWORD, 0.3f),
			new LootItem(Material.LEATHER_HELMET, 0.8f),
			new LootItem(Material.CHAINMAIL_CHESTPLATE, 0.6f),
			new LootItem(Material.MELON_SLICE, 3, 15, 0.6f),
			new LootItem(Material.ARROW, 1, 6, 0.5f),
	};

	private static final LootItem[] LOOT_2 = {
			new LootItem(Material.COOKED_BEEF, 1, 3, 0.7f),
			new LootItem(Material.IRON_SWORD, 0.6f),
			new LootItem(Material.DIAMOND_BOOTS, 0.4f),
			new LootItem(Material.BOW, 0.8f),
			new LootItem(new ItemBuilder(Material.LEATHER_HELMET).leatherArmorColor(Color.FUCHSIA).name("Stylish Helmet").lore("Makes you look cool but die just as quickly.").create(), 0.5f),
	};

	HungerGames() {
		super("Hunger Games", new String[] {
				"Get weapons and armor from chests.",
				"Stay alive!"
		}, 4, HungerGamesMap.MAPS);
	}

	private List<UUID> all;
	private List<UUID> dead;
	private HungerGamesMap map;

	@Override
	void begin(final GameMap genericMap) {
		this.map = (HungerGamesMap) genericMap;
		this.all = Utils.getOnlinePlayersUuidList();
		this.dead = new ArrayList<>();

		final Location[] spawnLocations = this.map.getStartLocations();
		int index = 0;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}

			player.teleport(spawnLocations[index]);
			index--;
		}

		this.placeBlocks(spawnLocations, Material.GLASS);

		for (final Player player : Bukkit.getOnlinePlayers()) {
			//Utils.giveInfiniteEffect(player, PotionEffectType.SLOW, 100);
			Utils.giveInfiniteEffect(player, PotionEffectType.JUMP, 200);
		}

		for (final Location location : this.map.getLootLevelOneLocations()) {
			new LootChest(location, LOOT_1).fill();
		}

		for (final Location location : this.map.getLootLevelTwoLocations()) {
			new LootChest(location, LOOT_2).fill();
		}

		new GameTimer(this, DURATION, PRE_START) {

			@Override
			public void onStart() {
				for (final Player player : Bukkit.getOnlinePlayers()) {
					Utils.clearPotionEffects(player);
					Minigames.setCanTakeDamage(player, true);
					SneakPrevention.setCanSneak(player, false);
					Minigames.CAN_MOVE_ITEMS.add(player.getUniqueId());
				}

				HungerGames.this.placeBlocks(spawnLocations, Material.AIR);
			}

			@Override
			public int gameTimer(final int secondsLeft) {
				if (Utils.getAliveAcountFromDeadAndAllList(HungerGames.this.dead, HungerGames.this.all) < 2 && secondsLeft > 10) {
					return 10;
				}

				return secondsLeft;
			}

			@Override
			public void onEnd() {
				HungerGames.this.endGame(Utils.getWinnersFromDeadAndAllList(HungerGames.this.dead, HungerGames.this.all, false));
				HungerGames.this.dead.clear();
				HungerGames.this.all.clear();
			}

		};
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final Player player = event.getPlayer();

		if (event.willBeDead()) {
			event.setCancelled(true); // Player must not die, or bad things happen.

			this.dead.add(player.getUniqueId());
			Spectator.dieUp(player, 2);

			if (event.getType().equals(DamageType.SELF)) {
				final Player killer = event.getDamagerPlayer();

				if (killer == null) {
					Bukkit.broadcastMessage("error, killer unknown");
				}

				final int playersLeft = Utils.getAliveAcountFromDeadAndAllList(this.dead, this.all);
				this.sendMessage(String.format("%s has been killed by %s. There are %s players left.",
						player.getName(), killer.getName(), playersLeft));
			} else if (event.getType().equals(DamageType.ENTITY)) {
				final int playersLeft = Utils.getAliveAcountFromDeadAndAllList(this.dead, this.all);
				this.sendMessage(String.format("%s has died. There are %s players left.",
						player.getName(), playersLeft));
			} else {
				throw new AssertionError();
			}

		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		this.all.remove(event.getPlayer().getUniqueId());
	}

	private void placeBlocks(final Location[] locations, final Material type) {
		for (final Location location : locations) {
			final Block block = location.getBlock();
			block.getRelative(BlockFace.NORTH).setType(type);
			block.getRelative(BlockFace.EAST).setType(type);
			block.getRelative(BlockFace.SOUTH).setType(type);
			block.getRelative(BlockFace.WEST).setType(type);
		}
	}

}
