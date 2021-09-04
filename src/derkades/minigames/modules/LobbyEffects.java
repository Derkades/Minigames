package derkades.minigames.modules;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import derkades.minigames.GameState;
import derkades.minigames.Var;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;

public class LobbyEffects extends Module {

	private static final String META_LOBBY_WATER_TELEPORTING = "lobby water teleporting";
	private static final PotionEffect SLIME_JUMP_EFFECT = new PotionEffect(PotionEffectType.JUMP, 30, 7, true, false);
	private static final Location parkourWater1 = new Location(Var.LOBBY_WORLD, 213, 78, 239);
	private static final Location parkourWater2 = new Location(Var.LOBBY_WORLD, 195, 78, 264);

	@EventHandler
	public void lobbyEffects(final PlayerMoveEvent event){
		if (GameState.isCurrentlyInGame()) {
			return;
		}

		final MPlayer player = new MPlayer(event);
		player.removeFire();

		final Block to = event.getTo().getBlock();
		final Material below = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
		if (
				(
						to.getType() == Material.WATER ||
						(
								to.getBlockData() != null &&
								to.getBlockData() instanceof Waterlogged &&
								((Waterlogged) to.getBlockData()).isWaterlogged()
						)
				) &&
				player.getGameMode() == GameMode.ADVENTURE &&
				!player.getMetadataBool(META_LOBBY_WATER_TELEPORTING, false) &&
				player.isIn2dBounds(parkourWater1, parkourWater2)) {
			player.setMetadata(META_LOBBY_WATER_TELEPORTING, true);
			Scheduler.delay(5, () -> {
				player.teleport(new Location(Var.LOBBY_LOCATION.getWorld(), 213.5, 68, 255.9, 70, 0));
				player.removeMetadata(META_LOBBY_WATER_TELEPORTING);
			});
		} else if (below == Material.SLIME_BLOCK) {
			player.giveEffect(SLIME_JUMP_EFFECT);
		}
	}

}
