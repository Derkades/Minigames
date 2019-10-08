package xyz.derkades.minigames.board;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.MPlayer;

public class BoardPlayer extends MPlayer {

	private static final Map<UUID, Integer> NPC_ID = new HashMap<>();

	private BukkitTask teleportTimer;

	public BoardPlayer(final Player player) {
		super(player);
	}

	public BoardPlayer(final MPlayer player) {
		super(player.bukkit());
	}

	public void jumpTiles(final int tiles) {
		// if the player gets teleported, while they are still teleporting
		if (this.teleportTimer != null) {
			this.teleportTimer.cancel();
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
		npc.teleport(location, TeleportCause.PLUGIN);
	}

	/**
	 * Create an NPC. Called when the player joins and on reload for all online players.
	 */
	public void createNpc() {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.createNPC(EntityType.PLAYER, getName());
		NPC_ID.put(getUniqueId(), npc.getId());

		// Teleport to correct location
		teleportNpcTo(getTile());
	}

	/**
	 * Remove an NPC. Called when the player quits and onDisable.
	 * TODO call on quit and on disable
	 */
	public void removeNpc() {
		final int id = NPC_ID.remove(getUniqueId());
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getById(id);
		npc.despawn(DespawnReason.REMOVAL);
		registry.deregister(npc); // TODO may not be needed
	}

	private void setTile(final Tile tile) {
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
