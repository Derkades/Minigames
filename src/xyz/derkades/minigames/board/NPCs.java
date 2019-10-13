package xyz.derkades.minigames.board;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import xyz.derkades.minigames.Logger;

public class NPCs {

	public static NPC getNPC(final String name) {
		for (final NPC npc : CitizensAPI.getNPCRegistry()) {
			if (npc.getName().equals(name))
				return npc;
		}

		return null;
	}

	public static void createNPC(final String name, final Location location) {
		if (getNPC(name) != null) {
			Logger.warning("Tried to create duplicate NPC %s", name);
			return;
		}

		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final NPC npc = registry.createNPC(EntityType.PLAYER, name);
		npc.spawn(location);
	}

	public static void removeNPCs() {
		for (final NPC npc : CitizensAPI.getNPCRegistry()) {
			npc.despawn(DespawnReason.REMOVAL);
		}

		CitizensAPI.getNPCRegistry().deregisterAll();
	}

	public static void removeNPC(final String name) {
		final List<NPC> toDeregister = new ArrayList<>();
		for (final NPC npc : CitizensAPI.getNPCRegistry()) {
			if (npc.getName().equals(name)) {
				npc.despawn(DespawnReason.REMOVAL);
			}
		}

		for (final NPC npc : toDeregister) {
			CitizensAPI.getNPCRegistry().deregister(npc);
		}
	}

}
