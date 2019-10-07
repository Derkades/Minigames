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

		final Tile currentTile = this.getTile();

		final Consumer<Tile> onMove = (tile) -> {
			this.teleportNpcTo(tile);
			this.setTile(tile);
			this.jumpTiles(tiles - 1);
			tile.getSpectateLocation().teleportIfOutside(this, false);
		};

		currentTile.moveToNextTile(this, onMove);
	}

	private void teleportNpcTo(final Tile tile) {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getById(NPC_ID.get(this.getUniqueId()));
		final Location location = this.getTile().getLocation();
		npc.teleport(location, TeleportCause.PLUGIN);
	}

	/**
	 * Create an NPC. Called when the player joins and on reload for all online players.
	 * TODO Call on reload for all online players
	 */
	public void createNpc() {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.createNPC(EntityType.PLAYER, this.getName());
		NPC_ID.put(this.getUniqueId(), npc.getId());

		// Teleport to correct location
		this.teleportNpcTo(this.getTile());
	}

	/**
	 * Remove an NPC. Called when the player quits and onDisable.
	 * TODO call on quit and on disable
	 */
	public void removeNpc() {
		final int id = NPC_ID.remove(this.getUniqueId());
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getById(id);
		npc.despawn(DespawnReason.REMOVAL);
		registry.deregister(npc); // TODO may not be needed
	}

	private void setTile(final Tile tile) {
		Minigames.getInstance().getConfig().set("tile." + this.getUniqueId(), tile.toString());
		Minigames.getInstance().saveConfig();
	}

	public Tile getTile() {
		final FileConfiguration config = Minigames.getInstance().getConfig();
		if (config.contains("tile." + this.getUniqueId()))
			return Tile.fromString(config.getString("tile." + this.getUniqueId()));
		else
			return Tile.START_TILE;
	}

	public void teleportToBoard(final boolean queue) {
		final Tile tile = this.getTile();
		tile.getSpectateLocation().teleportIfOutside(this, queue);

		this.setDisableDamage(true);
		this.setDisableHunger(true);
		this.setDisableItemMoving(true);
		this.setDisableSneaking(false);

		this.setGameMode(GameMode.ADVENTURE);
		this.setAllowFlight(false);

		this.clearPotionEffects();
		this.giveInfiniteEffect(PotionEffectType.INVISIBILITY);

		this.bukkit().setExp(0.0f);
		this.bukkit().setLevel(0);

		this.heal();
		this.clearInventory();
	}

}
