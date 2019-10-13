package xyz.derkades.minigames.board;

import java.util.function.Consumer;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.MPlayer;

public class BoardPlayer extends MPlayer {

	public BoardPlayer(final MPlayer player) {
		super(player.bukkit());
	}

	public void jumpTile(final Tile tile) {
		setTile(tile);
		teleportNpcTo(tile);
		tile.getSpectateLocation().teleportIfOutside(this, false);
	}

	public void jumpTiles(final int tiles) {
		Board.lastTurnTime = System.currentTimeMillis();

		if (tiles == 0) {
			final Tile currentTile = getTile();
			sendTitle("", currentTile.getColor() + "" + ChatColor.BOLD + currentTile.getName());
			currentTile.landOnTile(this);
			return;
		}

		final Tile currentTile = getTile();

		if (currentTile == Tile.END_TILE) {
			sendTitle("YAAAAAAAAAY!", "");
			jumpTile(Tile.START_TILE);
		}

		final Consumer<Tile> onMove = (tile) -> {
			if (tile == null)
				return;

			jumpTile(tile);
			jumpTiles(tiles - 1);
		};

		currentTile.moveToNextTile(this, onMove);
	}

	private void teleportNpcTo(final Tile tile) {
		final NPC npc = NPCs.getNPC(getName());

		final Location location = getTile().getLocation();

		Logger.debug("Teleporting NPC id %s name %s to (%s, %s, %s)",
				npc.getId(), npc.getName(),
				location.getX(), location.getY(), location.getZ());

		npc.teleport(location, TeleportCause.PLUGIN);
	}

	/**
	 * Create an NPC. Called when the player joins
	 */
	public void createNpc() {
		final Location location = getTile().getLocation();
		NPCs.createNPC(getName(), location);
	}

	/**
	 * Remove an NPC. Called when the player quits
	 */
	public void removeNpc() {
		NPCs.removeNPC(getName());
	}

	public void setTile(final Tile tile) {
		Logger.debug("Set %s's tile to %s", getName(), tile.toString());
		Minigames.getInstance().getConfig().set("tile." + getUniqueId(), tile.toString());
		Minigames.getInstance().saveConfig();
	}

	public Tile getTile() {
		final FileConfiguration config = Minigames.getInstance().getConfig();
		if (config.contains("tile." + getUniqueId()))
			return Tile.fromString(config.getString("tile." + getUniqueId()));
		else
			return Tile.START_TILE;
	}

	public void teleportToBoard(final boolean queue) {
		final Tile tile = getTile();
		tile.getSpectateLocation().teleportIfOutside(this, queue);

		setDisableDamage(true);
		setDisableHunger(true);
		setDisableItemMoving(true);
		setDisableSneaking(false);

		setGameMode(GameMode.ADVENTURE);
		setAllowFlight(false);

		clearPotionEffects();
		this.giveInfiniteEffect(PotionEffectType.INVISIBILITY);

		bukkit().setExp(0.0f);
		bukkit().setLevel(0);

		heal();
		clearInventory();
	}

}
