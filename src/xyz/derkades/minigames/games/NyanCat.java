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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class NyanCat extends Game {

	NyanCat() {
		super("Nyan Cat", new String[] {
				"Blocks go Green -> Yellow -> Orange -> Red",
				"Red blocks do damage.",
				"Try not to die."
		}, 2, 3, 6);
	}

	private List<UUID> dead;
	private BukkitTask task;
	
	@Override
	void begin() {
		dead = new ArrayList<>();
		
		BlockUtils.fillArea(193, 79, 142, 219, 79, 162, Material.STONE);
		BlockUtils.fillArea(193, 79, 142, 219, 79, 162, Material.STAINED_CLAY);
		
		task = new BlockRemover().runTaskTimer(Minigames.getInstance(), 10, 10);
		
		Bukkit.getOnlinePlayers().forEach((player) -> {
			player.teleport(new Location(Var.WORLD, 229, 80, 156));
			Utils.giveInfiniteEffect(player, PotionEffectType.REGENERATION, 2);
		});
	}
	
	private void finish() {
		task.cancel();
		super.startNextGame(Utils.getWinnersFromDeadList(dead));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Scheduler.runTaskLater(1, () -> {
			Player player = event.getEntity();
			player.spigot().respawn();
			player.teleport(new Location(Var.WORLD, 229.5, 93, 151));
			dead.add(player.getUniqueId());
			player.setHealth(20);
			Bukkit.getOnlinePlayers().forEach((online) -> online.hidePlayer(player));
		});
	}
	
	public final class BlockRemover extends BukkitRunnable {

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			int alive = Utils.getAliveCountFromDeadList(dead);
			if (alive == 1) {
				finish();
				return;
			}
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (dead.contains(player.getUniqueId())) {
					continue;
				}
				
				Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
				
				if (block.getType() != Material.STAINED_CLAY) {
					continue;
				}
				
				if (block.getData() == 15) { //Black (ground)
					player.damage(10);
				} else if (block.getData() == 0) {
					block.setData((byte) 4); //Yellow
				} else if (block.getData() == 4) { //Yellow
					block.setData((byte) 1); //Orange
				} else if (block.getData() == 1) { //Orange
					block.setData((byte) 14); //Red
				} else if (block.getData() == 14) { //Red
					Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), () -> {
						block.setType(Material.AIR);
					}, 20);
					player.damage(7);
				}
			}
		}

	}

}
