package derkades.minigames.games.hungergames;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.MinigamesPlayerDamageEvent;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.Winners;
import xyz.derkades.derkutils.bukkit.lootchests.LootChest;

public class HungerGames extends Game<HungerGamesMap> {

	@Override
	public String getIdentifier() {
		return "hunger_games";
	}

	@Override
	public String getName() {
		return "Hunger Games";
	}

	@Override
	public String getAlias() {
		return "hg";
	}

	@Override
	public String[] getDescription() {
		return  new String[] {
				"Get weapons and armor from chests.",
				"Stay alive!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.APPLE;
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

	private List<UUID> all; // TODO don't use all list
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

		Scheduler.delay(10*20, () -> {
			for (final MPlayer player : Minigames.getOnlinePlayers()) {
				player.setDisableDamage(false);
			}
			sendPlainMessage("PvP enabled");
		});
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (secondsLeft == 100) {
			final WorldBorder border = this.map.getWorld().getWorldBorder();
			border.setSize(this.map.getMinBorderSize(), 90);
		}

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return this.all.stream().filter(p -> this.dead.contains(p)).count() < 2;
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

			final MPlayer killer = event.getDamagerPlayer();
			if (killer != null) {
				final int playersLeft = (int) this.all.stream().filter(p -> this.dead.contains(p)).count();
				this.sendFormattedPlainMessage("%s has been killed by %s. There are %s players left.",
						player.getName(), killer.getName(), playersLeft);
			} else {
				final int playersLeft = (int) this.all.stream().filter(p -> this.dead.contains(p)).count();
				this.sendFormattedPlainMessage("%s has died. There are %s players left.",
						player.getName(), playersLeft);
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
