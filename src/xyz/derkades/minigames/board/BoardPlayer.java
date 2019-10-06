package xyz.derkades.minigames.board;

import org.bukkit.entity.Player;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.utils.MPlayer;

public class BoardPlayer extends MPlayer {

	public BoardPlayer(final Player player) {
		super(player);
	}

	private static final int TILE_TELEPORT_INTERVAL = 10;

//	public void jumpTiles(final int tiles) {
//		final int original = this.getTile().getPosition();
//		this.setTile(Tile.atPosition(tiles));
//
//		new BukkitRunnable() {
//
//			private int currentTile = original;
//			private int tilesToTeleport = tiles;
//
//			@Override
//			public void run() {
//				this.currentTile++;
//				this.tilesToTeleport--;
//
//				final Tile tile = Tile.atPosition(this.currentTile);
//				if (tile.equals(Tile.END_TILE))
////
//				if (this.tilesToTeleport < 0) {
//					this.cancel();
//					return;
//				}
//
//				//final Tile destinationTile = Tile.atPosition(position);
//			}
//		}.runTaskTimer(Minigames.getInstance(), 0, TILE_TELEPORT_INTERVAL);
//	}

	private void setTile(final Tile tile) {
		Minigames.getInstance().getConfig().set("tile." + this.getUniqueId(), tile.getPosition());
		Minigames.getInstance().saveConfig();
	}

//	public Tile getTile() {
//		return Tile.atPosition(
//				Minigames.getInstance().getConfig().getInt("tile." + this.getUniqueId(), Tile.START_TILE.getPosition()));
//	}

}
