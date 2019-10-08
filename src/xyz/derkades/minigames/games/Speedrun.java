package xyz.derkades.minigames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.speedrun.SpeedrunMap;
import xyz.derkades.minigames.utils.MPlayer;

public class Speedrun extends Game<SpeedrunMap> {

	@Override
	public String getName() {
		return "Speedrun";
	}

	@Override
	public String[] getDescription() {
		return new String[] {"Jump to the finish with super speed"};
	}

	@Override
	public int getRequiredPlayers() {
		return 1;
	}

	@Override
	public SpeedrunMap[] getGameMaps() {
		return SpeedrunMap.MAPS;
	}

	@Override
	public int getDuration() {
		return 50;
	}

	private List<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new ArrayList<>();

		for (final MPlayer player : Minigames.getOnlinePlayers()){
			player.queueTeleport(this.map.getStartLocation());
			player.giveInfiniteEffect(PotionEffectType.SPEED, 30);
		}
	}

	@Override
	public void onStart() {}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public void onEnd() {
		Speedrun.this.endGame(this.finished);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event.getPlayer());

		if (this.finished.contains(player.getUniqueId()))
			return;

		if (player.bukkit().isSneaking()){
			player.sendActionBar(ChatColor.RED + "Sprinting is not allowed");
			player.teleport(this.map.getWorld().getSpawnLocation());
			return;
		}

		if (player.bukkit().isSprinting()){
			player.sendActionBar(ChatColor.RED + "Sprinting is not allowed");
			player.teleport(this.map.getWorld().getSpawnLocation());
			return;
		}

		final Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
		final Material type = block.getType();

		if (type == this.map.getFloorBlock()){
			player.teleport(this.map.getWorld().getSpawnLocation());
			return;
		}

		if (type == this.map.getEndBlock()){
			player.clearPotionEffects();
			if (this.finished.isEmpty()){
				super.sendMessage(player.getName() + " finished first and got an extra point!");
				player.addPoints(1);
			} else {
				super.sendMessage(player.getName() + " has finished!");
			}
			this.finished.add(player.getUniqueId());
			player.teleport(this.map.getSpectatorLocation());
			player.spectator();
		}
	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		if (this.finished.contains(player.getUniqueId())) {
			player.teleport(this.map.getSpectatorLocation());
		} else {
			player.teleport(this.map.getStartLocation());
		}
	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
	}

}
