package derkades.minigames.games.breaktheblock;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;

public class HollowHills extends BreakTheBlockMap {

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public String getName() {
		return "Hollow Hills";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.BTB_HOLLOWHILLS;
	}

	@Override
	public String getCredits() {
		return "Sneewie";
	}

	@Override
	public String getIdentifier() {
		return "btb_hollowhills";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 0.5, 54, 19.5, -180, 0);
	}

	@Override
	boolean canTakeDamage(final MPlayer player) {
		return true;
	}


}
