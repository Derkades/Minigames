package derkades.minigames.games.breaktheblock;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Prototype extends BreakTheBlockMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public void onPreStart() {
		new Location(this.getWorld(), 16, 66, 0).getBlock().setType(Material.GOLD_BLOCK);
	}

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5, -90f, 0f);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.BTB_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public String getIdentifier() {
		return "breaktheblock_prototype";
	}

}
