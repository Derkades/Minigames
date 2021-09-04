package derkades.minigames.games.molepvp;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class Prototype extends MolePvPMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.MOLEPVP_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public String getIdentifier() {
		return "molepvp_prototype";
	}

	@Override
	void setUpMap() {
		BlockUtils.fillArea(this.getWorld(), -9, 72, -10, 11, 65, 9, Material.DIRT);
		BlockUtils.fillArea(this.getWorld(), -9, 72, -10, -7, 70, -8, Material.AIR);
		BlockUtils.fillArea(this.getWorld(), 11, 65, 9, 9, 67, 7, Material.AIR);
	}

	@Override
	Location getTeamRedSpawnLocation() {
		return new Location(this.getWorld(), 10.5, 65, 8.5);
	}

	@Override
	Location getTeamBlueSpawnLocation() {
		return new Location(this.getWorld(), -7.5, 70, -8.5);
	}

}
