package derkades.minigames.games.harvest;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameLabel;
import derkades.minigames.utils.Leaderboard;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.PaperItemBuilder;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Harvest extends Game<@NotNull HarvestMap> {

	private static final int CROPS_PER_SECOND = 30;
	private static final int RESPAWN_DELAY = 3*20;

	private static final ItemStack[] ITEMS = {
			new PaperItemBuilder(Material.IRON_HOE)
					.unbreakable()
					.canDestroyMinecraft("wheat")
					.create(),
			new ItemBuilder(Material.WOODEN_SWORD)
					.unbreakable()
					.create()
	};

	public Harvest() {
		super(
				"harvest",
				"Harvest",
				new String[]{
						"Harvest crops to get as much wheat as possible. Make",
						"sure to not break any crops that are not fully grown!"
				},
				Material.WHEAT,
				new HarvestMap[]{
						new Prototype(),
				},
				2,
				50,
				EnumSet.of(GameLabel.BLOCKS, GameLabel.NO_TEAMS, GameLabel.SINGLEPLAYER)
		);
	}

	private List<Location> blocks;
	private Leaderboard leaderboard;

	@Override
	public void onPreStart() {
		this.blocks = this.map.getCropLocations();

		this.leaderboard = new Leaderboard();

		this.map.getWorld().setGameRule(GameRule.DO_TILE_DROPS, true);
		this.map.getWorld().getEntitiesByClass(Item.class).forEach(Item::remove);

		Minigames.getOnlinePlayers().forEach(p -> p.queueTeleport(this.map.getSpawnLocation()));
	}

	private void giveItems(final MPlayer player) {
		player.clearInventory();
		player.giveItem(ITEMS);
	}

	private void updateLeaderboard(final int secondsLeft) {
		Minigames.getOnlinePlayers().forEach(p -> {
			final int amount = Arrays.stream(p.getInventory().getContents()).filter(Objects::nonNull).filter(i -> i.getType() == Material.WHEAT).mapToInt(ItemStack::getAmount).sum();
			this.leaderboard.setScore(p, amount);
		});
		this.leaderboard.update(secondsLeft);
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach(this::giveItems);
		this.leaderboard.show();
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
		Minigames.getOnlinePlayers().forEach(p -> p.getInventory().remove(Material.WHEAT_SEEDS));

		for (int i = 0; i < CROPS_PER_SECOND; i++) {
			tick(ListUtils.choice(this.blocks));
		}

		updateLeaderboard(secondsLeft);

		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false;
	}

	@Override
	public void onEnd() {
		endGame(this.leaderboard.getWinnersPrintHide());
		this.blocks = null;
		this.leaderboard = null;
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		player.teleport(this.map.getSpawnLocation());
		giveItems(player);
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		event.setCancelled(true);
		final MPlayer player = new MPlayer(event);

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
		player.sendChat(Component.text("This crop was not fully grown yet! You lost one wheat.", NamedTextColor.RED));
		player.sendTitle(Component.empty(), Component.text("-1", NamedTextColor.RED));
		player.getInventory().removeItem(new ItemStack(Material.WHEAT, 1));
	}

}
