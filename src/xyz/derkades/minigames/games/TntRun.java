package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.tntrun.TNTMap;
import xyz.derkades.minigames.utils.Utils;

public class TntRun extends Game {

	TntRun() {
		super("TNT Run", new String[]{"Insert description here"}, 2, 3, 7);
	}

	private TNTMap map;
	
	private BukkitTask task;
	private List<UUID> alive;
	private List<Block> removedBlocks;
	
	boolean removeBlocks;
	
	@Override
	void begin() {
		alive = new ArrayList<>();
		removedBlocks = new ArrayList<>();
		removeBlocks = false;
		
		map = ListUtils.getRandomValueFromArray(TNTMap.MAPS);
		
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
		
		Block block = event.getFrom().getBlock().getRelative(BlockFace.DOWN);
		
		if (block.getType().equals(Material.STAINED_CLAY)) {
			player.teleport(map.spawnLocation());
			player.setAllowFlight(true);
			Bukkit.getOnlinePlayers().forEach((online) -> online.hidePlayer(player));
			alive.remove(player.getUniqueId());
			return;
		}
		
		if (block.getType() != map.floorMaterial()) {
			return;
		}
		
		if (removedBlocks.contains(block)) {
			return;
		}
		
		removedBlocks.add(block);
		
		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
			BlockState state = block.getState();
			state.setType(Material.AIR);
			state.update(true, false);
		}, 7);
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
