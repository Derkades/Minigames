package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.tntrun.TNTMap;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Utils;

public class TntRun extends Game<TNTMap> {

	@Override
	public String getName() {
		return "TNT Run";
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"The floor disappears where you walk. Avoid",
				"falling for as long as possible.",
		};
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public TNTMap[] getGameMaps() {
		return TNTMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 150;
	}

	private List<UUID> alive;
	private List<Block> removedBlocks;

	@Override
	public void onPreStart() {
		this.removedBlocks = new ArrayList<>();

		this.map.restore();

		Minigames.getOnlinePlayers().forEach((player) -> {
			player.queueTeleport(this.map.spawnLocation());
		});
	}

	@Override
	public void onStart() {
		this.alive = Utils.getOnlinePlayersUuidList();
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (this.alive.size() < 2 && secondsLeft > 3) {
			return 3;
		}

		Minigames.getOnlinePlayers().forEach((p) -> {
			if (this.alive.contains(p.getUniqueId())) {
				TntRun.this.removeBlocks(p);
			}
		});

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		this.endGame(this.alive, true);
		this.removedBlocks = null;
		this.alive = null;
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (!this.started) {
			return;
		}

		final MPlayer player = new MPlayer(event);

		if (!this.alive.contains(player.getUniqueId())) {
			return;
		}

		final Block belowPlayer = event.getFrom().getBlock().getRelative(BlockFace.DOWN);

		if (belowPlayer.getType().equals(Material.RED_TERRACOTTA)) {
			this.alive.remove(player.getUniqueId());
			sendMessage(player.getName() + " has died. " + this.alive.size() + " players left.");
			player.dieUp(10);
			return;
		}

		removeBlocks(player);
	}

	private void removeBlocks(final MPlayer player) {
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
				block.setType(Material.AIR);
			}, 7);
		}
	}
	
	@Override
	public void onPlayerJoin(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
		player.dieTo(this.map.spawnLocation());
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		this.alive.remove(player.getUniqueId());
	}

}
