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
import xyz.derkades.minigames.games.tntrun.TNTMap;
import xyz.derkades.minigames.utils.Utils;

public class TntRun extends Game {

	TntRun() {
		super("TNT Run", new String[]{}, 2, 3, 7, TNTMap.MAPS);
	}

	private TNTMap map;
	
	private BukkitTask task;
	private List<UUID> alive;
	private List<Block> removedBlocks;
	
	boolean removeBlocks;
	
	@Override
	void begin(GameMap genericMap) {
		alive = new ArrayList<>();
		removedBlocks = new ArrayList<>();
		removeBlocks = false;
		
		map = (TNTMap) genericMap;
		
		map.restore();
		
		Bukkit.getOnlinePlayers().forEach((player) -> {
			player.teleport(map.spawnLocation());
			alive.add(player.getUniqueId());
		});
		
		task = Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), () -> {
			if (alive.size() <= 1) {
				finish();
			}
		}, 20, 20);
		
		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> removeBlocks = true, 4*20);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (!removeBlocks) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (!alive.contains(player.getUniqueId())) {
			return;
		}
		
		Block belowPlayer = event.getFrom().getBlock().getRelative(BlockFace.DOWN);
		
		if (belowPlayer.getType().equals(Material.RED_TERRACOTTA)) {
			player.teleport(map.spawnLocation());
			player.setAllowFlight(true);
			player.setFlying(true);
			Bukkit.getOnlinePlayers().forEach((online) -> online.hidePlayer(Minigames.getInstance(), player));
			alive.remove(player.getUniqueId());
			sendMessage(player.getName() + " has died. " + alive.size() + " players left.");
			return;
		}
		
		List<Block> blocks = new ArrayList<>();
		
		Location loc = player.getLocation();
		loc.setY(loc.getY() - 1);
		
		blocks.add(new Location(loc.getWorld(), loc.getX() + .2, loc.getY(), loc.getZ() + .2).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() - .2, loc.getY(), loc.getZ() - .2).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() + .2, loc.getY(), loc.getZ() - .2).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() - .2, loc.getY(), loc.getZ() + .2).getBlock());
		
		blocks.add(new Location(loc.getWorld(), loc.getX() - .2, loc.getY(), loc.getZ() + 0).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() + .2, loc.getY(), loc.getZ() + 0).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() + 0, loc.getY(), loc.getZ() - .2).getBlock());
		blocks.add(new Location(loc.getWorld(), loc.getX() - 0, loc.getY(), loc.getZ() + .2).getBlock());
		
		for (Block block : blocks) {
			if (block.getType() != map.floorMaterial()) {
				continue;
			}
			
			if (removedBlocks.contains(block)) {
				continue;
			}
			
			removedBlocks.add(block);
			
			Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
				BlockState state = block.getState();
				state.setType(Material.AIR);
				state.update(true, false);
			}, 7);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		alive.remove(event.getPlayer().getUniqueId());
	}
	
	private void finish() {
		task.cancel();
		super.startNextGame(Utils.getPlayerListFromUUIDList(alive));
	}

}
