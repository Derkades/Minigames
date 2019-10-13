package xyz.derkades.minigames.board;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import xyz.derkades.minigames.Logger;

public class NPCs {

	public static NPC getNPC(final String name) {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final Iterator<NPC> iterator = registry.iterator();
		while (iterator.hasNext()) {
			final NPC npc = iterator.next();
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
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		for (final NPC npc : registry) {
			npc.despawn(DespawnReason.REMOVAL);
		}

		registry.deregisterAll();
	}

	public static void removeNPC(final String name) {
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();
		final Iterator<NPC> iterator = registry.iterator();
		while (iterator.hasNext()) {
			final NPC npc = iterator.next();
			if (npc.getName().equals(name)) {
				npc.despawn(DespawnReason.REMOVAL);
				npc.destroy();
			}
		}
	}

}
