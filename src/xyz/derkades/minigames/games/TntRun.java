package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.games.tntrun.TNTMap;
import xyz.derkades.minigames.utils.Utils;

public class TntRun extends Game {

	TntRun() {
		super("TNT Run", new String[]{
				"The floor disappears where you walk. Avoid",
				"falling for as long as possible.",
		}, 2, TNTMap.MAPS);
	}

	private TNTMap map;

	private BukkitTask task;
	private List<UUID> alive;
	private List<Block> removedBlocks;

	boolean removeBlocks;

	@Override
	void begin(final GameMap genericMap) {
		this.alive = new ArrayList<>();
		this.removedBlocks = new ArrayList<>();
		this.removeBlocks = false;

		this.map = (TNTMap) genericMap;

		this.map.restore();

		Bukkit.getOnlinePlayers().forEach((player) -> {
			player.teleport(this.map.spawnLocation());
			this.alive.add(player.getUniqueId());
		});

		this.task = Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), () -> {
			if (this.alive.size() <= 1) {
				this.finish();
			}
		}, 20, 20);

		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> this.removeBlocks = true, 4*20);
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (!this.removeBlocks) {
			return;
		}

		final Player player = event.getPlayer();

		if (!this.alive.contains(player.getUniqueId())) {
			return;
		}

		final Block belowPlayer = event.getFrom().getBlock().getRelative(BlockFace.DOWN);

		if (belowPlayer.getType().equals(Material.RED_TERRACOTTA)) {
			player.teleport(this.map.spawnLocation());
			player.setAllowFlight(true);
			player.setFlying(true);
			Bukkit.getOnlinePlayers().forEach((online) -> online.hidePlayer(Minigames.getInstance(), player));
			this.alive.remove(player.getUniqueId());
			this.sendMessage(player.getName() + " has died. " + this.alive.size() + " players left.");
			return;
		}

		final List<Block> blocks = new ArrayList<>();

		final Location loc = player.getLocation();
		loc.setY(loc.getY() - 1);

		final double destroyRange = .4;

		blocks.add(new Location(loc.getWorld(), loc.getX() + destroyRange, loc.getY(), loc.getZ() + destroyRange).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() - destroyRange, loc.getY(), loc.getZ() - destroyRange).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() + destroyRange, loc.getY(), loc.getZ() - destroyRange).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() - destroyRange, loc.getY(), loc.getZ() + destroyRange).getBlock());

		blocks.add(new Location(loc.getWorld(), loc.getX() - destroyRange, loc.getY(), loc.getZ() + 0).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() + destroyRange, loc.getY(), loc.getZ() + 0).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() + 0, loc.getY(), loc.getZ() - .2).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() - 0, loc.getY(), loc.getZ() + .2).getBlock());

		for (final Block block : blocks) {
			if (block.getType() != this.map.floorMaterial()) {
				continue;
			}

			if (this.removedBlocks.contains(block)) {
				continue;
			}

			this.removedBlocks.add(block);

			Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
				final BlockState state = block.getState();
				state.setType(Material.AIR);
				state.update(true, false);
			}, 7);
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		this.alive.remove(event.getPlayer().getUniqueId());
	}

	private void finish() {
		this.task.cancel();
		super.endGame(Utils.getPlayerListFromUUIDList(this.alive));
	}

}
