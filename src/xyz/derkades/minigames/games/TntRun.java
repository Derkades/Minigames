package xyz.derkades.minigames.games;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	public String getIdentifier() {
		return "tnt_run";
	}
	
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
	private Set<Block> removedBlocks;

	@Override
	public void onPreStart() {
		this.removedBlocks = new HashSet<>();

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
		final Deque<Block> toRemove = new ArrayDeque<>();

		final double offset = 0.31;
		
		final Location loc = player.getLocation();
		loc.setY(loc.getY() - 1);
		
		final Block mainBlock = loc.getBlock();
		toRemove.add(mainBlock);

		final double origX = loc.getX();
		final double origZ = loc.getZ();
		
		final double[] xValues = new double[] {origX-offset, origX, origX+offset};
		final double[] zValues = new double[] {origZ-offset, origZ, origZ+offset};
		
		for (final double x : xValues) {
			for (final double z : zValues) {
				loc.setX(x);
				loc.setZ(z);
				addIfNotMain(toRemove, loc, mainBlock);
			}
		}

		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
			if (this.removedBlocks == null) {
				// game ended
				return;
			}
			
			while(!toRemove.isEmpty()) {
				final Block block = toRemove.pop();
				if (block.getType() != this.map.floorMaterial()) {
					continue;
				}
	
				if (this.removedBlocks.contains(block)) {
					continue;
				}
	
				this.removedBlocks.add(block);
				block.setType(Material.AIR);
			}
		}, 7);
	}
	
	private void addIfNotMain(final Deque<Block> stack, final Location loc, final Block mainBlock) {
		final Block block = loc.getBlock();
		if (!mainBlock.equals(block)) {
			stack.add(block);
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
