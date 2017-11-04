package xyz.derkades.minigames.games;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
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
	
	@Override
	void begin() {
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
			
			for (Player player : Utils.getPlayerListFromUUIDList(alive)) {
				Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
				if (block.getType() != map.floorMaterial()) {
					continue;
				}
				
				Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> block.setType(Material.AIR), 10);
			}
		}, 20, 20);
	}
	
	private void finish() {
		task.cancel();
		super.startNextGame(Utils.getPlayerListFromUUIDList(alive));
	}

}
