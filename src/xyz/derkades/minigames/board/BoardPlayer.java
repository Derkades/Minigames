package xyz.derkades.minigames.board;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
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

	public void jumpTiles(final int tiles) {
		// if the player gets teleported, while they are still teleporting
		if (this.teleportTimer != null) {
			this.teleportTimer.cancel();
		}

		// TODO move the specified amount of tiles
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

}
