package xyz.derkades.minigames.board;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.MPlayer;

public class BoardPlayer extends MPlayer {

	private static final Map<UUID, Integer> NPC_ID = new HashMap<>();

//	public BoardPlayer(final Player player) {
//		super(player);
//	}

	public BoardPlayer(final MPlayer player) {
		super(player.bukkit());
	}

	public void jumpTiles(final int tiles) {
		if (tiles == 0) {
			final Tile currentTile = getTile();
			sendTitle("", currentTile.getColor() + "" + ChatColor.BOLD + currentTile.getName());
			currentTile.landOnTile(this);
			return;
		}

		final Tile currentTile = getTile();

		final Consumer<Tile> onMove = (tile) -> {
			teleportNpcTo(tile);
			setTile(tile);
			jumpTiles(tiles - 1);
			tile.getSpectateLocation().teleportIfOutside(this, false);
		};

		currentTile.moveToNextTile(this, onMove);
	}

	private void teleportNpcTo(final Tile tile) {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getById(NPC_ID.get(getUniqueId()));

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
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.createNPC(EntityType.PLAYER, getName());
		final Location location = getTile().getLocation();
		npc.spawn(location);
		NPC_ID.put(getUniqueId(), npc.getId());
	}

	/**
	 * Remove an NPC. Called when the player quits
	 */
	public void removeNpc() {
		final int id = NPC_ID.remove(getUniqueId());
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getById(id);
		npc.despawn(DespawnReason.REMOVAL);
		npc.destroy();
		registry.deregister(npc); // TODO may not be needed
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
