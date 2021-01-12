package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.lootchests.LootChest;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.hungergames.HungerGamesLoot;
import xyz.derkades.minigames.games.hungergames.HungerGamesMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.utils.Winners;

public class HungerGames extends Game<HungerGamesMap> {

	@Override
	public String getName() {
		return "Hunger Games";
	}

	@Override
	public String[] getDescription() {
		return  new String[] {
				"Get weapons and armor from chests.",
				"Stay alive!"
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 4;
	}

	@Override
	public HungerGamesMap[] getGameMaps() {
		return HungerGamesMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 150;
	}

	private List<UUID> all;
	private List<UUID> dead;

	@Override
	public void onPreStart() {
		this.all = Utils.getOnlinePlayersUuidList();
		this.dead = new ArrayList<>();

		final Location[] spawnLocations = this.map.getStartLocations();
		int index = 0;
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (index < 1) {
				index = spawnLocations.length - 1;
			}

			player.queueTeleport(spawnLocations[index]);
			index--;
		}

		this.placeBlocks(spawnLocations, Material.GLASS);

		for (final Location location : this.map.getLootLevelOneLocations()) {
			new LootChest(location, HungerGamesLoot.LOOT_1).fill();
		}

		for (final Location location : this.map.getLootLevelTwoLocations()) {
			new LootChest(location, HungerGamesLoot.LOOT_2).fill();
		}
		
		final WorldBorder border = this.map.getWorld().getWorldBorder();
		border.setCenter(this.map.getCenterLocation());
		border.setSize(this.map.getMinBorderSize());
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.clearPotionEffects();
			player.setDisableHunger(false);
			player.bukkit().setSaturation(0);
			player.bukkit().setFoodLevel(10);
			player.enableSneakPrevention(p -> p.bukkit().damage(1000));
			player.setDisableItemMoving(false);
		}

		HungerGames.this.placeBlocks(this.map.getStartLocations(), Material.AIR);
		
		final WorldBorder border = this.map.getWorld().getWorldBorder();
		border.setSize(this.map.getMaxBorderSize(), 20);
		
		this.map.getWorld().getEntitiesByClasses(Item.class).forEach(e -> e.remove());
		
		Scheduler.delay(10*20, () -> {
			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				player.setDisableDamage(false);
			}
			sendMessage("PvP enabled");
		});
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (ListUtils.inFirstNotSecond(this.all, this.dead).size() < 2 && secondsLeft > 10) {
			return 10;
		}
		
		if (secondsLeft == 100) {
			final WorldBorder border = this.map.getWorld().getWorldBorder();
			border.setSize(this.map.getMinBorderSize(), 90);
		}
		
		return secondsLeft;
	}

	@Override
	public void onEnd() {
		HungerGames.this.endGame(Winners.fromDead(HungerGames.this.dead, HungerGames.this.all, false));

		this.dead = null;
		this.all = null;
	}

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();

		if (event.willBeDead()) {
			event.setCancelled(true); // Player must not die, or bad things happen.

			this.dead.add(player.getUniqueId());
			player.dropItems();
			player.dieUp(2);

			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();

				if (killer == null) {
					Bukkit.broadcastMessage("error, killer unknown");
				}

				final int playersLeft = ListUtils.inFirstNotSecond(this.all, this.dead).size();
				this.sendMessage(String.format("%s has been killed by %s. There are %s players left.",
						player.getName(), killer.getName(), playersLeft));
			} else if (event.getType().equals(DamageType.SELF)) {
				final int playersLeft = ListUtils.inFirstNotSecond(this.all, this.dead).size();
				this.sendMessage(String.format("%s has died. There are %s players left.",
						player.getName(), playersLeft));
			} else {
				throw new AssertionError();
			}

		}
	}

	private void placeBlocks(final Location[] locations, final Material type) {
		for (final Location location : locations) {
			final Block block = location.getBlock();
			block.getRelative(BlockFace.NORTH).setType(type);
			block.getRelative(BlockFace.EAST).setType(type);
			block.getRelative(BlockFace.SOUTH).setType(type);
			block.getRelative(BlockFace.WEST).setType(type);
			block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(type);
			block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(type);
			block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(type);
			block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(type);
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.dieTo(this.map.getCenterLocation());
		this.dead.add(player.getUniqueId());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.all.remove(player.getUniqueId());
	}

}
