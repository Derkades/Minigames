package derkades.minigames.games.hungergames;

import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.derkades.derkutils.bukkit.lootchests.LootChest;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class HungerGames extends Game<HungerGamesMap> {

	static final HungerGamesMap[] MAPS = {
			new Treehouse(),
			new Windmill(),
	};

	public HungerGames() {
		super(
				"hunger_games",
				"Hunger Games",
				new String[] {
						"Get weapons and armor from chests.",
						"Stay alive!"
				},
				Material.APPLE,
				new HungerGamesMap[] {
						new Treehouse(),
						new Windmill(),
				},
				4,
				150,
				EnumSet.of(GameLabel.MULTIPLAYER, GameLabel.PLAYER_COMBAT, GameLabel.LARGE_MAP, GameLabel.NO_TEAMS)
		);
	}

	private List<UUID> all; // TODO don't use all list
	private List<UUID> dead;
	private boolean pvpEnabled;

	@Override
	public void onPreStart() {
		this.all = Utils.getOnlinePlayersUuidList();
		this.dead = new ArrayList<>();
		this.pvpEnabled = false;

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
			this.pvpEnabled = true;
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

	private int playersLeft() {
		return (int) this.all.stream().filter(p -> !this.dead.contains(p)).count();
	}

	@Override
	public boolean endEarly() {
		return this.playersLeft() < 2;
	}

	@Override
	public void onEnd() {
		HungerGames.this.endGame(Winners.fromDead(HungerGames.this.dead, HungerGames.this.all, false));

		this.dead = null;
		this.all = null;
	}

	@EventHandler
	public void onDamage(MPlayerDamageEvent event) {
		event.setCancelled(!this.pvpEnabled);
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setCancelled(true);
		final MPlayer player = new MPlayer(event);

		this.dead.add(player.getUniqueId());
		player.dropItems();
		player.dieUp(2);

		this.sendMessage(Utils.getDeathMessage(event, this.playersLeft()));
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
