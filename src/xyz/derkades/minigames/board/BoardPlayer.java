package xyz.derkades.minigames.board;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.utils.MPlayer;

public class BoardPlayer extends MPlayer {

	private static final Map<UUID, Integer> NPC_ID = new HashMap<>();

	private BukkitTask teleportTimer;

	public BoardPlayer(final Player player) {
		super(player);
	}

	private static final int TILE_TELEPORT_INTERVAL = 10;

	public void jumpTiles(final int tiles) {
		if (this.teleportTimer != null) {
			this.teleportTimer.cancel();
		}

		final int original = this.getTile().getPosition();

		this.teleportTimer = new BukkitRunnable() {

			private int currentTile = original;
			private int tilesToTeleport = tiles;

			@Override
			public void run() {
				this.currentTile++;
				this.tilesToTeleport--;

				final Tile tile = Tile.atPosition(this.currentTile);

				BoardPlayer.this.teleportNpcTo(tile);

				if (tile.equals(Tile.END_TILE)) {
					// TODO handle end tile
				}

				if (this.tilesToTeleport < 0) {
					BoardPlayer.this.teleportTimer = null;
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, TILE_TELEPORT_INTERVAL);
	}

	private void teleportNpcTo(final Tile tile) {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getById(NPC_ID.get(this.getUniqueId()));
		final Location location = this.getTile().getLocation();
		npc.teleport(location, TeleportCause.PLUGIN);
	}

	public void createNpc() {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.createNPC(EntityType.PLAYER, this.getName());
		NPC_ID.put(this.getUniqueId(), npc.getId());

		// Teleport to correct location
		this.teleportNpcTo(this.getTile());
	}

	public void removeNpc() {
		final int id = NPC_ID.remove(this.getUniqueId());
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.getById(id);
		npc.despawn(DespawnReason.REMOVAL);
		registry.deregister(npc); // may not be needed
	}

	private void setTile(final Tile tile) {
		Minigames.getInstance().getConfig().set("tile." + this.getUniqueId(), tile.getPosition());
		Minigames.getInstance().saveConfig();
	}

	public Tile getTile() {
		return Tile.atPosition(
				Minigames.getInstance().getConfig().getInt("tile." + this.getUniqueId(), Tile.START_TILE.getPosition()));
	}

}
