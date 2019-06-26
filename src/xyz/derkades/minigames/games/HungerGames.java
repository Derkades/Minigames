package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.derkades.derkutils.bukkit.lootchests.LootChest;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.hungergames.HungerGamesLoot;
import xyz.derkades.minigames.games.hungergames.HungerGamesMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent;
import xyz.derkades.minigames.utils.MinigamesPlayerDamageEvent.DamageType;
import xyz.derkades.minigames.utils.Utils;

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
		return 5;
	}

	@Override
	public HungerGamesMap[] getGameMaps() {
		return HungerGamesMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 200;
	}

	private List<UUID> all;
	private List<UUID> dead;

	@Override
	public void onPreStart() {
		this.all = Utils.getOnlinePlayersUuidList();
		this.dead = new ArrayList<>();

		this.map.getWorld().setGameRule(GameRule.KEEP_INVENTORY, false);

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
	}

	@Override
	public void onStart() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			player.clearPotionEffects();
			player.setDisableSneaking(true);
			player.setDisableDamage(false);
			player.setDisableItemMoving(false);
		}

		HungerGames.this.placeBlocks(this.map.getStartLocations(), Material.AIR);
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

	@EventHandler
	public void onDamage(final MinigamesPlayerDamageEvent event) {
		final MPlayer player = event.getPlayer();

		if (event.willBeDead()) {
			event.setCancelled(true); // Player must not die, or bad things happen.

			this.dead.add(player.getUniqueId());
			player.dieUp(2);

			if (event.getType().equals(DamageType.ENTITY)) {
				final MPlayer killer = event.getDamagerPlayer();

				if (killer == null) {
					Bukkit.broadcastMessage("error, killer unknown");
				}

				final int playersLeft = Utils.getAliveAcountFromDeadAndAllList(this.dead, this.all);
				this.sendMessage(String.format("%s has been killed by %s. There are %s players left.",
						player.getName(), killer.getName(), playersLeft));
			} else if (event.getType().equals(DamageType.SELF)) {
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
			block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(type);
			block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(type);
			block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(type);
			block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(type);
		}
	}

}
