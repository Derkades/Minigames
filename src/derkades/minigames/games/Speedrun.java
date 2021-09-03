package derkades.minigames.games;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import derkades.minigames.Minigames;
import derkades.minigames.games.speedrun.SpeedrunMap;
import derkades.minigames.utils.MPlayer;
import net.md_5.bungee.api.ChatColor;

public class Speedrun extends Game<SpeedrunMap> {

	@Override
	public String getIdentifier() {
		return "speedrun";
	}

	@Override
	public String getName() {
		return "Speedrun";
	}

	@Override
	public String[] getDescription() {
		return new String[] {"Jump to the finish with super speed"};
	}

	@Override
	public Material getMaterial() {
		return Material.POTION;
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
	public int getPreDuration() {
		return 0;
	}

	@Override
	public int getDuration() {
		return 50;
	}

	private Set<UUID> finished;

	@Override
	public void onPreStart() {
		this.finished = new HashSet<>();

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
		this.finished = null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(final PlayerMoveEvent event){
		final MPlayer player = new MPlayer(event.getPlayer());

		if (this.finished.contains(player.getUniqueId())) {
			return;
		}

		if (player.bukkit().isSneaking() || player.bukkit().isSprinting()){
			player.sendActionBar(ChatColor.RED + "Sprinting is not allowed");
			player.teleport(this.map.getStartLocation());
			return;
		}

		final Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
		final Material type = block.getType();

		if (type == this.map.getFloorBlock()){
			player.teleport(this.map.getStartLocation());
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
			player.finishTo(this.map.getSpectatorLocation());
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
