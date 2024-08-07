package derkades.minigames.games.freefall;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

class Prototype extends FreeFallMap {

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.FREEFALL_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "freefall_prototype";
	}

	@Override
	Location getSpawnLocation() {
		return null; // TODO Spawn location
	}

	@Override
	Layer[] getLayers() {
		return null; // TODO Layers
	}



}
