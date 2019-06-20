package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Spectator;
import xyz.derkades.minigames.games.tntrun.TNTMap;
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
		return 200;
	}


	private List<UUID> dead;
	private List<UUID> all;
	private List<Block> removedBlocks;

	boolean removeBlocks;

	@Override
	public void onPreStart() {
		this.dead = new ArrayList<>();
		this.removedBlocks = new ArrayList<>();
		this.removeBlocks = false;

		this.map.restore();

		Bukkit.getOnlinePlayers().forEach((player) -> {
			player.teleport(this.map.spawnLocation());
		});
	}

	@Override
	public void onStart() {
		TntRun.this.removeBlocks = true;
		TntRun.this.all = Utils.getOnlinePlayersUuidList();
	}

	@Override
	public int gameTimer(final int secondsLeft) {
		if (Utils.getAliveAcountFromDeadAndAllList(TntRun.this.dead, TntRun.this.all) < 2 && secondsLeft > 2){
			return 2;
		}

		Bukkit.getOnlinePlayers().forEach(TntRun.this::removeBlocks);

		return secondsLeft;
	}

	@Override
	public void onEnd() {
		TntRun.this.endGame(Utils.getWinnersFromDeadAndAllList(TntRun.this.dead, TntRun.this.all, false));
		TntRun.this.removedBlocks.clear();
		TntRun.this.all.clear();
		TntRun.this.dead.clear();
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (!this.removeBlocks) {
			return;
		}

		final Player player = event.getPlayer();

		if (this.dead.contains(player.getUniqueId())) {
			return;
		}

		final Block belowPlayer = event.getFrom().getBlock().getRelative(BlockFace.DOWN);

		if (belowPlayer.getType().equals(Material.RED_TERRACOTTA)) {
			this.dead.add(player.getUniqueId());
			this.sendMessage(player.getName() + " has died. " + Utils.getAliveAcountFromDeadAndAllList(this.dead, this.all) + " players left.");
			Spectator.dieUp(player, 10);
			return;
		}

		this.removeBlocks(player);
	}

	private void removeBlocks(final Player player) {
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
				//final BlockState state = block.getState();
				//state.setType(Material.AIR);
				//state.update(true, false);
			}, 7);
		}
	}

}
